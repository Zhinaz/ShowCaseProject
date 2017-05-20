package molbak.showcaseproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        holder.txtTitle.setText(movie.getTitle());
        holder.txtDescription.setText(movie.getDescription());
        holder.txtYear.setText(Integer.toString(movie.getYear()));

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
        public TextView txtTitle, txtDescription, txtYear;
        public View viewContainer;

        public mViewHolder(View view) {
            super(view);
            txtTitle = (TextView) view.findViewById(R.id.tv_title);
            txtDescription = (TextView) view.findViewById(R.id.tv_description);
            txtYear = (TextView) view.findViewById(R.id.tv_year);

            viewContainer = view;
        }
    }
}
