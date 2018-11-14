package com.view;

import java.io.File;

import com.MainApp;
import javafx.fxml.FXML;
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

    /**
     * Викликаєтся головним приложенієм, щоб залишити ссику на самого себе.
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Відкриває незаповнене вікно.
     */
    @FXML
    private void handleNew() {
        mainApp.getPersonData().clear();
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
}