package molbak.showcaseproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button button;
    DatabaseHandler database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DatabaseHandler(this);

        initiateliseUI();

        //database.addPlayer(new Player(database.getPlayersCount() + 1, "Tobias", "Test"));
        //database.addPlayer(new Player(database.getPlayersCount() + 1, "Niels", "Test"));
        //database.addPlayer(new Player(database.getPlayersCount() + 1, "Julie", "Test"));
        //database.addPlayer(new Player(database.getPlayersCount() + 1, "Torsten", "Test"));


    }

    private void initiateliseUI() {
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Reading all contacts
                Log.d("Reading: ", "Reading all contacts..");
                List<Player> playerList = database.getAllPlayers();

                for (Player player : playerList) {
                    String log = "Id: " + player.getId() + " ,Name: " + player.getName() + " ,Phone: " + player.getPlacement();
                    Log.d("Name: ", log);
                }
            }
        });
    }
}
