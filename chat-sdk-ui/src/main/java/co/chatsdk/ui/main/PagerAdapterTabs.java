/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package co.chatsdk.ui.main;


import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import co.chatsdk.core.Tab;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.ui.news.EventListFragment;
import co.chatsdk.ui.news.NewsFragment;
import co.chatsdk.ui.news.NewsListFragment;
import co.chatsdk.ui.rooms.FirstPageFragmentListener;
import co.chatsdk.ui.rooms.RoomsFragment;
import co.chatsdk.ui.threads.PrivateThreadsFragment;
import co.chatsdk.ui.threads.PublicThreadsFragment;

/**
 * Created by itzik on 6/16/2014.
 */
public class PagerAdapterTabs extends FragmentPagerAdapter {

    private Boolean firsttime0 = true;
    private Boolean firsttime1 = true;

    private FragmentManager mFragmentManager;

    private FirstPageListener listener = new FirstPageListener();

    protected List<Tab> tabs;

    public PagerAdapterTabs(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
        tabs = ChatSDK.ui().tabs();
    }

    public List<Tab> getTabs() {
        return tabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).title;
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0 &&firsttime0){
            tabs.get(position).fragment = new NewsFragment(listener);
            firsttime0 = false;
        }

        if(position == 1 &&firsttime1){
            tabs.get(position).fragment = new RoomsFragment(listener);
            firsttime1 = false;
        }
        return tabs.get(position).fragment;
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public final class FirstPageListener implements
            FirstPageFragmentListener {
        public void onSwitchToNextFragment(int value) {
            if(value<3){
                boolean fm = tabs.get(1).fragment instanceof RoomsFragment;
                mFragmentManager.beginTransaction().remove(tabs.get(1).fragment)
                        .commitNow();
                if (fm && value==0){
                    tabs.get(1).fragment = new PublicThreadsFragment(listener,true);
                }else if(fm && (value==1)){ // Instance of NextFragment
                    tabs.get(1).fragment = new PublicThreadsFragment(listener,false);
                }
                else if(fm && (value==2)){ // Instance of NextFragment
                    tabs.get(1).fragment = new PrivateThreadsFragment(listener);
                }
                else tabs.get(1).fragment = new RoomsFragment(listener);

            }
            if(value==3||value==4){
                boolean fm = tabs.get(0).fragment instanceof NewsFragment;
                mFragmentManager.beginTransaction().remove(tabs.get(0).fragment)
                        .commitNow();
                if (fm && value==3){
                    tabs.get(0).fragment = new NewsListFragment(listener);
                }else if(fm){ // Instance of NextFragment
                    tabs.get(0).fragment = new EventListFragment(listener);
                }
                else tabs.get(0).fragment = new NewsFragment(listener);

            }
            notifyDataSetChanged();
        }
    }


}
