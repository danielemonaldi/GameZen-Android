package com.example.gamezen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamezen.InformationFragment;
import com.example.gamezen.classes.Products;
import com.example.gamezen.R;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Context mCtx;
    private List<Products> productsList;
    HomeAdapter.OnItemClickListener mListner;

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

    public void setOnItemClickListener(HomeAdapter.OnItemClickListener listener) {

        mListner = listener;
    }

    public HomeAdapter(Context mCtx, List<Products> productsList) {
        this.mCtx = mCtx;
        this.productsList = productsList;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.random, null);
        return new HomeAdapter.ViewHolder(view, mListner);
    }

    @Override
    public void onBindViewHolder(HomeAdapter.ViewHolder holder, int position) {

        Products products = productsList.get(position);

        Glide.with(mCtx)
                .load(products.getImage())
                .into(holder.image);

        holder.productName.setText(products.getName());

        float price = (float)products.getPrice()/100;

        holder.price.setText("€ " + price);

        if (products.getAvailable() == 1) {

            holder.available.setText("Disponibile");
            holder.available.setTextColor(Color.GREEN);

        } else {
            holder.available.setText("Non disponibile");
            holder.available.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public void filterList(ArrayList<Products> filteredList) {
        productsList = filteredList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, available, price;
        ImageView image;

        public ViewHolder(View v, HomeAdapter.OnItemClickListener listener) {
            super(v);
            productName = v.findViewById(R.id.productName);
            available = v.findViewById(R.id.available);
            price = v.findViewById(R.id.price);
            image = v.findViewById(R.id.image);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {

                        int position = getAdapterPosition();

                        InformationFragment nextFrag= new InformationFragment();

                        Bundle bundle = new Bundle();
                        bundle.putInt("code", (productsList.get(position).getCode()));
                        nextFrag.setArguments(bundle);

                        ((FragmentActivity)mCtx).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                                .addToBackStack(null)
                                .commit();

                        if(position != RecyclerView.NO_POSITION) {

                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
