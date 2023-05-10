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
                        switch (commandText[0]) {
                            case "add" -> {
                                return new PackagedCommand[]{new AddValid().addValid(commandText)};
                            }
                            case "add_if_min" -> {
                                return new PackagedCommand[]{new AddIfMinValid().addIfMinValid(commandText)};
                            }
                            case "show" -> {
                                return new PackagedCommand[]{new ShowValid().showValid(commandText)};
                            }
                            case "clear" -> {
                                return new PackagedCommand[]{new ClearValid().clearValid(commandText)};
                            }
                            case "execute_script" -> {
                                return new ExecuteScriptValid().executeScriptValid(commandText);
                            }
                            case "help" -> {
                                return new PackagedCommand[]{new HelpValid().helpValid(commandText)};
                            }
                            case "info" -> {
                                return new PackagedCommand[]{new InfoValid().infoValid(commandText)};
                            }
                            case "print_descending" -> {
                                return new PackagedCommand[]{new PrintDescendingValid().printDescendingValid(commandText)};
                            }
                            case "remove_by_id" -> {
                                return new PackagedCommand[]{new RemoveByIdValid().removeByIdValid(commandText)};
                            }
                            case "exit" -> {
                                return new PackagedCommand[]{new ExitValid().exitValid(commandText)};
                            }
                            case "update" -> {
                                return new PackagedCommand[]{new UpdateIdValid().updateIdValid(commandText)};
                            }
                            case "print_field_ascending_standard_of_living" -> {
                                return new PackagedCommand[]{new PrintFieldAscendingStandardOfLivingValid().printFieldAscendingStandardOfLivingValid(commandText)};
                            }
                            case "print_unique_standard_of_living" -> {
                                return new PackagedCommand[]{new PrintUniqueStandardOfLivingValid().printUniqueStandardOfLivingValid(commandText)};
                            }
                            case "remove_head" -> {
                                return new PackagedCommand[]{new RemoveHeadValid().removeHeadValid(commandText)};
                            }
                            case "remove_first" -> {
                                return new PackagedCommand[]{new RemoveFirstValid().removeFirstValid(commandText)};
                            }
                            case null, default ->
                                    System.out.println("Unknown command! Type \"help\" to open command list");
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
