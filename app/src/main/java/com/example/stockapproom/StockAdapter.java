package com.example.stockapproom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private List<StockItem> stockItemList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public StockAdapter(List<StockItem> stockItemList, Context context) {
        this.stockItemList = stockItemList;
        this.context = context;
    }
    public void setStockItemList(List<StockItem> stockItemList) {
        this.stockItemList = stockItemList;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        StockItem stockItem = stockItemList.get(position);
        holder.tvItem.setText(stockItem.getItem());
        holder.tvQuantity.setText(String.valueOf(stockItem.getQuantity()));
        holder.itemView
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(stockItem);
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        if (stockItemList == null) {
            return 0; // Return 0 if the list is null
        } else {
            return stockItemList.size(); // Return the size of the list if it's not null
        }
    }



    public static class StockViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItem;
        public TextView tvQuantity;

        public StockViewHolder(View view) {
            super(view);
            tvItem = view.findViewById(R.id.tv_item);
            tvQuantity = view.findViewById(R.id.tv_quantity);
        }
    }
}
