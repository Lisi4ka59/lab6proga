package com.lisi4ka.common;

import com.lisi4ka.commands.HelpCommand;
import com.lisi4ka.utils.CityLinkedList;
import com.lisi4ka.utils.PackagedCommand;
import com.lisi4ka.utils.PackagedResponse;
import com.lisi4ka.utils.ResponseStatus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class ServerApp {
    public static CityLinkedList cities = new CityLinkedList();
    public static void main(String[] args)
            throws Exception {
        Invoker invoker = new Invoker(cities);
        invoker.run("load");
        InetAddress host = InetAddress.getByName("localhost");
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel =
                ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(host, 1234));
        serverSocketChannel.register(selector, SelectionKey.
                OP_ACCEPT);
        SelectionKey key = null;
        int i=0;
        Queue<String> queue = new LinkedList<>();
        while (true) {
            if (selector.select() <= 0)
                continue;
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();
            while (iterator.hasNext()) {
                key = (SelectionKey) iterator.next();
                iterator.remove();
            }
                if (key.isAcceptable()) {
                    SocketChannel sc = serverSocketChannel.accept();
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    System.out.println("Connection Accepted: "
                            + sc.getLocalAddress() + "n");
                }
                if (key.isReadable()) {
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer bb = ByteBuffer.allocate(1000000);
                    sc.read(bb);
                    String result = new String(bb.array()).trim();
                    System.out.println(result);
                    //queue.add("command receive");

                    if (result.length() <= 0) {
                        sc.close();
                        System.out.println("Connection closed...");
                        System.out.println(
                                "Server will keep running. " +
                                        "Try running another client to " +
                                        "re-establish connection");
                    }
                    else {
                        if (result.length() > 20) {
                            byte[] data = Base64.getDecoder().decode(result);
                            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                            PackagedCommand packagedCommand = (PackagedCommand) ois.readObject();
                            ois.close();
                            System.out.println(packagedCommand.getCommandName());
                                String answer = invoker.run(packagedCommand.getCommandName());
                                queue.add(answer);
                        }
                        else {
                            queue.add("invalid message");
                        }
                    }

                }
                if (key.isWritable() && !queue.isEmpty()){
                    String answer = queue.poll();
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteArrayOutputStream str = new ByteArrayOutputStream();
                    ObjectOutputStream obj = new ObjectOutputStream(str);
                    PackagedResponse packagedResponse = new PackagedResponse(answer, ResponseStatus.OK);
                    obj.writeObject(packagedResponse);
                    String serCommand1 = Base64.getEncoder().encodeToString(str.toByteArray());
                    ByteBuffer bb = ByteBuffer.wrap(answer.getBytes());
                    sc.write(bb);
//                    ByteBuffer b1 = ByteBuffer.wrap("sssssss".getBytes());
//                    sc.write(b1);
//                    sc.close();
                    i++;
                    System.out.printf("Сообщение отправлено %d\n", i);
                }
            }
        }
    }
