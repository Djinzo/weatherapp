package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private TextView textViewcity, textViewDate, textViewTmp, textViewTmin, textViewTmax, textViewPression, textViewHmd,textViewStatus,textViewWind;
    private TextView textViewSunSet,textViewSunRise;
    private ImageView imageViewWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textViewcity = (TextView) findViewById(R.id.textViewCity);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewHmd = (TextView) findViewById(R.id.textViewHmd);
        textViewPression = (TextView) findViewById(R.id.textViewPression);
        textViewTmax = (TextView) findViewById(R.id.textViewTmpMax);
        textViewTmin = (TextView) findViewById(R.id.textViewTmpMin);
        textViewTmp = (TextView) findViewById(R.id.textViewTmp);
        imageViewWeather=(ImageView) findViewById(R.id.imageView);
        textViewStatus=(TextView) findViewById(R.id.status);
        textViewWind=(TextView) findViewById(R.id.textViewWind);
        textViewSunRise=(TextView) findViewById(R.id.textViewSunRise);
        textViewSunSet=(TextView) findViewById(R.id.textViewSunSet);
        getWeather("TanTan");

    }

    public void setImage(String imageName){
        MyAsyc mt=new MyAsyc();
        mt.execute(imageName);
    }

    class MyAsyc extends AsyncTask<String,String,String>{
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url = new URL("http://openweathermap.org/img/wn/"+strings[0]+"@2x.png");
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                imageViewWeather.setImageBitmap(bmp);
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                getWeather(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_button) {
            Log.wtf("this is the tag ------:", "clikerd");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void getWeather(String query){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        String url = "http://api.openweathermap.org/data/2.5/weather?q="
                + query + "&appid=70d2baa7b091d5e92bfed9ebc50d7993&units=metric";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.wtf("hello this is the tag :",response);
                        try{
                            JSONObject jsonObject=new JSONObject(response);

                            textViewcity.setText(jsonObject.getString("name"));
                            Date dt=new Date(jsonObject.getLong("dt"));
                            textViewDate.setText(dt.toString());
                            JSONObject jsonObjectMain=jsonObject.getJSONObject("main");
                            textViewTmp.setText(jsonObjectMain.getString("temp")+" C°");
                            textViewTmin.setText("T°min :"+jsonObjectMain.getString("temp_min"));
                            textViewTmax.setText("T°max :"+jsonObjectMain.getString("temp_max"));
                            textViewPression.setText("Pression :"+jsonObjectMain.getString("pressure"));
                            textViewHmd.setText("Humidite :"+jsonObjectMain.getString("humidity"));
                            JSONArray jsonObjectWeather=jsonObject.getJSONArray("weather");
                            textViewStatus.setText(jsonObjectWeather.getJSONObject(0).getString("description"));
                            textViewWind.setText("Speed:"+jsonObject.getJSONObject("wind").getString("speed"));
                            JSONObject jsonObjectsys=jsonObject.getJSONObject("sys");
                            Date t=new Date(jsonObjectsys.getLong("sunrise"));
                            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm:ss");
                            textViewSunRise.setText(simpleDateFormat.format(t));
                            Date t2=new Date(jsonObjectsys.getLong("sunset"));
                            SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("hh:mm:ss");
                            textViewSunSet.setText(simpleDateFormat.format(t2));
                            setImage(jsonObjectWeather.getJSONObject(0).getString("icon"));
                        }catch (Exception e){
                            Log.wtf("Tag",e.getMessage());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                            Toast.makeText(MainActivity.this,"City not fond",Toast.LENGTH_LONG).show();

                        Log.wtf("hello this is the tag :",error.toString());
                    }
                });

        requestQueue.add(stringRequest);

    }
}
