package com.example.spidertask2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchItemsAdapter extends RecyclerView.Adapter<SearchItemsAdapter.SearchItemsViewHolder> {
    private ArrayList<DataItems> dataItems;
    private OnItemListener onItemListener;

    public static class SearchItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView description;
        OnItemListener onItemListener;

        public SearchItemsViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            this.onItemListener = onItemListener;

            title = itemView.findViewById(R.id.textView_search_title);
            description = itemView.findViewById(R.id.textView_search_description);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }

    }

    public SearchItemsAdapter(ArrayList<DataItems> dataItems, OnItemListener onItemListener) {
        this.dataItems = dataItems;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public SearchItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        SearchItemsViewHolder viewHolder = new SearchItemsViewHolder(v, onItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemsViewHolder holder, int position) {
        DataItems currentItem = dataItems.get(position);
        holder.title.setText(currentItem.getTitle());
        holder.description.setText(currentItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public interface OnItemListener {
        void onItemClick(int pos);
    }

}
