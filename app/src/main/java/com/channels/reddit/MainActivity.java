package com.channels.reddit;

import com.R;
import com.morse.App;
import java.util.List;
import android.os.Build;
import android.os.Bundle;
import java.util.ArrayList;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.content.Intent;
import android.view.ViewGroup;
import net.dean.jraw.RedditClient;
import androidx.annotation.NonNull;
import java.lang.ref.WeakReference;
import net.dean.jraw.models.Subreddit;
import androidx.annotation.RequiresApi;
import org.jetbrains.annotations.NotNull;
import net.dean.jraw.pagination.Paginator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import net.dean.jraw.pagination.BarebonesPaginator;
import androidx.recyclerview.widget.LinearLayoutManager;


/**
 * The main activity is a list of all stored authentication data. This data is provided from
 * App.getTokenStore(). When an item in the list is clicked, we attempt to authenticate ourselves as
 * that user. If we have an unexpired access token, we use that. If we only have a refresh token,
 * we use that and request a fresh access token on the next normal request. After we authenticate,
 * the UserOverviewActivity is started.
 * <p>
 * This activity has a FAB in the bottom right-hand corner for authenticating new users. When
 * pressed, the NewUserActivity is started. See that class' documentation for what it does.
 *
 * @version 0.1.1
 */
public class MainActivity extends AppCompatActivity {
    private SubredditDataAdapter mAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (App.getTokenStore().size() == 0) {
            startActivity(new Intent(this, NewUserActivity.class));
        }

        setContentView(R.layout.activity_main);

        // Create the RecyclerView's LayoutManager and Adapter
        RecyclerView storedDataList = findViewById(R.id.storedDataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        storedDataList.setLayoutManager(layoutManager);

        // Configure the RecyclerView
        this.mAdapter = new SubredditDataAdapter(this, storedDataList);
        storedDataList.setAdapter(mAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        /* If the user doesn't log in but presses the back button, terminate this activity, thus
         * resulting in the user being redirected to the previous activity.
         */
        if (App.getTokenStore().size() != 0) {
            mAdapter.update();
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
        private final RecyclerView recyclerView;
        private RedditClient redditClient;

        private SubredditDataAdapter(MainActivity mainActivity, RecyclerView recyclerView) {
            this.activity = new WeakReference<>(mainActivity);
            this.recyclerView = recyclerView;
            subreddits = new ArrayList<>();

            if (App.getTokenStore().size() != 0) {
                update();
            }
        }

        private void update() {
            redditClient = App.getAccountHelper().getReddit();
            subreddits = new ArrayList<>();

            BarebonesPaginator<Subreddit> subredditBareBonesPaginator =
                    redditClient.me().subreddits("subscriber").limit(
                            Paginator.RECOMMENDED_MAX_LIMIT).build();

            for (List<Subreddit> subreddit : subredditBareBonesPaginator) {
                subreddits.addAll(subreddit);
            }

            // Prefer this instead of tokenStore.getUsernames() because this.data.keySet() is sorted
            notifyDataSetChanged();
        }

        @NotNull
        @Override
        public SubredditsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            SubredditsView view = new SubredditsView(parent.getContext());

            // Give the view max width and minimum height
            view.setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            view.setOnClickListener(v -> {
                int id = recyclerView.getChildAdapterPosition(v);
                Intent postIntent = new Intent(MainActivity.this.getApplicationContext(),
                        PostActivity.class);
                postIntent.putExtra("subreddit", subreddits.get(id));
                startActivity(postIntent);
            });

            // Listen for the view being clicked
            new ReAuthenticationTask(activity).execute(redditClient.me().getUsername());

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

    private static class ReAuthenticationTask extends AsyncTask<String, Void, Void> {
        private final WeakReference<MainActivity> activity;

        ReAuthenticationTask(WeakReference<MainActivity> activity) {
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(String... usernames) {
            App.getAccountHelper().switchToUser(usernames[0]);
            return null;
        }
    }
}
