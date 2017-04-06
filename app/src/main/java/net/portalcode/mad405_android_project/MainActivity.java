package net.portalcode.mad405_android_project;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainFragment.OnFragmentInteractionListener,
        ChatFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener{

    FragmentManager fm;
    public static FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        } else if (id == R.id.nav_subitem2) {
            // Add all entries to the database.
            DatabaseHandler db = new DatabaseHandler(getBaseContext());

            // Add temporary Users to the User's table for testing purposes
            db.addUser(new User("Test1", R.drawable.ic_adb_black_24dp));
            db.addUser(new User("Test2", R.drawable.ic_android_black_24dp));
            db.addUser(new User("Test3", R.drawable.ic_forum_black_24dp));
            db.addUser(new User("Test4", R.drawable.ic_album_black_24dp));
            db.addUser(new User("Test5", R.drawable.ic_attach_file_black_24dp));
            db.addUser(new User("Test6", R.drawable.ic_colorize_black_24dp));


            // Add all the messages required for the chat client to the Messages table
            db.addMessage(new Message("Mar 10, 2017 1:03pm", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", 1));
            db.addMessage(new Message("Mar 10, 2017 1:03pm", "Sed malesuada, enim sit amet facilisis mattis, est lorem vestibulum lectus, at laoreet odio odio at ligula. Vivamus id facilisis leo, ac vehicula orci.", 2));
            db.addMessage(new Message("Mar 10, 2017 1:03pm", "Aenean nulla tellus, euismod a vestibulum eget, consequat et lectus. ", 3));
            db.addMessage(new Message("Mar 10, 2017 1:03pm", "Aliquam at tempus enim. Praesent mattis sed nisi in dignissim. Maecenas sit amet malesuada sapien. Praesent eget erat ut nisi eleifend feugiat.", 4));
            db.addMessage(new Message("Mar 10, 2017 1:03pm", "Cras auctor, erat rutrum auctor tincidunt, elit libero sollicitudin diam, eu pellentesque nulla massa at ante. Maecenas dolor neque, tempor ut cursus et, malesuada eu neque. Vestibulum ac hendrerit nunc. Integer eleifend ex in mi ultrices elementum. Ut facilisis id libero quis dignissim. Vestibulum venenatis euismod rhoncus.", 5));
            db.addMessage(new Message("Mar 10, 2017 1:03pm", "Aenean eleifend, magna nec interdum luctus, ligula sem pulvinar nisi, ut consectetur turpis mi in tortor. Phasellus sagittis ullamcorper odio, ac iaculis quam gravida eget.", 6));
            db.addMessage(new Message("Mar 10, 2017 1:03pm", "Sed eros orci, sollicitudin sit amet ex at, mollis ullamcorper mauris. Nunc nec consectetur dolor.", 1));
            db.addMessage(new Message("Mar 10, 2017 1:03pm", "Suspendisse sapien mauris, pulvinar sed augue elementum, tincidunt lacinia mi.", 2));
            db.addMessage(new Message("Mar 10, 2017 1:03pm", "In vel porta augue. Praesent lacinia ex ac cursus posuere. Cras ornare volutpat velit, id aliquam augue dictum vitae.", 3));
            db.addMessage(new Message("Mar 10, 2017 1:03pm", "Integer varius est id lectus fermentum, et tempor arcu pretium. Cras posuere maximus ipsum.", 4));
            db.addMessage(new Message("Mar 10, 2017 1:03pm", "Proin posuere arcu a iaculis dictum. Sed suscipit aliquam lorem eget mollis.", 5));
            db.addMessage(new Message("Mar 10, 2017 1:03pm", "Donec sollicitudin elit a sem cursus lacinia. Nunc quis ligula eget sapien sagittis porttitor id vitae felis. Sed commodo arcu sit amet magna laoreet, quis eleifend nibh ultricies. ", 6));

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
