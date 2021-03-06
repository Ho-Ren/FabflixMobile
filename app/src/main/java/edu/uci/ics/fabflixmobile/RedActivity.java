package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RedActivity extends ActionBarActivity {
    private EditText username = null;
    private EditText password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("last_activity") != null) {
                Toast.makeText(this, "Last activity was " + bundle.get("last_activity") + ".", Toast.LENGTH_LONG).show();
            }
            String msg = bundle.getString("message");
            if(msg != null && !"".equals(msg)){
                ((TextView)findViewById(R.id.last_page_msg_container)).setText(msg);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_red, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void connectToTomcat(final View view){
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        final String email = username.getText().toString();
        final String pass = password.getText().toString();

        Log.d("email: ", email);
        Log.d("password: ", pass);
        final Map<String, String> params = new HashMap<String, String>();
        // no user is logged in, so we must connect to the server
        RequestQueue queue = Volley.newRequestQueue(this);
        final Context context = this;
        String url = "http://10.0.2.2:8080/AndroidServlets/servlet/androidlogin?";
        url = url + "email=" + email;
        url = url + "&pw=" + pass;
        Intent goToIntent = new Intent(this, GreenActivity.class);
        goToIntent.putExtra("last_activity", "red");
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {


                        Log.d("response", response);
                        ((TextView)findViewById(R.id.http_response)).setText(response);
                        String login = ((TextView) findViewById(R.id.http_response)).getText().toString();
                        Log.d("CREDS: ", login);
                        if(login.equals("successful"))
                        {

                            Log.d("LOGIN ", "loginsuccessful");
                            goToGreen(view);
                        }
                        else
                        {
                            Log.d("NOPE: ", "Login nope");
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
    public void goToBlue(View view){
        String msg = ((EditText)findViewById(R.id.username)).getText().toString();
        Intent goToIntent = new Intent(this, BlueActivity.class);
        goToIntent.putExtra("last_activity", "red");
        goToIntent.putExtra("message", msg);
        startActivity(goToIntent);
    }
    public void goToGreen(View view){
        String msg = ((EditText)findViewById(R.id.username)).getText().toString();
        Intent goToIntent = new Intent(this, GreenActivity.class);
        goToIntent.putExtra("last_activity", "red");
        goToIntent.putExtra("message", msg);
        startActivity(goToIntent);
    }
}
