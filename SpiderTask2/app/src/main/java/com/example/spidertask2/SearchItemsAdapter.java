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

    public static class SearchItemsViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;

        public SearchItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textView_search_title);
            description = itemView.findViewById(R.id.textView_search_description);

        }
    }

    public SearchItemsAdapter(ArrayList<DataItems> dataItems) {
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public SearchItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        SearchItemsViewHolder viewHolder = new SearchItemsViewHolder(v);
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

}
