package net.portalcode.mad405_android_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends PreferenceActivity {

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_NIGHT_MODE = "night_mode";
    SharedPreferences SP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useNightMode = preferences.getBoolean(PREF_NIGHT_MODE, false);

//        if(useNightMode) {
//            setTheme(R.style.AppTheme_Dark_NoActionBar);
//        }

        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new ChatPrefFragment()).commit();
        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SP.registerOnSharedPreferenceChangeListener(preferencesChangeListener);
    }

    // This listens for changes to values in Shared Preferences
    SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            if(key.equals("theme")){
                Boolean themeChoice = SP.getBoolean(key, false);
                toggleTheme(themeChoice);
            }
        }
    };

    private void toggleTheme(boolean nightMode) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_NIGHT_MODE, nightMode);
        editor.apply();

        Intent intent = new Intent(this, SettingsActivity.class);
        finish();
        startActivity(intent);
    }

    public static class ChatPrefFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

    }
}
