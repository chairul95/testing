package com.example.pacinterviewtesting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pacinterviewtesting.adapter.AdapterRv;
import com.example.pacinterviewtesting.model.ResponseData;
import com.example.pacinterviewtesting.model.detail.ResponseDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView txtTitle,txtPrice,txtDesc;
    TextView txtRate,txtStock,txt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imageView = findViewById(R.id.iv_detail);
        txtTitle = findViewById(R.id.tvTitle_detail);
        txtPrice = findViewById(R.id.tvPrice_detail);
        txtDesc = findViewById(R.id.tvDesc_detail);
        txtStock = findViewById(R.id.tvStock_detail);
        txtRate = findViewById(R.id.tvRate_detail);

        loadGson();
        Bundle extras = getIntent().getExtras();
        String useId = null;
        if (extras != null) {
            if (extras.containsKey("ID")) {
                useId = extras.getString("ID");
            }
        }
//        if (useId!=null) {
////            loadGson(useId);
//        }
//        Intent i= getIntent();
//        String id = i.getStringExtra("ID");
//        Log.i("infodatax",id);
    }
    @Override
    protected void onStart() {
        super.onStart();



    }
    private void loadGson() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fakestoreapi.com/products/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiInterface request = retrofit.create(ApiInterface.class);
        Call<ResponseDetail> call = null;
        call = request.getDataDetailReq(Global.ID);
        Log.i("Tracking", "false");


        call.enqueue(new Callback<ResponseDetail> () {
            @Override
            public void onResponse(Call<ResponseDetail>  call, Response<ResponseDetail>  response) {
                if (response == null) {
                    Toast.makeText(DetailActivity.this,"Data Is Empty",Toast.LENGTH_SHORT).show();
                }
                if (response.code() == 404) {
                    Toast.makeText(DetailActivity.this,"404 Not Found",Toast.LENGTH_SHORT).show();
                }
                if (response.code() == 502) {
                    Toast.makeText(DetailActivity.this,"502 Bad Gateway",Toast.LENGTH_SHORT).show();
                }
                else{

                    Picasso.get().load(response.body().getImage()).into(imageView);
                    txtTitle.setText(response.body().getTitle());
                    txtDesc.setText(response.body().getDescription());
                    txtPrice.setText("Price : "+String.valueOf(response.body().getPrice()));
                    txtRate.setText("Rate : "+String.valueOf(response.body().getRating().getRate()));
                    txtStock.setText("Stocks : "+String.valueOf(response.body().getRating().getCount()));


                }

            }

            @Override
            public void onFailure(Call<ResponseDetail>  call, Throwable t) {
                Log.e("Err",t.getMessage());

            }
        });
    }

}