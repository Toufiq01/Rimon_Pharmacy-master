package com.pauloavelar.inventory.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.pauloavelar.inventory.R;
import com.pauloavelar.inventory.model.InventoryItem;

import java.util.ArrayList;


class InventoryItemAdapter extends RecyclerView.Adapter<InventoryItemAdapter.ViewHolder>
    implements Filterable {

    private ArrayList<InventoryItem> mItems;
    private ArrayList<InventoryItem> mItemsfull;
    private final OnItemInteraction mListener;



    interface OnItemInteraction {
        void onItemClick(InventoryItem item);
        void onItemLongPress(InventoryItem item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView product, lotCode, bagCount;

        ViewHolder(View itemView) {
            super(itemView);
            product  = (TextView) itemView.findViewById(R.id.list_product);
            lotCode  = (TextView) itemView.findViewById(R.id.list_lot_code);
            bagCount = (TextView) itemView.findViewById(R.id.list_bag_count);
        }

        void bind(final InventoryItem item, final OnItemInteraction listener) {
            product.setText(item.getProduct());
            lotCode.setText(item.getLotCode());
            bagCount.setText(String.valueOf(item.getBagCount()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override public boolean onLongClick(View view) {
                    listener.onItemLongPress(item);
                    return true;
                }
            });
        }
    }



    void clearAll() {
        if (mItems != null) mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext())
                                     .inflate(R.layout.layout_list, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InventoryItem item = mItems.get(position);
        holder.bind(item, mListener);
    }

    @Override
    public long getItemId(int i) {
        return (mItems.get(i) == null ? 0 : mItems.get(i).getId());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    InventoryItemAdapter(OnItemInteraction listener) {
        mListener = listener;
    }

    void setItems(ArrayList<InventoryItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

//    public void updateList(ArrayList<InventoryItem> newList){
//        mItems = new ArrayList<>();
//        mItems.addAll(newList);
//        notifyDataSetChanged();
//    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                mItemsfull = new ArrayList<>(mItems);
                ArrayList <InventoryItem> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(mItemsfull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (InventoryItem item : mItemsfull) {

                        // name match condition. this might differ depending on your requirement

                        if (item.getProduct().toLowerCase().contains(filterPattern) ) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                mItems.clear();
//                mItems.addAll((List) filterResults.values);
                mItems = (ArrayList<InventoryItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}