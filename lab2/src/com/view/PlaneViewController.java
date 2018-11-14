package com.view;

import com.MainApp;
import com.model.Plane;
import com.model.PlaneID;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.DateTimeException;
/**
 * Контроллер для  поля додатку де буде відображатися дані з колекції літаків.
 * @author Bogdan Ovsienko
 */
public class PlaneViewController {
    @FXML
    private TableView<Plane> planeTable;
    @FXML
    private TableColumn<Plane, String> pointStartColumn;
    @FXML
    private TableColumn<Plane, String> pointFinishColumn;
    @FXML
    private Label id;
    @FXML
    private TextField pointStartField;
    @FXML
    private TextField pointFinishField;
    @FXML
    private TextField raceField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField dateField;
    private MainApp mainApp;

    /**
     * Конструктор
     */
    public PlaneViewController() {
    }
    /**
     * Ініціалізація класа-контроллера. Визивається коли fxml-файл буде загруженим.
     */
    @FXML
    private void initialize() {
        pointStartColumn.setCellValueFactory(cellData -> cellData.getValue().pointStartProperty());
        pointFinishColumn.setCellValueFactory(cellData -> cellData.getValue().pointFinishProperty());
        showPersonDetails(null);
        planeTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails((PlaneID)newValue));
    }

    /**
     * Викликається головним приложенієм, щоб залишити силку на самого себе.
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        planeTable.setItems(mainApp.getPersonData());
    }
    /**
     * Викликаєтся коли користувач нажимає на кнопку delete.
     */
    @FXML
    private void handleDeletePlane() {
        int selectedIndex = planeTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            planeTable.getItems().remove(selectedIndex);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();
        }
    }
    /**
     * Заповнює всі текстові поля.
     * Якщо передано = null, то всі текстові поля очищуються.
     * @param plane —  типа Plane або null
     */
    private void showPersonDetails(PlaneID plane) {
        if (plane != null) {
            id.setText(Integer.toString(plane.getId()));
            pointStartField.setText(plane.getPointStart());
            pointFinishField.setText(plane.getPointFinish());
            raceField.setText(Integer.toString(plane.getRace()));
            typeField.setText(plane.getType());
            dateField.setText(plane.dateToString());
        } else {
            pointStartField.setText("");
            pointFinishField.setText("");
            raceField.setText("");
            typeField.setText("");
            dateField.setText("");
        }
    }
    /**
     * Викликається коли користувач нажав кнопку ОК
     * Зберігає зміни, при модифікації обєкта.
     */
    @FXML
    private void handleEditPlane(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error updating object");
        try {
            Plane plane = planeTable.getItems().get(planeTable.getSelectionModel().getSelectedIndex());
            plane.setPointStart(pointStartField.getText());
            plane.setPointFinish(pointFinishField.getText());
            plane.setRace(Integer.parseInt(raceField.getText()));
            plane.setType(typeField.getText());
            plane.setDate(dateField.getText());
        }catch (NumberFormatException e){
            alert.setContentText("Rase not number");
            alert.showAndWait();
        }catch (IllegalArgumentException e){
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }catch (DateTimeException e){
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}