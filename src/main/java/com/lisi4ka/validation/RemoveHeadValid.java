package com.lisi4ka.validation;

import com.lisi4ka.utils.PackagedCommand;

public class RemoveHeadValid {
    public PackagedCommand removeHeadValid(String[] commandText){
        if (commandText.length == 1){
            return new PackagedCommand(commandText[0], null);
        }
        else {
            throw new IllegalArgumentException("Command \"remove_head\" takes no arguments!\n");
        }
    }
}
