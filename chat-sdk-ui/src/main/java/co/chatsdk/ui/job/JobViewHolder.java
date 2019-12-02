package co.chatsdk.ui.job;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import co.chatsdk.ui.R;

/**
 * Created by benjaminsmiley-andrews on 07/06/2017.
 */

public class JobViewHolder extends RecyclerView.ViewHolder {

    public TextView nameTextView;
    public TextView dateTextView;
    public TextView locationTextView;
    public SimpleDraweeView imageView;
    public View indicator;

    public JobViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.text_name_events);
        locationTextView = itemView.findViewById(R.id.event_location);
        dateTextView = itemView.findViewById(R.id.event_date);
        imageView = itemView.findViewById(R.id.img_event_image);
        indicator = itemView.findViewById(R.id.events_indicator);

    }




}