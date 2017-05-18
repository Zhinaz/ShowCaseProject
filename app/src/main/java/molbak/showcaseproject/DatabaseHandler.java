package molbak.showcaseproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "playerManager";
    private static final String TABLE_PLAYERS = "players";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PLACEMENT = "placement";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase dataBase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PLAYERS + "(" +
                KEY_ID + " TEXT PRIMARY KEY," +
                KEY_NAME + " TEXT," +
                KEY_PLACEMENT + " TEXT" +
                ")";
        dataBase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase dataBase, int oldVersion, int newVersion) {
        // Drop older table if existed
        dataBase.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);

        // Recreate table again
        onCreate(dataBase);
    }

    public void addPlayer(Player player) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, player.getId());
        contentValues.put(KEY_NAME, player.getName());
        contentValues.put(KEY_PLACEMENT, player.getPlacement());

        sqLiteDatabase.insert(TABLE_PLAYERS, null, contentValues);
        sqLiteDatabase.close();
    }

    public Player getPlayer(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(
                TABLE_PLAYERS,
                new String[] {KEY_ID, KEY_NAME, KEY_PLACEMENT},
                KEY_ID + "=?", new String[] {String.valueOf(id)},
                null, null, null, null
        );
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Player player = new Player(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        return player;
    }

    public List<Player> getAllPlayers() {
        List<Player> playerList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PLAYERS;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do {
                Player player = new Player(0, null, null);
                player.setId(Integer.parseInt(cursor.getString(0)));
                player.setName(cursor.getString(1));
                player.setPlacement(cursor.getString(2));

                playerList.add(player);
            } while (cursor.moveToNext());
        }
        return playerList;
    }

    public int getPlayersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PLAYERS;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updatePlayer(Player player) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, player.getName());
        values.put(KEY_PLACEMENT, player.getPlacement());

        return sqLiteDatabase.update(TABLE_PLAYERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(player.getId()) });
    }

    public void deletePlayer(Player player) {}
}
