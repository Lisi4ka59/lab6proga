package com.lisi4ka.commands;

import com.lisi4ka.models.*;
import com.lisi4ka.utils.CityComparator;
import java.util.Date;
import java.util.List;
import static com.lisi4ka.utils.Checker.checkDate;
import static com.lisi4ka.utils.CityLinkedList.idRepeat;
import static com.lisi4ka.utils.DefaultSave.defaultSave;

/**
 * add command, use to make a city
 * @autor Mikhail Nachinkin
 * @version 1.0
 */
public class AddCommand implements Command {
    private final List<City> collection;
    public AddCommand(List<City> collection){
        this.collection = collection;
    }
    @Override
    public String execute(String args) {
        City city = getCityArgs(args);
        collection.add(city);
        idRepeat+=1;
        collection.sort(new CityComparator());
        return "Congratulations! City added to collection" + defaultSave(collection);
    }
    public static City getCityArgs(String args){
        String[] cityArgs = args.trim().split(",");
        if (!(cityArgs.length == 11)){
            throw new IllegalArgumentException();
        }
        String name = cityArgs[0];
        double x = Double.parseDouble(cityArgs[1]);
        float y = Float.parseFloat(cityArgs[2]);
        double area = Double.parseDouble(cityArgs[3]);
        long population = Long.parseLong(cityArgs[4]);
        int metersAboveSeaLevel = Integer.parseInt(cityArgs[5]);
        Climate climate = Climate.fromInt(Integer.parseInt(cityArgs[6]));
        Government government = Government.fromInt(Integer.parseInt(cityArgs[7]));
        StandardOfLiving standardOfLiving = StandardOfLiving.fromInt(Integer.parseInt(cityArgs[8]));
        long age = Long.parseLong(cityArgs[9]);
        Date birthday = checkDate(cityArgs[10]);
        if (x <= -25 || area <= 0 || population <= 0 || metersAboveSeaLevel>8849 ||
                metersAboveSeaLevel < -432 || age <= 0) {
            throw new IllegalArgumentException();
        }
        Coordinates coordinates = new Coordinates(x, y);
        Human governor = new Human(age, birthday);
        return new City(name, coordinates, population, area, metersAboveSeaLevel, climate, government,
                standardOfLiving, governor);
    }
}