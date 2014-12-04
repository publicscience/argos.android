package co.publicscience.argos;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.publicscience.argos.Models.Event;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private List<Event> events;
    private int rowLayout;
    private Context mContext;
    OnItemClickListener mItemClickListener;

    public EventAdapter(List<Event> events, int rowLayout, Context context) {
        this.events = events;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        CardView v = (CardView)LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Event event = events.get(i);

        // TO DO make a better way of evaluating event importance/popularity
        if (event.getScore() > 6304.0) {
            viewHolder.view.setCardBackgroundColor(mContext.getResources().getColor(R.color.highlight));
            viewHolder.eventTitle.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        viewHolder.eventTitle.setText(event.getTitle());
        viewHolder.eventTimeAgo.setText(event.getTimeAgo());
        viewHolder.eventArticleCount.setText(String.format("%d articles", event.getArticles().size()));
        viewHolder.event = event;
    }

    @Override
    public int getItemCount() {
        return events == null ? 0 : events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView eventTitle;
        public TextView eventTimeAgo;
        public TextView eventArticleCount;
        public CardView view;
        public Event event;

        public ViewHolder(CardView itemView) {
            super(itemView);
            eventTitle = (TextView)itemView.findViewById(R.id.eventTitle);
            eventTimeAgo = (TextView)itemView.findViewById(R.id.eventTimeAgo);
            eventArticleCount = (TextView)itemView.findViewById(R.id.eventArticleCount);
            view = itemView;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition(), event);
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position, Event event);
    }
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}