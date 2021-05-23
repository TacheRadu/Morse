package com.channels.twitter;

import java.util.List;
import twitter4j.Twitter;
import android.os.Looper;
import com.morse.Message;
import android.os.Handler;
import java.util.ArrayList;
import android.widget.Toast;
import android.content.Context;
import twitter4j.DirectMessage;
import twitter4j.TwitterException;
import twitter4j.DirectMessageList;
import com.channels.twitter.models.TwitterMessageInfo;


/**
 * This Class is responsible for message management: list, send, delete.
 *
 * @author Peiu Iulian
 */
public class TwitterMessage implements Message{
    private final Twitter mTwitter;
    private final Long mToUserId;
    private String mMessageText;
    long mDelayedMilliSeconds;
    private Context mContext;

    public void setContext(Context context) {
        this.mContext = context;
    }

    public TwitterMessage(Twitter twitter, Long toUserId, String messageText) {
        this.mTwitter = twitter;
        this.mToUserId = toUserId;
        this.mMessageText = messageText;
    }

    public TwitterMessage(Twitter twitter) {
        super();
        this.mTwitter = twitter;
        mToUserId = null;
        mMessageText = null;
    }

    public TwitterMessage(Twitter twitter, Long toUserId) {
        this.mTwitter = twitter;
        this.mToUserId = toUserId;
        this.mMessageText = null;
    }

    /**
     * Send a message with the properties: toUserId, messageText that were initialized via
     * constructor. If the operation was done successfully, on the screen it will be printed the
     * message:
     * "Direct message successfully sent to ...", otherwise "Failed to send a direct message: [cause]"
     */
    @Override
    public void send() {
        try {
            DirectMessage message = mTwitter.sendDirectMessage(
                    mTwitter.showUser(mToUserId).getScreenName(), mMessageText);
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(mContext,
                    "Message was sent!", Toast.LENGTH_LONG).show());
        } catch (TwitterException te) {
            te.printStackTrace();
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(mContext,
                    "Failed to send message", Toast.LENGTH_LONG).show());
        }
    }

    /**
     * Sends a message after an amount of minutes specified in params, delayedMinutes by
     * making a delayed call to send() method.
     * See <code>TwitterMessage.send()</code
     *
     * @param delayedMinutes    the number of minutes for which the message sent will be delayed
     */
    @Override
    public void sendDelayed(long delayedMinutes) {
        mDelayedMilliSeconds = delayedMinutes * 60 * 1000;
        new Thread(() -> {
            try {
                Thread.sleep(mDelayedMilliSeconds);
                send();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * List all message of a user specified by its id via params and will return them.
     *
     * @param fromUserId    the user ID for whom the messages will be listed
     * @return              messagesFromUserId
     */
    @Deprecated
    public List<TwitterMessageInfo> getMessages(Long fromUserId){
        try {
            String cursor = null;
            int count = 20;
            DirectMessageList currentMessages;
            List<TwitterMessageInfo> messagesFromUserId = new ArrayList<>();

            do {
                currentMessages = cursor == null ? mTwitter.getDirectMessages(count)
                        : mTwitter.getDirectMessages(count, cursor);
                for (DirectMessage message : currentMessages) {
                    if (message.getRecipientId() == mToUserId || message.getSenderId() == mToUserId) {
                        messagesFromUserId.add(new TwitterMessageInfo(message));
                    }
                }
                cursor = currentMessages.getNextCursor();
            } while (currentMessages.size() > 0 && cursor != null);

            return messagesFromUserId;
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get messages: " + te.getMessage());
        }

        return null;
    }

    @Deprecated
    @Override
    public Boolean delete(long id) {
        try {
            mTwitter.destroyDirectMessage(id);
            System.out.println("Successfully deleted message with id: " +id);
            return true;
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to delete message: " + te.getMessage());
        }
        return false;
    }

    public void setMessageText(String messageText) {
        this.mMessageText = messageText;
    }
}
