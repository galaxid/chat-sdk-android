package co.chatsdk.ui.job;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import co.chatsdk.core.dao.Keys;
import co.chatsdk.core.dao.Message;
import co.chatsdk.core.dao.Thread;
import co.chatsdk.core.dao.User;
import co.chatsdk.core.handlers.TypingIndicatorHandler;
import co.chatsdk.core.interfaces.ThreadType;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.core.utils.CrashReportingCompletableObserver;
import co.chatsdk.ui.R;
import co.chatsdk.ui.chat.ChatActivity;
import co.chatsdk.ui.main.BaseActivity;
import co.chatsdk.ui.search.NameInterpreter;
import co.chatsdk.ui.utils.ToastHelper;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static co.chatsdk.ui.search.NameInterpreter.isAdmin;
import static co.chatsdk.ui.search.NameInterpreter.isURL2;


public class JobActivity extends BaseActivity {

    protected Thread thread;
    protected Thread userThread;
    protected boolean like = false;
    protected Bundle bundle;
    public TextView nameTextView;
    public TextView dateTextView;
    public TextView locationTextView;
    public TextView desTextView;
    public TextView companyTextView;

    protected boolean removeUserFromChatOnExit = ChatSDK.config().removeUserFromPublicThreadOnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(activityLayout());
        if (!updateThreadFromBundle(savedInstanceState)) {
            return;
        }
        if(!isAdmin()) {
            setUserThread();
            setLike();
        }
        setChatState(TypingIndicatorHandler.State.active);
        initViews();

    }

    protected void initViews () {
        setTitle("Job details");
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
        companyTextView = findViewById(R.id.event_company);
        String name = thread.getName();
        NameInterpreter i = new NameInterpreter(name);
        nameTextView.setText(i.returnName());
        dateTextView.setText(i.returnDate());
        locationTextView.setText(i.returnLoc());
        desTextView.setText(i.returnDes());
        companyTextView.setText(i.returnCom());

        Button button2= (Button) findViewById(R.id.join_event);
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
        Button button3= (Button) findViewById(R.id.add_fav);
        button3.setText(like?"dislike":"like");
        if(like)button3.setBackgroundColor(Color.parseColor("#FFE4B5"));
        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                if(isAdmin()){
                    showToast("You are a Admin!");
                }
                else {
                    if(!like){
                        sendMessage(thread.getEntityID());
                        like = true;
                        button3.setText("dislike");
                        button3.setBackgroundColor(Color.parseColor("#FFE4B5"));
                    }
                    else {
                        for(Message m :userThread.getMessages()){
                            if(m.getText().equals(thread.getEntityID())){
                                ChatSDK.thread().deleteMessage(m).subscribe(() -> {
                                    // Message has been deleted
                                });
                            }
                        }
                        like = false;
                        button3.setText("like");
                        button3.setBackgroundColor(Color.parseColor("#DCDCDC"));

                    }
                }
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
        return R.layout.activity_job;
    }


    protected void handleMessageSend (Completable completable) {
        completable.observeOn(AndroidSchedulers.mainThread()).doOnError(throwable -> {
            ChatSDK.logError(throwable);
            ToastHelper.show(getApplicationContext(), throwable.getLocalizedMessage());
        }).subscribe(new CrashReportingCompletableObserver());
    }


    public void sendMessage(String text) {

        handleMessageSend(ChatSDK.thread().sendMessageWithText(text.trim(),userThread));

    }
    protected void setChatState (TypingIndicatorHandler.State state) {
        if(ChatSDK.typingIndicator() != null) {
            ChatSDK.typingIndicator().setChatState(state, thread)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CrashReportingCompletableObserver(disposableList));
        }
    }


    public void setUserThread(){
        List<Thread> threads = ChatSDK.thread().getThreads(ThreadType.Public);
        for (Thread t : threads){
            if(t.getCreator().isMe()){
                userThread = t;
            }
        }
    }

    public void setLike(){
        for(Message m :userThread.getMessages()){
            if(m.getText().equals(thread.getEntityID()))
                like = true;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userThread != null && userThread.typeIs(ThreadType.Public)) {
             User currentUser = ChatSDK.currentUser();
            ChatSDK.thread().addUsersToThread(userThread, currentUser)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CrashReportingCompletableObserver(disposableList));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (userThread != null && userThread.typeIs(ThreadType.Public) && removeUserFromChatOnExit) {
            ChatSDK.thread().removeUsersFromThread(userThread, ChatSDK.currentUser()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CrashReportingCompletableObserver());
        }
    }
}
