package edu.uci.ics.fabflixmobile;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlueActivity extends ActionBarActivity {
    public int listSize;
    public int pageLen = 10;
    private int n;
    private int numPages;
    private int inc = 0;
    private Button prev;
    private Button next;
    private ArrayList<String> movieList;
    private ArrayAdapter adapter;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);

        Bundle bundle = getIntent().getExtras();
        //Toast.makeText(this, "Last activity was " + bundle.get("last_activity") + ".", Toast.LENGTH_LONG).show();

        Log.d("IN ACTIVITY", "AFA");
        String msg = bundle.getString("message");
        String movies = bundle.getString("movies");
        Log.d("Movies: ", movies);

        lv = (ListView)findViewById(R.id.movie_list);
        prev = (Button) findViewById(R.id.button_prev);
        next = (Button) findViewById(R.id.button_next);

        Log.d("Initial inc ", ""+inc);

        prev.setEnabled(false);
        prev.setClickable(false);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inc--;
                loadPage(inc);
                buttonEnable();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inc++;
                loadPage(inc);
                buttonEnable();
            }
        });


        movieList = new ArrayList<String>();
        if(movies != null) {
            if (!movies.equals("")) {
                for (String s : movies.split("\n")) {
                    movieList.add(s);
                }
                listSize = movieList.size();
                if (listSize == 1) {
                    Toast.makeText(this, "No results found.", Toast.LENGTH_LONG).show();
                    movieList.add("No Results found.");
                    listSize = 2;
                    prev.setEnabled(false);
                    next.setEnabled(false);
                }

            }

        }

        n = listSize % pageLen;
        n = (n == 0 ? 0 : 1);
        numPages = listSize / pageLen + n;

        if(numPages <= 1)
        {
            next.setEnabled(false);
            next.setClickable(false);
        }
        loadPage(0);
    }

    // Check if the the prev or next button needs to be grayed out
    private void buttonEnable() {
        Log.d("buttonEnable INC: ", ""+inc);
        Log.d("NUMPAGES ", ""+numPages);
        if(numPages <= 1)
        {
            next.setClickable(false);
            next.setEnabled(false);
        }
        if(numPages > 1)
        {
            next.setEnabled(true);
            next.setClickable(true);
        }
         if (inc == 0) {
            prev.setEnabled(false);
            prev.setClickable(false);

        }
        else if((inc + 1) ==  numPages) {
            next.setEnabled(false);
            next.setClickable(false);
             prev.setEnabled(true);
             prev.setClickable(true);

         }
         else {
            next.setEnabled(true);
            prev.setEnabled(true);
            next.setClickable(true);
            prev.setClickable(true);
        }
    }

    // Generate enough movies for one page
    private void loadPage(int num) {
        ArrayList<String> page = new ArrayList<String>();
        for(int i = num * pageLen; i < (num * pageLen)+pageLen; i++) {
            if (i < movieList.size()) {
                page.add(movieList.get(i));
            } else { break; }
        }

        adapter = new ArrayAdapter<String>(this, R.layout.listview_layout, page);
        lv.setAdapter(adapter);
    }
}
