package com.channels.reddit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.morse.App;
import com.morse.activities.SelectChannelActivity;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.PersistedAuthData;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.oauth.DeferredPersistentTokenStore;
import net.dean.jraw.pagination.BarebonesPaginator;
import net.dean.jraw.pagination.Paginator;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * The main activity is a list of all stored authentication data. This data is provided from
 * App.getTokenStore(). When an item in the list is clicked, we attempt to authenticate ourselves as
 * that user. If we have an unexpired access token, we use that. If we only have a refresh token,
 * we use that and request a fresh access token on the next normal request. After we authenticate,
 * the UserOverviewActivity is started.
 * <p>
 * This activity has a FAB in the bottom right-hand corner for authenticating new users. When
 * pressed, the NewUserActivity is started. See that class' documentation for what it does.
 */
public class MainActivity extends AppCompatActivity {
    private static final int REQ_CODE_LOGIN = 0;

    private RecyclerView storedDataList;
    private SubredditDataAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (App.getTokenStore().size() == 0)
            startActivity(new Intent(this, NewUserActivity.class));

        setContentView(R.layout.activity_main);

        // Create the RecyclerView's LayoutManager and Adapter
        this.storedDataList = findViewById(R.id.storedDataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        storedDataList.setLayoutManager(layoutManager);
        // Configure the RecyclerView
        this.adapter = new SubredditDataAdapter(this, storedDataList);
        storedDataList.setAdapter(adapter);
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        /* If the user doesn't log in but presses the back button,  */
        if (App.getTokenStore().size() != 0) {
            adapter.update();
        } else {
            finish();
        }
    }

    /**
     * This Adapter pulls its data from a TokenStore
     */
    private class SubredditDataAdapter extends RecyclerView.Adapter<SubredditsViewHolder> {
        private final WeakReference<MainActivity> activity;
        private List<Subreddit> subreddits;
        private RecyclerView recyclerView;
        private RedditClient redditClient;

        private SubredditDataAdapter(MainActivity mainActivity, RecyclerView recyclerView) {
            this.activity = new WeakReference<>(mainActivity);
            this.recyclerView = recyclerView;
            subreddits = new ArrayList<>();
            if (App.getTokenStore().size() != 0)
                update();
        }

        private void update() {
            redditClient = App.getAccountHelper().getReddit();
            subreddits = new ArrayList<>();
            BarebonesPaginator<Subreddit> subredditBarebonesPaginator = redditClient.me().subreddits("subscriber").limit(Paginator.RECOMMENDED_MAX_LIMIT).build();
            for (List<Subreddit> subreddit : subredditBarebonesPaginator) {
                subreddits.addAll(subreddit);
            }

            // Prefer this instead of tokenStore.getUsernames() because this.data.keySet() is sorted
            notifyDataSetChanged();
        }

        @Override
        public SubredditsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            SubredditsView view = new SubredditsView(parent.getContext());
            // Create a new TokenStoreUserView when requested

            // Give the view max width and minimum height
            view.setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            view.setOnClickListener(v -> {
                int id = recyclerView.getChildAdapterPosition(v);
                Intent postIntent = new Intent(MainActivity.this.getApplicationContext(), PostActivity.class);
                postIntent.putExtra("subreddit", subreddits.get(id));
                startActivity(postIntent);
            });

            // Listen for the view being clicked
            new ReauthenticationTask(activity).execute(redditClient.me().getUsername());

            return new SubredditsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SubredditsViewHolder holder, int position) {
            // Tell the TokenStoreUserView to change the data it's showing when the view holder gets
            // recycled
            holder.view.display(subreddits.get(position));
        }

        @Override
        public int getItemCount() {
            return subreddits.size();
        }
    }

    private static class SubredditsViewHolder extends RecyclerView.ViewHolder {

        public SubredditsView view;

        public SubredditsViewHolder(@NonNull @NotNull SubredditsView itemView) {
            super(itemView);
            view = itemView;
        }
    }

    private static class ReauthenticationTask extends AsyncTask<String, Void, Void> {
        private final WeakReference<MainActivity> activity;

        ReauthenticationTask(WeakReference<MainActivity> activity) {
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(String... usernames) {
            App.getAccountHelper().switchToUser(usernames[0]);
            return null;
        }
    }
}
