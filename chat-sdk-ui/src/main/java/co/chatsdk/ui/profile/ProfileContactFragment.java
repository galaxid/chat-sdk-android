package co.chatsdk.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import co.chatsdk.ui.R;
import co.chatsdk.ui.main.BaseFragment;
import co.chatsdk.ui.rooms.FirstPageFragmentListener;

/**

 */

public class ProfileContactFragment extends BaseFragment {


    private static FirstPageFragmentListener firstPageListener;

    public ProfileContactFragment() {
    }

    public ProfileContactFragment(FirstPageFragmentListener listener) {
        firstPageListener = listener;
    }
    public View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        title = "UCI Career";
        this.getActivity().setTitle(title);
        super.onCreate(savedInstanceState);
    }

    public static ProfileContactFragment newInstance() {
        ProfileContactFragment f = new ProfileContactFragment();

        Bundle b = new Bundle();

        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_profile_contacts,container,false);
        Button button1 = (Button) view.findViewById(R.id.button_profile);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                firstPageListener.onSwitchToNextFragment(5);
            }
        });
        button1.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View arg0) {
                return true;
            }

        });

        Button button2= (Button) view.findViewById(R.id.button_contacts);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                firstPageListener.onSwitchToNextFragment(6);
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
