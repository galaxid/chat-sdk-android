package co.chatsdk.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

import co.chatsdk.core.dao.Keys;
import co.chatsdk.core.dao.User;
import co.chatsdk.core.events.EventType;
import co.chatsdk.core.events.NetworkEvent;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.core.types.ConnectionType;
import co.chatsdk.core.utils.StringChecker;
import co.chatsdk.ui.R;
import co.chatsdk.ui.main.BaseFragment;
import co.chatsdk.ui.main.PagerAdapterTabs;
import co.chatsdk.ui.profile.ProfileFragment;
import co.chatsdk.ui.rooms.FirstPageFragmentListener;
import co.chatsdk.ui.threads.PrivateThreadsFragment;
import co.chatsdk.ui.threads.PublicThreadsFragment;
import co.chatsdk.ui.utils.AvailabilityHelper;
import co.chatsdk.ui.utils.ToastHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**

 */

public class NewsFragment extends BaseFragment {


    private static FirstPageFragmentListener firstPageListener;

    public NewsFragment() {
    }

    public NewsFragment(FirstPageFragmentListener listener) {
        firstPageListener = listener;
    }
    public View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        title = "UCI Career";
        this.getActivity().setTitle(title);

        super.onCreate(savedInstanceState);
    }



    public static NewsFragment newInstance() {
        NewsFragment f = new NewsFragment();

        Bundle b = new Bundle();

        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_news,container,false);
        Button button1 = (Button) view.findViewById(R.id.button_news);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                firstPageListener.onSwitchToNextFragment(3);
            }
        });
        button1.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View arg0) {
                return true;
            }

        });

        Button button2= (Button) view.findViewById(R.id.button_events);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                firstPageListener.onSwitchToNextFragment(4);
            }
        });
        button2.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View arg0) {
                return true;
            }

        });
        return view;

    }

    @Override
    public void clearData() {

    }

    @Override
    public void reloadData() {

    }

}
