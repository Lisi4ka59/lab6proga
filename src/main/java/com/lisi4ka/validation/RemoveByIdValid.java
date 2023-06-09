package com.lisi4ka.validation;

import com.lisi4ka.utils.PackagedCommand;

import static com.lisi4ka.utils.Checker.inputLong;

public class RemoveByIdValid {
    public PackagedCommand removeByIdValid(String[] commandText){
        if (commandText.length == 1){
            long id = inputLong("Input city ID: ");
            return new PackagedCommand(commandText[0], String.format("%d",id));
        }
        else if (commandText.length == 2){
            long id;
            try{
                id = Long.parseLong(commandText[1]);
            }catch (Exception ex){
                throw new IllegalArgumentException("Illegal ID for command \"remove_by_id\"!\n");
            }
            return new PackagedCommand(commandText[0], String.format("%d",id));
        }
        else {
            throw new IllegalArgumentException("Illegal arguments for command \"remove_by_id\"!\n");
        }
    }
}
