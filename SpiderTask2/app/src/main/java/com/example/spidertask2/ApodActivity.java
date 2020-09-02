package com.example.spidertask2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
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

    private YouTubePlayerView youTubePlayerView;

    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod);
        getSupportActionBar().setTitle("APOD");
        getSupportActionBar().setSubtitle("Astronomy Picture of the Day");

        constraintLayout = findViewById(R.id.constraint_layout_apod_activity);
        datePicker = findViewById(R.id.imageView_datePicker);
        apod = findViewById(R.id.imageView_apod);
        date = findViewById(R.id.textView_date);
        title = findViewById(R.id.textView_title);
        explanation = findViewById(R.id.textView_explanation);

        explanation.setMovementMethod(new ScrollingMovementMethod());

        youTubePlayerView = findViewById(R.id.youtube_player);
        getLifecycle().addObserver(youTubePlayerView);

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
                    videoToImageLayout();
                    apod.setImageResource(R.drawable.placeholder);
                    title.setText("");
                    explanation.setText("Error code: " + response.code());
                    return;
                }

                Apod content = response.body();

                title.setText(content.getTitle());
                explanation.setText("Explanation: " + content.getExplanation());

                if(content.getMedia_type().equals("image")) {
                    videoToImageLayout();

                    Picasso.get()
                            .load(content.getUrl())
                            .placeholder(R.drawable.placeholder)
                            .into(apod);
                }

                else if(content.getMedia_type().equals("video")) {
                    imageToVideoLayout();

                    String url = content.getUrl();
                    Log.d(TAG, "onResponse: url: " + url);
                    String[] str = url.split("\\?");
                    String[] str2 = str[0].split("/");
                    String videoID = str2[str2.length - 1];
                    Log.d(TAG, "onReady: videoid: " + videoID);

                    youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
                        youTubePlayer.cueVideo(videoID, 0);
                    });

                }

            }

            @Override
            public void onFailure(Call<Apod> call, Throwable t) {
                explanation.setText(t.getMessage());
                videoToImageLayout();
                apod.setImageResource(R.drawable.placeholder);
                title.setText("");
                explanation.setText(t.getMessage());
            }
        });
    }

    public void imageToVideoLayout() {
        apod.setVisibility(View.GONE);
        youTubePlayerView.setVisibility(View.VISIBLE);
        Log.d(TAG, "imageToVideoLayout: player height: " + youTubePlayerView.getMeasuredHeight());
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.textView_title, ConstraintSet.TOP, R.id.youtube_player, ConstraintSet.BOTTOM, 8);
        constraintSet.applyTo(constraintLayout);
    }

    public void videoToImageLayout() {
        apod.setVisibility(View.VISIBLE);
        youTubePlayerView.setVisibility(View.GONE);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.textView_title, ConstraintSet.TOP, R.id.imageView_apod, ConstraintSet.BOTTOM, 8);
        constraintSet.applyTo(constraintLayout);
    }

}
