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

public class ImportDialog extends DialogFragment {
    private TextView header;
    private TextInputLayout input;
    private Button action_ok;
    private Button action_cancel;

    public interface OnImportSelected{
        void sendImport(String input);
    }
    public OnImportSelected onImportSelected;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.dialog_import, container, false);

        header = v.findViewById(R.id.header);
        input = v.findViewById(R.id.input);
        action_ok = v.findViewById(R.id.ok);
        action_cancel = v.findViewById(R.id.cancel);

        Bundle args = getArguments();

        action_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        action_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _input = input.getEditText().getText().toString();

                if(!_input.equals("")){
                    onImportSelected.sendImport(_input);
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
            onImportSelected = (OnImportSelected) getTargetFragment();
        }catch (ClassCastException e){
            Log.e("ImportDialog", "onAttach: ClassCastException : "+e.getMessage());
        }
    }
}
