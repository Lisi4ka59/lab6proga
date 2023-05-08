package com.lisi4ka.common;

import com.lisi4ka.models.City;
import com.lisi4ka.utils.PackagedCommand;
import com.lisi4ka.validation.ExecuteScriptValid;


import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static com.lisi4ka.utils.CityReader.inputName;

public class ClientValidation {
    public static PackagedCommand[] validation() {
        Scanner scanner = new Scanner(System.in);
        Invoker invoker = new Invoker(new LinkedList<City>());
        while (true) {
            System.out.print("> ");
            try {
                String[] commandText = scanner.nextLine().trim().split(" ");

                if (invoker.commands.containsKey(commandText[0])) {
                    try {
                        if ("add".equals(commandText[0])) {
                            return new PackagedCommand[] {addValid(commandText)};
                        } else if ("add_if_min".equals(commandText[0])) {
                            return new PackagedCommand[] {addValid(commandText)};
                        } else if ("show".equals(commandText[0])) {
                            return new PackagedCommand[] {showValid(commandText)};
                        } else if ("clear".equals(commandText[0])) {
                            return new PackagedCommand[] {showValid(commandText)};
                        } else if ("execute_script".equals(commandText[0])) {
                            return new ExecuteScriptValid().executeScriptValid(commandText);
                        } else if ("help".equals(commandText[0])) {
                            return new PackagedCommand[] {showValid(commandText)};
                        } else if ("info".equals(commandText[0])) {
                            return new PackagedCommand[] {showValid(commandText)};
                        } else if ("print_descending".equals(commandText[0])) {
                            return new PackagedCommand[] {showValid(commandText)};
                        } else if ("remove_by_id".equals(commandText[0])) {
                            return new PackagedCommand[] {showValid(commandText)};
                        }
                    }catch (IllegalArgumentException ex){
                        System.out.printf(ex.getMessage());
                    }
            }else {
                    System.out.println("Unknown command! Type \"help\" to open command list");
                }
            } catch (NoSuchElementException e) {
                System.exit(0);
            }
        }
    }
    private static PackagedCommand addValid(String[] commandText){
        return new PackagedCommand(commandText[0], commandText[1]);
    }
    private static PackagedCommand showValid(String[] commandText){
            if (commandText.length == 1){
                return new PackagedCommand(commandText[0], null);
            }
            else {
                throw new IllegalArgumentException("Command \"show\" takes no arguments!\n");
            }
    }
}
