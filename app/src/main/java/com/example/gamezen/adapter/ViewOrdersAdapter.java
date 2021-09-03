package com.example.gamezen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamezen.OrderDetailFragment;
import com.example.gamezen.ProductsFragment;
import com.example.gamezen.R;
import com.example.gamezen.classes.Orders;

import java.util.List;

public class ViewOrdersAdapter extends RecyclerView.Adapter<ViewOrdersAdapter.ViewHolder> {

    private Context mCtx;
    private List<Orders> ordersList;
    OnItemClickListener mListner;
    Orders orders;

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        mListner = listener;
    }

    public ViewOrdersAdapter(Context mCtx, List<Orders> ordersList) {
        this.mCtx = mCtx;
        this.ordersList = ordersList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.orders, null);
        return new ViewHolder(view, mListner);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        orders = ordersList.get(position);

        holder.number.setText("Ordine n. #" + String.valueOf(orders.getId()));

        if(orders.getStateid() == 1) {

            holder.state.setTextColor(Color.RED);
            holder.state.setText(orders.getState());
            holder.delivery.setText("-");

        } else if(orders.getStateid() == 2 || orders.getStateid() == 3 || orders.getStateid() == 4 || orders.getStateid() == 5) {

            holder.state.setTextColor(Color.rgb(255, 165, 0));
            holder.state.setText(orders.getState());
            holder.delivery.setText("Consegna prevista: " + orders.getDelivery());

        } else if(orders.getStateid() == 6) {

            holder.state.setTextColor(Color.GREEN);
            holder.state.setText("Consegnato il: " + orders.getDelivery());
            holder.delivery.setText("-");
        }
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView number, delivery, state;

        public ViewHolder(View v, OnItemClickListener listener) {
            super(v);

            number = v.findViewById(R.id.number);
            delivery = v.findViewById(R.id.delivery);
            state = v.findViewById(R.id.state);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {

                        int position = getAdapterPosition();

                        OrderDetailFragment nextFrag= new OrderDetailFragment();

                        Bundle bundle = new Bundle();
                        bundle.putInt("id", (ordersList.get(position).getId()));
                        bundle.putString("date", String.valueOf(ordersList.get(position).getDate()));
                        bundle.putString("total", String.valueOf(ordersList.get(position).getTotal()));
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