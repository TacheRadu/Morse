package com.channels.reddit;

import androidx.appcompat.app.AppCompatActivity;

import com.R;

import net.dean.jraw.RedditClient;
import net.dean.jraw.Version;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.pagination.Paginator;

public class HomeActivity extends AppCompatActivity {



    public Listing<Submission> getMostPopularPosts(){
        RedditClient redditClient = getAppRedditClient();

        DefaultPaginator<Submission> paginator = redditClient.frontPage()
                .limit(Paginator.RECOMMENDED_MAX_LIMIT)
                .sorting(SubredditSort.TOP)
                .timePeriod(TimePeriod.MONTH)
                .build();

        Listing<Submission> firstPage = paginator.next();

//        //debug:
//        for (Submission post : firstPage) {
//            if (post.getDomain().contains("imgur.com")) {
//                System.out.println(String.format("%s (/r/%s, %s points) - %s",
//                        post.getTitle(), post.getSubreddit(), post.getScore(), post.getUrl()));
//            }
//        }

        return firstPage;
    }

    public RedditClient getAppRedditClient(){
        Credentials credentials =
                Credentials.script(getString(R.string.com_reddit_sdk_android_USERNAME),
                        getString(R.string.com_reddit_sdk_android_PASSWORD),
                        getString(R.string.com_reddit_sdk_android_CLIENT_ID),
                        getString(R.string.com_reddit_sdk_android_CLIENT_SECRET));

        UserAgent userAgent =
                new UserAgent("bot", "net.dean.jraw.example.script", Version.get(), "thatJavaNerd");;

        NetworkAdapter http = new OkHttpNetworkAdapter(userAgent);
        RedditClient redditClient = OAuthHelper.automatic(http, credentials);

        return redditClient;
    }
}
