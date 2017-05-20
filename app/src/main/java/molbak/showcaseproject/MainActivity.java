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
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private Button btnAddMovie;
    private EditText txtTitle;
    private EditText txtGenre;
    private EditText txtYear;

    public DatabaseHandler database = null;

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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dblist) {
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
        } /* else if (id == R.id.nav_gallery) {

        } */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initiateliseUI() {
        txtTitle = (EditText) findViewById(R.id.txt_title);
        txtGenre = (EditText) findViewById(R.id.txt_genre);
        txtYear = (EditText) findViewById(R.id.txt_year);

        btnAddMovie = (Button) findViewById(R.id.btn_add_movie);
        btnAddMovie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Movie movie = new Movie();
                if (txtTitle.getText().toString().equals("") ||
                        txtGenre.getText().toString().equals("") ||
                        txtYear.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    movie.setId(UUID.randomUUID().toString());
                    movie.setTitle(txtTitle.getText().toString());
                    movie.setGenre(txtGenre.getText().toString());
                    movie.setYear(txtYear.getText().toString());
                    database.addMovie(movie);

                    txtTitle.setText("");
                    txtGenre.setText("");
                    txtYear.setText("");
                    Toast.makeText(getApplicationContext(), "Movie added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initiateliseDatabase() {
        Log.i(TAG, "Initialising Database");
        database.addMovie(new Movie(UUID.randomUUID().toString(), "Guardians of the Galaxy 2", "Science Fiction", "2017"));
        database.addMovie(new Movie(UUID.randomUUID().toString(), "Pulp Fiction", "Crime Drama", "1994"));
        database.addMovie(new Movie(UUID.randomUUID().toString(), "The Empire Strikes Back", "Fantasy", "1980"));
        database.addMovie(new Movie(UUID.randomUUID().toString(), "Forrest Gump", "Comedy Romance", "1994"));
        database.addMovie(new Movie(UUID.randomUUID().toString(), "Inception", "Adventure", "2010"));
        database.addMovie(new Movie(UUID.randomUUID().toString(), "The Dark Knight", "Action", "2008"));
        database.addMovie(new Movie(UUID.randomUUID().toString(), "The Shawshank Redemption", "Crime Drama", "1994"));
        database.addMovie(new Movie(UUID.randomUUID().toString(), "The Return of the King", "Adventure Fantasy", "2003"));
    }
}
