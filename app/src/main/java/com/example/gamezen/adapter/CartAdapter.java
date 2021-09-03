package com.example.gamezen.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.gamezen.CartFragment;
import com.example.gamezen.Client;
import com.example.gamezen.InformationFragment;
import com.example.gamezen.R;
import com.example.gamezen.classes.Products;
import java.io.IOException;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Client client = new Client();

    Products products;
    private Context mCtx;
    public List<Products> productsList;
    private int count;
    private String id;
    CartAdapter.OnItemClickListener mListner;

    CartFragment cart = new CartFragment();

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

    public void setOnItemClickListener(CartAdapter.OnItemClickListener listener) {

        mListner = listener;
    }

    public CartAdapter(Context mCtx, List<Products> productsList) {
        this.mCtx = mCtx;
        this.productsList = productsList;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cart, null);
        return new CartAdapter.ViewHolder(view, mListner);
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

        holder.quantity.setText(String.valueOf(products.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, quantity, price;
        ImageView image;
        Button increment, decrement, delete;

        public ViewHolder(View v, CartAdapter.OnItemClickListener listener) {
            super(v);

            productName = v.findViewById(R.id.productName);
            price = v.findViewById(R.id.price);
            quantity = v.findViewById(R.id.quantity);
            image = v.findViewById(R.id.image);

            increment = v.findViewById(R.id.cartIncrement);
            decrement = v.findViewById(R.id.cartDecrement);
            delete = v.findViewById(R.id.delete);

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

            increment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences settings = mCtx.getSharedPreferences("user", Context.MODE_PRIVATE);

                    id = settings.getString("id", "");

                    int position = getAdapterPosition();

                    count = Integer.parseInt((String) quantity.getText());

                    count++;
                    quantity.setText("" + count);

                    setQuantity(productsList.get(position).getCode());
                }
            });

            decrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences settings = mCtx.getSharedPreferences("user", Context.MODE_PRIVATE);

                    id = settings.getString("id", "");

                    count = Integer.parseInt((String) quantity.getText());

                    int position = getAdapterPosition();

                    if(count <= 1) {

                        count = 1;

                    } else {

                        count--;
                    }

                    quantity.setText("" + count);

                    setQuantity(productsList.get(position).getCode());
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences settings = mCtx.getSharedPreferences("user", Context.MODE_PRIVATE);

                    id = settings.getString("id", "");

                    int position = getAdapterPosition();

                    deleteProduct(productsList.get(position).getCode());

                    productsList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, productsList.size());
                }
            });
        }
    }

    public void setQuantity(int position) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    client.setQuantity(id, String.valueOf(position), count);
                    cart.refresh(mCtx);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        thread.start();
    }

    public void deleteProduct(int position) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    client.deleteProduct(id, String.valueOf(position));
                    cart.refresh(mCtx);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        thread.start();
    }
}
