package com.lisi4ka.validation;

import com.lisi4ka.utils.PackagedCommand;

public class PrintDescendingValid {
    public PackagedCommand printDescendingValid(String[] commandText){
        if (commandText.length == 1){
            return new PackagedCommand(commandText[0], null);
        }
        else {
            throw new IllegalArgumentException("Command \"print_descending\" takes no arguments!\n");
        }
    }
}
