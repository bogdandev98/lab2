package com;

import java.io.*;
import java.time.DateTimeException;
import java.util.prefs.Preferences;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Plane;
import com.model.PlaneID;
import com.view.PlaneViewController;
import com.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
/**
 * Головний клас програмного додатка, завантажує і зберігає дані, ініціалізує макети
 * @author Bogdan Ovsienko
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Plane> planeData = FXCollections.observableArrayList();
    private static final Logger logger  = Logger.getLogger(MainApp.class);

    /**
     * Конструктор
     */
    public MainApp() {    }

    /**
     * Повертає список з Літаками.
     * @return
     */
    public ObservableList<Plane> getPersonData() {
        return planeData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Plane");
        this.primaryStage.getIcons().add(new Image("file:res/images.png"));
        initRootLayout();
        showPlaneView();
    }

    /**
     * Ініціалізує корневой макет.
     */
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Показує в корневом макеті дані про літаки.
     */
    public void showPlaneView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PlaneView.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            rootLayout.setCenter(personOverview);
            PlaneViewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Загружає дані з csv-файла і запоню ними колекцію літаків.
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
                    }catch (DateTimeException e) {
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
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
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
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");
            primaryStage.setTitle("AddressApp");
        }
    }
    /**
     * Повертає головну сцену.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}