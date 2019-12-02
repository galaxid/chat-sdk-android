/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package co.chatsdk.ui.mock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import co.chatsdk.core.dao.Thread;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.ui.R;
import co.chatsdk.ui.job.JobViewHolder;
import co.chatsdk.ui.search.NameInterpreter;
import co.chatsdk.ui.threads.ThreadSorter;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class MockListAdapter extends RecyclerView.Adapter<MockViewHolder> {

    public static int ThreadCellType = 0;

    protected WeakReference<Context> context;

    protected List<Thread> threads = new ArrayList<>();

    protected HashMap<Thread, String> typing = new HashMap<>();
    protected PublishSubject<Thread> onClickSubject = PublishSubject.create();
    protected PublishSubject<Thread> onLongClickSubject = PublishSubject.create();

    public MockListAdapter(Context context) {
        this.context = new WeakReference(context);
    }

    @Override
    public MockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.view_mock_row, null);
        return new MockViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final MockViewHolder holder, int position) {

        final Thread thread = threads.get(position);

        holder.itemView.setOnClickListener(view -> onClickSubject.onNext(thread));

        holder.itemView.setOnLongClickListener(view -> {
            onLongClickSubject.onNext(thread);
            return true;
        });

        String name = thread.getName();
        NameInterpreter inter = new NameInterpreter(name);
        holder.nameTextView.setText(thread.getCreator().getName());
        holder.majorTextView.setText(inter.returnDes());
        holder.dateTextView.setText(inter.returnDate());
        holder.locationTextView.setText(inter.returnLoc());
        holder.introTextView.setText(inter.returnIntro());
        holder.imageView.setImageURI(thread.getCreator().getAvatarURL());

        //ThreadImageBuilder.load(holder.imageView, thread);
    }


    @Override
    public int getItemViewType(int position) {
        return ThreadCellType;
    }

    @Override
    public int getItemCount() {
        return threads.size();
    }

    public boolean addRow (Thread thread, boolean notify) {
        for (Thread t : threads) {
            if (t.equalsEntity(thread)) {
                return false;
            }
        }

        threads.add(thread);
        if (notify) {
            notifyDataSetChanged();
        }
        return true;
    }

    public void addRow(Thread thread) {
        addRow(thread, true);
    }

    public void setTyping (Thread thread, String message) {
        if (message != null) {
            typing.put(thread, message);
        }
        else {
            typing.remove(thread);
        }
    }

    protected void sort() {
        Collections.sort(threads, new ThreadSorter());
    }

    public void clearData () {
        clearData(true);
    }

    public void clearData (boolean notify) {
        threads.clear();
        if (notify) {
            notifyDataSetChanged();
        }
    }

    public Observable<Thread> onClickObservable () {
        return onClickSubject;
    }

    public Observable<Thread> onLongClickObservable () {
        return onLongClickSubject;
    }


    public void updateThreads (List<Thread> threads) {
        boolean added = false;
        for (Thread t : threads) {
            added = addRow(t, false) || added;
        }
        // Maybe the last message has changed. I think this can lead to a race condition
        // Which causes the thread not to update when a new message comes in
//        if (added) {
            sort();
            notifyDataSetChanged();
//        }
    }

    public void setThreads(List<Thread> threads) {
        clearData(false);
        updateThreads(threads);
    }


}
