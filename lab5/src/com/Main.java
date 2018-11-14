package com;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene( root);
        stage.setTitle("lab5");
        stage.setScene(scene);
        Controller controller = loader.getController();
        stage.show();
        controller.initialize(loadData("/data20180405.csv"));
    }

    private static List<List<Double>> loadData(String filename) {
        List result = new ArrayList();
        try {
            List<String> list = new ArrayList<String>();
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
                String str;
                while ((str = br.readLine()) != null) {
                    list.add(str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (String str : list) {
                String arr[] = str.split(",");
                List<Double> numberList = new ArrayList<>();
                numberList.add(Double.parseDouble(arr[0]));
                numberList.add(Double.parseDouble(arr[1]));
                numberList.add(Double.parseDouble(arr[2]));
                numberList.add(Double.parseDouble(arr[3]));
                numberList.add(Double.parseDouble(arr[4]));
                numberList.add(Double.parseDouble(arr[5]));
                numberList.add(Double.parseDouble(arr[6]));
                numberList.add(Double.parseDouble(arr[7]));
                numberList.add(Double.parseDouble(arr[8]));
                result.add(numberList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static void main(String args[]) {
        launch(args);
    }
}