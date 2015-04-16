package com.alieeen.snitch.adapter;

import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alieeen.snitch.R;
import com.alieeen.snitch.model.Event;

import java.util.List;

/**
 * Created by alinekborges on 09/04/15.
 */
public class EventsAdapter  extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<Event> items;
    private Activity context;

    private static final int TYPE_NEW = 1;
    private static final int TYPE_OLD = 0;

    public EventsAdapter(Activity context, List<Event> items) {
        this.items = items;
        this.context = context;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        if (viewType == TYPE_NEW) {


            //inflate your layout and pass it to view holder
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_new, parent, false);
            return new ViewHolder(v);
        }

        else {
            //inflate your layout and pass it to view holder
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_old, parent, false);
            return new ViewHolder(v);
        }

        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);
        //return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //TODO
        //conect view holder

    }



    @Override
    public int getItemViewType(int position) {
        Event event = items.get(position);
        if (!event.getViewed())
            return TYPE_NEW;
        else {
            return TYPE_OLD;
        }
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView camera;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            //image = (ImageView) itemView.findViewById(R.id.project_image);


        }
    }

}



