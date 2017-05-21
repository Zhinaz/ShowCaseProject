package molbak.showcaseproject;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity {

    private String query = null;
    private TextView txtScore, txtVotes, txtDescription;
    private ImageView imgBackground;
    private static final String POSTER_WIDTH = "w500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        initialiseUI();

        Intent intent = getIntent();
        query = intent.getStringExtra(ListActivity.SEND_TITLE);
        new TMDBQueryManager().execute(query);
    }

    private void initialiseUI() {
        txtScore = (TextView) findViewById(R.id.txt_score);
        txtVotes = (TextView) findViewById(R.id.txt_score_votes);
        txtDescription = (TextView) findViewById(R.id.txt_description);
        imgBackground = (ImageView) findViewById(R.id.img_background);
    }

    private class TMDBQueryManager extends AsyncTask<String, Void, List<String>> {

        private final String TMDB_API_KEY = getResources().getString(R.string.tmdb_api);
        private static final String DEBUG_TAG = "TMDBQueryManager";

        @Override
        protected List<String> doInBackground(String... params) {
            try {
                Log.i(DEBUG_TAG, "Do in background");
                return searchIMDB(params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> movie) {
            Log.i(DEBUG_TAG, "PostExecute");
            if (movie != null) {
                String posterQuery = "https://image.tmdb.org/t/p/" +
                        POSTER_WIDTH +
                        movie.get(1);
                Picasso.with(getApplicationContext()).load(posterQuery).into(imgBackground);

                setTitle(movie.get(2));
                txtScore.setText(movie.get(3) + "/10");
                txtVotes.setText(" Votes: " + movie.get(4));
                txtDescription.setText(movie.get(5));
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

        public List<String> searchIMDB(String query) throws IOException {
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

        private List<String> parseResult(String result) {
            String streamAsString = result;
            List<String> movie = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(streamAsString);
                JSONArray array = (JSONArray) jsonObject.get("results");
                JSONObject jsonMovieObject = array.getJSONObject(0);

                movie.add(jsonMovieObject.getString("id"));
                movie.add(jsonMovieObject.getString("poster_path"));
                movie.add(jsonMovieObject.getString("original_title"));
                movie.add(jsonMovieObject.getString("vote_average"));
                movie.add(jsonMovieObject.getString("vote_count"));
                movie.add(jsonMovieObject.getString("overview"));

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
