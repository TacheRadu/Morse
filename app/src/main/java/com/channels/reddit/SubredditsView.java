package com.channels.reddit;

import com.R;
import android.content.Context;
import android.widget.TextView;
import androidx.annotation.IdRes;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import net.dean.jraw.models.Subreddit;
import androidx.constraintlayout.widget.ConstraintLayout;


/**
 * Class used to implement in code the view of the subreddits that an user has.
 *
 * @version 0.1.1
 */
public class SubredditsView extends ConstraintLayout {
    private final Context context;

    public SubredditsView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SubredditsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public SubredditsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void display(Subreddit subreddit) {
        textView(R.id.subreddit).setText(String.format("/r/%s", subreddit.getName()));
    }

    private TextView textView(@IdRes int id) {
        return (TextView) findViewById(id);
    }
    private void init() {
        inflate(getContext(), R.layout.view_tokenstore_row, this);
    }
}
