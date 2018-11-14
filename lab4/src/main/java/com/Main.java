package com;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static List<List<Number>> loadData(String filename) {
        List result = new ArrayList();
        try {
            List<String> list = new ArrayList<String>();
            BufferedReader br = null;
            try {
                FileInputStream fstream = new FileInputStream(filename);
                br = new BufferedReader(new InputStreamReader(fstream));
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
                List<Number> numberList = new ArrayList<Number>();
                numberList.add(Integer.parseInt(arr[0]));
                numberList.add(Integer.parseInt(arr[1]));
                numberList.add(Integer.parseInt(arr[2]));
                numberList.add(Integer.parseInt(arr[3]));
                numberList.add(Integer.parseInt(arr[4]));
                numberList.add(Double.parseDouble(arr[5]));
                numberList.add(Double.parseDouble(arr[6]));
                numberList.add(Integer.parseInt(arr[7]));
                numberList.add(Integer.parseInt(arr[8]));
                result.add(numberList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Input name of file with data: ");
        String filename = in.nextLine();
//"data20180405.csv";
        Random rand = new Random(5);
        List<List<Number>> list = loadData(filename);
        List<List<Number>> listTest = new ArrayList<List<Number>>();
        List<List<Number>> listTeach = new ArrayList<List<Number>>();
        int row = list.size();
        int p30 = (int) (list.size()*0.3);
        ArrayList<Integer> list_random = new ArrayList<Integer>();
        while (list_random.size() < p30) {
            int newRandom = rand.nextInt(row);
            if (!list_random.contains(newRandom))
                list_random.add(newRandom);
        }
        for (int i = 0; i < row; i++) {
            if (!list_random.contains(i))
                listTeach.add(list.get(i));
            else
                listTest.add(list.get(i));
        }
        row = listTest.size();
        int col = listTest.get(0).size();
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(row, col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                testMatrix.addToEntry(i, j, listTest.get(i).get(j).doubleValue());
            }
        }
        row = listTeach.size();
        col = listTeach.get(0).size();
        RealMatrix teachMatrix = MatrixUtils.createRealMatrix(row, col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                teachMatrix.addToEntry(i, j, listTeach.get(i).get(j).doubleValue());
            }
        }
        Perceptron perceptron = new Perceptron(list.get(0).size() - 1);
        RealVector vector = perceptron.vectorizedForwardPass(teachMatrix);
        perceptron.trainUntilConvergence(teachMatrix, vector, 500);
        vector = perceptron.vectorizedForwardPass(testMatrix);
        RealVector trueVector = testMatrix.getColumnVector(8);
        System.out.print(perceptron.result(vector, trueVector));
    }
}


