package com.lisi4ka.commands;

import com.github.cliftonlabs.json_simple.*;
import com.lisi4ka.models.City;
import com.lisi4ka.utils.CityComparator;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LoadCommand implements Command {
    private final List<City> collection;
    public static String filepath;

    public LoadCommand(List<City> collection){

        this.collection = collection;
    }
    private String load() {
        String path = System.getenv("CITIES_PATH");
        try {
            Reader reader = Files.newBufferedReader(Paths.get(path));
            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(reader);
            JsonArray jsonArray = (JsonArray)jsonObject.get("cities");
            filepath = path;
            for (Object obj: jsonArray) {
                JsonObject jo = (JsonObject) obj;
                City city = new City(jo);
                collection.add(city);
            }
            collection.sort(new CityComparator());
            return "Collection uploaded";
        } catch (JsonException | IllegalArgumentException | NullPointerException e) {
            return "Can not upload collection, data in the file incorrect! " + e.getMessage()  + "\n";
        } catch (SecurityException e) {
            return "Do not have sufficient rights to read file %s\n" + path + "\n";
        } catch (IOException e) {
            return "Can not upload collection, the file " + path + " does not exist!\n";
        }
    }
    @Override
    public String execute(){
        return load();
    }
}
