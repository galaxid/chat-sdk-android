/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package co.chatsdk.ui.threads;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.chatsdk.core.dao.Thread;
import co.chatsdk.core.events.NetworkEvent;
import co.chatsdk.core.interfaces.ThreadType;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.ui.R;
import co.chatsdk.ui.helpers.DialogUtils;
import co.chatsdk.ui.rooms.FirstPageFragmentListener;
import co.chatsdk.ui.utils.ToastHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;

/**
 * Created by itzik on 6/17/2014.
 */
public class PublicThreadsFragment extends ThreadsFragment {

    private static FirstPageFragmentListener firstPageListener;
    private boolean major;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(major&&ChatSDK.currentUser().getEntityID().equals(UserList.xuyang1)){
            setHasOptionsMenu(true);
        }
        else if((!major)&&ChatSDK.currentUser().getEntityID().equals(UserList.jueruil))
            setHasOptionsMenu(true);
        else setHasOptionsMenu(false);
    }

    @Override
    public void initViews() {
        super.initViews();

        if((major&&ChatSDK.currentUser().getEntityID().equals(UserList.xuyang1))||((!major)&&ChatSDK.currentUser().getEntityID().equals(UserList.jueruil))) {
            disposableList.add(adapter.onLongClickObservable().subscribe(thread -> DialogUtils.showToastDialog(getContext(), "", getResources().getString(R.string.alert_delete_thread), getResources().getString(R.string.delete),
                    getResources().getString(R.string.cancel), null, () -> {
                        disposableList.add(ChatSDK.thread().deleteThread(thread)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    adapter.clearData();
                                    reloadData();
                                }, throwable -> ToastHelper.show(getContext(), throwable.getLocalizedMessage())));
                        return null;
                    })));
        }
    }

    public PublicThreadsFragment() {
    }

    public PublicThreadsFragment(FirstPageFragmentListener listener, boolean value) {
        firstPageListener = listener;
        major=value;

    }

    public void backPressed() {
        firstPageListener.onSwitchToNextFragment(0);
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
            if(!t.isDeleted()) {
                if (major && (t.getCreatorEntityId().equals(UserList.xuyang1))) {
                    filtered.add(t);
                } else if ((!major) && (t.getCreatorEntityId().equals(UserList.jueruil)))
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
