package rs.aleph.android.example13.activities.activity;


import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rs.aleph.android.example13.R;
import rs.aleph.android.example13.activities.db.DatabaseHelper;
import rs.aleph.android.example13.activities.db.model.Notes;


import static rs.aleph.android.example13.R.id.input_notes_name;

import static rs.aleph.android.example13.activities.activity.FirstActivity.NOTIF_TOAST;


public class SecondActivity extends AppCompatActivity  {

    private int position = 0;

    private DatabaseHelper databaseHelper;
    private Notes notes;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        // TOOLBAR
        // aktiviranje toolbara 2 koji je drugaciji od onog iz prve aktivnosti
        Toolbar toolbar = (Toolbar) findViewById(R.id.second_toolbar);
        setSupportActionBar(toolbar);


        // prikazivanje strelice u nazad u toolbaru ... mora se u manifestu definisati zavisnost parentActivityName
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }


        // status podesavanja
        preferences = PreferenceManager.getDefaultSharedPreferences(this);


        // hvatamo intent iz prve aktivnosti
        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");

        // na osnovu dobijene pozicije od intenta, pupunjavamo polja u drugoj aktivnosti
        try {

            notes = getDatabaseHelper().getmNotesDao().queryForId((int) position);
            String name = notes.getmName();
            String description = notes.getmDescription();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");
            String date = sdf.format(notes.getmDate());

            // name
            TextView notesName = (TextView) findViewById(input_notes_name);
            notesName.setText(name);

            //description
            TextView notesDescription = (TextView) findViewById(R.id.input_notes_description);
            notesDescription.setText(description);

            //date
            TextView notesDate = (TextView) findViewById(R.id.input_notes_date);
            notesDate.setText(date);



//
//            // prikazujemo listu  u drugoj aktivnosti
//            final ListView listView = (ListView) findViewById(R.id.inputListaFilmovaGlumac);
//
//            List<Notes> notes = getDatabaseHelper().getmNotesDao(). // konstruisemo QueryBuilder
//                    queryBuilder().
//                    where().
//                    eq(Notes.FIELD_NAME_NAME, position).
//                    query();
//
//            List<String> filmoviNazivi = new ArrayList<>();
//            for (Film f : filmovi) {
//                filmoviNazivi.add(f.getFilmNaziv());
//            }
//            ListAdapter adapter = new ArrayAdapter<String>(SecondActivity.this, R.layout.list_item_notes, filmoviNazivi);
//            listView.setAdapter(adapter);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // //provera podesavanja (toast ) ...
    private void showMessage(String message) {

        boolean toast = preferences.getBoolean(NOTIF_TOAST, false);


        if (toast) {  // ako je aktivan toast prikazi ovo
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }




    // MENU
    // prikaz menija
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // sta se desi kada kliknemo na stavke iz menija
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            // kada pritisnemo ikonicu za brisanje
            case R.id.action_delete:
                try {
                    getDatabaseHelper().getmNotesDao().delete(notes);

                    showMessage("The notes has been deleted");

                    finish();


                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }







    //Metoda koja komunicira sa bazom podataka
    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }


}
