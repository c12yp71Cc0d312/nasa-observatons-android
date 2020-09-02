package com.example.spidertask2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NasaIVLibActivity extends AppCompatActivity implements SearchItemsAdapter.OnItemListener {

    private static final String TAG = "NasaIVLibActivity";

    private RecyclerView recyclerView_items;
    private SearchItemsAdapter searchItemsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button search;
    private EditText text;

    private ArrayList<DataItems> dataItems;
    private NasaIVLibApi nasaIVLibApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivlib);
        getSupportActionBar().setTitle("NASA Image and Video Library");

        search = findViewById(R.id.button_search);
        text = findViewById(R.id.editText_search_text);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://images-api.nasa.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nasaIVLibApi = retrofit.create(NasaIVLibApi.class);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContent();
            }
        });

    }

    public void getContent() {

        if(!(text.getText().toString().trim().equals("") || text.getText() == null)) {
            Call<ImageAndVideoLibrary> call = nasaIVLibApi.getCollection(text.getText().toString());
            call.enqueue(new Callback<ImageAndVideoLibrary>() {
                @Override
                public void onResponse(Call<ImageAndVideoLibrary> call, Response<ImageAndVideoLibrary> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(NasaIVLibActivity.this, "Code: " + response.code(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onResponse: Code: " + response.code());
                        return;
                    }

                    CollectionImageAndVideoLibrary collection;
                    ArrayList<ItemsIVLib> itemsIVLibs;

                    collection = response.body().getCollection();
                    Log.d(TAG, "onResponse: response body: " + response.body());

                    itemsIVLibs = new ArrayList<>();

                    if (collection != null) {
                        for (ItemsIVLib i : collection.getItems())
                            itemsIVLibs.add(i);
                    } else {
                        Log.d(TAG, "onResponse: collection is null");
                        Toast.makeText(NasaIVLibActivity.this, "No Search Results", Toast.LENGTH_SHORT).show();
                    }

                    dataItems = new ArrayList<>();

                    if (itemsIVLibs != null) {
                        for (ItemsIVLib item : itemsIVLibs) {
                            for (DataItems d : item.getData()) {
                                dataItems.add(d);
                            }
                        }
                    } else {
                        Log.d(TAG, "onResponse: ItemsIVLibs false");
                        Toast.makeText(NasaIVLibActivity.this, "No Search Results", Toast.LENGTH_SHORT).show();
                    }

                    setUpRecyclerView();
                }

                @Override
                public void onFailure(Call<ImageAndVideoLibrary> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    Toast.makeText(NasaIVLibActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        else {
            Toast.makeText(this, "Text field is empty", Toast.LENGTH_SHORT).show();
        }

    }

    public void setUpRecyclerView() {

        recyclerView_items = findViewById(R.id.recycler_view_search_results);
        recyclerView_items.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        searchItemsAdapter = new SearchItemsAdapter(dataItems, this);

        recyclerView_items.setAdapter(searchItemsAdapter);
        recyclerView_items.setLayoutManager(layoutManager);

    }

    @Override
    public void onItemClick(int pos) {
        DataItems item = dataItems.get(pos);
        Intent toSearchResultActivity = new Intent(NasaIVLibActivity.this, SearchResultActivity.class);
        toSearchResultActivity.putExtra("dataItem", item);
        startActivity(toSearchResultActivity);

        Log.d(TAG, "onItemClick: title: " + item.getTitle());
        Log.d(TAG, "onItemClick: desc: " + item.getDescription());

    }
}
