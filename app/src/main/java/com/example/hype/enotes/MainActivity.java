package com.example.hype.enotes;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * class DB взят с сайта startandroid.ru
 * и то он там немного переписан, вернее дописаны мои методы
 * зачем изобретать велосипед, если можно взять "шаблон"?)
 * Краткая история создания проекта:
 * Если интересно, то читайте)
 * Сидел с ноутом на xfce, дабы меньше жрало оперативки
 * Потом все проекты скинул на флешку, ибо решил я deepin15.9 натянуть на свою железку
 * , так как приобрел +4 к озу(8 в сумме)
 * (да и больно он мне симпатичен)
 * И в момент, когда поставил систему и вставил флешку с проектами....
 * Ошибка вылетела: "накопитель поврежден, требуется формат"
 * Я рыдал, я страдал, пробовал в другие пк впихнуть, вдруг прочтет...
 * Но все зря, пошло по... Я взял в руки себя и через день решил
 * по новой запилить)
 * Мораль сей басни такова: хранить на гитхабе свои проекты надо!)
*/
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DB db;
    Cursor cursor;

    View hView;
    NavigationView navigationView;

    static SharedPreferences sPref;

    protected static TextView tvHeaderName;
    protected static TextView tvHeaderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Notes notes = new  Notes();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment, notes);
        ft.commit();


        db = new DB(this);
        db.open();

        // получаем курсор
        cursor = db.getAllData();
        startManagingCursor(cursor);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //тут в навиге работаем с хэдером
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        hView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);


        /**ддвумя методами апдейтнем статус и имя в хедере
         * ,и количество записей
           */
        setNameStatus();
        setNoteCounts();


    }

    /** !!!!!!!!!!!       Все, что не онкрейт    !!!!!!!!!!!!! */

    //метод для апдейтить число записей
    void setNoteCounts(){
        //update Count of notes
        Menu menuNav = navigationView.getMenu();
        //get menuItem notes
        MenuItem notesItem = menuNav.findItem(R.id.notes);
        String before = notesItem.getTitle().toString();

        //Notes + countOfNotes
        cursor.requery();
        String counter = Integer.toString(cursor.getCount());
        //добавляем значение
        String s = "Все записи" + "   "+counter+" ";
        SpannableString sColored = new SpannableString( s );

        //взято из интернета, сетим цвет циферки
        sColored.setSpan(new BackgroundColorSpan( Color.BLUE ), s.length()-(counter.length()+2), s.length(), 0);
        sColored.setSpan(new ForegroundColorSpan( Color.WHITE ), s.length()-(counter.length()+2), s.length(), 0);
        notesItem.setTitle(sColored);

    }

    //метод для сеттить имя и статус
    void setNameStatus(){
        //сами преференсы достанем
        sPref = getPreferences(MODE_PRIVATE);
        String hName = sPref.getString(Settings.H_NAME, "Saly");
        String hStatus = sPref.getString(Settings.H_STATUS, "My notes");
        //из префа сетим имена в текствью в хедере
        tvHeaderName= (TextView) hView.findViewById(R.id.nameBar);
        tvHeaderName.setText(hName);
        //берем статус из натсроек преференсис
        tvHeaderStatus= (TextView) hView.findViewById(R.id.sayBar);
        tvHeaderStatus.setText(hStatus);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //фигня настроек
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Settings settings = new Settings();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment, settings);
            ft.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //чтобы не создавать активити и был постоянный навигдровер - будем
        //будем делать фрагментами и сетить их в одно активити
        if (id == R.id.new_note) {
            NewNote newnote = new  NewNote();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment, newnote);
            ft.commit();
        } else if (id == R.id.notes) {
            Notes notes = new  Notes();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment, notes);
            ft.commit();
        } else if (id == R.id.settings) {
            Settings settings = new Settings();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment, settings);
            ft.commit();
        } else if (id == R.id.exit) {
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
