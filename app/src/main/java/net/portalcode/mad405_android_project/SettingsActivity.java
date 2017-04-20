package net.portalcode.mad405_android_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity {

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_NIGHT_MODE = "night_mode";
    private static final String PREF_USER_COLOR = "color";
    public static SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // This will make the app switch themes if necessary
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useNightMode = preferences.getBoolean(PREF_NIGHT_MODE, false);
        if(useNightMode) {
            setTheme(R.style.AppTheme_Light_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new ChatPrefFragment()).commit();

        // This will tell the app to listen for a change to the color theme and run code when it has changed.
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        sharedPref.registerOnSharedPreferenceChangeListener(preferencesChangeListener);
    }

    // This listens for changes to values in Shared Preferences
    SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            if(key.equals("theme")){
                Boolean themeChoice = sharedPref.getBoolean(key, false);
                toggleTheme(themeChoice);
            } else if(key.equals("color")){
                int colorChoice = sharedPref.getInt(key, 0);
                Log.i("LOG", "black:" + Color.BLACK);
                Log.i("LOG", "selected" + colorChoice);
                changeUserColor(colorChoice);
            }
        }
    };

    // This code will switch the preferences value for the theme
    private void toggleTheme(boolean nightMode) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_NIGHT_MODE, nightMode);
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    private void changeUserColor(int color) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(PREF_USER_COLOR, color);
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
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
