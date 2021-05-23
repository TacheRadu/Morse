package com.morse;


/**
 * Class which defines general-purpose constants to be used throughout the Application's code.
 *
 * @author Ionuț Roșca
 * @version 0.1.1
 */
public class Constants {
    /* The type of channel that will be selected  */
    public static final String CHANNEL_TYPE = "com.morse.CHANNEL_TYPE";

    /* Defines the integer selector constant for the Android SMS Channel. */
    public static final int CHANNEL_ANDROID_SMS = 0;

    /* Defines the integer selector constant for the Reddit Channel. */
    public static final int CHANNEL_REDDIT = 1;

    /* Defines the integer selector constant for Twitter Channel. */
    public static final int CHANNEL_TWITTER = 2;

    /*Defines the string for twitter OAUTH token*/
    public static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";

    /*Defines the string for twitter OAUTH secret*/
    public static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";

    /*Defines the string to check whether the user is logged in or not*/
    public static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

    /*Defines the string for the username */
    public static final String PREF_USER = "username";

    /*Defines the string for the id of a user*/
    public static final String PREF_ID = "id";
}
