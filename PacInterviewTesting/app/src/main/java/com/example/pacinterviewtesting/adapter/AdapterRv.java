package com.example.pacinterviewtesting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pacinterviewtesting.DetailActivity;
import com.example.pacinterviewtesting.Global;
import com.example.pacinterviewtesting.R;
import com.example.pacinterviewtesting.model.ResponseData;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class AdapterRv extends RecyclerView.Adapter<AdapterRv.CustomViewHolder> implements Filterable {

    private ArrayList<ResponseData> dataList;
    private List<ResponseData> dataListFull;
    private Context context;


    public AdapterRv(Context context, ArrayList<ResponseData> dataList) {

        if (dataList == null) {
            return;
        }
        this.context = context;
        this.dataList = dataList;
        dataListFull = new ArrayList<>(dataList);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        private TextView txtTitil,txtPrice;
        private ImageView imgRowData;
        private LinearLayout lnRowDataAct;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtTitil = itemView.findViewById(R.id.tvTitleData);
            txtPrice = itemView.findViewById(R.id.tvPriceData);
            imgRowData = itemView.findViewById(R.id.ivRowData);
            lnRowDataAct = itemView.findViewById(R.id.lnRowData);



        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.txtTitil.setText(dataList.get(position).getTitle());
        holder.txtPrice.setText(String.valueOf(dataList.get(position).getPrice()));
        Picasso.get().load(dataList.get(position).getImage()).into(holder.imgRowData);
        Log.i("infodata",dataList.get(position).getImage());

        holder.lnRowDataAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, DetailActivity.class);
                in.putExtra("ID",String.valueOf(dataList.get(position).getId()));
                Global.ID = dataList.get(position).getId();
                context.startActivity(in);
            }
        });


    }


    @Override
    public int getItemCount() {
        return (dataList == null) ? 0 : dataList.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void removeItem(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(ResponseData item, int position) {
        dataList.add(position, item);
        notifyItemInserted(position);
    }

    public ArrayList<ResponseData> getData() {
        return dataList;
    }
    @Override
    public Filter getFilter() {
        return myFilter;
    }

    private Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<ResponseData> filterData = new ArrayList<>();
            if (charSequence == null|| charSequence.length()==0){
                filterData.addAll(dataListFull);
            }else {
                String txtFilter = charSequence.toString().toLowerCase().trim();
                for (ResponseData item : dataListFull){
                    if (item.getTitle().toLowerCase().contains(txtFilter)){
                        filterData.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterData;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            dataList.clear();
            dataList.addAll((Collection<? extends ResponseData>) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
