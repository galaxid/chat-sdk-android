package co.chatsdk.ui.news;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.chatsdk.core.dao.Keys;
import co.chatsdk.core.dao.Thread;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.ui.R;
import co.chatsdk.ui.main.BaseActivity;
import co.chatsdk.ui.search.NameInterpreter;

import static co.chatsdk.ui.search.NameInterpreter.isURL2;


public class NewsActivity extends BaseActivity {

    protected Thread thread;
    protected Bundle bundle;
    public TextView nameTextView;
    public TextView dateTextView;
    public TextView introTextView;
    public TextView desTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(activityLayout());
        if (!updateThreadFromBundle(savedInstanceState)) {
            return;
        }

        initViews();

    }

    protected void initViews () {
        setTitle("Event details");
        Context context = this;
        Button button1 = (Button)findViewById(R.id.start_dis);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                ChatSDK.ui().startChatActivityForID(context, thread.getEntityID());
            }
        });
        button1.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View arg0) {
                return true;
            }

        });

        nameTextView = findViewById(R.id.event_title);
        dateTextView = findViewById(R.id.event_date);
        introTextView = findViewById(R.id.event_location);
        desTextView = findViewById(R.id.event_des);
        String name = thread.getName();
        NameInterpreter i = new NameInterpreter(name);
        nameTextView.setText(i.returnName());
        dateTextView.setText(i.returnDate());
        introTextView.setText(i.returnIntro());
        desTextView.setText(i.returnDes());

        Button button2= (Button) findViewById(R.id.view_more);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                String link = i.returnLink().trim();
                if(!isURL2(link))
                    link ="https://www.google.com/";
                Uri uri = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                startActivity(intent);
            }
        });

    }

    protected boolean updateThreadFromBundle(Bundle bundle) {

        if (bundle != null && (bundle.containsKey(Keys.IntentKeyThreadEntityID))) {
            this.bundle = bundle;
        }
        else {
            if (getIntent() == null || getIntent().getExtras() == null) {
                finish();
                return false;
            }
            this.bundle = getIntent().getExtras();
        }

        if (this.bundle.containsKey(Keys.IntentKeyThreadEntityID)) {
            String threadEntityID = this.bundle.getString(Keys.IntentKeyThreadEntityID);
            if(threadEntityID != null) {
                thread = ChatSDK.db().fetchThreadWithEntityID(threadEntityID);
            }
        }

        if (thread == null) {
            finish();
            return false;
        }

        return true;
    }

    protected @LayoutRes
    int activityLayout() {
        return R.layout.activity_news;
    }
}
