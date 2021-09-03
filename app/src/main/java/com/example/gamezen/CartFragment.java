package com.example.gamezen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamezen.adapter.CartAdapter;
import com.example.gamezen.classes.Products;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    List<Products> productsList;

    RecyclerView recyclerView;

    CartAdapter cartAdapter;

    String id;

    Button order;

    public TextView total;

    float totalPrice, unit;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        SharedPreferences settings = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        id = settings.getString("id", "");

        productsList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.cartRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        total = view.findViewById(R.id.total);
        order = view.findViewById(R.id.returnHome);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vibrator.vibrate(80);

                AddressesFragment nextFrag= new AddressesFragment();

                Bundle bundle = new Bundle();
                bundle.putString("total", String.valueOf(total.getText()));
                nextFrag.setArguments(bundle);

                (getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        loadProducts();

        cartAdapter = new CartAdapter(getActivity(), productsList);
        recyclerView.setAdapter(cartAdapter);

        return view;
    }

    public void loadProducts() {

        String URL = "http://antonserver.ddns.net/gamezen/cart.php?id=" + id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);

                    if (array.length() == 0) {
                        totalPrice = 0;
                        total.setText(" € " + String.valueOf(round(totalPrice, 2)));
                        order.setEnabled(false);
                    }

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject products = array.getJSONObject(i);

                        productsList.add(new Products(

                                products.getInt("code"),
                                products.getString("name"),
                                products.getInt("price"),
                                products.getString("image"),
                                products.getInt("quantity")
                        ));

                        unit = ((float) products.getInt("price")/100) * products.getInt("quantity");
                        totalPrice = totalPrice + unit;

                        total.setText(" € " + String.valueOf(round(totalPrice, 2)));
                    }

                    totalPrice = 0;

                    cartAdapter = new CartAdapter(getActivity(), productsList);
                    recyclerView.setAdapter(cartAdapter);

                    cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {

                            productsList.get(position);

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
        );

        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void refresh(Context test) {

        CartFragment nextFrag= new CartFragment();

        ((FragmentActivity)test).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }
}