package com;

import com.model.PlaneID;

import java.sql.*;
/**
 * Клас для співпраці з базою даних
 * @author Bogdan Ovsienko
 */
public class DataBase {
    /**  URL - складаєтся з назви СУБД сервера і порта */
    private final String url;
    /**  пароль */
    private final String password;
    /**  логін */
    private final String login;
    /**  вказує чи створена таблиця */
    private boolean table = false;
    /**
     * Повертає чи створено таблицю
     * @return
     */
    public boolean isTable() {
        return table;
    }
    /** Конструктор
     * @param nameClass: String - Назва класу яка вказує на вид СУБД
     * @param url: String - Пункт призначення
     * @param login: String - логін
     * @param password: String - пароль
     * @throws ClassNotFoundException - незнайдений клас
     */
    public DataBase(String nameClass, String url, String login, String password)throws ClassNotFoundException {
        Class.forName(nameClass);
        this.url = url;
        this.password = password;
        this.login = login;
    }
    /**
     * Створює таблицю
     * @throws SQLException
     */
    public void create()throws SQLException {
        try {
            deleteTable("plane");
        }catch (SQLException e){  }
        Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement();
        statement.executeUpdate(
                "CREATE TABLE plane " +
                        "( id integer NOT NULL," +
                        "point_start varchar(20) NOT NULL," +
                        "point_finish varchar(20) NOT NULL," +
                        "rase varchar(20) NOT NULL," +
                        "type varchar(20) NOT NULL," +
                        "data timestamp NOT NULL," +
                        "PRIMARY KEY (id));");
        table = true;
        try {
            statement.executeUpdate("CREATE TABLE logger (id serial PRIMARY KEY, text varchar(50),data timestamp Default Now());");
        }catch (SQLException e){}
        statement.close();
        connection.close();
    }
    /**
     * Додає обєкт типу PlaneID в таблицю
     * @param plane: PlaneID - літак
     * @throws SQLException
     */
    public void insert(PlaneID plane)throws SQLException {
        try {
            delete(plane.getId());
        }catch (SQLException e){ }
        Connection connection = DriverManager.getConnection(url, login, password);
        PreparedStatement statement = connection.prepareStatement("INSERT INTO plane(id, point_start, point_finish, rase, type, data) " +
                "VALUES(?,?,?,?,?,?);");
        statement.setInt(1, plane.getId());
        statement.setString(2, plane.getPointStart());
        statement.setString(3, plane.getPointFinish());
        statement.setString(4, Integer.toString(plane.getRace()));
        statement.setString(5, plane.getType());
        statement.setTimestamp(6, new Timestamp(plane.getDate().getTime()));
        statement.execute();
        insertMassageToLogger("Data insert into table");
        statement.close();
        connection.close();
    }
    /**
     * Додає запис в журнальну таблицю
     * @param massage: String - повідомлення для запису в логер
     * @throws SQLException
     */
    public void insertMassageToLogger(String massage){
        try {
            Connection connection = DriverManager.getConnection(url,login, password);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO logger(text) " +
                    "VALUES(?);");
            statement.setString(1,massage);
            statement.execute();
            statement.close();
            connection.close();
        }catch (SQLException e){ }
    }
    /**
     * Видаляє таблицю
     * @throws SQLException
     */
    public void deleteTable(String nameTable)throws SQLException {
        Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement();
        statement.executeUpdate("Drop table " + nameTable);
        statement.close();
        connection.close();
    }
    /**
     * Видаляє запис з таблиці
     * @param  id: int - id запису
     * @throws SQLException
     */
    public void delete(int id) throws SQLException {
        Connection connection = DriverManager.getConnection(url, login, password);
        PreparedStatement statement = connection.prepareStatement("DELETE FROM plane Where id =?");
        statement.setInt(1, id);
        statement.execute();
        insertMassageToLogger("Plane deleted");
        statement.close();
    }
    /**
     * Повертає вміс журнальної таблиці
     * @throws SQLException
     * @return resultSet - множина значень запиту до logger
     */
    public ResultSet showLogger()throws SQLException{
        Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("Select data, text From logger Order by data DESC;");
        return resultSet;
    }
}