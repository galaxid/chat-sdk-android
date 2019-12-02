/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package co.chatsdk.ui.job;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.chatsdk.core.dao.Message;
import co.chatsdk.core.dao.Thread;
import co.chatsdk.core.events.NetworkEvent;
import co.chatsdk.core.interfaces.ThreadType;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.ui.R;
import co.chatsdk.ui.rooms.FirstPageFragmentListener;
import co.chatsdk.ui.threads.UserList;
import io.reactivex.functions.Predicate;

import static co.chatsdk.ui.search.NameInterpreter.isAdmin;

/**
 * Created by itzik on 6/17/2014.
 */
public class JobListFragment extends JobBaseFragment {

    private static FirstPageFragmentListener firstPageListener;
    public boolean like;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if((!like)&&ChatSDK.currentUser().getEntityID().equals(UserList.ljr)){
            setHasOptionsMenu(true);
        }
        else setHasOptionsMenu(false);
    }

    public JobListFragment() {
    }

    public JobListFragment(FirstPageFragmentListener listener,boolean value) {
        firstPageListener = listener;
        like = value;

    }

    public void backPressed() {
        firstPageListener.onSwitchToNextFragment(7);
    }

    @Override
    public Predicate<NetworkEvent> mainEventFilter() {
        return NetworkEvent.filterPublicThreadsUpdated();
    }

    @Override
    public boolean allowThreadCreation () {
        return ChatSDK.config().publicRoomCreationEnabled;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Cant use switch in the library*/
        int id = item.getItemId();

        if (id == R.id.action_add) {
            ChatSDK.ui().startThreadEditDetailsActivity(getContext(), null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected List<Thread> getThreads() {
        List<Thread> threads =  ChatSDK.thread().getThreads(ThreadType.Public);
        List<Thread> filtered = new ArrayList<>();
        /*if (ChatSDK.config().publicChatRoomLifetimeMinutes == 0) {
            return threads;
        } else {
            // Do we need to filter the list to remove old chat rooms?
            long now = new Date().getTime();
            List<Thread> filtered = new ArrayList<>();
            for (Thread t : threads) {
                if (t.getCreationDate() == null || now - t.getCreationDate().getTime() < ChatSDK.config().publicChatRoomLifetimeMinutes * 60000) {
                    filtered.add(t);
                }
            }
            return filtered;
        }*/
        Thread fav = null;
        for (Thread t : threads) { //改detail里面的
            if ((!like)&&t.getCreatorEntityId().equals(UserList.ljr)) {
                filtered.add(t);
            }
            else if(like&& isAdmin()&&t.getCreatorEntityId().equals(UserList.ljr))
                filtered.add(t);
            else if (like &&(!isAdmin())&&t.getCreator().isMe())
                fav = t;
        }
        if(fav!=null){
            Set<String> set = new HashSet<>();
            for (Message m:fav.getMessages()){
                set.add(m.getText());
            }
            for (Thread t : threads){
                if(set.contains(t.getEntityID())){
                    filtered.add(t);
                }
            }
        }

            return filtered;
    }

    public void setTabVisibility (boolean isVisible) {
        super.setTabVisibility(isVisible);
        if (isVisible) {
            reloadData();
        }
    }



}
