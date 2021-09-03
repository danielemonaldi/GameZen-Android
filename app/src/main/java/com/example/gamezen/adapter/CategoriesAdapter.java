package com.example.gamezen.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamezen.classes.Categories;
import com.example.gamezen.ProductsFragment;
import com.example.gamezen.R;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private Context mCtx;
    private List<Categories> categoriesList;
    OnItemClickListener mListner;

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        mListner = listener;
    }

    public CategoriesAdapter(Context mCtx, List<Categories> categoriesList) {
        this.mCtx = mCtx;
        this.categoriesList = categoriesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.categories, null);
        return new ViewHolder(view, mListner);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Categories categories = categoriesList.get(position);

        holder.name.setText(categories.getName());
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ViewHolder(View v, OnItemClickListener listener) {
            super(v);
            name = v.findViewById(R.id.categoryname);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {

                        int position = getAdapterPosition();

                        ProductsFragment nextFrag= new ProductsFragment();

                        Bundle bundle = new Bundle();
                        bundle.putInt("id", (categoriesList.get(position).getId()));
                        bundle.putString("category", String.valueOf(categoriesList.get(position).getName()));
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