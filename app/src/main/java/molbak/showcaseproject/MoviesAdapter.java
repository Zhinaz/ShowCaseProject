package molbak.showcaseproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.mViewHolder> {

    private List<Movie> movieList = null;

    public class mViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_year, tv_genre;

        public mViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_year = (TextView) view.findViewById(R.id.tv_year);
            tv_genre = (TextView) view.findViewById(R.id.tv_genre);
        }
    }

    public MoviesAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item_list, parent, false);
        return new mViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.tv_title.setText(movie.getTitle());
        holder.tv_genre.setText(movie.getGenre());
        holder.tv_year.setText(movie.getYear());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
