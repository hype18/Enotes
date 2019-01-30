package com.example.hype.enotes;

import android.content.ContentValues;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LookNote extends AppCompatActivity {

    DB db;

    TextView numNote;

    EditText etTheme;
    EditText etText;

    Button btnImp;

    FloatingActionButton save;
    ContentValues cv;

    static boolean star = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_note);

        db = new DB(this);
        db.open();

        numNote = (TextView) findViewById(R.id.noteNum);
        etTheme = (EditText) findViewById(R.id.editText2);
        etText = (EditText) findViewById(R.id.etText);


        final Intent intent = getIntent();

        final String id = intent.getStringExtra("ID");
        final String numb = intent.getStringExtra("POSITION");
        final String theme = intent.getStringExtra("THEME");
        final String text = intent.getStringExtra("TEXT");
        final String important = intent.getStringExtra("IMPORTANT");

        int num = Integer.parseInt(numb)+1;
        numNote.setText("Запись №"+(num));
        etTheme.setText(theme);
        etText.setText(text);


        cv = new ContentValues();
        btnImp = (Button) findViewById(R.id.buttonImp);
        //принимаем екстразы из интента, которые послал активити

        /**Завтра разгребу, 3:35
         * Допилить активити под запись, добавить инфо
         * а дальше хз, как фантазия
         * */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSave);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imp;
                if (star == true)
                    imp = "17301516";
                else
                    imp = "17301515";
                db.updateNote(id, etTheme.getText().toString(), etText.getText().toString(), imp);
                Toast.makeText(getBaseContext(), "Cохранено", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        if(important.equals("17301516")) {
            btnImp.setBackgroundResource(android.R.drawable.btn_star_big_on);
            star = true;
        }else {
            btnImp.setBackgroundResource(android.R.drawable.btn_star_big_off);
            star = false;
        }

        Log.d("IntentExtras", "Theme:"+intent.getStringExtra("THEME")+" Text:"+intent.getStringExtra("TEXT")
                +" IMPORTANT:"+intent.getStringExtra("IMPORTANT")
                +" POSITION:"+intent.getStringExtra("POSITION"));

    }


    public void onClick(View view) {
        if(!star) {
            //onclick change background and update listview
            btnImp.setBackgroundResource(android.R.drawable.btn_star_big_on);
            star = true;
        }else{
            //onclick change background and update listview
            btnImp.setBackgroundResource(android.R.drawable.btn_star_big_off);
            star = false;

        }

    }

//    public void onBackPressed() {
    //}
}
