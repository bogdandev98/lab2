package com;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.DateTimeException;

/**
 * Використовуєтся для зберігання ID літака
 * Клас готовий до автоматичної сериалізації в jason файл
 * Дочіній клас від класу Plane
 * @author Bogdan Ovsienko
 */
@JsonAutoDetect
public class PlaneID extends Plane{
    /**  Статична змінна для генерації ID літаків */
    private static int statId = 0;
    /**  ID літака */
    private int id;

    /** Конструктор, генерує унікальне id
     * @param pointStart: String - Пункт відправлення
     * @param pointFinish: String - Пункт призначення
     * @param race: int - Номер рейсу
     * @param type: String - Тип літака
     * @param date: String -  Час вильоту
     * @throws IllegalArgumentException - невірний формат вхідного параметру
     * @throws DateTimeException - невірний формат дати
     */
    public PlaneID(String pointStart, String pointFinish, int race, String type, String date) {
        super(pointStart, pointFinish, race, type, date);
        this.id = statId;
        statId++;

    }

    /**
     * Геттер поля id
     * @return id: int
     */
    public int getId() {
        return id;
    }

    /**
     * Перетворює дані в строку
     * @return string: String
     */
    @Override
    public String toString() {
        return "Plane "+ id+"  "+ super.toString() ;
    }
}