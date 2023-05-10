package com.lisi4ka.validation;

import com.lisi4ka.utils.PackagedCommand;

public class PrintUniqueStandardOfLivingValid {
    public PackagedCommand printUniqueStandardOfLivingValid(String[] commandText){
        if (commandText.length == 1){
            return new PackagedCommand(commandText[0], null);
        }
        else {
            throw new IllegalArgumentException("Command \"print_unique_standard_of_living\" takes no arguments!\n");
        }
    }
}
