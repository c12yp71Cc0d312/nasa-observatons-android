package com.example.spidertask2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApodActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "ApodActivity";

    private ImageView datePicker;
    private ImageView apod;
    private TextView date;
    private TextView title;
    private TextView explanation;

    private NasaApodApi nasaApodApi;
    public static final String API_KEY = "N5tmmGjzqvEa8yilpjmrh2ya4eFv6Yau8xxto7de";
    private String dateFormatted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod);
        getSupportActionBar().setTitle("APOD");
        getSupportActionBar().setSubtitle("Astronomy Picture of the Day");

        datePicker = findViewById(R.id.imageView_datePicker);
        apod = findViewById(R.id.imageView_apod);
        date = findViewById(R.id.textView_date);
        title = findViewById(R.id.textView_title);
        explanation = findViewById(R.id.textView_explanation);

        explanation.setMovementMethod(new ScrollingMovementMethod());

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dP = new DatePickerFragment();
                dP.show(getSupportFragmentManager(), "date picker");
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nasaApodApi = retrofit.create(NasaApodApi.class);

        setTodayDate();
        getApodDetails();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String dateString = DateFormat.getDateInstance().format(calendar.getTime());
        date.setText(dateString);

        dateFormatted = year + "-" + (month+1) + "-" + dayOfMonth;
        //Toast.makeText(this, dateFormatted, Toast.LENGTH_SHORT).show();
        getApodDetails();
    }

    public void setTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        dateFormatted = year + "-" + (month+1) + "-" + day;

        String dateString = DateFormat.getDateInstance().format(calendar.getTime());
        date.setText(dateString);
    }

    public void getApodDetails() {
        Call<Apod> call = nasaApodApi.getPosts(API_KEY, dateFormatted, true);
        call.enqueue(new Callback<Apod>() {
            @Override
            public void onResponse(Call<Apod> call, Response<Apod> response) {
                if(!response.isSuccessful()) {
                    explanation.setText("Error code: " + response.code());
                    apod.setImageResource(R.drawable.placeholder);
                    title.setText("");
                    explanation.setText("Error code: " + response.code());
                    return;
                }

                Apod content = response.body();

                title.setText(content.getTitle());
                explanation.setText("Explanation: " + content.getExplanation());
                if(content.getMedia_type().equals("image")) {
                    Picasso.get()
                            .load(content.getUrl())
                            .placeholder(R.drawable.placeholder)
                            .into(apod);
                }
            }

            @Override
            public void onFailure(Call<Apod> call, Throwable t) {
                explanation.setText(t.getMessage());
                apod.setImageResource(R.drawable.placeholder);
                title.setText("");
                explanation.setText(t.getMessage());
            }
        });
    }
}
