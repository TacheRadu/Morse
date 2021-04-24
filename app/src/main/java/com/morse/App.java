package com.morse;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.R;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.List;

/**
 *
 */
public class App extends AppCompatActivity {

    public App(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

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


    private void createTableChannels(){
        String url = "jdbc:sqlite:app/src/main/database.db";
        String sqlCommand = "CREATE TABLE IF NOT EXISTS channels (\n"
                + "	id int PRIMARY KEY,\n"
                + "	name text NOT NULL\n"
                + ");";
        try (Connection conn = DriverManager.getConnection(url)) {
            Statement statement = conn.createStatement();
            statement.execute(sqlCommand);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void createTableUsers() {
        String url = "jdbc:sqlite:app/src/main/database.db";
        String sqlCommand = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	channel_id int NOT NULL,\n"
                + "	username text NOT NULL,\n"
                + "	password text NOT NULL\n"
                + "CONSTRAINT users_ct FOREIGN KEY(channel_id) \n"
                + " REFERENCES channels(id) ON DELETE CASCADE ON UPDATE CASCADE"
                + ");";
        try (Connection conn = DriverManager.getConnection(url)) {
            Statement statement = conn.createStatement();
            statement.execute(sqlCommand);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void insertIntoChannels(int id, String name){
        String url = "jdbc:sqlite:app/src/main/database.db";
        String insert = "INSERT INTO channels(id, name) VALUES(?, ?);";
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(insert);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertIntoUsers(int id, String userName, String hashedPassword){
        String url = "jdbc:sqlite:app/src/main/database.db";
        String insert = "INSERT INTO users(channel_id, username, password) VALUES(?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(insert);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, hashedPassword);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void removeUser(String username){
        String url = "jdbc:sqlite:app/src/main/database.db";
        String remove = "DELETE FROM users WHERE trim(username) = trim(?); ";
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(remove);
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private List<String> queryChannel(String channel){
        List<String> userData = new ArrayList<>(2);
        String url = "jdbc:sqlite:app/src/main/database.db";
        String select = "SELECT username, password from accounts WHERE channel = ?";

        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(select);
            preparedStatement.setString(1, channel);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                userData.add(resultSet.getString("username"));
                userData.add(resultSet.getString("password"));
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return userData;
    }

}