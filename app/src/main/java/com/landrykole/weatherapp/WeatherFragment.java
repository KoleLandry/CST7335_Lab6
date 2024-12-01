package com.landrykole.weatherapp;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class WeatherFragment extends Fragment {

    private LinearLayout weatherLinearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        weatherLinearLayout = view.findViewById(R.id.linearLayout_weather);
        return view;
    }

    public void updateWeatherData(String data) {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            weatherLinearLayout.removeAllViews();
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray listArray = jsonObject.getJSONArray("list");

                for (int i = 0; i < listArray.length(); i++) {
                    JSONObject listObject = listArray.getJSONObject(i);
                    long dateTimeInMillis = listObject.getLong("dt") * 1000;
                    Date date = new Date(dateTimeInMillis);
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, MMMM d, yyyy h:mm a", Locale.getDefault());
                    String dateText = dateFormatter.format(date);

                    // Determine the time of the day for background color
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                    int backgroundColor;

                    // Define colors for different times of day
                    if (hourOfDay >= 6 && hourOfDay < 9) { // Dawn
                        backgroundColor = Color.parseColor("#FFD54F");
                    } else if (hourOfDay >= 9 && hourOfDay < 17) { // Day
                        backgroundColor = Color.parseColor("#FFEB38");
                    } else if (hourOfDay >= 17 && hourOfDay < 20) { // Dusk
                        backgroundColor = Color.parseColor("FF7043");
                    } else { // Night
                        backgroundColor = Color.parseColor("#2196F3");
                    }

                    // Create a container for each weather entry
                    LinearLayout entryContainer = new LinearLayout(getContext());
                    entryContainer.setOrientation(LinearLayout.VERTICAL);
                    entryContainer.setPadding(20,20,20,20);
                    GradientDrawable background = new GradientDrawable();
                    background.setColor(backgroundColor);
                    background.setCornerRadius(10);
                    entryContainer.setBackground(background);

                    // Create a new TextView for the date and add it to the entryContainer
                    TextView dateTextView = new TextView(getContext());
                    dateTextView.setText(dateText);
                    entryContainer.addView(dateTextView);

                    // Create a new ImageView for the weather icon
                    ImageView weatherIcon = new ImageView(getContext());
                    // Set the image resource based on the weather description
                    // Example: if (description.contains("cloud")) weatherIcon.setImageResource(R.drawable.ic_cloud);

                    // TODO: Gotta implement my own logic to get icon working
                    entryContainer.addView(weatherIcon);

                    // Create a new TextView for the weather description
                    JSONObject weather = listObject.getJSONArray("weather").getJSONObject(0);
                    String description = weather.getString("description");
                    TextView descriptionTextView = new TextView(getContext());
                    descriptionTextView.setText(description);
                    entryContainer.addView(descriptionTextView);

                    // Create a new TextView for the temperature and add it to the entryContainer
                    JSONObject main = listObject.getJSONObject("main");
                    double temp = main.getDouble("temp") - 273.15; // Convert from Kelvin to Celsius
                    String temperature = String.format(Locale.getDefault(), "%.1f°C", temp);
                    TextView tempTextView = new TextView(getContext());
                    tempTextView.setText(temperature);
                    entryContainer.addView(tempTextView);

                    // Add the entry container to the main layout
                    weatherLinearLayout.addView(entryContainer);

                    // Add a divider or some space after each entry
                    View divider = new View(getContext());
                    divider.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            1
                    ));
                    divider.setBackgroundColor(Color.LTGRAY);
                    weatherLinearLayout.addView(divider);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}