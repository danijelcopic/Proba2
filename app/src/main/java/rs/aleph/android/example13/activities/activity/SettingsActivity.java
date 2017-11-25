package rs.aleph.android.example13.activities.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;


import rs.aleph.android.example13.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ucitavanje i postavljanje preferences.xml-a
        addPreferencesFromResource(R.xml.preferences);
    }

}
