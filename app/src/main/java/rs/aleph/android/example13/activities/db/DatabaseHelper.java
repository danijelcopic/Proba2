package rs.aleph.android.example13.activities.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import rs.aleph.android.example13.activities.db.model.Notes;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {


    private static final String DATABASE_NAME = "ormlite2.db";


    private static final int DATABASE_VERSION = 2;

    private Dao<Notes, Integer> mNotesDao = null;


    //Potrebno je dodati konstruktor zbog pravilne inicijalizacije biblioteke
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Prilikom kreiranja baze potrebno je da pozovemo odgovarajuce metode biblioteke
    //prilikom kreiranja moramo pozvati TableUtils.createTable za svaku tabelu koju imamo
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Notes.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //kada zelimo da izmenomo tabele, moramo pozvati TableUtils.dropTable za sve tabele koje imamo
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Notes.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //jedan Dao objekat sa kojim komuniciramo. Ukoliko imamo vise tabela
    //potrebno je napraviti Dao objekat za svaku tabelu
    public Dao<Notes, Integer> getmNotesDao() throws SQLException {
        if (mNotesDao == null) {
            mNotesDao = getDao(Notes.class);
        }

        return mNotesDao;
    }


    //obavezno prilikom zatvarnaj rada sa bazom osloboditi resurse
    @Override
    public void close() {
        mNotesDao = null;

        super.close();
    }
}
