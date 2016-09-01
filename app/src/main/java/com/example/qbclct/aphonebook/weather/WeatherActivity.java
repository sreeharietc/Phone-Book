package com.example.qbclct.aphonebook.weather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qbclct.aphonebook.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    String location;
    EditText et;
    private final static String API_KEY = "d9991cc658f744a47552eb58762b7133";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        et = (EditText) findViewById(R.id.editText3);
        Button button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = et.getText().toString();
                onClickHandler(location);
            }
        });
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call= apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode =response.code();
                List<Movie> movies = response.body().getResults();

            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

            }
        });
    }

    public void onClickHandler(String location) {
        String weatherUrl = "http://api.openweathermap.org/data/2.5/weather?q="+location+",in&appid=e54c548c1aa48386ae06c6c7d74cf03c";
        ConnectivityManager connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMngr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            new DownloadWebPageTask().execute(weatherUrl);
        }
        else{

        }
    }
    public  class DownloadWebPageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
               return downloadUrl(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private String downloadUrl(String url) throws IOException {
            InputStream inputStream = downloadIs(url);
            ///
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            ///

            String weatherXMLString = total.toString();

            if(inputStream != null){
                inputStream.close();
            }
            System.out.println(weatherXMLString);
            return weatherXMLString;
        }
        private InputStream downloadIs(String stringUrl) throws IOException {
            URL url = new URL(stringUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream inputStream =conn.getInputStream();
            return inputStream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                JSONArray weather = object.getJSONArray("weather");
                JSONObject weather1 = weather.getJSONObject(0);
                String description = weather1.getString("description");
                TextView tv = (TextView) findViewById(R.id.textView5);
                tv.setText(description);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
