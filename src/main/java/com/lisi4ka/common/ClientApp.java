package com.lisi4ka.common;

import com.lisi4ka.utils.PackagedCommand;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;


public class ClientApp {
    static long timeOut = currentTimeMillis();
    static boolean serverWork = true;
    static Queue<ByteBuffer> queue = new LinkedList<>();
    public static void main(String[] args) throws Exception {
        while (true) {
            boolean doneStatus = true;
            serverWork = true;
            InetSocketAddress addr = new InetSocketAddress(InetAddress.getByName("localhost"), 1234);
            Selector selector = Selector.open();
            SocketChannel sc = SocketChannel.open();
            sc.configureBlocking(false);
            sc.connect(addr);
            sc.register(selector, SelectionKey.OP_CONNECT |
                    SelectionKey.OP_READ | SelectionKey.
                    OP_WRITE);
            while (true) {
                if (selector.select() > 0) {
                    try {
                        doneStatus = processReadySet(selector.selectedKeys());
                    }
                    catch (ConnectException ex){
                        System.out.println("Lost server connection. Repeat connecting in 10 seconds");
                        break;
                    }
                    if (doneStatus || !serverWork) {
                        break;
                    }
                }
            }
            if (doneStatus) {
                break;
            }
            sc.close();
            System.out.println("Lost server connection. Repeat connecting in 10 seconds");
            sleep(10000);
            timeOut = currentTimeMillis();
        }
    }

    public static Boolean processReadySet(Set readySet)
            throws Exception {

        SelectionKey key = null;
        Iterator iterator = readySet.iterator();
        while (iterator.hasNext()) {
            key = (SelectionKey) iterator.next();
            iterator.remove();
        }
        assert key != null;
        if (key.isConnectable()) {
            boolean connected = false;
            try{
                connected = processConnect(key);
                System.out.println("Connection Accepted");
            }catch (Exception e){
                System.out.println("Lost server connection");
            }

            if (!connected) {
                return false;
            }
        }
        if (key.isReadable()) {
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer bb = ByteBuffer.allocate(8192);
            try{
                sc.read(bb);
            }catch (Exception e){
                serverWork = false;
                return false;
            }
            System.out.println(new String(bb.array()).trim());
        }
        if (key.isWritable() && timeOut < (currentTimeMillis()-300)) {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            if (queue.isEmpty()) {
                for (PackagedCommand packagedCommand : ClientValidation.validation()) {
                    if ("exit".equals(packagedCommand.getCommandName())){
                        return true;
                    }
                    ByteArrayOutputStream stringOut = new ByteArrayOutputStream();
                    ObjectOutputStream serializeObject = new ObjectOutputStream(stringOut);
                    serializeObject.writeObject(packagedCommand);
                    String serializeCommand = Base64.getEncoder().encodeToString(stringOut.toByteArray());
                    ByteBuffer byteBuffer = ByteBuffer.wrap(serializeCommand.getBytes());
                    queue.add(byteBuffer);
                }
            }
            if (!queue.isEmpty()) {
                try {
                    socketChannel.write(queue.poll());
                    timeOut = currentTimeMillis();
                } catch (Exception e) {
                    System.out.println("Error while sending message!");
                }
            }
            return false;
        }
        return false;
    }
    public static Boolean processConnect(SelectionKey key) {
        SocketChannel sc = (SocketChannel) key.channel();
        try {
            while (sc.isConnectionPending()) {
                sc.finishConnect();
            }
        } catch (IOException e) {
            key.cancel();
            serverWork = false;
            return false;
        }
        return true;
    }
}