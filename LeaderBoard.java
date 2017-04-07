package io.techies.com.puzzle_8;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Queue;

public class LeaderBoard extends ListActivity {

    private ArrayList<Player> listOfLeaders;
    private ListView listView;
    private ArrayList<String> leaderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        listOfLeaders = PuzzleBoardView.listOfPlayers;
        leaderList = new ArrayList<>();

        listView = (ListView) findViewById(android.R.id.list);

//        TextView textView = (TextView) findViewById(R.id.label);


        for(Player p : listOfLeaders) {
            leaderList.add(p.getUserName() + "\t\t\t\t\t" + p.getMoves());
        }

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, leaderList);
        listView.setAdapter(adapter);
    }
}
