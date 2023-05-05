package com.lisi4ka.common;

import com.lisi4ka.utils.PackagedCommand;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

public class ClientApp {
    private static BufferedReader input = null;
    static int i = 100001;
    public static void main(String[] args) throws Exception {
        InetSocketAddress addr = new InetSocketAddress(
                InetAddress.getByName("localhost"), 1234);
        Selector selector = Selector.open();
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        sc.connect(addr);
        sc.register(selector, SelectionKey.OP_CONNECT |
                SelectionKey.OP_READ | SelectionKey.
                OP_WRITE);
        input = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            if (selector.select() > 0) {
                Boolean doneStatus = processReadySet
                        (selector.selectedKeys());
                if (doneStatus) {
                    break;
                }
            }

        }
        sc.close();
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
        if (key.isConnectable()) {
            Boolean connected = processConnect(key);
            if (!connected) {
                return true;
            }
        }
        if (key.isReadable()) {
            i++;
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer bb = ByteBuffer.allocate(1000000);
            sc.read(bb);
            String result = new String(bb.array()).trim();
            if (result.length() > 0){
                i= 100001;
            }
            System.out.println("Message received from Server: " + result + " Message length= "
                    + result.length());
        }
        if (key.isWritable() && i > 100000) {
            i = 0;
            System.out.println("Type a message (type quit to stop): ");
            Scanner scanner = new Scanner(System.in);
            String msg;
            try {
                System.out.print("> ");
                    msg = scanner.nextLine();
            if (msg.equalsIgnoreCase("quit")) {
                return true;
            }

            if (msg.equalsIgnoreCase("help")) {
                PackagedCommand packagedCommand = new PackagedCommand();
                packagedCommand.setCommandName("help");
                ByteArrayOutputStream str = new ByteArrayOutputStream();
                ObjectOutputStream obj = new ObjectOutputStream(str);
                obj.writeObject(packagedCommand);
                String serCommand1 = Base64.getEncoder().encodeToString(str.toByteArray());
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer bb = ByteBuffer.wrap(serCommand1.getBytes());
                System.out.println(serCommand1);
                sc.write(bb);
                return false;
            }
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer bb = ByteBuffer.wrap(msg.getBytes());
            sc.write(bb);
            return false;
            } catch (NoSuchElementException e) {
                System.exit(0);
            }
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
            return false;
        }
        return true;
    }
}