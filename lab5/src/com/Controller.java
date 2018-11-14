package com;

import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.util.List;

public class Controller {
    @FXML
    ScatterChart scatterChart;
    @FXML
    RadioButton radioButton11;
    @FXML
    RadioButton radioButton12;
    @FXML
    RadioButton radioButton13;
    @FXML
    RadioButton radioButton14;
    @FXML
    RadioButton radioButton15;
    @FXML
    RadioButton radioButton16;
    @FXML
    RadioButton radioButton17;
    @FXML
    RadioButton radioButton18;
    @FXML
    RadioButton radioButton21;
    @FXML
    RadioButton radioButton22;
    @FXML
    RadioButton radioButton23;
    @FXML
    RadioButton radioButton24;
    @FXML
    RadioButton radioButton25;
    @FXML
    RadioButton radioButton26;
    @FXML
    RadioButton radioButton27;
    @FXML
    RadioButton radioButton28;

    XYChart.Series series1 = new XYChart.Series();
    XYChart.Series series2 = new XYChart.Series();
    List<List<Double>> data;

    public void initialize(List<List<Double>> data){
        this.data=data;

        ToggleGroup group1 = new ToggleGroup();
        ToggleGroup group2 = new ToggleGroup();
        radioButton11.setSelected(true);
        radioButton21.setSelected(true);

        radioButton11.setToggleGroup(group1);
        radioButton12.setToggleGroup(group1);
        radioButton13.setToggleGroup(group1);
        radioButton14.setToggleGroup(group1);
        radioButton15.setToggleGroup(group1);
        radioButton16.setToggleGroup(group1);
        radioButton17.setToggleGroup(group1);
        radioButton18.setToggleGroup(group1);

        radioButton21.setToggleGroup(group2);
        radioButton22.setToggleGroup(group2);
        radioButton23.setToggleGroup(group2);
        radioButton24.setToggleGroup(group2);
        radioButton25.setToggleGroup(group2);
        radioButton26.setToggleGroup(group2);
        radioButton27.setToggleGroup(group2);
        radioButton28.setToggleGroup(group2);

        radioButton11.setUserData(0);
        radioButton12.setUserData(1);
        radioButton13.setUserData(2);
        radioButton14.setUserData(3);
        radioButton15.setUserData(4);
        radioButton16.setUserData(5);
        radioButton17.setUserData(6);
        radioButton18.setUserData(7);

        radioButton21.setUserData(0);
        radioButton22.setUserData(1);
        radioButton23.setUserData(2);
        radioButton24.setUserData(3);
        radioButton25.setUserData(4);
        radioButton26.setUserData(5);
        radioButton27.setUserData(6);
        radioButton28.setUserData(7);

    }
    @FXML
    public void paint(){
        scatterChart.getData().removeAll(series1,series2);
        series1 = new XYChart.Series();
        series2 = new XYChart.Series();
        series1.setName("-1");
        series2.setName("-0");
        for (List<Double> vector:data) {
            double X = vector.get((int)radioButton11.getToggleGroup().getSelectedToggle().getUserData());
            double Y = vector.get((int)radioButton21.getToggleGroup().getSelectedToggle().getUserData());
            XYChart.Data xyChart = new XYChart.Data(X,Y);
            if (vector.get(8).equals(1.0)){
                series1.getData().add(xyChart);
            }
            if (vector.get(8).equals(0.0))
                series2.getData().add(xyChart);
        }
        scatterChart.getData().addAll(series1,series2);
    }
}
