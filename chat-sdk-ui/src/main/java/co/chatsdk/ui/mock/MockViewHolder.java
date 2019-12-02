package co.chatsdk.ui.mock;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import co.chatsdk.ui.R;

/**
 * Created by benjaminsmiley-andrews on 07/06/2017.
 */

public class MockViewHolder extends RecyclerView.ViewHolder {

    public TextView nameTextView;
    public TextView majorTextView;
    public TextView dateTextView;
    public TextView locationTextView;
    public TextView introTextView;
    public SimpleDraweeView imageView;
    public View indicator;

    public MockViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.text_name_events);
        majorTextView= itemView.findViewById(R.id.text_major);
        dateTextView = itemView.findViewById(R.id.event_date);
        locationTextView = itemView.findViewById(R.id.event_location);
        introTextView = itemView.findViewById(R.id.text_intro);
        imageView = itemView.findViewById(R.id.img_event_image);
        indicator = itemView.findViewById(R.id.events_indicator);

    }




}