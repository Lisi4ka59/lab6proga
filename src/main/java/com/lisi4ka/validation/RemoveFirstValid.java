package com.lisi4ka.validation;

import com.lisi4ka.utils.PackagedCommand;

public class RemoveFirstValid {
    public PackagedCommand removeFirstValid(String[] commandText){
        if (commandText.length == 1){
            return new PackagedCommand(commandText[0], null);
        }
        else {
            throw new IllegalArgumentException("Command \"remove_first\" takes no arguments!\n");
        }
    }
}
