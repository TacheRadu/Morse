package com.morse;

import java.util.List;
import java.util.UUID;
import android.util.Log;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import java.sql.PreparedStatement;
import net.dean.jraw.http.LogAdapter;
import net.dean.jraw.oauth.AccountHelper;
import com.channels.reddit.RedditChannel;
import com.channels.androidsms.SmsChannel;
import com.channels.twitter.TwitterChannel;
import net.dean.jraw.android.AndroidHelper;
import net.dean.jraw.http.SimpleHttpLogger;
import net.dean.jraw.android.AppInfoProvider;
import android.database.sqlite.SQLiteStatement;
import net.dean.jraw.android.ManifestAppInfoProvider;
import net.dean.jraw.android.SimpleAndroidLogAdapter;
import net.dean.jraw.android.SharedPreferencesTokenStore;


/**
 * The starting point of the Morse Application.
 *
 * @version 0.1.1
 */
public final class App extends Application {
    private static AccountHelper sAccountHelper;
    private static SharedPreferencesTokenStore sTokenStore;
    private static Database sDatabase;
    private static Context sContext;
    public List<Channel> mChannels;
    public Sender mSender;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sDatabase = new Database(getApplicationContext());
        // Get UserAgent and OAuth2 data from AndroidManifest.xml
        AppInfoProvider provider = new ManifestAppInfoProvider(getApplicationContext());

        // Ideally, this should be unique to every device
        UUID deviceUuid = UUID.randomUUID();

        // Store our access tokens and refresh tokens in shared preferences
        sTokenStore = new SharedPreferencesTokenStore(getApplicationContext());
        // Load stored tokens into memory
        sTokenStore.load();
        // Automatically save new tokens as they arrive
        sTokenStore.setAutoPersist(true);

        // An AccountHelper manages switching between accounts and into/out of user-less mode.
        sAccountHelper = AndroidHelper.accountHelper(provider, deviceUuid, sTokenStore);

        if(sTokenStore.size() != 0)
            sAccountHelper.switchToUser(sTokenStore.getUsernames().get(0));

        // Every time we use the AccountHelper to switch between accounts (from one account to
        // another, or into/out of user-less mode), call this function
        sAccountHelper.onSwitch(redditClient -> {
            // By default, JRAW logs HTTP activity to System.out. We're going to use Log.i()
            // instead.
            LogAdapter logAdapter = new SimpleAndroidLogAdapter(Log.INFO);

            // We're going to use the LogAdapter to write down the summaries produced by
            // SimpleHttpLogger
            redditClient.setLogger(
                    new SimpleHttpLogger(SimpleHttpLogger.DEFAULT_LINE_LENGTH, logAdapter));

            return null;
        });
    }

    public static AccountHelper getAccountHelper() {
        return sAccountHelper;
    }

    public static SharedPreferencesTokenStore getTokenStore() {
        return sTokenStore;
    }

    public void addChannel(String channelName, String userName, String password) {}

    public void checkCredentials(Channel channel) {}

    public static void insertIntoChannels(String name) {
        String insert = "INSERT INTO channels (name) VALUES(?);";
        SQLiteStatement statement = sDatabase.getWritableDatabase().compileStatement(insert);
        statement.bindString(1, name);
        statement.execute();
    }

    private void insertIntoUsers(int id, String userName, String hashedPassword) {
        String insert = "INSERT INTO users(channel_id, username, password) VALUES(?, ?, ?);";
    }

    private void removeUser(String username) {
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

    private List<String> queryChannel(String channel) {
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

    public static List<Channel> getChannels() {
        String select = "SELECT * from channels;";
        List<Channel> channels = new ArrayList<>();
        Cursor cursor = sDatabase.getReadableDatabase().rawQuery(select, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            switch (name) {
                case "sms":
                    channels.add(new SmsChannel(sContext));
                    break;
                case "reddit":
                    channels.add(new RedditChannel(sContext));
                    break;
                case "twitter":
                    channels.add(new TwitterChannel(sContext));
                    break;
            }
        }

        cursor.close();
        return channels;
    }
}