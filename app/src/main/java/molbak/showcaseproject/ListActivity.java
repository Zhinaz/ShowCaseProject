package molbak.showcaseproject;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
        movieList = database.getAllMovies();
        setupRecyclerView();
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
                    .make(recyclerView, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);

            snackbar.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MoviesAdapter(movieList, new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Movie movie = movieList.get(position);
                Toast.makeText(getApplicationContext(), "Movie: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
            }
        },
                new MoviesAdapter.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(View view, int position) {
                        Movie movie = movieList.get(position);
                        AlertDialog deleteDialog = confirmDeletionDialog(movie, position);
                        deleteDialog.show();
                    }
                });
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    private AlertDialog confirmDeletionDialog(final Movie movie, final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Deleting movie")
                .setMessage("Do you want to delete " + movie.getTitle() + "?")
                .setIcon(R.drawable.ic_menu_delete)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteMovie(movie, position);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return alertDialog;
    }

    private void deleteMovie(Movie movie, int position) {
        database.deleteMovie(movie);
        movieList.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, movieList.size());

    }
}
