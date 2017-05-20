package molbak.showcaseproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "movieCollection";
    private static final String TABLE_MOVIES = "movies";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_GENRE = "genre";
    private static final String KEY_YEAR = "year";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase dataBase) {
        String CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_MOVIES + "(" +
                KEY_ID + " TEXT PRIMARY KEY," +
                KEY_TITLE + " TEXT," +
                KEY_GENRE + " TEXT," +
                KEY_YEAR + " TEXT" +
                ")";
        dataBase.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase dataBase, int oldVersion, int newVersion) {
        // Drop older table if existed
        dataBase.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);

        // Recreate table again
        onCreate(dataBase);
    }

    public void addMovie(Movie movie) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, movie.getId());
        contentValues.put(KEY_TITLE, movie.getTitle());
        contentValues.put(KEY_GENRE, movie.getGenre());
        contentValues.put(KEY_YEAR, movie.getYear());

        sqLiteDatabase.insert(TABLE_MOVIES, null, contentValues);
        sqLiteDatabase.close();
    }

    public Movie getMovie(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(
                TABLE_MOVIES,
                new String[]{KEY_ID, KEY_TITLE, KEY_GENRE, KEY_YEAR},
                KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null
        );
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Movie movie = new Movie(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        cursor.close();
        return movie;
    }

    public List<Movie> getAllMovies() {
        List<Movie> movieList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MOVIES;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(cursor.getString(0));
                movie.setTitle(cursor.getString(1));
                movie.setGenre(cursor.getString(2));
                movie.setYear(cursor.getString(3));

                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return movieList;
    }

    public int getMoviesCount() {
        String countQuery = "SELECT * FROM " + TABLE_MOVIES;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int playerCount = cursor.getCount();
        cursor.close();

        return playerCount;
    }

    public int updateMovie(Movie movie) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, movie.getTitle());
        values.put(KEY_GENRE, movie.getGenre());
        values.put(KEY_YEAR, movie.getYear());

        return sqLiteDatabase.update(TABLE_MOVIES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(movie.getId())});
    }

    public void deleteMovie(Movie movie) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_MOVIES, KEY_ID + "=?",
                new String[]{String.valueOf(movie.getId())});

        sqLiteDatabase.close();
    }
}
