package com.morse;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.R;
import com.androidsms.SmsChannel;

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

    private final Database database;
    private final AppCompatActivity parentActivity;

    public App(AppCompatActivity activity){
        this.parentActivity = activity;
        database = new Database(activity.getApplicationContext());

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


    public void insertIntoChannels(String name){
        String insert = "INSERT INTO channels (name) VALUES(?);";
        SQLiteStatement statement = database.getWritableDatabase().compileStatement(insert);
        statement.bindString(1, name);
        statement.execute();
    }

    private void insertIntoUsers(int id, String userName, String hashedPassword){
        String insert = "INSERT INTO users(channel_id, username, password) VALUES(?, ?, ?);";
        //TODO
    }
    private void removeUser(String username){
        String url = "jdbc:sqlite:database.db";
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
        String url = "jdbc:sqlite:database.db";
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

    public List<Channel> getChannels(){
        String select = "SELECT * from channels;";
        List<Channel> channels = new ArrayList<>();
        Cursor cursor = database.getReadableDatabase().rawQuery(select, null);
        while(cursor.moveToNext()){
            String name = cursor.getString(1);
            switch (name){
                case "sms":
                    channels.add(new SmsChannel(parentActivity));
                    break;
                case "reddit":
                    //TODO
                    break;
                case "twitter":
                    //TODO
            }
        }
        cursor.close();
        return channels;
    }

}