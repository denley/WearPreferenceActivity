package me.denley.wearpreferenceactivity.sample;

import android.os.Bundle;

import preference.WearPreferenceActivity;

public class MainActivity extends WearPreferenceActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preferences);
    }

}
