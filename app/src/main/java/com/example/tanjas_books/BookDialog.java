package com.example.tanjas_books;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

public class BookDialog extends DialogFragment {
    private TextView header;
    private TextInputLayout author;
    private TextInputLayout title;
    private TextInputLayout isbn;
    private Button action_ok;
    private Button action_cancel;

    private String method;
    private int position;

    public interface OnInputSelected{
        void sendInput(String method, int position, String author, String title, String isbn);
    }
    public OnInputSelected onInputSelected;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.dialog_book, container, false);

        header = v.findViewById(R.id.header);
        author = v.findViewById(R.id.author);
        title = v.findViewById(R.id.title);
        isbn = v.findViewById(R.id.isbn);
        action_ok = v.findViewById(R.id.ok);
        action_cancel = v.findViewById(R.id.cancel);

        Bundle args = getArguments();
        method = args.getString(getString(R.string.intent_method));
        position = args.getInt(getString(R.string.intent_position));

        if(method.equals(getString(R.string.dialog_header_new))){
            header.setText(getString(R.string.dialog_header_new));
            author.getEditText().setText("");
            title.getEditText().setText("");
            isbn.getEditText().setText("");
        }else if(method.equals(getString(R.string.dialog_header_edit))){
            header.setText(getString(R.string.dialog_header_edit));
            author.getEditText().setText(args.getString(getString(R.string.intent_author)));
            title.getEditText().setText(args.getString(getString(R.string.intent_title)));
            isbn.getEditText().setText(args.getString(getString(R.string.intent_isbn)));
        }

        action_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        action_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _author = author.getEditText().getText().toString();
                String _title = title.getEditText().getText().toString();
                String _isbn = isbn.getEditText().getText().toString();

                if(!_author.equals("") && !_title.equals("")){
                    onInputSelected.sendInput(method, position, _author, _title, _isbn);
                    getDialog().dismiss();
                }else{
                    Toast.makeText(getContext(), getString(R.string.error_wrongInput), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        try{
            onInputSelected = (OnInputSelected) getTargetFragment();
        }catch (ClassCastException e){
            Log.e("BookDialog", "onAttach: ClassCastException : "+e.getMessage());
        }
    }
}
