package co.chatsdk.ui.mock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import co.chatsdk.core.dao.Thread;
import co.chatsdk.core.interfaces.ThreadType;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.ui.R;
import co.chatsdk.ui.main.BaseActivity;
import co.chatsdk.ui.main.BaseFragment;
import co.chatsdk.ui.rooms.FirstPageFragmentListener;

import static co.chatsdk.ui.search.NameInterpreter.isAdmin;

/**

 */

public class MockFragment extends BaseFragment {


    protected Thread userThread;

    private static FirstPageFragmentListener firstPageListener;

    public MockFragment() {
    }

    public MockFragment(FirstPageFragmentListener listener) {
        firstPageListener = listener;
    }
    public View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        title = "UCI Career";
        this.getActivity().setTitle(title);

        if(isAdmin())userThread = null;
        else setUserThread();

        super.onCreate(savedInstanceState);
    }

    public static MockFragment newInstance() {
        MockFragment f = new MockFragment();

        Bundle b = new Bundle();

        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_mock,container,false);
        Button button1 = (Button) view.findViewById(R.id.button_mock);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                firstPageListener.onSwitchToNextFragment(9);
            }
        });
        button1.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View arg0) {
                return true;
            }

        });
        Button button2 = (Button) view.findViewById(R.id.button_post);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                if(userThread == null) {
                    ((BaseActivity)getActivity()).showToast("You are a Admin!");
                }
                else ChatSDK.ui().startMockEditDetailsActivity(ChatSDK.shared().context(), userThread.getEntityID());
            }
        });
        button2.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View arg0) {
                return true;
            }

        });
        Button button3 = (Button) view.findViewById(R.id.button_delete);
        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                if(userThread == null) {
                    ((BaseActivity)getActivity()).showToast("You are a Admin!");
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setTitle("Warning")
                            .setMessage("Do you want to delete your request?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(userThread.getName().equals(ChatSDK.currentUser().getName())){
                                        ((BaseActivity)getActivity()).showToast("Request doesn't exist");
                                    }
                                    else {
                                        userThread.setName(ChatSDK.currentUser().getName());
                                        userThread.update();
                                        disposableList.add(ChatSDK.thread().pushThread(userThread).subscribe());
                                        ((BaseActivity) getActivity()).showToast("Request deleted");
                                    }
                                }
                            })

                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .create();
                    alertDialog.show();

                }

            }
        });
        button3.setOnLongClickListener(new View.OnLongClickListener(){
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

    public void setUserThread(){
        List<Thread> threads = ChatSDK.thread().getThreads(ThreadType.Public);
        for (Thread t : threads){
            if(t.getCreator().isMe()){
                userThread = t;
            }
        }
    }

}
