package com.hongik.insecurememo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hongik.insecurememo.R;
import com.hongik.insecurememo.item.MemoItem;

import java.util.ArrayList;

public class MemoListAdapter extends RecyclerView.Adapter<MemoListAdapter.ViewHolder> {
    private Context context;
    private int resource;
    private ArrayList<MemoItem> itemList;

    public MemoListAdapter(Context context, int resource, ArrayList<MemoItem> itemList) {
        this.context = context;
        this.resource = resource;
        this.itemList = itemList;
    }

    public void addItem(MemoItem item) {
        this.itemList.add(0, item);
        notifyDataSetChanged();
    }

    public void addItemList(ArrayList<MemoItem> itemList) {
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MemoItem memoItem = itemList.get(position);

        holder.categoryText.setText(memoItem.category);
        holder.memoText.setText(memoItem.memo);
        holder.dateText.setText(memoItem.regDate);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(context, memoItem.memo, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryText;
        TextView memoText;
        TextView dateText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryText = itemView.findViewById(R.id.category);
            memoText = itemView.findViewById(R.id.memo);
            dateText = itemView.findViewById(R.id.regdate);
        }


    }


}
