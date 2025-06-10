package com.example.weadther_a.ui.home;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.os.Bundle;
import android.os.CpuUsageInfo;
import android.os.Trace;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weadther_a.R;
import com.example.weadther_a.databinding.FragmentHomeBinding;
import com.example.weadther_a.models.Clouds;
import com.example.weadther_a.models.Main;
import com.example.weadther_a.models.Model;
import com.example.weadther_a.models.Sys;
import com.example.weadther_a.models.wind;
import com.example.weadther_a.remote_data.Retrofit_Bulder;
import com.example.weadther_a.remote_data.WeatherApi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.MemoryHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    Integer temperature;

    Integer tempMaximal;

    Integer tempMinimal;

    int humadity_c;

    Trace trace;



    String currentTime = java.text.DateFormat.getDateTimeInstance().format(new Date());

    final String apiKey = WeatherApi.URL_KEY;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.rainLotty.setAnimation(R.raw.bad_weather);
        binding.snowLotty.setAnimation(R.raw.snow_json);

        binding.localtime.setText(currentTime);




        Call<Model> call = Retrofit_Bulder.getInstance().getCurrentWeather("Bishkek", apiKey);


        call.enqueue(new Callback<Model>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Main main_model = response.body().getMain_model();
                    wind wind_model = response.body().getWind_model();
                    Clouds clouds_model = response.body().getClouds_model();
                    Sys sys_model = response.body().getSys_model();


                    Double temp = main_model.getTemp();
                    Double temp_max = main_model.getTempMax();
                    Double temp_min = main_model.getTempMin();

                    temperature = makeFromFaringate(temp);
                    tempMaximal = makeFromFaringate(temp_max);
                    tempMinimal = makeFromFaringate(temp_min);


                    binding.tempMain.setText(String.valueOf(temperature) + "°C");
                    if (temperature <= 14) {
                        binding.sun.setVisibility(View.INVISIBLE);
                        setNoHotWeather();
                    }
                    binding.maxMinTemp.setText(String.valueOf(tempMaximal) + "°C↑  \n" + String.valueOf(tempMinimal) + "°C↓");

                    binding.cityName.setText("Bishkek");

                    binding.humidity.setText(String.valueOf(main_model.getHumidity()) + "%");

                    humadity_c = main_model.getHumidity();
                    if (humadity_c >= 60) {
                        rainy_possible();
                    }
                    binding.pressure.setText(main_model.getPressure() + "\nmbar");
                    binding.wint5.setText(wind_model.getSpeed() + "m/s");
                    binding.Cloudy.setText(clouds_model.getAll() + "%");
                    binding.sunrise.setText(String.valueOf(getCurrDateTime(sys_model.getSunrise())));
                    binding.sunset.setText(String.valueOf(getCurrDateTime(sys_model.getSunset())));
                    binding.timeZone.setText(String.valueOf(response.body().getTimeZone()));

                }
                setCondition();
                if (response.body().getTimeZone() <= 6500 && response.body().getTimeZone() >= -27500) {
                    setNight();
                } else {
                    setDay();
                }
            }


            @Override
            public void onFailure(Call<Model> call, @NonNull Throwable throwable) {
                Toast.makeText(requireActivity(), "У тебя нету интернета", Toast.LENGTH_SHORT).show();
                Log.e("TAG", throwable.getLocalizedMessage());
            }
        });
        return root;
    }

    public String getCurrDateTime(long m_seconds) {
        long timeStampMillis = m_seconds * 1000;
        Date date = new Date(timeStampMillis);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String new_m = timeFormat.format(date);
        return new_m;
    }


    private void rainy_possible() {
        binding.isRainOrNot.setVisibility(View.VISIBLE);
        binding.isRainOrNot.setText("Rain is \npossible");
        binding.rainLotty.setVisibility(View.VISIBLE);
        binding.sun.setVisibility(View.INVISIBLE);
        binding.badWeatherSky.setVisibility(View.VISIBLE);
        snow_monitoring();
    }

    private void setNoHotWeather() {
        binding.sun.setVisibility(View.INVISIBLE);
        binding.blueSky.setVisibility(View.INVISIBLE);
        binding.badWeatherSky.setVisibility(View.VISIBLE);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.slideUpBottomSheet.setOnClickListener(v -> {
            if (binding.bottomSheet.getVisibility() == View.GONE) {
                binding.bottomSheet.setVisibility(View.VISIBLE);
            } else {
                binding.bottomSheet.setVisibility(View.GONE);
            }
            binding.rainLotty.setVisibility(View.INVISIBLE);
            binding.blueSky.setVisibility(View.VISIBLE);
            binding.snowLotty.setVisibility(View.INVISIBLE);
            binding.badWeatherSky.setVisibility(View.INVISIBLE);
            binding.inputCity.setText("...");
            binding.condition.setText("...");
            binding.isRainOrNot.setText("...");
            binding.sun.setVisibility(View.INVISIBLE);

        });

        binding.search.setOnClickListener(v1 -> {
            if (!binding.inputCity.getText().toString().isEmpty()) {
                Call<Model> call = Retrofit_Bulder.getInstance()
                        .getCurrentWeather(binding.inputCity.getText().toString(), apiKey);
                call.enqueue(new Callback<Model>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Main main_model = response.body().getMain_model();
                            wind wind_model = response.body().getWind_model();
                            Clouds clouds_model = response.body().getClouds_model();
                            Sys sys_model = response.body().getSys_model();


                            Double temp = main_model.getTemp();
                            Double temp_max = main_model.getTempMax();
                            Double temp_min = main_model.getTempMin();

                            temperature = makeFromFaringate(temp);
                            tempMaximal = makeFromFaringate(temp_max);
                            tempMinimal = makeFromFaringate(temp_min);


                            binding.tempMain.setText(String.valueOf(temperature) + "°C");
                            if (temperature <= 14) {
                                binding.sun.setVisibility(View.INVISIBLE);
                                setNoHotWeather();
                            }
                            binding.maxMinTemp.setText(String.valueOf(tempMaximal) + "°C↑  \n" + String.valueOf(tempMinimal) + "°C↓");

                            binding.cityName.setText(binding.inputCity.getText().toString());



                            binding.humidity.setText(String.valueOf(main_model.getHumidity()) + "%");

                            humadity_c = main_model.getHumidity();
                            if (humadity_c >= 60) {
                                rainy_possible();
                            }
                            binding.pressure.setText(main_model.getPressure() + "\nmbar");
                            binding.wint5.setText(wind_model.getSpeed() + "m/s");
                            binding.Cloudy.setText(clouds_model.getAll() + "%");
                            binding.sunrise.setText(String.valueOf(getCurrDateTime(sys_model.getSunrise())));
                            binding.sunset.setText(String.valueOf(getCurrDateTime(sys_model.getSunset())));
                            binding.timeZone.setText(String.valueOf(response.body().getTimeZone()));

                        }
                       setCondition();
                        if (response.body().getTimeZone() <= 6500 && response.body().getTimeZone() >= -27500) {
                            setNight();
                        } else {
                            setDay();
                        }
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable throwable) {
                        Toast.makeText(requireActivity(), "У тебя нету интернета", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", throwable.getLocalizedMessage());
                    }


                });
                binding.bottomSheet.setVisibility(View.GONE);

            } else {
                Toast.makeText(requireActivity(), "Input name of city!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void rain_snow_possible() {
        if (humadity_c > 60) {
            if (temperature > 3) {
                rain();
            } else {
            if(temperature <= 3) {
                snow();
            }
            }
        }
    }

    public void setDay() {
        binding.nightSky.setVisibility(View.INVISIBLE);
        binding.blueSky.setVisibility(View.VISIBLE);
    }

    public void setNight() {
        binding.sun.setVisibility(View.INVISIBLE);
        binding.nightSky.setVisibility(View.VISIBLE);
        binding.blueSky.setVisibility(View.INVISIBLE);
    }


    public int makeFromFaringate(double tt) {
        Integer gr = (int) (tt - 273.15);
        return gr;
    }


    public void setCondition() {
        if (temperature <= 14) {
            rain_snow_possible();
            if (temperature < 14 && temperature > -10) {
                setCoolWeather();
            } else {
                setVeryColdWeather();    }

        } else {
            if (temperature <= 50 && temperature > 20) {
            hot_sunnyWeather();
            } else {
            if (temperature <= 20) {
                warm_Weather();
            }
            }
        }
    }


    public void hot_sunnyWeather() {
        binding.blueSky.setVisibility(View.VISIBLE);
        binding.condition.setText("Today is: \nvery hot");
        binding.snowLotty.setVisibility(View.INVISIBLE);
        binding.rainLotty.setVisibility(View.INVISIBLE);
        binding.badWeatherSky.setVisibility(View.INVISIBLE);
        binding.blueSky.setVisibility(View.VISIBLE);
        binding.sun.setVisibility(View.VISIBLE);
        binding.sun.setMaxWidth(180);
        binding.sun.setMaxHeight(180);
    }


    private void warm_rain() {
        binding.blueSky.setVisibility(View.VISIBLE);
        binding.badWeatherSky.setVisibility(View.INVISIBLE);
        binding.sun.setVisibility(View.INVISIBLE);
        binding.condition.setText("Today is possible: \nWarm Rain !!");
    }

    private void setCoolWeather() {
        rain_snow_possible();
        binding.sun.setVisibility(View.INVISIBLE);
        binding.badWeatherSky.setVisibility(View.VISIBLE);
        binding.condition.setText("Today: \nCold !!");
    }


    public void rain() {
        binding.isRainOrNot.setText("Rain is \npossible ");
        binding.badWeatherSky.setVisibility(View.VISIBLE);
        binding.rainLotty.setVisibility(View.VISIBLE);
        binding.sun.setVisibility(View.INVISIBLE);
    }
    public void snow() {
        binding.isRainOrNot.setText("Snow is \npossible");
        binding.badWeatherSky.setVisibility(View.VISIBLE);
        binding.snowLotty.setVisibility(View.VISIBLE);
        binding.sun.setVisibility(View.INVISIBLE);
    }

    private void setVeryColdWeather() {
        rain_snow_possible();
        binding.badWeatherSky.setVisibility(View.VISIBLE);
        binding.sun.setVisibility(View.INVISIBLE);
        binding.condition.setText("Today is: Very Cold !!");
    }

    private void checkHumidity() {
        if (humadity_c >= 60) {
            warm_rain();
        } else {
            warm_Weather();
        }
    }

    private void warm_Weather() {
        binding.blueSky.setVisibility(View.VISIBLE);
        binding.badWeatherSky.setVisibility(View.INVISIBLE);
        binding.rainLotty.setVisibility(View.INVISIBLE);
        binding.snowLotty.setVisibility(View.INVISIBLE);
        if (temperature >= 25) {
            hot_sunnyWeather();
        } else if (temperature < 25 && temperature > 15) {
            binding.condition.setText("Today is: \nsunny!!");
            binding.sun.setVisibility(View.VISIBLE);
        } else {
            binding.condition.setText("Today is: \nlight");
            binding.sun.setVisibility(View.INVISIBLE);
        }
        checkHumidity();
    }



        public void snow_monitoring () {
            if (temperature <= 3 && humadity_c >= 60) {
                binding.snowLotty.setVisibility(View.VISIBLE);
                binding.isRainOrNot.setText("Snow is possible");
                binding.rainLotty.setVisibility(View.INVISIBLE);
                binding.sun.setVisibility(View.INVISIBLE);

            }
        }


        @Override
        public void onDestroyView () {
            super.onDestroyView();
            binding = null;
        }
    }
