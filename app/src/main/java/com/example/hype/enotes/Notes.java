package com.example.hype.enotes;


import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Notes extends Fragment {

    DB db;
    Cursor cursor;

    final int DIALOG_INFO = 1;

    ListView lv;
    SimpleCursorAdapter scAdapter;

    Button impButton;

    static String[] from = new String[] { DB.COLUMN_THEME, DB.COLUMN_TXT, DB.COLUMN_FAM };
    static int[] to = new int[] { R.id.lvtheme, R.id.lvtext, R.id.impimage };

    static boolean star = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_notes, container, false);
        getActivity().setTitle("Все записи");


        db = new DB(getContext());
        db.open();
        cursor = db.getAllData();


        //listview creeate
        scAdapter = new SimpleCursorAdapter(getContext(), R.layout.list_item, cursor, from, to);
        lv = (ListView) v.findViewById(R.id.listnote);
        lv.setAdapter(scAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d("CHIK", "itemClick: position = " + position + ", id = "
                        + id);
                Intent intent = new Intent(getContext(), LookNote.class);
                cursor.moveToPosition(position);


                int idColIndex = cursor.getColumnIndex("_id");
                int themeColIndex = cursor.getColumnIndex("theme");
                int textlColIndex = cursor.getColumnIndex("txt");
                int importantColIndex = cursor.getColumnIndex("famous");

                //делаем интент с датой и фигачим ено в активити
                intent.putExtra("ID", cursor.getString(idColIndex));
                intent.putExtra("THEME", cursor.getString(themeColIndex));
                intent.putExtra("TEXT", cursor.getString(textlColIndex));
                intent.putExtra("IMPORTANT", cursor.getString(importantColIndex));//cursor.getString(importantColIndex));
                String pos = Integer.toString(position++);
                intent.putExtra("POSITION", pos);
                startActivity(intent);
            }
        });

        // добавляем контекстное меню к списку
        registerForContextMenu(lv);

        //for bttn del all
        FloatingActionButton fabDel = (FloatingActionButton) v.findViewById(R.id.fabSave);
        fabDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.delAll();
                cursor.requery();
                ((MainActivity)getActivity()).setNoteCounts();
                Toast.makeText(getContext(), "Очищено", Toast.LENGTH_SHORT).show();
            }
        });

        //for bttn for refresh
        FloatingActionButton fabRefresh = (FloatingActionButton) v.findViewById(R.id.fabRefresh);
        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewNote newnote = new  NewNote();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment, newnote);
                ft.commit();
            }
        });

        //for bttn info
        FloatingActionButton fabInfo = (FloatingActionButton) v.findViewById(R.id.fabInfo);
        fabInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dlg = new DialogInfo();
                dlg.show(getActivity().getFragmentManager(), "dlg");
            }
        });

        //For important
        impButton = (Button) v.findViewById(R.id.buttonImp);
        impButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //тыкаем на кнопку "звездочка"
            //меняем картинку на on/off
            //и апдейтим курсор)
            //курсор заполняем датой с выборкой по условию
            public void onClick(View view) {
                if(!star) {
                    //onclick change background and update listview
                    impButton.setBackgroundResource(android.R.drawable.btn_star_big_on);
                    star = true;
                    //курсор заполняем датой с выборкой по условию
                    cursor = db.getImportant();
                    cursor.requery();
                    scAdapter = new SimpleCursorAdapter(getContext(), R.layout.list_item, cursor, from, to);
                    lv.setAdapter(scAdapter);
                    logCursor(cursor);
                }else{
                    //onclick change background and update listview
                    impButton.setBackgroundResource(android.R.drawable.btn_star_big_off);
                    star = false;
                    refresh();
                }

            }
        });

        return v;
    }

    //////////////
    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of LoginFragment");
        refresh();
        super.onResume();
    }
    //апдейт списка
    public void refresh(){
        cursor = db.getAllData();
        cursor.requery();
        scAdapter = new SimpleCursorAdapter(getContext(), R.layout.list_item, cursor, from, to);
        lv.setAdapter(scAdapter);
        logCursor(cursor);
    }
    //удержанием меню зовем)
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Удалить запись");
    }

    //команды при контекстном меню
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            // извлекаем id записи и удаляем соответствующую запись в БД
            db.delRec(acmi.id);
            ((MainActivity)getActivity()).setNoteCounts();
            // обновляем курсор
            cursor.requery();
            return true;
        }
        return super.onContextItemSelected(item);
    }


    //для теста выводим в лог БД чтобы видеть что там вообще происходит
    void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d("LogCursor", str);
                } while (c.moveToNext());
            }
        } else
            Log.d("LogCursor", "Cursor is null");
    }

}
