package molbak.showcaseproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private Button btn_show_db;
    DatabaseHandler database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        database = new DatabaseHandler(this);

        //database.dropTable();

        initiateliseUI();

        if (database.getMoviesCount() == 0) {
            initiateliseDatabase();
        }
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

    //@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dblist) {
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initiateliseUI() {
        btn_show_db = (Button) findViewById(R.id.btn_show_db);
        btn_show_db.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Reading all contacts
                Log.d("Reading: ", "Reading all movies..");
                List<Movie> playerList = database.getAllMovies();

                for (Movie player : playerList) {
                    String log = "Id: " + player.getId() +
                            " ,Title: " + player.getTitle() +
                            " ,Genre: " + player.getGenre() +
                            " ,Year: " + player.getYear();
                    Log.d("Movie: ", log);
                }
            }
        });
    }

    private void initiateliseDatabase() {
        Log.i(TAG, "Initialising Database");
        database.addMovie(new Movie(database.getMoviesCount() + 1, "Guardians of the Galaxy 2", "Science Fiction", "2017"));
        database.addMovie(new Movie(database.getMoviesCount() + 1, "Pulp Fiction", "Crime Drama", "1994"));
        database.addMovie(new Movie(database.getMoviesCount() + 1, "The Empire Strikes Back", "Fantasy", "1980"));
        database.addMovie(new Movie(database.getMoviesCount() + 1, "Forrest Gump", "Comedy Romance", "1994"));
        database.addMovie(new Movie(database.getMoviesCount() + 1, "Inception", "Adventure", "2010"));
        database.addMovie(new Movie(database.getMoviesCount() + 1, "The Dark Knight", "Action", "2008"));
        database.addMovie(new Movie(database.getMoviesCount() + 1, "The Shawshank Redemption", "Crime Drama", "1994"));
        database.addMovie(new Movie(database.getMoviesCount() + 1, "The Return of the King", "Adventure Fantasy", "2003"));
    }
}
