package com.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private StringProperty pointStart;
    /**  Пункт призначення */
    @JsonProperty("Destination point")
    private StringProperty pointFinish;
    /** Номер рейсу */
    @JsonProperty("Flight number")
    private IntegerProperty race;
    /** Тип літака */
    @JsonProperty("Type of airplane")
    private StringProperty type;
    /** Час вильоту */
    @JsonProperty("Date and Time")
    private ObjectProperty<Date> date ;

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
            this.pointStart = new SimpleStringProperty(pointStart);
        else
            throw new IllegalArgumentException("Departure point - empty line");
        if(!pointFinish.equals(""))
            this.pointFinish = new SimpleStringProperty(pointFinish);
        else
            throw new IllegalArgumentException("Destination point - empty line");
        if(race>=1000 && race<10000)
            this.race = new SimpleIntegerProperty(race);
        else
            throw new IllegalArgumentException("The flight number must be four-digit ");
        if(type.equalsIgnoreCase("passager") ||type.equalsIgnoreCase("freight"))
            this.type = new SimpleStringProperty(type);
        else
            throw new IllegalArgumentException("Wrong type plane");
        this.date = new SimpleObjectProperty<Date>(toDate(date));

    }

    public StringProperty pointStartProperty() {
        return pointStart;
    }

    public void setPointStart(String pointStart) {
        if(!pointStart.equals(""))
            this.pointStart.set(pointStart);
        else
            throw new IllegalArgumentException("Departure point - empty line");
    }

    public StringProperty pointFinishProperty() {
        return pointFinish;
    }

    public void setPointFinish(String pointFinish) {
        if(!pointFinish.equals(""))
            this.pointFinish.set(pointFinish);
        else
            throw new IllegalArgumentException("Destination point - empty line");
    }

    public IntegerProperty raceProperty() {
        return race;
    }

    public void setRace(int race) {
        if(race>=1000 && race<10000)
            this.race.set(race);
        else
            throw new IllegalArgumentException("The flight number must be four-digit ");;
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        if(type.equalsIgnoreCase("passager") ||type.equalsIgnoreCase("freight"))
            this.type.set(type);
        else
            throw new IllegalArgumentException("Wrong type plane");
    }

    public ObjectProperty<Date> dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(toDate(date));
    }

    /**
     * Порівнює екземпляр класу з іншим екземпляром по полю date
     * Повертає позицію на якій позиції знаходится текущий екземпляр відносно іншого в порядку сортіровки
     */
    @Override
    public int compareTo(Plane o) {
        return date.get().compareTo(o.getDate());
    }
    /**
     * Геттер поля date
     * @return date: Date
     */
    public Date getDate() {
        return date.get();
    }
    /**
     * Геттер поля date
     * @return date: String
     */
    @JsonGetter("Date and Time")
    public String dateToString(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date.get());
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return formatter.format(calendar.getTime());
    }
    /**
     * Геттер поля pointStart
     * @return pointStart: String
     */
    public String getPointStart() {
        return pointStart.get();
    }
    /**
     * Геттер поля pointFinish
     * @return pointFinish: String
     */
    public String getPointFinish() {
        return pointFinish.get();
    }
    /**
     * Геттер поля race
     * @return race: int
     */
    public int getRace() {
        return race.get();
    }
    /**
     * Геттер поля type
     * @return type: String
     */
    public String getType() {
        return type.get();
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
              DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy kk:mm");
              return formatter.parse(line);
        }catch (Exception e){
            throw new DateTimeException("Invalid date format");
        }
    }
}
