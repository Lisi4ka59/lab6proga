package com.lisi4ka.utils;

import java.io.Serializable;

public class PackagedCommand implements Serializable{
        private String commandName;
        private String commandArguments;
        private String commandMisc;
        public void setCommandName(String name){
            commandName = name;
        }
        public void setCommandArguments(String arguments){
            commandArguments = arguments;
        }
        public  void setCommandMisc(String misc){
            commandMisc = misc;
        }
        public String getCommandName(){
            return commandName;
        }
        public String getCommandArguments(){
            return commandArguments;
        }
        public String getCommandMisc(){
            return commandMisc;
        }
}
