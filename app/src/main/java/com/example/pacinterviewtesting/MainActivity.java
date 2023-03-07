package com.example.pacinterviewtesting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.pacinterviewtesting.adapter.AdapterRv;
import com.example.pacinterviewtesting.model.ResponseData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
//    GridView myGrid;
//    GridAdapter gridAdapter;
    ArrayList<ResponseData> myData = new ArrayList<>();
    RecyclerView rv_fixlist;
    AdapterRv rv_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv_fixlist = findViewById(R.id.rv_fixlist);


//        loadGson();
//        gridAdapter = new GridAdapter(this, myData);
//        myGrid.setAdapter(gridAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadGson();

    }

    private void loadGson() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fakestoreapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiInterface request = retrofit.create(ApiInterface.class);
        Call<ArrayList<ResponseData>> call = null;
        call = request.getDataReq();
        Log.i("Tracking", "false");


        call.enqueue(new Callback<ArrayList<ResponseData>>() {
            @Override
            public void onResponse(Call<ArrayList<ResponseData>> call, Response<ArrayList<ResponseData>> response) {
                if (response == null) {
                    Toast.makeText(MainActivity.this,"Data Is Empty",Toast.LENGTH_SHORT).show();
                }
                if (response.code() == 404) {
                    Toast.makeText(MainActivity.this,"404 Not Found",Toast.LENGTH_SHORT).show();
                }
                if (response.code() == 502) {
                    Toast.makeText(MainActivity.this,"502 Bad Gateway",Toast.LENGTH_SHORT).show();
                }
                else{
                    myData.addAll(response.body());
                    rv_Adapter = new AdapterRv(MainActivity.this, myData);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    rv_fixlist.setLayoutManager(layoutManager);
                    rv_fixlist.setAdapter(rv_Adapter);
                    rv_Adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<ResponseData>> call, Throwable t) {
                Log.e("Err",t.getMessage());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rv_Adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
}