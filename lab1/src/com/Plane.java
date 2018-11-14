package com;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.DateTimeException;
import java.util.Calendar;
import java.util.Date;
/**
    * Використовуєтся для зберігання даних про літак
    * Клас готовий до автоматичної сериалізації в jason файл
    * Наслідуєтся від інтерфейсу  Comparable
    * @author Bogdan Ovsienko
 */
@JsonAutoDetect
public class Plane implements Comparable<Plane>{
    /**  Пункт відправлення */
    @JsonProperty("Departure point")
    private String pointStart;
    /**  Пункт призначення */
    @JsonProperty("Destination point")
    private String pointFinish;
    /** Номер рейсу */
    @JsonProperty("Flight number")
    private int race;
    /** Тип літака */
    @JsonProperty("Type of airplane")
    private String type;
    /** Час вильоту */
    @JsonProperty("Date and Time")
    private Date date ;

    /** Конструктор
     * @param pointStart: String - Пункт відправлення
     * @param pointFinish: String - Пункт призначення
     * @param race: int - Номер рейсу
     * @param type: String - Тип літака
     * @param date: String -  Час вильоту
     * @throws IllegalArgumentException - невірний формат вхідного параметру
     * @throws DateTimeException - невірний формат дати
     */
    public Plane(String pointStart, String pointFinish, int race, String type, String date){
        if(!pointStart.equals(""))
            this.pointStart = pointStart;
        else
            throw new IllegalArgumentException("Departure point - empty line");
        if(!pointFinish.equals(""))
            this.pointFinish = pointFinish;
        else
            throw new IllegalArgumentException("Destination point - empty line");
        if(race>=1000 && race<10000)
            this.race = race;
        else
            throw new IllegalArgumentException("The flight number must be four-digit ");
        if(type.equalsIgnoreCase("passager") ||type.equalsIgnoreCase("freight"))
            this.type = type;
        else
            throw new IllegalArgumentException("Wrong type plane");
        DateHelper dateHelper = new DateHelper();
        this.date = toDate(date);

    }
    /**
     * Порівнює екземпляр класу з іншим екземпляром по полю date
     * Повертає позицію на якій позиції знаходится текущий екземпляр відносно іншого в порядку сортіровки
     */
    @Override
    public int compareTo(Plane o) {
        return date.compareTo(o.getDate());
    }
    /**
     * Геттер поля date
     * @return date: Date
     */
    public Date getDate() {
        return date;
    }
    /**
     * Геттер поля date
     * @return date: String
     */
    @JsonGetter("Date and Time")
    public String dateToString(){
        return date.toString();
    }
    /**
     * Геттер поля pointStart
     * @return pointStart: String
     */
    public String getPointStart() {
        return pointStart;
    }
    /**
     * Геттер поля pointFinish
     * @return pointFinish: String
     */
    public String getPointFinish() {
        return pointFinish;
    }
    /**
     * Геттер поля race
     * @return race: int
     */
    public int getRace() {
        return race;
    }
    /**
     * Геттер поля type
     * @return type: String
     */
    public String getType() {
        return type;
    }
    /**
     * Перетворює дані в строку
     * @return string: String
     */
    @Override
    public String toString() {
        return "Departure point: "+pointStart+"; Destination point: "+pointFinish+"; Flight number: "+race+"; Type of airplane: "+type+"; Date and Time: "+date;
    }
    /**
     * Перетворює передану строку в тип Data
     * @param line: String -  Час вильоту
     * @return date: Date
     */
    private Date toDate(String line){
        try {
            String[] strings = line.split(" ");
            String[] date = strings[0].split("\\.");
            String[] time = strings[1].split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(date[2]), Integer.parseInt(date[1])-1,Integer.parseInt(date[0]),Integer.parseInt(time[0]),Integer.parseInt(time[1]),0);
            return calendar.getTime();
        }catch (Exception e){
            throw new DateTimeException("Invalid date format");
        }
    }
}
