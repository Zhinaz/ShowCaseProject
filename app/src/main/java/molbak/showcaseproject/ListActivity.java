package molbak.showcaseproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";

    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView = null;
    private MoviesAdapter mAdapter = null;
    private RecyclerView.LayoutManager layoutManager = null;
    private DatabaseHandler database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        database = new DatabaseHandler(this);

        setupRecyclerView();
        getMoviesDB();
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MoviesAdapter(movieList);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void getMoviesDB() {
        movieList = database.getAllMovies();
        Log.i(TAG, "Database size: " + database.getMoviesCount());
        for (Movie movie : movieList) {
            Log.i(TAG, "Movie: " + movie.getTitle());
        }
        mAdapter.notifyDataSetChanged();
    }
}
