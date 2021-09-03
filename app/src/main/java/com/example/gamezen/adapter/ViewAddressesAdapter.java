package com.example.gamezen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gamezen.classes.Addresses;
import com.example.gamezen.R;

import java.util.List;

public class ViewAddressesAdapter extends RecyclerView.Adapter<ViewAddressesAdapter.ViewHolder> {

    private Context mCtx;
    private List<Addresses> addressesList;
    OnItemClickListener mListner;
    Addresses addresses;

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        mListner = listener;
    }

    public ViewAddressesAdapter(Context mCtx, List<Addresses> addressesList) {
        this.mCtx = mCtx;
        this.addressesList = addressesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.viewaddresses, null);
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
    }

    @Override
    public int getItemCount() {
        return addressesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView address, city, cap, province, phone;

        public ViewHolder(View v, OnItemClickListener listener) {
            super(v);
            address = v.findViewById(R.id.address);
            city = v.findViewById(R.id.city);
            cap = v.findViewById(R.id.cap);
            province = v.findViewById(R.id.province);
            phone = v.findViewById(R.id.phone);
        }
    }
}
