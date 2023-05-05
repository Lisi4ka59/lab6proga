package com.lisi4ka.commands;

import com.lisi4ka.common.Invoker;
import com.lisi4ka.models.City;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExecuteScriptCommand implements Command{
    private final List<City> collection;
    static ArrayList<String> exeRecursion = new ArrayList<>();
    public ExecuteScriptCommand(List<City> collection){
        this.collection = collection;
    }

    private String exe(String scriptContent){
//        System.out.printf("Start of executing script from file \"%s\"\n", fileName);
//        if (exeRecursion.contains(fileName)) {
//            System.out.printf("Can not execute script from file %s, because it can be very dangerous as it can cause recursion!\n", fileName);
//            return;
//        }
//        exeRecursion.add(fileName);
        Invoker invoker = new Invoker(collection);
        StringBuilder answer = new StringBuilder();
        ArrayList<String> lines = readScript(scriptContent);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            answer.append("Running... ").append(line).append("\n");
            if ("add".equals(line) || "update_id".equals(line)){
                int j = i + 12;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(line).append(" ");
                i++;
                for (; i < j; i++)
                    stringBuilder.append(lines.get(i)).append(i!=j-1 ? ",":"");
                line = stringBuilder.toString();
            }
            else
                i++;
            //answer.append(invoker.run(line)).append("\n");
        }
        return answer.toString();
//        exeRecursion.remove(fileName);
//        System.out.printf("End of executing script from file \"%s\"\n", fileName);
    }

    @Override
    public String execute(String fileName) {
        return exe(fileName);
    }

    private ArrayList<String> readScript(String scriptContent){
        return new ArrayList<>(Arrays.asList(scriptContent.split("\n")));
    }
}
