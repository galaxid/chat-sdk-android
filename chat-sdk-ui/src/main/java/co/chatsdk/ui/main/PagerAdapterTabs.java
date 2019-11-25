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
import co.chatsdk.core.ui.ProfileFragmentProvider;
import co.chatsdk.ui.contacts.ContactsFragment;
import co.chatsdk.ui.job.JobFragment;
import co.chatsdk.ui.mock.MockFragment;
import co.chatsdk.ui.news.EventListFragment;
import co.chatsdk.ui.news.NewsFragment;
import co.chatsdk.ui.news.NewsListFragment;
import co.chatsdk.ui.profile.ProfileContactFragment;
import co.chatsdk.ui.profile.ProfileFragment;
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
    private Boolean firsttime2 = true;
    private Boolean firsttime3 = true;
    private Boolean firsttime4 = true;


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
        if(position == 2 &&firsttime2){
            tabs.get(position).fragment = new JobFragment(listener);
            firsttime2 = false;
        }

        if(position == 3 &&firsttime3){
            tabs.get(position).fragment = new MockFragment(listener);
            firsttime3 = false;
        }
        if(position == 4 &&firsttime4){
            tabs.get(position).fragment = new ProfileContactFragment(listener);
            firsttime4 = false;
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
            if(value==3||value==4){
                boolean b = tabs.get(0).fragment instanceof NewsFragment;
                mFragmentManager.beginTransaction().remove(tabs.get(0).fragment)
                        .commitNow();
                if (b && value==3){
                    tabs.get(0).fragment = new NewsListFragment(listener);
                }else if(b){ // Instance of NextFragment
                    tabs.get(0).fragment = new EventListFragment(listener);
                }
                else tabs.get(0).fragment = new NewsFragment(listener);

            }
            if(value<3){
                boolean b = tabs.get(1).fragment instanceof RoomsFragment;
                mFragmentManager.beginTransaction().remove(tabs.get(1).fragment)
                        .commitNow();
                if (b && value==0){
                    tabs.get(1).fragment = new PublicThreadsFragment(listener,true);
                }else if(b && (value==1)){ // Instance of NextFragment
                    tabs.get(1).fragment = new PublicThreadsFragment(listener,false);
                }
                else if(b && (value==2)){ // Instance of NextFragment
                    tabs.get(1).fragment = new PrivateThreadsFragment(listener);
                }
                else tabs.get(1).fragment = new RoomsFragment(listener);

            }
            if(value==5||value==6){
                boolean b = tabs.get(4).fragment instanceof ProfileContactFragment;
                mFragmentManager.beginTransaction().remove(tabs.get(4).fragment)
                        .commitNow();
                if (b && value==5){
                    ProfileFragmentProvider profileFragmentProvider = ProfileFragment::newInstance;
                    tabs.get(4).fragment = profileFragmentProvider.profileFragment(null);
                    ((ProfileFragment)tabs.get(4).fragment).setListener(listener);
                }else if(b){ // Instance of NextFragment
                    tabs.get(4).fragment = new ContactsFragment();
                    ((ContactsFragment)tabs.get(4).fragment).setListener(listener);
                }
                else tabs.get(4).fragment = new ProfileContactFragment(listener);

            }

            notifyDataSetChanged();
        }
    }


}
