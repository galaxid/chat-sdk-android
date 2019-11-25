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
import co.chatsdk.ui.rooms.FirstPageFragmentListener;
import co.chatsdk.ui.rooms.RoomsFragment;
import co.chatsdk.ui.threads.PrivateThreadsFragment;
import co.chatsdk.ui.threads.PublicThreadsFragment;

/**
 * Created by itzik on 6/16/2014.
 */
public class PagerAdapterTabs extends FragmentPagerAdapter {

    Boolean firsttime = true;

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
        if(position == 1 &&firsttime){
            tabs.get(position).fragment = new RoomsFragment(listener);
            firsttime = false;
        }
        Log.w("1",tabs.get(position).fragment.toString());
        return tabs.get(position).fragment;
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public final class FirstPageListener implements
            FirstPageFragmentListener {
        public void onSwitchToNextFragment(int value) {
           Boolean fm = tabs.get(1).fragment instanceof RoomsFragment;
            Log.w("1",fm.toString());
            mFragmentManager.beginTransaction().remove(tabs.get(1).fragment)
                    .commitNow();
            if (fm && (value==0||value==1)){
                tabs.get(1).fragment = new PublicThreadsFragment(listener);
            }else if(fm && (value==2)){ // Instance of NextFragment
                tabs.get(1).fragment = new PrivateThreadsFragment(listener);
            }
            else tabs.get(1).fragment = new RoomsFragment(listener);
            notifyDataSetChanged();
        }
    }


}
