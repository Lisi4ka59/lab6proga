package com.lisi4ka.common;

import com.lisi4ka.commands.*;
import com.lisi4ka.utils.PackagedCommand;

import java.util.HashMap;
import java.util.List;

public class Invoker {

    HashMap<String, Command> commands = new HashMap<>();
    public Invoker (List collection) {
        commands.put("help", new HelpCommand());
        //commands.put("finish_server", new ServerExitCommand());
        commands.put("add", new AddCommand(collection));
        commands.put("info", new InfoCommand(collection));
        commands.put("show", new ShowCommand(collection));
        commands.put("clear", new ClearCommand(collection));
        commands.put("remove_first", new RemoveFirstCommand(collection));
        commands.put("remove_head", new RemoveHeadCommand(collection));
        //commands.put("save", new SaveCommand(collection));
        commands.put("load", new LoadCommand(collection));
        commands.put("remove_by_id", new RemoveByIdCommand(collection));
        commands.put("update_id", new UpdateIdCommand(collection));
        commands.put("print_descending", new PrintDescendingCommand(collection));
        commands.put("print_unique_standard_of_living", new PrintUniqueStandardOfLivingCommand(collection));
        commands.put("print_field_ascending_standard_of_living", new PrintFieldAscendingStandardOfLivingCommand(collection));
        commands.put("add_if_min", new AddIfMinCommand(collection));
        commands.put("execute_script", new ExecuteScriptCommand(collection));
    }
    public String run(PackagedCommand packagedCommand) {
        if (packagedCommand != null){
            return run(packagedCommand.getCommandName() + " " + packagedCommand.getCommandArguments());
        }
        throw new IllegalArgumentException("Incorrect command format");
    }
    public String run(String commandText) {
        try {
            String[] command =  commandText.split("\s+");
            if (commands.containsKey(command[0])) {
                Command cmd = commands.get(command[0]);
                String result;
                if (command.length==1) {
                    result = cmd.execute();
                }
                else{
                    result = cmd.execute(command[1]);
                }
                System.out.println(result);
                return result;
            }
            else {
                //if (!commandText.trim().isEmpty()) {
                    System.out.println("Unknown command. Type \"help\" to see list of commands");
                    return ("Unknown command. Type \"help\" to see list of commands");
                //}
            }
        } catch (Exception e){
            System.out.printf("Error while execution command %s\n", commandText);
            return String.format("Error while execution command %s\n", commandText);
        }
    }
}
