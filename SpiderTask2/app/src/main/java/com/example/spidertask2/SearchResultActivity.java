package com.example.spidertask2;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultActivity extends AppCompatActivity {

    private static final String TAG = "SearchResultActivity";

    ImageView image;
    TextView title;
    TextView description;

    String nasaID;

    MediaAssetApi mediaAssetApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        getSupportActionBar().setTitle("Content");

        image = findViewById(R.id.imageView_search_result);
        title = findViewById(R.id.textView_title_search_item);
        description = findViewById(R.id.textView_description_search_item);
        description.setMovementMethod(new ScrollingMovementMethod());

        Intent fromNasaIVLib = getIntent();
        DataItems item = fromNasaIVLib.getParcelableExtra("dataItem");

        title.setText(item.getTitle());
        description.setText(item.getDescription());

        nasaID = item.getNasa_id();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://images-api.nasa.gov/")
                .build();

        mediaAssetApi = retrofit.create(MediaAssetApi.class);

        getImage();

    }

    public void getImage() {

        Call<MediaAsset> call = mediaAssetApi.getAsset(nasaID);
        call.enqueue(new Callback<MediaAsset>() {
            @Override
            public void onResponse(Call<MediaAsset> call, Response<MediaAsset> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: code: " + response.code());
                    title.setText("");
                    description.setText("Error code: " + response.code());
                    image.setImageResource(R.drawable.placeholder);
                    return;
                }

                CollectionMediaAsset collection = response.body().getCollection();

                ArrayList<MediaAssetItems> items = collection.getItems();

                String imageUrl = "";
                for(int i = 0; i < items.size(); i++) {
                    String mediaItemUrl = items.get(i).getHref();
                    if(mediaItemUrl.endsWith(".jpg") && (!mediaItemUrl.contains("orig")) && (!mediaItemUrl.contains("thumb"))) {
                        imageUrl = mediaItemUrl;
                        imageUrl = imageUrl.replace("http", "https");
                        break;
                    }
                }

                Log.d(TAG, "onResponse: nasaid: " + nasaID);
                Log.d(TAG, "onResponse: url: " + imageUrl);

                if(!imageUrl.equals("")) {
                    Picasso.get()
                            .load(imageUrl)
                            .placeholder(R.drawable.placeholder)
                            .into(image);
                }
                else {
                    Toast.makeText(SearchResultActivity.this, "No image available", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MediaAsset> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                title.setText("");
                description.setText(t.getMessage());
                image.setImageResource(R.drawable.placeholder);
            }
        });

    }

}
