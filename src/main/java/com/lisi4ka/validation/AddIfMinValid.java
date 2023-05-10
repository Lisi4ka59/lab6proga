package com.lisi4ka.validation;

import com.lisi4ka.utils.PackagedCommand;

public class AddIfMinValid {
    public PackagedCommand addIfMinValid(String[] commandText) {
        if (commandText.length == 1) {
            AddValid addValid = new AddValid();
            String[] command = "add".split(" ");
            PackagedCommand args;
            try {
                args = addValid.addValid(command);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Error while reading arguments for command \"add_if_min\"!\n");
            }
            return new PackagedCommand(commandText[0], args.getCommandArguments());
        } else if (commandText.length == 2) {
            AddValid addValid = new AddValid();
            String[] command = String.format("add %s", commandText[1]).split(" ");
            PackagedCommand args;
            try {
                args = addValid.addValid(command);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Illegal arguments for command \"add_if_min\"!\n");
            }
            return new PackagedCommand(commandText[0], args.getCommandArguments());
        } else {
            throw new IllegalArgumentException("Illegal arguments for command \"add_if_min\"!\n");
        }
    }
}
