package com.example.gamezen.adapter;

import android.content.Context;
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
import com.example.gamezen.R;
import com.example.gamezen.classes.Products;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    Products products;
    private Context mCtx;
    public List<Products> productsList;
    OnItemClickListener mListner;

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

    public void setOnItemClickListener(OrderDetailAdapter.OnItemClickListener listener) {

        mListner = listener;
    }

    public OrderDetailAdapter(Context mCtx, List<Products> productsList) {
        this.mCtx = mCtx;
        this.productsList = productsList;
    }

    @Override
    public OrderDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.orderdetail, null);
        return new OrderDetailAdapter.ViewHolder(view, mListner);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        products = productsList.get(position);

        Glide.with(mCtx)
                .load(products.getImage())
                .into(holder.image);

        holder.productName.setText(products.getName());

        float price = (float)products.getPrice()/100;

        holder.price.setText("€ " + price);

        holder.orderQuantity.setText("Quantità: " + String.valueOf(products.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, price, orderQuantity;
        ImageView image;

        public ViewHolder(View v, OrderDetailAdapter.OnItemClickListener listener) {
            super(v);

            productName = v.findViewById(R.id.productName);
            price = v.findViewById(R.id.price);
            orderQuantity = v.findViewById(R.id.orderQuantity);
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
