package com.morse;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 */
public class App {

    /**
     *
     */
    public List<Channel> channels;
    /**
     *
     */
    public Sender sender;

    /**
     * Default constructor
     */
    public App() {
    }

    /**
     *
     */
    public void App() {
        // TODO implement here
    }

    /**
     * @param channelName
     * @param userName
     * @param password
     */
    public void addChannel(String channelName, String userName, String password) {
        // TODO implement here
    }

    /**
     * @param channel
     */
    public void checkCredentials(Channel channel) {
        // TODO implement here
    }

    private void createDB() {
        String url = "jdbc:sqlite:app/src/main/database.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Database created!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createTable() {
        String url = "jdbc:sqlite:app/src/main/database.db";
        String sqlCommand = "CREATE TABLE IF NOT EXISTS accounts (\n"
                + "	channel text PRIMARY KEY,\n"
                + "	username text NOT NULL,\n"
                + "	password text NOT NULL\n"
                + ");";
        try (Connection conn = DriverManager.getConnection(url)) {
            Statement statement = conn.createStatement();
            statement.execute(sqlCommand);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertIntoTable(String channel, String userName, String hashedPassword){
        String url = "jdbc:sqlite:app/src/main/database.db";
        String insert = "INSERT INTO accounts(channel, username, password) VALUES(?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(insert);
            preparedStatement.setString(1, channel);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, hashedPassword);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void queryChannel(String channel){
        String url = "jdbc:sqlite:app/src/main/database.db";
        String select = "SELECT username, password from accounts WHERE channel = ?";
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(select);
            preparedStatement.setString(1, channel);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getString("username") +
                        " " + resultSet.getString("password"));

            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

}