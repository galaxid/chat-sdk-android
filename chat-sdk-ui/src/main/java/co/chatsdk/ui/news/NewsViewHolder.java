package co.chatsdk.ui.news;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import co.chatsdk.ui.R;

/**
 * Created by benjaminsmiley-andrews on 07/06/2017.
 */

public class NewsViewHolder extends RecyclerView.ViewHolder {

    public TextView nameTextView;
    public TextView dateTextView;
    public TextView introTextView;
    public SimpleDraweeView imageView;
    public View indicator;

    public NewsViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.text_name_events);
        dateTextView = itemView.findViewById(R.id.event_date);
        introTextView = itemView.findViewById(R.id.event_intro);
        imageView = itemView.findViewById(R.id.img_event_image);
        indicator = itemView.findViewById(R.id.events_indicator);

    }



}