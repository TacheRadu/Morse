package com.morse;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class App extends AppCompatActivity {
    private static final String EMAIL = "email";
    CallbackManager callbackManager;
    public App(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);
        System.out.println("loginButton");
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("such success");
            }

            @Override
            public void onCancel() {
                // App code
                System.out.println("many success");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                System.out.println("much success");

            }
        });
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