package com.controller;

import com.MainApp;
import com.model.Plane;
import com.model.PlaneID;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
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
    @FXML
    private Button ok;
    private MainApp mainApp;
    private boolean isNewPlane;

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
        showPlaneDetails(null);
        ok.setDisable(true);
        planeTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPlaneDetails((PlaneID)newValue));
    }

    /**
     * Викликається головним приложенієм, щоб залишити силку на самого себе.
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        planeTable.setItems(mainApp.getPlaneData());
    }
    /**
     * Викликаєтся коли користувач нажимає на кнопку delete.
     * Видаляє вибрані дані
     */
    @FXML
    private void handleDeletePlane() {
        int selectedIndex = planeTable.getSelectionModel().getSelectedIndex();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(mainApp.getPrimaryStage());
        if (selectedIndex >= 0) {
            PlaneID planeID= (PlaneID)planeTable.getItems().get(selectedIndex);
            planeTable.getItems().remove(selectedIndex);
            if(mainApp.getDataBase().isTable()){
                try {
                    mainApp.getDataBase().delete(planeID.getId());
                }catch (SQLException e){
                    alert.setTitle("Error");
                    alert.setHeaderText("Error deleted object");
                    alert.setContentText("");
                    alert.showAndWait();
                }
            }
        } else {
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();
        }
    }
    /**
     * Викликається коли користувач нажав кнопку ADD
     * Додає новий обєкт класа Plane
     */
    @FXML
    private void handleAddPlane(){
        isNewPlane= true;
        showPlaneDetails( null);
        ok.setDisable(false);
    }
    /**
     * Заповнює всі текстові поля.
     * Якщо передано = null, то всі текстові поля очищуються.
     * @param plane —  типа Plane або null
     */
    private void showPlaneDetails(PlaneID plane) {
        if (plane != null) {
            ok.setDisable(false);
            id.setText(Integer.toString(plane.getId()));
            pointStartField.setText(plane.getPointStart());
            pointFinishField.setText(plane.getPointFinish());
            raceField.setText(Integer.toString(plane.getRace()));
            typeField.setText(plane.getType());
            dateField.setText(plane.dateToString());
        } else {
            id.setText("");
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
    private void handleOkPlane() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error updating object");
        try {
            int index = planeTable.getSelectionModel().getSelectedIndex();
            PlaneID plane;
            if (!isNewPlane) {
                plane = (PlaneID) planeTable.getItems().get(index);
                initializePlane(plane);
                if (mainApp.getDataBase().isTable()) {
                    mainApp.getDataBase().delete(plane.getId());
                    mainApp.getDataBase().insert(plane);
                }
            } else {
                isNewPlane = false;
                plane = new PlaneID();
                initializePlane(plane);
                mainApp.getPlaneData().add(plane);
                if (mainApp.getDataBase().isTable()) {
                    mainApp.getDataBase().insert(plane);
                }
            }
        }catch (NumberFormatException e){
            alert.setContentText("Rase not number");
            alert.showAndWait();
        }catch (IllegalArgumentException e){
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }catch (DateTimeException e){
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }catch (SQLException e){
            mainApp.getDataBase().insertMassageToLogger("Error: Data do not instance to table; "+e.getMessage());
            alert.setContentText("Data do not instance to table :\n"+e.getMessage());
            alert.showAndWait();
        }
    }
    /**
     * Ініціалізує поля обєкта Plane
     */
    private void initializePlane(Plane plane){
        plane.setPointStart(pointStartField.getText());
        plane.setPointFinish(pointFinishField.getText());
        plane.setRace(Integer.parseInt(raceField.getText()));
        plane.setType(typeField.getText());
        plane.setDate(dateField.getText());
    }
}