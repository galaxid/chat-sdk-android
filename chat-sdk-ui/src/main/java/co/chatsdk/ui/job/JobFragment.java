package co.chatsdk.ui.job;

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

public class JobFragment extends BaseFragment {


    private static FirstPageFragmentListener firstPageListener;

    public JobFragment() {
    }

    public JobFragment(FirstPageFragmentListener listener) {
        firstPageListener = listener;
    }
    public View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static JobFragment newInstance() {
        JobFragment f = new JobFragment();

        Bundle b = new Bundle();

        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_job,container,false);
        Button button1 = (Button) view.findViewById(R.id.button_job);
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
        return view;

    }

    @Override
    public void clearData() {

    }

    @Override
    public void reloadData() {

    }

}
