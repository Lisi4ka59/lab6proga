package com.lisi4ka.common;

import com.lisi4ka.utils.CityLinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class AppClient {
    public static CityLinkedList cities = new CityLinkedList();
    private void start() {
        Invoker invoker = new Invoker(cities);
        Scanner scanner = new Scanner(System.in);
        invoker.run("load");
        while (true) {
            System.out.print("> ");
            try {
                String commandText = scanner.nextLine();
                invoker.run(commandText);
            } catch (NoSuchElementException e) {
                System.exit(0);
            }
        }
    }
    public static void runApp(){
        AppClient appClient = new AppClient();
        appClient.start();
    }
}
