/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package co.chatsdk.ui.mock;

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
import co.chatsdk.ui.job.JobBaseFragment;
import co.chatsdk.ui.rooms.FirstPageFragmentListener;
import co.chatsdk.ui.threads.UserList;
import io.reactivex.functions.Predicate;

import static co.chatsdk.ui.search.NameInterpreter.isAdmin;

/**
 * Created by itzik on 6/17/2014.
 */
public class MockListFragment extends MockBaseFragment {

    private static FirstPageFragmentListener firstPageListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    public MockListFragment() {
    }

    public MockListFragment(FirstPageFragmentListener listener) {
        firstPageListener = listener;

    }

    public void backPressed() {
        firstPageListener.onSwitchToNextFragment(9);
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
        for (Thread t : threads) { //改detail里面的
            if(t.getName().indexOf('\r')>=0){
                if((!t.getCreatorEntityId().equals(UserList.ljr))&&
                        (!t.getCreatorEntityId().equals(UserList.lucas))&&
                                (!t.getCreatorEntityId().equals(UserList.jueruil))&&
                        (!t.getCreatorEntityId().equals(UserList.jueruilics))&&
                        (!t.getCreatorEntityId().equals(UserList.xuyang1)))
                filtered.add(t);
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
