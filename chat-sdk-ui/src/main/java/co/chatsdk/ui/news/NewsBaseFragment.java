package co.chatsdk.ui.news;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.chatsdk.core.dao.Keys;
import co.chatsdk.core.dao.Thread;
import co.chatsdk.core.dao.User;
import co.chatsdk.core.events.EventType;
import co.chatsdk.core.events.NetworkEvent;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.ui.R;
import co.chatsdk.ui.main.BaseFragment;
import co.chatsdk.ui.threads.UserList;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

public abstract class NewsBaseFragment extends BaseFragment {

    protected RecyclerView listThreads;
    protected EditText searchField;
    protected NewsListAdapter adapter;
    protected String filter;
    protected MenuItem addMenuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        title = "News";
        this.getActivity().setTitle(title);
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

        if(ChatSDK.currentUser().getEntityID().equals(UserList.jueruilics)){
                setHasOptionsMenu(true);
        }
        else setHasOptionsMenu(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        disposableList.add(ChatSDK.events().sourceOnMain()
                .filter(mainEventFilter())
                .subscribe(networkEvent -> {
                    if (tabIsVisible) {
                        reloadData();
                    }
                }));

        disposableList.add(ChatSDK.events().sourceOnMain()
                .filter(NetworkEvent.filterType(EventType.TypingStateChanged))
                .subscribe(networkEvent -> {
                    if (tabIsVisible) {
                        adapter.setTyping(networkEvent.thread, networkEvent.text);
                        adapter.notifyDataSetChanged();
                    }
                }));

        reloadData();

        mainView = inflater.inflate(activityLayout(), null);

        initViews();

        return mainView;
    }

    protected abstract Predicate<NetworkEvent> mainEventFilter ();

    protected  @LayoutRes int activityLayout () {
        return R.layout.activity_threads;
    }

    public void initViews() {
        searchField = mainView.findViewById(R.id.search_field);
        listThreads = mainView.findViewById(R.id.list_threads);

        adapter = new NewsListAdapter(getActivity());

        listThreads.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        listThreads.setAdapter(adapter);

        Disposable d = adapter.onClickObservable().subscribe(thread -> {
            ChatSDK.ui().startNewsActivityForID(getContext(), thread.getEntityID());
        });
    }

    protected boolean allowThreadCreation () {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (allowThreadCreation()) {
            addMenuItem = menu.add(Menu.NONE, R.id.action_add, 10, getString(R.string.thread_fragment_add_item_text));
            addMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            addMenuItem.setIcon(R.drawable.ic_plus);
        }
    }

    // Override this in the subclass to handle the plus button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadData();

        if (searchField != null) {
            searchField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filter = searchField.getText().toString();
                    reloadData();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    @Override
    public void clearData() {
        if (adapter != null) {
            adapter.clearData();
        }
    }

    public void setTabVisibility (boolean isVisible) {
        super.setTabVisibility(isVisible);
        reloadData();
    }

    @Override
    public void reloadData() {
        if (adapter != null) {
            adapter.clearData();
            List<Thread> threads = filter(getThreads());
            adapter.updateThreads(threads);
        }
    }

    protected abstract List<Thread> getThreads ();

    public List<Thread> filter (List<Thread> threads) {
        if (filter == null || filter.isEmpty()) {
            //for(Message m:threads.get(0).getMessages())
                    return threads;
        }

        List<Thread> filteredThreads = new ArrayList<>();
        for (Thread t : threads) {
            if (t.getName() != null && t.getName().toLowerCase().contains(filter.toLowerCase())) {
                filteredThreads.add(t);
            }
            else {
                for (User u : t.getUsers()) {
                    if (u.getName() != null && u.getName().toLowerCase().contains(filter.toLowerCase())) {
                        filteredThreads.add(t);
                        break;
                    }
                }
            }
        }
        return filteredThreads;
    }
}
