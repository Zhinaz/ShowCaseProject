package molbak.showcaseproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.mViewHolder> {

    private List<Movie> movieList = null;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        public void onItemLongClick(View view, int position);
    }

    public MoviesAdapter(List<Movie> movieList, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        this.movieList = movieList;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item_list, parent, false);
        return new mViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, final int position) {
        final Movie movie = movieList.get(position);
        holder.tv_title.setText(movie.getTitle());
        holder.tv_genre.setText(movie.getGenre());
        holder.tv_year.setText(movie.getYear());

        holder.viewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
            }
        });
        holder.viewContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClickListener.onItemLongClick(v, position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_genre, tv_year;
        public View viewContainer;

        public mViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_genre = (TextView) view.findViewById(R.id.tv_genre);
            tv_year = (TextView) view.findViewById(R.id.tv_year);

            viewContainer = view;
        }
    }
}
