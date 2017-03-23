package net.portalcode.mad405_android_project;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Created by web on 2017-03-23.
 */

public class ServerAPI extends AsyncTask<String, String, String> {

    private HttpURLConnection urlConnection;

    @Override
    protected String doInBackground(String... params) {

        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL("https://androidtutorialpoint.com/api/MobileJSONArray.json");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        Log.i("SCREAM", result.toString());

        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        // Do something with the JSON string
    }
}
