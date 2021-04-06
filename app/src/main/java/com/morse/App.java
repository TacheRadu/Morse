package com.morse;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
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
        try (Connection conn = DriverManager.getConnection(url)) {
            Statement statement = conn.createStatement();
            statement.execute("INSERT INTO accounts(id, username, password) VALUES(" +
                    "\"" + channel + "\"" + ", " +
                    "\"" + userName + "\"" + ", " +
                    "\"" + hashedPassword + "\"" +");");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}