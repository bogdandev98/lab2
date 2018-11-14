package com.controller;

import java.io.File;
import java.sql.SQLException;

import com.MainApp;
import com.model.Plane;
import com.model.PlaneID;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

/**
 * Контроллер для  базового макета додатка, складаєтся з строки меню
 * і місця, де будуть разміщені інші элементи JavaFX.
 * @author Bogdan Ovsienko
 */
public class RootLayoutController {
    /**
     * Силка на головне приложеніє
     */
    private MainApp mainApp;
    @FXML
    private MenuItem saveToDataBaseButton;
    /**
     * Викликаєтся головним приложенієм, щоб залишити ссику на самого себе.
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    /**
     * Блокуэ кнопку Save to database.
     */
    public void disableButton() {
        saveToDataBaseButton.setDisable(true);
    }

    /**
     * Відкриває незаповнене вікно.
     */
    @FXML
    private void handleNew() {
        mainApp.getPlaneData().clear();
        mainApp.setPersonFilePath(null);
    }

    /**
     * Відкриває FileChooser, щобкористуач міг відкрити файл для загрузки даних.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Csv files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (file != null) {
            mainApp.loadPlaneFromCsv(file);
        }
        mainApp.setPersonFilePath(null);
    }

    /**
     * Зберігає дані в файл , який в даний час використовуєтся.
     * Якщо файл не було вибрано, то вілображаєтся "save as".
     */
    @FXML
    private void handleSave() {
        File personFile = mainApp.getPersonFilePath();
        if (personFile != null) {
            mainApp.saveToJson(personFile);
        } else {
            handleSaveAs();
        }
    }

    /**
     * Відкриває FileChooser, щоб користувач міг вибрати файл куди зберігати дані
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Json files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        if (file != null) {
            if (!file.getPath().endsWith(".json")) {
                file = new File(file.getPath() + ".json");
            }
            mainApp.saveToJson(file);
            mainApp.setPersonFilePath(file);
        }
    }
    /**
     * Створює таблицю в БД
     */
    @FXML
    private void handleCreateTable() {
        try {
            mainApp.getDataBase().create();
            saveToDataBaseButton.setDisable(false);
        } catch (SQLException e) {
            mainApp.getDataBase().insertMassageToLogger("Error: Data not save; " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Table not create");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    /**
     * Зберігає усі дані в таблицю
     */
    @FXML
    private void handleSaveToDataBase() {
        for (Plane plane : mainApp.getPlaneData()) {
            try {
                mainApp.getDataBase().insert((PlaneID) plane);
            } catch (SQLException e) {
                mainApp.getDataBase().insertMassageToLogger("Error: Data not save; " + e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Data not save");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }
    /**
     * Відображає дані з журнальної таблиці
     */
    @FXML
    private void handleShowLogger(){
        try {
            mainApp.showLogger();
        }catch (SQLException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Logger not load");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}