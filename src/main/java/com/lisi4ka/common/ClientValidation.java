package com.lisi4ka.common;

import com.lisi4ka.models.City;
import com.lisi4ka.utils.PackagedCommand;
import com.lisi4ka.validation.*;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientValidation {
    public static PackagedCommand[] validation() {
        Scanner scanner = new Scanner(System.in);
        Invoker invoker = new Invoker(new LinkedList<City>());
        while (true) {
            System.out.print("> ");
            try {
                String[] commandText = scanner.nextLine().trim().split(" ");
                if ("".equals(commandText[0])){
                    continue;
                }
                if (invoker.commands.containsKey(commandText[0])) {
                    try {
                        if ("add".equals(commandText[0])) {
                            return new PackagedCommand[] {new AddValid().addValid(commandText)};
                         //else if ("add_if_min".equals(commandText[0])) {
//                            return new PackagedCommand[] {new AddIfMinValid().addIfMinValid(commandText)};
                        } else if ("show".equals(commandText[0])) {
                            return new PackagedCommand[]{new ShowValid().showValid(commandText)};
                        }
//                        } else if ("clear".equals(commandText[0])) {
//                            return new PackagedCommand[] {new ClearValid().clearValid(commandText)};}
                        else if ("execute_script".equals(commandText[0])) {
                            return new ExecuteScriptValid().executeScriptValid(commandText);
//                        } else if ("help".equals(commandText[0])) {
//                            return new PackagedCommand[] {new HelpValid().helpValid(commandText)};
//                        } else if ("info".equals(commandText[0])) {
//                            return new PackagedCommand[] {new InfoValid().infoValid(commandText)};
//                        } else if ("print_descending".equals(commandText[0])) {
//                            return new PackagedCommand[] {new PrintDescendingValid().printDescendingValid(commandText)};
//                        } else if ("remove_by_id".equals(commandText[0])) {
//                            return new PackagedCommand[] {new RemoveByIdValid().removeByIdValid(commandText)};
                        } else if ("exit".equals(commandText[0])) {
                            return new PackagedCommand[] {new ExitValid().exitValid(commandText)};
                        }
                    }catch (IllegalArgumentException ex){
                        System.out.printf(ex.getMessage());
                    }
            }else {
                    System.out.println("Unknown command! Type \"help\" to open command list");
                }
            } catch (NoSuchElementException e) {
                System.out.println("Program termination");
                System.exit(0);
            }
        }
    }
}
