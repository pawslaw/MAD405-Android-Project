package net.portalcode.mad405_android_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import static java.security.AccessController.getContext;
import static net.portalcode.mad405_android_project.ChatFragment.adapter;
import static net.portalcode.mad405_android_project.ChatFragment.messageList;

/**
 * Created by web on 2017-04-13.
 */

public class APICall extends AsyncTask<String, String, String> {

    private static final String APIURL = "http://chat.portalcode.net/index.php";
    SharedPreferences prefs;

    public APICall() {}

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String JsonResponse = null;
        String JsonDATA = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(APIURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(JsonDATA);

            writer.close();
            InputStream inputStream = urlConnection.getInputStream();

            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {return null;}
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {buffer.append(inputLine + "\n");}
            if (buffer.length() == 0) {return null;}
            JsonResponse = buffer.toString();

            //Log.i("LOG", JsonResponse);
            return JsonResponse;

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("LOG", "Error closing stream", e);
                }
            }
        }
        return null;
    }

    private WeakReference<Context> contextRef;

    public APICall(Context context) {
        contextRef = new WeakReference<Context>(context);
    }

    @Override
    protected void onPostExecute(String result) {
        // Update UI
        try {
            JSONObject json = new JSONObject(result);

            if (json.optString("error").equals("login")) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.context, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                    }
                });
//            } else if ((json.optJSONArray("newmessages")).length() != 0) {
//                Log.i("LOG", "That was a joke... haha. fat chance.");
//                Log.i("LOG", json.getString("newmessages"));

            } else if (!json.optString("username").isEmpty()) {
                Log.i("LOG", "LOGGED IN");

                SharedPreferences.Editor editor = MainActivity.sharedPref.edit();
                editor.putString("username", json.getString("username"));
                editor.putString("password", json.getString("password"));
                editor.commit();

                FragmentTransaction fragmentTransaction = MainActivity.fm.beginTransaction();

                // Temporarily redirecting to the login screen on bootup
                //fragmentTransaction.add(R.id.content_main, new MainFragment());
                fragmentTransaction.replace(R.id.content_main, new ChatFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            } else if (json.optJSONArray("newmessages") != null && json.optJSONArray("newmessages").length() != 0) {

                //Log.i("LOG", "Test: " + json.optJSONArray("newmessages").toString());
                DatabaseHandler db = new DatabaseHandler(MainActivity.context);
                for (int i = 0; i < json.optJSONArray("newmessages").length(); i++) {
                    Message message = new Message();

                    message.setContent(json.optJSONArray("newmessages").getJSONObject(i).getString("message"));
                    message.setId(json.optJSONArray("newmessages").getJSONObject(i).getInt("id"));
                    message.setTimeSent(json.optJSONArray("newmessages").getJSONObject(i).getString("timestamp"));
                    message.setUser_id(json.optJSONArray("newmessages").getJSONObject(i).getInt("user"));

//                    User user = db.getUser(message.getUser_id());
//                    if (user == null) {
//                        JSONObject post_dict = new JSONObject();
//
//                        try {
//                            post_dict.put("action" , "getuser");
//                            post_dict.put("userid" , message.getUser_id());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        new APICall().execute(String.valueOf(post_dict));
//                    }

                    //Log.i("LOG", json.optJSONArray("newmessages").getString(i));
                    db.addMessage(message);

//                    Log.i("LOGSCREAM", message.toString());
                }
                //messageList = db.getAllMessages();

//                Log.i("TEST", "----- PRINTING MESSAGES -----");
//                for (int i = 0; i< messageList.size(); i++) {
//                    Log.i("TEST", messageList.get(i).getContent());
//                }

//                Log.i("TEST", "----- RESETTING MESSAGE LIST -----");

                messageList.clear();
                messageList.addAll(db.getAllMessages());
                db.closeDB();

//                Log.i("TEST", "----- PRINTING MESSAGES -----");
//                for (int i = 0; i< messageList.size(); i++) {
//                    Log.i("TEST", messageList.get(i).getContent());
//                }

                //adapter.notifyDataSetChanged();

                ChatFragment.adapter = new MessagesAdapter(MainActivity.context, messageList);
                ChatFragment.rvMessages.setAdapter(adapter);
                ChatFragment.rvMessages.scrollToPosition(adapter.getItemCount()-1);

            } else if (json.optJSONArray("users") != null && json.optJSONArray("users").length() != 0) {
                DatabaseHandler db = new DatabaseHandler(MainActivity.context);
                db.deleteAllUsers();
                for (int i = 0; i < json.optJSONArray("users").length(); i++) {
                    User user = new User();

                    user.setId(json.optJSONArray("users").getJSONObject(i).getInt("id"));
                    user.setName(json.optJSONArray("users").getJSONObject(i).getString("username"));
                    user.setAvatar(R.drawable.ic_adb_black_24dp);
                    //Log.i("LOG", json.optJSONArray("newmessages").getString(i));

                    db.addUser(user);
//                    Log.i("LOGSCREAM", message.toString());
                }
                db.closeDB();
            }
//            } else if (json.optJSONArray("user") != null && json.optJSONArray("user").length() != 0) {
//                User user = new User();
//                user.setId(json.optJSONArray("user").getJSONObject(0).getInt("id"));
//                user.setName(json.optJSONArray("user").getJSONObject(0).getString("username"));
//
//                Log.i("LOG", user.toString());
//
//                DatabaseHandler db = new DatabaseHandler(MainActivity.context);
//                db.addUser(user);
//                db.closeDB();
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
