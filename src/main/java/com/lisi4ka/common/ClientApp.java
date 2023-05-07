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
    public static void main(String[] args) throws Exception {
        while (true) {
            System.out.println("samuy pervuy while");
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
            sleep(10000);
            timeOut = currentTimeMillis();
        }
    }

    public static Boolean processReadySet(Set readySet)
            throws Exception {

        SelectionKey key = null;
        Iterator iterator = null;
        iterator = readySet.iterator();
        while (iterator.hasNext()) {
            key = (SelectionKey) iterator.next();
            iterator.remove();
        }
        assert key != null;
        if (key.isConnectable()) {
            Boolean connected = false;
            try{
                connected = processConnect(key);
            }catch (Exception e){
                System.out.println("Lost server connection. Repeat connecting in 15 seconds");
            }

            if (!connected) {
                return false;
            }
        }
        if (key.isReadable()) {
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer bb = ByteBuffer.allocate(1000000);
            try{
                sc.read(bb);
            }catch (Exception e){
                System.out.println("sc read oshibka" + e);
                serverWork = false;
                return false;
            }

            String result = new String(bb.array()).trim();
            System.out.println("Message received from Server: " + result + " Message length= "
                    + result.length());
        }
        if (key.isWritable() && timeOut < (currentTimeMillis()-500)) {
            for (PackagedCommand packagedCommand:  ClientValidation.validation()) {


                if ("exit".equalsIgnoreCase(packagedCommand.getCommandName())) {
                    return true;
                }
                ByteArrayOutputStream stringOut = new ByteArrayOutputStream();
                ObjectOutputStream serializeObject = new ObjectOutputStream(stringOut);
                serializeObject.writeObject(packagedCommand);
                String serializeCommand = Base64.getEncoder().encodeToString(stringOut.toByteArray());
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer byteBuffer = ByteBuffer.wrap(serializeCommand.getBytes());
                try {
                    socketChannel.write(byteBuffer);
                } catch (Exception e) {
                    System.out.println(e + " 114");
                }
                timeOut = currentTimeMillis();
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
            e.printStackTrace();
            serverWork = false;
            System.out.println(e + " 130");
            return false;
        }
        return true;
    }
}