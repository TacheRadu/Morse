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
    Button post;
    EditText postContent;
    EditText title;
    Subreddit subreddit;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reddit_post_activity);
        init();

        post.setOnClickListener(f -> postSubmission());
    }

    private void postSubmission(){
        String text = postContent.getText().toString();
        String titleString = title.getText().toString();

        postContent.setText("");
        title.setText("");

        SubredditReference subredditReference = subreddit.toReference(
                App.getAccountHelper().getReddit());

        subredditReference.submit(SubmissionKind.SELF, titleString, text, true);
        Toast.makeText(this.getApplicationContext(), "Submission posted!",
                Toast.LENGTH_SHORT).show();
    }

    private void init(){
        post = findViewById(R.id.button);
        postContent = findViewById(R.id.post);
        title = findViewById(R.id.title);
        intent = getIntent();
        subreddit = (Subreddit) intent.getSerializableExtra("subreddit");
    }

}
