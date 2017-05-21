package molbak.showcaseproject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private Button btnAddMovie;
    private EditText txtTitle;
    private RelativeLayout relativeLayout;

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

        //moviesDB = new TmdbApi(getResources().getString(R.string.tmdb_api)).getMovies();
        //MovieDb movieDb = moviesDB.getMovie()

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
        int id = item.getItemId();

        if (id == R.id.nav_dblist) {
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btn_info) {
            Snackbar snackbar = Snackbar
                    .make(relativeLayout, "Add a partial movie name to database", Snackbar.LENGTH_LONG);

            snackbar.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiateliseUI() {
        txtTitle = (EditText) findViewById(R.id.txt_title);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);

        btnAddMovie = (Button) findViewById(R.id.btn_add_movie);
        btnAddMovie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (txtTitle.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "No name stated", Toast.LENGTH_SHORT).show();
                } else {
                    if (isOnline()) {
                        new TMDBQueryManager().execute(txtTitle.getText().toString());
                    }
                }
            }
        });
    }

    private void initiateliseDatabase() {
        Log.i(TAG, "Initialising Database");
        new TMDBQueryManager().execute("Pulp Fiction");
        new TMDBQueryManager().execute("Return of the King");
        new TMDBQueryManager().execute("Forrest Gump");
        new TMDBQueryManager().execute("Shawshank");
        new TMDBQueryManager().execute("Empire Strikes Back");
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class TMDBQueryManager extends AsyncTask<String, Void, Movie> {

        private final String TMDB_API_KEY = getResources().getString(R.string.tmdb_api);
        private static final String DEBUG_TAG = "TMDBQueryManager";

        @Override
        protected Movie doInBackground(String... params) {
            try {
                Log.i(DEBUG_TAG, "Do in background");
                return searchIMDB(params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie movie) {
            Log.i(DEBUG_TAG, "PostExecute");
            if (movie != null) {
                Log.i(DEBUG_TAG, "Adding movie to DB");
                database.addMovie(movie);
                Toast.makeText(getApplicationContext(), "Movie added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Movie NOT added", Toast.LENGTH_SHORT).show();
            }

        }

        /**
         * Searches IMDBs API for the given query
         * @param query The query to search.
         * @return A list of all hits.
         */
        public Movie searchIMDB(String query) throws IOException {
            // Build URL
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://api.themoviedb.org/3/search/movie");
            stringBuilder.append("?api_key=" + TMDB_API_KEY);
            stringBuilder.append("&query=" + query.replaceAll(" ", "+"));
            URL url = new URL(stringBuilder.toString());

            InputStream stream = null;
            try {
                // Establish a connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.addRequestProperty("Accept", "application/json"); // Required to get TMDB to play nicely.
                conn.setDoInput(true);
                conn.connect();

                int responseCode = conn.getResponseCode();
                Log.d(DEBUG_TAG, "The response code is: " + responseCode + " " + conn.getResponseMessage());

                stream = conn.getInputStream();
                return parseResult(stringify(stream));
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }

        private Movie parseResult(String result) {
            String streamAsString = result;
            Movie movie = new Movie();
            try {
                JSONObject jsonObject = new JSONObject(streamAsString);
                JSONArray array = (JSONArray) jsonObject.get("results");

                JSONObject jsonMovieObject = array.getJSONObject(0);
                movie.setTitle(jsonMovieObject.getString("original_title"));
                movie.setDescription(jsonMovieObject.getString("overview"));

                String releaseYear = jsonMovieObject.getString("release_date");
                String[] releaseYearParts = releaseYear.split("-");
                movie.setYear(Integer.parseInt(releaseYearParts[0]));

            } catch (JSONException e) {
                System.err.println(e);
                Log.d(DEBUG_TAG, "Error parsing JSON. String was: " + streamAsString);
            }
            return movie;
        }

        public String stringify(InputStream stream) throws IOException {
            Reader reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        }
    }
}
