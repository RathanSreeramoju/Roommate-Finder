package example.abhiandroid.searchviewexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    String[] rooms;
    ArrayList<roomNames> arraylist = new ArrayList<roomNames>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        roomsNameList = new String[]{"atwater","cote-des-neiges"};


        list = (ListView) findViewById(R.id.listview);

        for (int i = 0; i < roomsNameList.length; i++) {
            roomsNames animalNames = new roomsNames(roomsNameList[i]);

            arraylist.add(roomsNames);
        }


        adapter = new ListViewAdapter(this, arraylist);


        list.setAdapter(adapter);


        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }
}