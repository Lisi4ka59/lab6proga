package com.lisi4ka.commands;

import java.util.NoSuchElementException;

public class NullCommand implements Command{
    @Override
    public String execute(){
        throw new NoSuchElementException("Can not execute this command on server!");
    }
}
