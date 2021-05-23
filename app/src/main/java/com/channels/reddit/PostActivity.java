package com.channels.reddit;

import com.R;
import com.morse.App;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.models.SubmissionKind;
import androidx.appcompat.app.AppCompatActivity;
import net.dean.jraw.references.SubredditReference;


/**
 * Class that handles the process of posting to a subreddit.
 *
 * @version 0.1.1
 */
public class PostActivity extends AppCompatActivity {
    Button mPost;
    EditText mPostContent;
    EditText mTitle;
    Subreddit mSubreddit;
    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reddit_post_activity);
        init();

        mPost.setOnClickListener(f -> postSubmission());
    }

    private void postSubmission(){
        String text = mPostContent.getText().toString();
        String titleString = mTitle.getText().toString();

        mPostContent.setText("");
        mTitle.setText("");

        SubredditReference subredditReference = mSubreddit.toReference(
                App.getAccountHelper().getReddit());

        subredditReference.submit(SubmissionKind.SELF, titleString, text, true);
        Toast.makeText(this.getApplicationContext(), "Submission posted!",
                Toast.LENGTH_SHORT).show();
    }

    private void init(){
        mPost = findViewById(R.id.button);
        mPostContent = findViewById(R.id.post);
        mTitle = findViewById(R.id.title);
        mIntent = getIntent();
        mSubreddit = (Subreddit) mIntent.getSerializableExtra("subreddit");
    }

}
