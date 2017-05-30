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

public class BlueActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);

        Bundle bundle = getIntent().getExtras();
        /*Toast.makeText(this, "Last activity was " + bundle.get("last_activity") + ".", Toast.LENGTH_LONG).show();

        String msg = bundle.getString("message");
        if(msg != null && !"".equals(msg)){
            ((TextView)findViewById(R.id.last_page_msg_container)).setText(msg);
        }*/

        // Need this for ListView (but can't get last param yet):
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_blue, getMovieList());
        ListView lv = (ListView)findViewById(R.id.movie_list);
        lv.setAdapter(adapter);

    }

    public String[] getMovieList() {
        String[] movies; // The destination for the list of movies after getting them from the DB
        final Map<String, String> params = new HashMap<String, String>();


        // no user is logged in, so we must connect to the server
        RequestQueue queue = Volley.newRequestQueue(this);

        final Context context = this;
        // Servlet just has all the movies on a separate line each (no html tags)
        String url = "http://10.0.2.2:8080/login2/GenerateMovies";


        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        //String[] movies;
                        Log.d("response", response);
                    }

                    // Might consider writing a function here to return a list version of the response

                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("security.error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }
        };


        // Add the request to the RequestQueue.
        queue.add(postRequest);
        return params.get("response").split("\n");

    }


    public void goToRed(View view){
        //String msg = ((EditText)findViewById(R.id.blue_2_red_message)).getText().toString();

        Intent goToIntent = new Intent(this, RedActivity.class);

        goToIntent.putExtra("last_activity", "blue");
        //goToIntent.putExtra("message", msg);

        startActivity(goToIntent);
    }
    public void goToGreen(View view){
        //String msg = ((EditText)findViewById(R.id.blue_2_green_message)).getText().toString();

        Intent goToIntent = new Intent(this, GreenActivity.class);

        goToIntent.putExtra("last_activity", "blue");
       // goToIntent.putExtra("message", msg);

        startActivity(goToIntent);
    }

}
