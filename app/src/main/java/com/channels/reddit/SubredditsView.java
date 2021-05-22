package com.channels.reddit;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.R;

import net.dean.jraw.models.Subreddit;

public class SubredditsView extends ConstraintLayout {
    private Context context;
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
        textView(R.id.subreddit).setText("/r/" + subreddit.getName());
    }

    private TextView textView(@IdRes int id) {
        return (TextView) findViewById(id);
    }
    private void init() {
        inflate(getContext(), R.layout.view_tokenstore_row, this);
    }
}
