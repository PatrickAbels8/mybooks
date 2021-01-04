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

public class DeleteDialog extends DialogFragment {
    private TextView author;
    private TextView title;
    private TextView isbn;
    private Button action_ok;
    private Button action_cancel;

    private int position;

    public interface OnInputSelected{
        void sendDeletion(int position);
    }
    public OnInputSelected onInputSelected;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.dialog_delete, container, false);

        author = v.findViewById(R.id.author);
        title = v.findViewById(R.id.title);
        isbn = v.findViewById(R.id.isbn);
        action_ok = v.findViewById(R.id.ok);
        action_cancel = v.findViewById(R.id.cancel);

        Bundle args = getArguments();
        position = args.getInt(getString(R.string.intent_position));

        author.setText(args.getString(getString(R.string.intent_author)));
        title.setText(args.getString(getString(R.string.intent_title)));
        isbn.setText(args.getString(getString(R.string.intent_isbn)));

        action_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        action_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInputSelected.sendDeletion(position);
                getDialog().dismiss();
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
            Log.e("DeleteDialog", "onAttach: ClassCastException : "+e.getMessage());
        }
    }
}
