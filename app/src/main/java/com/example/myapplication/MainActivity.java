package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView day, date, temperature, city, feelsLike, humidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        day = findViewById(R.id.textView_day);
        date = findViewById(R.id.textView_date);
        temperature = findViewById(R.id.textView_temperature);
        city = findViewById(R.id.textView_city);
        feelsLike = findViewById(R.id.textView_feelsLike);
        humidity = findViewById(R.id.textView_humidity);


        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int exactMon = month+1;

       int dates = calendar.get(Calendar.DAY_OF_MONTH);
       int dayName = calendar.getFirstDayOfWeek();
       day.setText("" +dayName);



       date.setText("" +dates+", "+exactMon);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApiInterface weatherApiInterface = retrofit.create(WeatherApiInterface.class);
        Call<CurrentWeather> call = weatherApiInterface.getCurrentWeather("Dhaka","1f462dda351a1f36c3d8b29280ef0fdb");

        call.enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {

                if(response.isSuccessful()){

                   updateWeather(response.body());

                }else {
                    Log.d("response", "failed to load data in failure");
                    
                }

            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {

                Log.d("response", "failed to load data in failure");

            }
        });



    }

    void updateWeather(CurrentWeather weather){

        String cityName = weather.getName();
        city.setText("Current City: "+cityName);

        double temp = weather.getMain().getTemp();
        double tempInCel = temp-273.15;

        

        temperature.setText("Temperature: " +tempInCel+"* Celsius");

        double feelLike =  weather.getMain().getFeelsLike();
        double feelLikeTemp = feelLike-273.15;
        feelsLike.setText("Real Feel Like: " +feelLikeTemp+"* Celsius");

        double hmidity =  weather.getMain().getHumidity();
        humidity.setText("Humidity: " +hmidity);

    }


}