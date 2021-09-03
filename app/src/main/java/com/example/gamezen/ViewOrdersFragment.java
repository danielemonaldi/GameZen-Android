package com.example.gamezen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamezen.adapter.CategoriesAdapter;
import com.example.gamezen.adapter.ViewAddressesAdapter;
import com.example.gamezen.adapter.ViewOrdersAdapter;
import com.example.gamezen.classes.Addresses;
import com.example.gamezen.classes.Orders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewOrdersFragment extends Fragment {

    List<Orders> ordersList;

    RecyclerView recyclerView;

    ViewOrdersAdapter ordersAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewOrdersFragment newInstance(String param1, String param2) {
        ViewOrdersFragment fragment = new ViewOrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_view_orders, container, false);

        ordersList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.viewOrdersRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadOrders();

        ordersAdapter = new ViewOrdersAdapter(getActivity(), ordersList);
        recyclerView.setAdapter(ordersAdapter);

        return view;
    }

    private void loadOrders() {

        SharedPreferences settings = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        String id = settings.getString("id", "");

        String URL = "http://antonserver.ddns.net/gamezen/getOrders.php?id="+ id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject addresses = array.getJSONObject(i);

                        ordersList.add(new Orders(

                                addresses.getInt("id"),
                                addresses.getString("date"),
                                addresses.getString("delivery"),
                                addresses.getInt("stateid"),
                                addresses.getString("state"),
                                addresses.getString("total")
                        ));
                    }

                    ordersAdapter = new ViewOrdersAdapter(getActivity(), ordersList);
                    recyclerView.setAdapter(ordersAdapter);

                    ordersAdapter.setOnItemClickListener(new ViewOrdersAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {

                            ordersList.get(position);
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
}