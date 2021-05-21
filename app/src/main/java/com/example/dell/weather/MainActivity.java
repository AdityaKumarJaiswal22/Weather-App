package com.example.dell.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText inputCity;
    TextView outputScreen;
    public void getWeather (View view) {

        String cityName = inputCity.getText().toString();
        DownloadTask task = new DownloadTask();
        try {
            String encodedCityName = URLEncoder.encode(cityName,"UTF-8");

            task.execute("https://openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=439d4b804bc8187953eb36d2a8c26a02").get();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Opps!!! Could'nt find", Toast.LENGTH_SHORT).show();
        }
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(inputCity.getWindowToken(),0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputCity = findViewById(R.id.editText);
        outputScreen = findViewById(R.id.textView2);
    }

    public class DownloadTask extends AsyncTask<String ,Void , String>
    {


        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data!=-1)
                {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Opps!!! Could'nt find", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("WEATHER",weatherInfo);
                JSONArray arr = new JSONArray(weatherInfo);
                String main = "" ;
                String description = "";
                for (int i = 0; i<arr.length();i++)
                {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");
                    Log.i("main",main);
                    Log.i("description",description);

                }
                outputScreen.setText("Main: "+main +"\n"+"Description: "+ description);

            }catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Opps!!! Could'nt find", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
