package co.publicscience.argos.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.publicscience.argos.Models.Event;
import co.publicscience.argos.R;

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
        viewHolder.setEvent(event);
    }

    @Override
    public int getItemCount() {
        return events == null ? 0 : events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView view;
        private Event event;

        public ViewHolder(CardView itemView) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(this);
        }

        public void setEvent(Event event) {
            this.event = event;

            TextView eventTitle = (TextView)itemView.findViewById(R.id.eventTitle);
            TextView eventTimeAgo = (TextView)itemView.findViewById(R.id.eventTimeAgo);
            TextView eventArticleCount = (TextView)itemView.findViewById(R.id.eventArticleCount);

            // TO DO make a better way of evaluating event importance/popularity
            if (event.getScore() > 6304.0) {
                view.setCardBackgroundColor(mContext.getResources().getColor(R.color.highlight));
                eventTitle.setTextColor(mContext.getResources().getColor(R.color.white));
            }
            eventTitle.setText(event.getTitle());
            eventTimeAgo.setText(event.getTimeAgo());
            eventArticleCount.setText(String.format("%d articles", event.getArticles().size()));
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