package com.example.tanjas_books;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DoneBorrowedFragment extends Fragment {
    private Context context;

    private DatabaseHelper databaseHelper;

    private RecyclerView recycler;

    private BooksRecyclerAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private List<BooksRecyclerItem> itemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_books_noaction, container, false);
        context = getContext();

        // init coms
        recycler = v.findViewById(R.id.recycler);

        // init database
        databaseHelper = new DatabaseHelper(context);

        // init recycler
        itemList = new ArrayList<BooksRecyclerItem>(Arrays.asList(
                new BooksRecyclerItem(1, "astrid lindgren", "pippi langstrumpf", "0123456789", false, false)
        ));
        recycler.setHasFixedSize(true);
        manager = new LinearLayoutManager(context);
        adapter = new BooksRecyclerAdapter(context, itemList);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);

        // read items
        onReadAll();

        return v;
    }

    public void sort(){
        Collections.sort(itemList, new Comparator<BooksRecyclerItem>() {
            @Override
            public int compare(BooksRecyclerItem o1, BooksRecyclerItem o2) {
                if(o1.getAuthor().compareTo(o2.getAuthor()) != 0)
                    return o1.getAuthor().compareTo(o2.getAuthor());
                else
                    return o1.getIsbn().compareTo(o2.getIsbn());
            }
        });
        adapter.notifyDataSetChanged();
    }

    /***
     * database connection
     */

    public void onReadAll(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        Cursor books = databaseHelper.readAll();
        while(books.moveToNext()){
            int id = books.getInt(0);
            String author = books.getString(1);
            String title = books.getString(2);
            String isbn = books.getString(3);
            boolean done = books.getInt(4)==1;
            boolean borrowed = books.getInt(5)==1;

            if(done && borrowed)
                itemList.add(new BooksRecyclerItem(id, author, title, isbn, done, borrowed));
        }
        adapter.notifyDataSetChanged();
        sort();
    }
}
