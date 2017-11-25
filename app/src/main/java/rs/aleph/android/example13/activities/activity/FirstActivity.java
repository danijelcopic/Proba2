package rs.aleph.android.example13.activities.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rs.aleph.android.example13.R;
import rs.aleph.android.example13.activities.db.DatabaseHelper;
import rs.aleph.android.example13.activities.db.model.Notes;
import rs.aleph.android.example13.activities.dialogs.AboutDialog;




public class FirstActivity extends AppCompatActivity {



    private DatabaseHelper databaseHelper;
    private AlertDialog dialogAlert;
    private SharedPreferences preferences;


    public static String NOTIF_TOAST = "pref_toast";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);



        // TOOLBAR
        // aktiviranje toolbara
        Toolbar toolbar = (Toolbar) findViewById(R.id.first_toolbar);
        setSupportActionBar(toolbar);


//        // toolbar i ikonica za Navigatio drawer
//        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//           // actionBar.setHomeAsUpIndicator(R.drawable.ic_action_drawer); // ikona Navigation Drawer
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.show();
//        }




        // status podesavanja
        preferences = PreferenceManager.getDefaultSharedPreferences(this);


        //  ZA BAZU
        // ucitamo sve podatke iz baze u listu
        List<Notes> notes = new ArrayList<Notes>();
        try {
            notes = getDatabaseHelper().getmNotesDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // u String izvucemo iz gornje liste  i sa adapterom posaljemo na View
        List<String> notesName = new ArrayList<String>();
        for (Notes i : notes) {
            notesName.add(i.getmName());
        }

        final ListView listView = (ListView) findViewById(R.id.listFirstActivity); // definisemo u koji View saljemo podatke (listFirstActivity)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(FirstActivity.this, R.layout.list_item, notesName);  // definisemo kako ce izgledati jedna stavka u View (list_item)
        listView.setAdapter(adapter);


        // sta se desi kada kliknemo na stavku iz liste
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notes notes = (Notes) listView.getItemAtPosition(position);
                Intent intentNotes = new Intent(FirstActivity.this, SecondActivity.class);
                intentNotes.putExtra("position", notes.getmId());  // saljemo intent o poziciji
                startActivity(intentNotes);

            }

        });

    }


    /**
     * MENU
     */

    // prikaz menija
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.first_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // sta se desi kada kliknemo na stavke iz menija
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.action_add: // otvara se dialog za upis u bazu


                final Dialog dialog = new Dialog(FirstActivity.this); // aktiviramo dijalog
                dialog.setContentView(R.layout.dialog_notes);


                final EditText notesName = (EditText) dialog.findViewById(R.id.input_notes_name);
                final EditText notesDescription = (EditText) dialog.findViewById(R.id.input_notes_description);
                final EditText notesDate = (EditText) dialog.findViewById(R.id.input_notes_date);


                Button ok = (Button) dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = notesName.getText().toString();
                        if (name.isEmpty()) {
                            Toast.makeText(FirstActivity.this, "Must be entered", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String description = notesDescription.getText().toString();
                        if (description.isEmpty()) {
                            Toast.makeText(FirstActivity.this, "Must be entered", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");
                        Date date = null;
                        try {
                            date = sdf.parse(notesDate.getText().toString());
                        } catch (ParseException e) {
                            Toast.makeText(FirstActivity.this, "Must be entered in format: dd.mm.yyyy.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Notes notes = new Notes();
                        notes.setmName(name);
                        notes.setmDescription(description);
                        notes.setmDate(date);


                        try {
                            getDatabaseHelper().getmNotesDao().create(notes);

                            //provera podesavanja
                            boolean toast = preferences.getBoolean(NOTIF_TOAST, false);

                            if (toast) {
                                Toast.makeText(FirstActivity.this, "New notes is entered", Toast.LENGTH_SHORT).show();
                            }

                            refresh(); // osvezavanje baze

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();

                    }
                });

                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;

            case R.id.action_settings:
                Intent settings = new Intent(FirstActivity.this, SettingsActivity.class);  // saljemo intent SettingsActivity.class
                startActivity(settings);
                break;

            case R.id.action_about:
                if (dialogAlert == null) {
                    dialogAlert = new AboutDialog(FirstActivity.this).prepareDialog(); // pozivamo prepareDialog() iz klase AboutDialog
                } else {
                    if (dialogAlert.isShowing()) {
                        dialogAlert.dismiss();
                    }

                }
                dialogAlert.show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * TABELE I BAZA
     */

    //Metoda koja komunicira sa bazom podataka
    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }


    // refresh() prikazuje novi sadrzaj.Povucemo nov sadrzaj iz baze i popunimo listu
    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.listFirstActivity);
        if (listview != null) {
            ArrayAdapter<Notes> adapter = (ArrayAdapter<Notes>) listview.getAdapter();
            if (adapter != null) {
                adapter.clear();
                try {
                    List<Notes> list = getDatabaseHelper().getmNotesDao().queryForAll();
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();

                }
            }
        }
    }





    // kompatibilnost u nazad
    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }


    // ovde refreshujemo bazu kada smo se vratili iz druge aktivnosti (kada je glumac obrisan, pa da se vise ne pokazuje)
    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // nakon rada sa bazo podataka potrebno je obavezno
        //osloboditi resurse!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }


}