package co.chatsdk.ui.main;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.viewpager.widget.ViewPager;
import co.chatsdk.core.Tab;
import co.chatsdk.core.dao.Thread;
import co.chatsdk.core.interfaces.LocalNotificationHandler;
import co.chatsdk.core.interfaces.ThreadType;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.ui.R;
import co.chatsdk.ui.contacts.ContactsFragment;
import co.chatsdk.ui.news.EventListFragment;
import co.chatsdk.ui.news.NewsListFragment;
import co.chatsdk.ui.profile.ProfileFragment;
import co.chatsdk.ui.threads.PrivateThreadsFragment;
import co.chatsdk.ui.threads.PublicThreadsFragment;

public class MainAppBarActivity extends MainActivity {
    protected TabLayout tabLayout;
    protected ViewPager viewPager;
    protected PagerAdapterTabs adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected @LayoutRes
    int activityLayout() {
        return R.layout.activity_view_pager;
    }

    protected void initViews() {
        setContentView(activityLayout());
        viewPager = findViewById(R.id.pager);

        tabLayout = findViewById(R.id.tab_layout);
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        // Only creates the adapter if it wasn't initiated already
        if (adapter == null) {
            adapter = new PagerAdapterTabs(getSupportFragmentManager());
        }

        final List<Tab> tabs = adapter.getTabs();
        for (Tab tab : tabs) {
            tabLayout.addTab(tabLayout.newTab().setText(tab.title).setIcon(tab.icon));
        }

        ((BaseFragment) tabs.get(0).fragment).setTabVisibility(true);

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                updateLocalNotificationsForTab();

                // We mark the tab as visible. This lets us be more efficient with updates
                // because we only
                for(int i = 0; i < tabs.size(); i++) {
                    ((BaseFragment) tabs.get(i).fragment).setTabVisibility(i == tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setOffscreenPageLimit(3);
    }

    public void updateLocalNotificationsForTab () {
        TabLayout.Tab tab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
        ChatSDK.ui().setLocalNotificationHandler(thread -> showLocalNotificationsForTab(tab, thread));
    }

    public boolean showLocalNotificationsForTab (TabLayout.Tab tab, Thread thread) {
        // Don't show notifications on the threads tabs
        Tab t = adapter.getTabs().get(tab.getPosition());

        if (thread.typeIs(ThreadType.Private)) {
            Class privateThreadsFragmentClass = ChatSDK.ui().privateThreadsFragment().getClass();
            return !t.fragment.getClass().isAssignableFrom(privateThreadsFragmentClass);
        }
        if (thread.typeIs(ThreadType.Public)) {
            Class publicThreadsFragmentClass = ChatSDK.ui().publicThreadsFragment().getClass();
            return !t.fragment.getClass().isAssignableFrom(publicThreadsFragmentClass);
        }
        return true;
    }

    public void clearData () {
        for(Tab t : adapter.getTabs()) {
            if(t.fragment instanceof BaseFragment) {
                ((BaseFragment) t.fragment).clearData();
            }
        }
    }

    public void reloadData () {
        for(Tab t : adapter.getTabs()) {
            if(t.fragment instanceof BaseFragment) {
                ((BaseFragment) t.fragment).safeReloadData();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() == 0) {
            if (adapter.getItem(0) instanceof NewsListFragment) {
                ((NewsListFragment) adapter.getItem(0)).backPressed();
            }
            else if (adapter.getItem(0) instanceof EventListFragment) {
                ((EventListFragment) adapter.getItem(0)).backPressed();
            }
        }
        if(viewPager.getCurrentItem() == 1) {
            if (adapter.getItem(1) instanceof PublicThreadsFragment) {
                ((PublicThreadsFragment) adapter.getItem(1)).backPressed();
            }
            else if (adapter.getItem(1) instanceof PrivateThreadsFragment) {
                ((PrivateThreadsFragment) adapter.getItem(1)).backPressed();
            }
        }
        if(viewPager.getCurrentItem() == 4) {
            if (adapter.getItem(4) instanceof ProfileFragment) {
                ((ProfileFragment) adapter.getItem(4)).backPressed();
            }
            else if (adapter.getItem(4) instanceof ContactsFragment) {
                ((ContactsFragment) adapter.getItem(4)).backPressed();
            }
        }
    }
}
