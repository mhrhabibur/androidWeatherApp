package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView day, date, temperature, city, feelsLike, humidity, status, sunrise, sunset;
    ImageView weatherStatus;

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
        status = findViewById(R.id.textView_status);
        weatherStatus = findViewById(R.id.imageView_status);
        sunrise = findViewById(R.id.textView_sunrise);
        sunset = findViewById(R.id.textView_sunset);


        Calendar calendar = Calendar.getInstance();
        String currentdate  =  DateFormat.getDateInstance().format(calendar.getTime());

        String currentDay  =  DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        String[] splitday = currentDay.split(", ");
        day.setText(""+splitday[0].trim());



       date.setText("" +currentdate);

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

        Weather cureentWeather = weather.getWeather().get(0);
        String currentStatus = cureentWeather.getMain();

        status.setText(currentStatus);


       String statusId = cureentWeather.getIcon();
       String url = "https://openweathermap.org/img/wn/"+statusId+"@2x.png";
       Picasso.get().load(url).into(weatherStatus);


        temperature.setText("Temperature: " +tempInCel+"* Celsius");

        double feelLike =  weather.getMain().getFeelsLike();
        double feelLikeTemp = feelLike-273.15;
        String feelLiketemperature = String.format("%.2f", feelLikeTemp);
        feelsLike.setText("Real Feel Like: " +feelLiketemperature+"* Celsius");

        double hmidity =  weather.getMain().getHumidity();
        humidity.setText("Humidity: " +hmidity);

        Integer sunriseStatus = weather.getSys().getSunrise();
        String sunriseTime = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date(sunriseStatus/1000));
        sunrise.setText(sunriseTime);

        Integer sunsetStatus = weather.getSys().getSunset();
        String sunsetsTime = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date(sunsetStatus/1000));
        sunset.setText(sunsetsTime);

    }


}