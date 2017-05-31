package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class GreenActivity extends ActionBarActivity {
    private EditText search = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green);

        String user = "Signed in as ";
        Bundle bundle = getIntent().getExtras();
        Toast.makeText(this, "You have logged in successfully.", Toast.LENGTH_LONG).show();

        String msg = bundle.getString("message");
        if(msg != null && !"".equals(msg)){
            ((TextView)findViewById(R.id.last_page_msg_container)).setText(user + msg);
            user = user + ((TextView) findViewById(R.id.last_page_msg_container)).getText().toString();
        }
    }

    public void search(final View view){
        search = (EditText) findViewById(R.id.search);
        final String searchstr = search.getText().toString();

        Log.d("title: ", searchstr);
        final Map<String, String> params = new HashMap<String, String>();
        // no user is logged in, so we must connect to the server
        RequestQueue queue = Volley.newRequestQueue(this);
        final Context   context = this;
        String url = "http://10.0.2.2:8080/AndroidServlets/servlet/androidsearch?";
        String encodedUrl = null;
        try {
            encodedUrl = URLEncoder.encode(searchstr, "UTF-8");
            Log.d("ENCODED:", encodedUrl);
        } catch (UnsupportedEncodingException ignored) {
            // Can be safely ignored because UTF-8 is always supported
        }
        url = url + "title=" + encodedUrl;
        Log.d("FINAL URL:", url);

        Intent goToIntent = new Intent(this, GreenActivity.class);
        goToIntent.putExtra("last_activity", "red");

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        ((TextView)findViewById(R.id.http_response)).setText(response);
                        if(!"".equals(searchstr))
                        {
                            Log.d("search ", "successful");
                            goToBlue(view);
                        }
                        else
                        {
                            Log.d("NOPE: ", "Enter into search");
                        }

                    }
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
//                params.put("username", email);
//                params.put("password", pass);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(postRequest);

        //goToIntent.putExtra("message", msg);
        return ;
    }
    public void goToRed(View view){
       // String msg = ((EditText)findViewById(R.id.green_2_red_message)).getText().toString();

        Intent goToIntent = new Intent(this, RedActivity.class);

        goToIntent.putExtra("last_activity", "green");
    //    goToIntent.putExtra("message", msg);

        startActivity(goToIntent);
    }
    public void goToBlue(View view){
        String msg = ((EditText)findViewById(R.id.search)).getText().toString();
        String movies = ((TextView)findViewById(R.id.http_response)).getText().toString();
        Log.d("movies", movies);
        Intent goToIntent = new Intent(this, BlueActivity.class);
        goToIntent.putExtra("last_activity", "green");

        goToIntent.putExtra("movies", movies);
        goToIntent.putExtra("message", msg);
        Log.d("BEFORE ACT", "BEFORE");
        startActivity(goToIntent);
    }

}
