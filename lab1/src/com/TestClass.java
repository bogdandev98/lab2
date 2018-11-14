package com;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import java.io.*;
import java.time.DateTimeException;
import java.util.*;
/**
 * Клас зберігає список літаків
 * Клас готовий до автоматичної сериалізації в jason файл
 * Містить метод static void main()
 * Реалізує запис в лог
 * @author Bogdan Ovsienko
 */
@JsonAutoDetect
public class TestClass {
    /**  Список літаків */
    @JsonDeserialize(as=ArrayList.class)
    private ArrayList<Plane> planes = new ArrayList<Plane>();
    /**  Логер */
    @JsonIgnore
    private static final Logger logger  = Logger.getLogger(TestClass.class);
    /**
     * Точка входу
     * Зчитує дані з файлу
     * @param args: String[] - шлях до файла формату csv
     */
    public static void main(String[] args) throws IOException{
        String csvFile = args[0];
        Reader reader = new FileReader(csvFile);
        Iterable<CSVRecord> records= CSVFormat.EXCEL.withDelimiter(';').parse(reader);
        int i=0;
        TestClass testClass = new TestClass();
        for(CSVRecord r: records) {
            if (i > 0)
                testClass.addPlane(r);
            i++;
        }
        Collections.sort(testClass.getPlanes());
        testClass.writeJson(csvFile.replace(".csv",".json"));
    }

    /**
     * Додає літак у список
     * Обробляє помилки
     * @param  jsonFile: String -  - шлях до файла формату json
     */
    public void writeJson(String jsonFile) throws IOException{
        Writer writer = new FileWriter(new File(jsonFile));
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(writer, this);
    }
    /**
     * Геттер поля planes
     * @return planes: ArrayList<Plane>
     */
    public ArrayList<Plane> getPlanes() {
        return planes;
    }

    /**
     * Додає літак у список
     * Обробляє помилки
     * @param  record: CSVRecord - зчитана строка з csv файлу
     */
    public void addPlane(CSVRecord record){
        try {
            planes.add(new PlaneID(record.get(0), record.get(1), Integer.parseInt(record.get(2)), record.get(3), record.get(4)));
            logger.debug("Plane add.");
        }catch (NumberFormatException e){
            logger.error("Rase not number");
        }catch (IllegalArgumentException e){
            logger.error(e.getMessage());
        }catch (DateTimeException e){
            logger.error(e.getMessage());
        }
    }
}