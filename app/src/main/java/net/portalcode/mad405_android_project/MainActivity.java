package net.portalcode.mad405_android_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import static net.portalcode.mad405_android_project.ChatFragment.adapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainFragment.OnFragmentInteractionListener,
        ChatFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener{

    public static FragmentManager fm;
    public static FloatingActionButton fab;
    public static Context context;

    public static SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = getPreferences(Context.MODE_PRIVATE);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fab.hide();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // As soon as the app opens, change the current view to the Main Fragment
        fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        // Temporarily redirecting to the login screen on bootup
        //fragmentTransaction.add(R.id.content_main, new MainFragment());
        fragmentTransaction.add(R.id.content_main, new LoginFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction trans = fm.beginTransaction();

        if (id == R.id.nav_chat) {
            trans.replace(R.id.content_main, new ChatFragment());
            trans.commit();
        } else if (id == R.id.nav_item2) {

        } else if (id == R.id.nav_item3) {

        } else if (id == R.id.nav_item4) {

        } else if (id == R.id.nav_subitem1) {
            JSONObject post_dict = new JSONObject();

            String latestMessage = "0000-00-00 00:00:00.000";

            DatabaseHandler db = new DatabaseHandler(getBaseContext());
            Message message = db.getLatestMessage();
            if (message == null) {
                message = new Message();
                message.setTimeSent(latestMessage);
            }
            db.closeDB();



            try {
                post_dict.put("action" , "getnewmessages");
                post_dict.put("timestamp" , message.getTimeSent());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.i("LOG", String.valueOf(post_dict));

            if (post_dict.length() > 0) {

                new APICall().execute(String.valueOf(post_dict));

                //Log.i("LOG", );
            }

            adapter.notifyDataSetChanged();
        } else if (id == R.id.nav_subitem2) {
            // Add all entries to the database.
            DatabaseHandler db = new DatabaseHandler(getBaseContext());

            // Add temporary Permissions to the Permissions table for testing purposes
            // Permission 1(first entry) is the standard user, 2 is a moderator. 3 is just a tester permission
            // -RW
            db.addPermissions(new Permissions(0, 1, 1));
            // ERW
            db.addPermissions(new Permissions(1, 1, 1));
            // -R-
            db.addPermissions(new Permissions(0, 1, 0));



            // Add temporary Users to the Users table for testing purposes
            db.addUser(new User("Android User", R.drawable.ic_adb_black_24dp, 1));
            db.addUser(new User("Mr. Robot", R.drawable.ic_android_black_24dp, 1));
            db.addUser(new User("Chatty Cathy", R.drawable.ic_forum_black_24dp, 1));
            db.addUser(new User("DJ Disco", R.drawable.ic_album_black_24dp, 1));
            db.addUser(new User("Mr. Helpful", R.drawable.ic_attach_file_black_24dp, 1));
            db.addUser(new User("Sword Drop", R.drawable.ic_colorize_black_24dp, 1));

            db.closeDB();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
