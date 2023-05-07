package com.lisi4ka.validation;

import com.lisi4ka.utils.PackagedCommand;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class ExecuteScriptValid {
    static ArrayList<String> exeRecursion = new ArrayList<>();
    static ArrayList<PackagedCommand> commandsToRun = new ArrayList<>();
    public PackagedCommand[] executeScriptValid(String[] commandText) throws IllegalArgumentException {
        String fileName = commandText[1];
        if (commandText.length == 2) {
            System.out.printf("Start of executing script from file \"%s\"\n", fileName);
            if (exeRecursion.contains(fileName)) {
                System.out.printf("Can not execute script from file %s, because it can be very dangerous as it can cause recursion!\n", fileName);
                throw new NegativeArraySizeException();
            }
            exeRecursion.add(fileName);



            ArrayList<String> lines = new ArrayList<>();
            try {
                FileInputStream fStream = new FileInputStream(fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(fStream));
                String line;

                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            } catch (SecurityException e) {
                System.out.printf("Do not have sufficient rights to execute file %s\n", fileName);
            } catch (IOException e) {
                System.out.printf("File %s does not exist\n", fileName);
            }

            for (int i = 0; i < lines.size(); i++) {
                String[] line = lines.get(i).split(" ");
                if ("execute_script".equals(line[0])) {
                    commandsToRun.addAll(Arrays.stream(executeScriptValid(line)).toList());
                } else {
                    if (line.length == 1) {
                        if ("add".equals(line[0]) || "update_id".equals(line[0])) {
                            try {
                                int j = i + 12;
                                i++;
                                int g = 1;
                                StringBuilder lineArgs = null;
                                for (; i < j; i++) {
                                    lineArgs.append(line[g]);
                                    g++;
                                }
                            } catch (Exception ex) {
                                System.out.printf("Illegal arguments for command %s!\n", line[0]);
                            }
                        } else {
                            i++;
                            commandsToRun.add(new PackagedCommand(line[0], null));
                        }
                    } else
                        i++;
                    commandsToRun.add(new PackagedCommand(line[0], line[1]));
                }
            }
            exeRecursion.remove(fileName);
            System.out.printf("End of executing script from file \"%s\"\n", fileName);
            return commandsToRun.toArray(PackagedCommand[]::new);
        }
        else
            throw new IllegalArgumentException("Invalid arguments for command execute_script! ");
    }
}
