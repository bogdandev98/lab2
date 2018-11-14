package com;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.controller.LoggerViewController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Plane;
import com.model.PlaneID;
import com.controller.PlaneViewController;
import com.controller.RootLayoutController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
/**
 * Головний клас програмного додатка, завантажує і зберігає дані, ініціалізує макети
 * @author Bogdan Ovsienko
 */
public class MainApp extends javafx.application.Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Plane> planeData = FXCollections.observableArrayList();
    private static final Logger logger  = Logger.getLogger(MainApp.class);
    private String filePath;
    private static DataBase dataBase;

    /**
     * Конструктор
     */
    public MainApp() {}

    /**
     * Повертає список обєктів типу Plane.
     * @return
     */
    public ObservableList<Plane> getPlaneData() {
        return planeData;
    }
    /**
     * Ініціалізує сцену, головне вікно додатку.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Plane");
        this.primaryStage.getIcons().add(new Image("file:res/images.png"));
        initRootLayout();
        showPlaneView();
    }

    /**
     * Ініціалізує корінний макет.
     */
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            controller.disableButton();
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Показує в корневом макеті поля обєкта типу PlaneID.
     */
    public void showPlaneView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/PlaneView.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            rootLayout.setCenter(personOverview);
            PlaneViewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Загружає дані з csv-файла і запоню ними колекцію типу PlaneID.
     * @param file
     */
    public void loadPlaneFromCsv(File file){
        try {
            Reader reader = new FileReader(file);
            Iterable<CSVRecord> records= CSVFormat.EXCEL.withDelimiter(';').parse(reader);
            int i=0;
            for(CSVRecord record: records) {
                if (i > 0){
                    try {
                        planeData.add(new PlaneID(record.get(0), record.get(1), Integer.parseInt(record.get(2)), record.get(3), record.get(4)));
                        logger.debug("Plane add.");
                    }catch (NumberFormatException e){
                        logger.error("Rase not number");
                    }catch (IllegalArgumentException e){
                        logger.error(e.getMessage());
                    }catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
                i++;
            }
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }
    /**
     * Зберігає дані в json-файл.
     * @param file
     */
    public void saveToJson(File file){
        try {
            Writer writer = new FileWriter(file);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(writer, planeData);
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }
    /**
     * Повертає останній відкритий файл.
     * @return
     */
    public File getPersonFilePath() {
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Задає шлях до поточного загруженному файлу.
     * @param file - файл або null
     */
    public void setPersonFilePath(File file) {
        if(file==null){
            filePath=null;
        }else
            filePath = file.getPath();
    }
    /**
     * Повертає головну сцену.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    /**
     * Повертає базу даних
     * @return
     */
    public DataBase getDataBase() {
        return dataBase;
    }
    /**
     * Ініціалізує вікно на яком виводится logger.
     */
    public void showLogger()throws SQLException{
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/LoggerView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Logger");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            LoggerViewController controller = loader.getController();
            ResultSet set = dataBase.showLogger();
            String line="";
            while (set.next()){
                line +=set.getTimestamp(1)+"\t"+set.getString(2)+"\n";
            }
            controller.setTextArea(line);
            dialogStage.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Створює базу даних і запускає додаток
     */
    public static void main(String[] args) {
        try {
            args="org.postgresql.Driver jdbc:postgresql://localhost:5432/Planes postgres 159753".split(" ");
            MainApp.dataBase = new DataBase(args[0],args[1],args[2],args[3]);
        }catch (ClassNotFoundException e){
            logger.error("Data base not found");
        }
        launch(args);
    }
}