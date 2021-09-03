package com.example.gamezen.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamezen.ProductsFragment;
import com.example.gamezen.classes.Addresses;
import com.example.gamezen.R;

import java.util.List;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> {

    private Context mCtx;
    private List<Addresses> addressesList;
    OnItemClickListener mListner;
    Addresses addresses;

    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    public String selectedAddress = "";
    public String selectedId = "";

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        mListner = listener;
    }

    public AddressesAdapter(Context mCtx, List<Addresses> addressesList) {
        this.mCtx = mCtx;
        this.addressesList = addressesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.addresses, null);
        return new ViewHolder(view, mListner);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        addresses = addressesList.get(position);

        holder.address.setText(addresses.getAddress() + ", " +  addresses.getCivic());
        holder.city.setText(addresses.getCity());
        holder.cap.setText(addresses.getCap());
        holder.province.setText(addresses.getProvince());
        holder.phone.setText(addresses.getPhone());

        holder.select.setChecked(addressesList.get(position).isSelected());
        holder.select.setTag(new Integer(position));

        //for default check in first item
        if(position == 0 && addressesList.get(0).isSelected() && holder.select.isChecked())
        {
            lastChecked = holder.select;
            lastCheckedPos = 0;
        }

        holder.select.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckBox cb = (CheckBox)v;
                int clickedPos = ((Integer)cb.getTag()).intValue();

                if(cb.isChecked())
                {
                    if(lastChecked != null)
                    {
                        lastChecked.setChecked(false);
                        addressesList.get(lastCheckedPos).setSelected(false);
                    }

                    lastChecked = cb;
                    lastCheckedPos = clickedPos;

                    selectedAddress = addressesList.get(lastCheckedPos).getAddress();
                    selectedId = String.valueOf(addressesList.get(lastCheckedPos).getId());
                }
                else
                    lastChecked = null;

                addressesList.get(clickedPos).setSelected(cb.isChecked());
            }
        });

    }

    @Override
    public int getItemCount() {
        return addressesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView address, city, cap, province, phone;
        CheckBox select;

        public ViewHolder(View v, OnItemClickListener listener) {
            super(v);
            address = v.findViewById(R.id.address);
            city = v.findViewById(R.id.city);
            cap = v.findViewById(R.id.cap);
            province = v.findViewById(R.id.province);
            phone = v.findViewById(R.id.phone);
            select = v.findViewById(R.id.select);
        }
    }
}