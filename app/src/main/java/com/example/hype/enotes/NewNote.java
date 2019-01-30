package com.example.hype.enotes;


import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewNote extends Fragment {

    DB db;
    Cursor cursor;

    EditText theme;
    EditText text;
    CheckBox imp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_new_note, container, false);
        getActivity().setTitle("Новая запись");
        //весь код после строк выше в онкрейте

        db = new DB(getContext());
        db.open();
        cursor = db.getAllData();

        theme = (EditText) v.findViewById(R.id.etTheme);
        text = (EditText) v.findViewById(R.id.etText);
        imp = (CheckBox) v.findViewById(R.id.cbFam);



        //сохранить запись
        FloatingActionButton fabSave = (FloatingActionButton) v.findViewById(R.id.fabSave);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int important = 0;
                int bool = 0;
                if(imp.isChecked()){
                    important = android.R.drawable.btn_star_big_on;
                }else
                {
                    important = android.R.drawable.btn_star_big_off;
                }

                db.addRec(theme.getText().toString(), text.getText().toString(), important);
                    Snackbar.make(view, "Запись сохранена "+theme.getText(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    cursor.requery();
                ((MainActivity)getActivity()).setNoteCounts();
                theme.setText("");
                text.setText("");
                Notes notes = new  Notes();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment, notes);
                ft.commit();

                logcursor();
            }
        });

        //переход ко всем записям
        FloatingActionButton fabNote = (FloatingActionButton) v.findViewById(R.id.fabNotes);
        fabNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notes notes = new  Notes();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment, notes);
                ft.commit();
            }
        });

        return v;
    }

    //для теста все ли гуд, будем выводить в логи всю базу
    private void logcursor() {
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : cursor.getColumnNames()) {
                        str = str.concat(cn + " = "
                                + cursor.getString(cursor.getColumnIndex(cn)) + "; ");
                    }
                    Log.d("DataBase", str);

                } while (cursor.moveToNext());
            }

        }
    }
}