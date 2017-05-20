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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
                    .make(recyclerView, "Long click to manipute movies", Snackbar.LENGTH_LONG);

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
                        AlertDialog manipulateDialog = manipulateMovieDialog(movie, position);
                        manipulateDialog.show();
                    }
                });
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    private AlertDialog manipulateMovieDialog(final Movie movie, final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Manipulate movie")
                .setMessage("Make changes to " + movie.getTitle())
                .setIcon(R.drawable.ic_menu_manage)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlertDialog deleteDialog = confirmDeleteDialog(movie, position);
                        deleteDialog.show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog updateDialog = updateDialog(movie, position);
                        updateDialog.show();
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return alertDialog;
    }

    private AlertDialog updateDialog(final Movie movie, final int position) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(12, 10, 12, 10);

        final EditText txtTitle = new EditText(this);
        txtTitle.setText(movie.getTitle());
        layout.addView(txtTitle, layoutParams);

        final EditText txtDescription = new EditText(this);
        txtDescription.setText(movie.getDescription());
        layout.addView(txtDescription, layoutParams);

        final EditText txtYear = new EditText(this);
        txtYear.setText(Integer.toString(movie.getYear()));
        layout.addView(txtYear, layoutParams);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update movie");
        builder.setView(layout);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Movie mov = movie;
                mov.setTitle(txtTitle.getText().toString());
                mov.setDescription(txtDescription.getText().toString());
                mov.setYear(Integer.parseInt(txtYear.getText().toString()));
                database.updateMovie(mov);
                mAdapter.notifyItemChanged(position);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    private AlertDialog confirmDeleteDialog(final Movie movie, final int position) {
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
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
