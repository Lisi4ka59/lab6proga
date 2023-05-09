package com.lisi4ka.common;

import com.lisi4ka.utils.CityLinkedList;
import com.lisi4ka.utils.PackagedCommand;
import com.lisi4ka.utils.PackagedResponse;
import com.lisi4ka.utils.ResponseStatus;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class ServerApp {
    public static CityLinkedList cities = new CityLinkedList();
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Server started");
            Invoker invoker = new Invoker(cities);
            Queue<String> queue = new LinkedList<>();
            queue.add(invoker.run("load"));
            InetAddress host = InetAddress.getByName("localhost");
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(host, 1234));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            SelectionKey key = null;
            while (true) {
                if (selector.select() <= 0)
                    continue;
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                }
                assert key != null;
                if (key.isValid() && key.isAcceptable()) {
                    SocketChannel sc = serverSocketChannel.accept();
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    System.out.println("Connection Accepted: " + sc.getLocalAddress());
                }
                if (key.isValid() && key.isReadable()) {
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer bb = ByteBuffer.allocate(8192);
                    boolean flag = true;
                    try {
                        sc.read(bb);
                    } catch (SocketException ex) {
                        sc.close();
                        flag = false;
                        System.out.print("Client close connection!\nServer will keep running\nTry running another client to re-establish connection\n");
                    }
                    if (flag) {
                        String result = new String(bb.array()).trim();
                        byte[] data = Base64.getDecoder().decode(result);
                        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                        PackagedCommand packagedCommand = (PackagedCommand) ois.readObject();
                        ois.close();
                        String answer;
                        if (packagedCommand.getCommandArguments() == null) {
                            answer = invoker.run(packagedCommand.getCommandName());
                        } else {
                            answer = invoker.run(packagedCommand);
                        }
                        queue.add(answer);
                    }
                }
                if (key.isValid() && key.isWritable() && !queue.isEmpty()) {
                    String answer = queue.poll();
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteArrayOutputStream stringOut = new ByteArrayOutputStream();
                    ObjectOutputStream serializeObject = new ObjectOutputStream(stringOut);
                    PackagedResponse packagedResponse = new PackagedResponse(answer, ResponseStatus.OK);
                    serializeObject.writeObject(packagedResponse);
                    socketChannel.write(ByteBuffer.wrap(answer.getBytes()));
                }
            }
    }
}