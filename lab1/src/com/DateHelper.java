package com;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Клас реалізує функціонал роботи з атою
 * @author Bogdan Ovsienko
 */
public class DateHelper {
    /**  Статичне поле регулярний вираз для перевірки формату дати */
    private static final String DATE_PATTERN
            = "(0?[1-9]|[12][0-9]|3[01])\\.(0?[1-9]|1[012])" + "\\.((19|20)\\d\\d)"+" ([01]?[0-9]|2[0-3]):[0-5][0-9]";
    /** Поля для збереження патерна регулярно виразу*/
    private final Pattern pattern;
    /**  Пункт відправлення */
    private Matcher matcher;
    /** Конструктор без параметрів */
    public DateHelper() {
        pattern = Pattern.compile(DATE_PATTERN);
    }

    /**
     * Перевіряє чи передана строка віповідає регулярному виразу для даних
     * @param date: String -  Час вильоту
     */
    public boolean validate(String date) {
        matcher = pattern.matcher(date);
        if (matcher.matches()) {
            matcher.reset();
            if (matcher.find()) {
                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));
                if ("31".equals(day)) {
                    return Arrays.asList(new String[]{"1", "01", "3", "03",
                            "5", "05", "7", "07", "8", "08", "10", "12"})
                            .contains(month);
                } else if ("2".equals(month) || "02".equals(month)) {
                    if (year % 4 == 0) {
                        if (year % 100 == 0) {
                            if (year % 400 == 0) {
                                // Високосний рік
                                return Integer.parseInt(day) <= 29;
                            }
                            // Невисокосный год
                            return Integer.parseInt(day) <= 28;
                        }
                        // Високосний рік
                        return Integer.parseInt(day) <= 29;
                    } else {
                        // Невисокосний рік
                        return Integer.parseInt(day) <= 28;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Перетворює передану строку в тип Data
     * @param line: String -  Час вильоту
     * @return date: Date
     */
    public Date toDate(String line){
        String[] strings = line.split(" ");
        String[] date = strings[0].split("\\.");
        String[] time = strings[1].split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(date[2]), Integer.parseInt(date[1])-1,Integer.parseInt(date[0]),Integer.parseInt(time[0]),Integer.parseInt(time[1]),0);
        return calendar.getTime();
    }
}