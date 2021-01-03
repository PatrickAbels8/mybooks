package com.example.tanjas_books;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BooksRecyclerAdapter extends RecyclerView.Adapter<BooksRecyclerAdapter.ItemViewHolder> {
    private Context context;
    private List<BooksRecyclerItem> booksRecyclerItemList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onBorrowedClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public BooksRecyclerAdapter(Context context, List<BooksRecyclerItem> booksRecyclerItemList){
        this.context = context;
        this.booksRecyclerItemList = booksRecyclerItemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item_books, parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position){
        holder.itemAuthor.setText(booksRecyclerItemList.get(position).getAuthor());
        String _title = booksRecyclerItemList.get(position).getTitle()+" "+booksRecyclerItemList.get(position).getIsbn();
        holder.itemTitle.setText(_title);
        if(booksRecyclerItemList.get(position).getBorrowed())
            holder.itemBorrowed.setImageResource(R.drawable.ic_baseline_local_fire_department_24);
        else
            holder.itemBorrowed.setImageResource(R.drawable.ic_baseline_add_24);
        if(booksRecyclerItemList.get(position).getDone())
            holder.itemDone.setBackgroundResource(R.drawable.diagonal);
        else
            holder.itemDone.setBackgroundResource(R.drawable.transparent);
    }

    @Override
    public int getItemCount(){
        return booksRecyclerItemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView itemAuthor;
        TextView itemTitle;
        ImageView itemBorrowed;
        RelativeLayout itemDone;

        public ItemViewHolder(@NonNull View itemView, final OnItemClickListener clickListener){
            super(itemView);

            itemAuthor = itemView.findViewById(R.id.item_author);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemBorrowed = itemView.findViewById(R.id.item_borrowed);
            itemDone = itemView.findViewById(R.id.item_done);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            clickListener.onItemClick(position);
                        }
                    }
                }
            });

            itemBorrowed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            clickListener.onBorrowedClick(position);
                        }
                    }
                }
            });
        }
    }
}
