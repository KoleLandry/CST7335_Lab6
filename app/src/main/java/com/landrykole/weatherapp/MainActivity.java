package com.landrykole.weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements RefreshButtonFragment.OnRefreshButtonClickListener {

    private WeatherFragment weatherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherFragment = new WeatherFragment();
        RefreshButtonFragment refreshButtonFragment = new RefreshButtonFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, weatherFragment, "WeatherFragment");
        transaction.add(R.id.fragment_container, refreshButtonFragment, "RefreshButtonFragment");
        transaction.commit();
    }

    public void onRefreshButtonClick() {
                                                        // API key goes here
        new FetchWeatherTask(weatherFragment).execute("");
        // Used to test if this stupid button works, and it DOES, but no weather will show up???
        // I copied the API key from their email so idk man
        Toast.makeText(this, "Button Clicked", Toast.LENGTH_SHORT).show();
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String> {
        private WeatherFragment fragment;

        public FetchWeatherTask(WeatherFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed to fetch data";
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            fragment.updateWeatherData(result);
        }
    }
}