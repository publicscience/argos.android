package co.publicscience.argos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.publicscience.argos.Models.Concept;

public class ConceptAdapter extends RecyclerView.Adapter<ConceptAdapter.ViewHolder> {
    private List<Concept> concepts;
    private int rowLayout;
    private Context mContext;
    OnItemClickListener mItemClickListener;

    public ConceptAdapter(List<Concept> concepts, int rowLayout, Context context) {
        this.concepts = concepts;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = (View)LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Concept concept = concepts.get(i);
        viewHolder.setConcept(concept);
    }

    @Override
    public int getItemCount() {
        return concepts == null ? 0 : concepts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Concept concept;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(this);
        }

        public void setConcept(Concept concept) {
            this.concept = concept;

            TextView conceptName = (TextView)view.findViewById(R.id.conceptName);
            TextView conceptSummary = (TextView)view.findViewById(R.id.conceptSummary);
            ImageView imageView = (ImageView)view.findViewById(R.id.conceptImage);

            conceptName.setText(concept.getName());
            conceptSummary.setText(concept.getSummary());
            Picasso.with(mContext).load(concept.getImage()).fit().centerCrop().into(imageView);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition(), concept);
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position, Concept concept);
    }
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
