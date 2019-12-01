package co.chatsdk.ui.news;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import co.chatsdk.core.dao.Keys;
import co.chatsdk.core.dao.Thread;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.core.utils.CrashReportingCompletableObserver;
import co.chatsdk.ui.R;
import co.chatsdk.ui.chat.ChatActivity;
import co.chatsdk.ui.chat.MessageListAdapter;
import co.chatsdk.ui.main.BaseActivity;
import co.chatsdk.ui.search.NameInterpreter;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class EventActivity extends BaseActivity {

    protected Thread thread;
    protected Bundle bundle;
    public TextView nameTextView;
    public TextView dateTextView;
    public TextView locationTextView;
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
        locationTextView = findViewById(R.id.event_location);
        desTextView = findViewById(R.id.event_des);
        String name = thread.getName();
        NameInterpreter i = new NameInterpreter(name);
        nameTextView.setText(i.returnName());
        dateTextView.setText(i.returnDate());
        locationTextView.setText(i.returnLoc());
        desTextView.setText(i.returnDes());

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
        /*if (this.bundle.containsKey(Keys.IntentKeyListPosSelectEnabled)) {
            listPos = (Integer) this.bundle.get(Keys.IntentKeyListPosSelectEnabled);
            scrollListTo(ChatActivity.ListPosition.Current, false);
        }*/

        if (thread == null) {
            finish();
            return false;
        }

        return true;
    }

    protected @LayoutRes
    int activityLayout() {
        return R.layout.activity_event;
    }
}
