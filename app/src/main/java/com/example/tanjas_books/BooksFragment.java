package com.example.tanjas_books;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class BooksFragment extends Fragment implements BookDialog.OnInputSelected, DeleteDialog.OnInputSelected {
    private Context context;

    private DatabaseHelper databaseHelper;

    private RecyclerView recycler;
    private FloatingActionButton add;

    private BooksRecyclerAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private List<BooksRecyclerItem> itemList;

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch(direction){
                case ItemTouchHelper.LEFT:
                    BooksRecyclerItem item = itemList.get(position);

                    Bundle args = new Bundle();
                    args.putInt(getString(R.string.intent_position), viewHolder.getAdapterPosition());
                    args.putString(getString(R.string.intent_author), item.getAuthor());
                    args.putString(getString(R.string.intent_title), item.getTitle());
                    args.putString(getString(R.string.intent_isbn), item.getIsbn());

                    DeleteDialog dialog = new DeleteDialog();
                    dialog.setTargetFragment(BooksFragment.this, 1);
                    dialog.setArguments(args);
                    dialog.show(getFragmentManager(), "");

                    adapter.notifyDataSetChanged();
                    break;
                case ItemTouchHelper.RIGHT:
                    onDone(position);
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(context, R.color.bad))
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(context, R.color.good))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_check_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_books, container, false);
        context = getContext();

        // init coms
        recycler = v.findViewById(R.id.recycler);
        add = v.findViewById(R.id.add);

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

        // read/unread/delete item
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler);


        // add item
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString(getString(R.string.intent_method), getString(R.string.dialog_header_new));

                BookDialog dialog = new BookDialog();
                dialog.setTargetFragment(BooksFragment.this, 1);
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), "");
            }
        });

        // edit item
        adapter.setOnItemClickListener(new BooksRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                BooksRecyclerItem item = itemList.get(position);

                Bundle args = new Bundle();
                args.putString(getString(R.string.intent_method), getString(R.string.dialog_header_edit));
                args.putInt(getString(R.string.intent_position), position);
                args.putString(getString(R.string.intent_author), item.getAuthor());
                args.putString(getString(R.string.intent_title), item.getTitle());
                args.putString(getString(R.string.intent_isbn), item.getIsbn());

                BookDialog dialog = new BookDialog();
                dialog.setTargetFragment(BooksFragment.this, 1);
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), "");
            }

            @Override
            public void onBorrowedClick(int position) {
                BooksRecyclerItem item = itemList.get(position);
                if(item.getBorrowed())
                    onBorrowed(position, false);
                else
                    onBorrowed(position, true);
            }
        });

        return v;
    }

    @Override
    public void sendInput(String method, int position, String author, String title, String isbn){
        if(method.equals(getString(R.string.dialog_header_edit))){
            onEdit(position, author, title, isbn);
        }else if(method.equals(getString(R.string.dialog_header_new))){
            onNew(author, title, isbn);
        }
    }

    @Override
    public void sendDeletion(int position) {
        onDelete(position);
    }

    public void sort(){
        Collections.sort(itemList, new Comparator<BooksRecyclerItem>() {
            @Override
            public int compare(BooksRecyclerItem o1, BooksRecyclerItem o2) {
                return o1.getAuthor().compareTo(o2.getAuthor());
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
            itemList.add(new BooksRecyclerItem(id, author, title, isbn, done, borrowed));
        }
        adapter.notifyDataSetChanged();
        sort();
    }

    public void onDelete(int position){
        databaseHelper.delete(itemList.get(position).getId());
        itemList.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void onDone(int position){
        if(itemList.get(position).getDone()) {
            databaseHelper.done(itemList.get(position).getId(), false);
            itemList.get(position).setDone(false);
        }
        else {
            databaseHelper.done(itemList.get(position).getId(), true);
            itemList.get(position).setDone(true);
        }
        adapter.notifyItemChanged(position);

    }

    public void onBorrowed(int position, boolean borrowed){
        databaseHelper.borrowed(itemList.get(position).getId(), borrowed);
        itemList.get(position).setBorrowed(borrowed);
        adapter.notifyItemChanged(position);
    }

    public void onNew(String author, String title, String isbn){
        long result = databaseHelper.add(author, title, isbn);
        if(result == -1){
            Toast.makeText(context, getString(R.string.error_dbError), Toast.LENGTH_SHORT).show();
            return;
        }else{
            BooksRecyclerItem item = new BooksRecyclerItem(Math.toIntExact(result), author, title, isbn, false, false);
            itemList.add(item);
            adapter.notifyItemInserted(itemList.size());
        }
        sort();
    }

    public void onEdit(int position, String author, String title, String isbn){
        databaseHelper.edit(itemList.get(position).getId(), author, title, isbn);
        itemList.get(position).setAuthor(author);
        itemList.get(position).setTitle(title);
        itemList.get(position).setIsbn(isbn);
        adapter.notifyItemChanged(position);
        sort();
    }
}
