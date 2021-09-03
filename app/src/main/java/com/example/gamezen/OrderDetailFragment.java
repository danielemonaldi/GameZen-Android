package com.example.gamezen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamezen.adapter.CartAdapter;
import com.example.gamezen.adapter.CategoriesAdapter;
import com.example.gamezen.adapter.OrderDetailAdapter;
import com.example.gamezen.classes.Products;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderDetailFragment extends Fragment {

    List<Products> productsList;

    RecyclerView recyclerView;

    OrderDetailAdapter orderDetailAdapter;

    TextView orderID, dateText, totalText;

    int id;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderDetailFragment newInstance(String param1, String param2) {
        OrderDetailFragment fragment = new OrderDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        Bundle bundle = this.getArguments();
        id = bundle.getInt("id");
        String date = bundle.getString("date");
        String total = bundle.getString("total");

        productsList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.orderDetailRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        orderID = view.findViewById(R.id.orderID);
        dateText = view.findViewById(R.id.date);
        totalText = view.findViewById(R.id.total);

        orderID.setText("Ordine n. #" + id);
        dateText.setText("Data ordine: " + date);
        totalText.setText("Totale: " + total);

        loadProducts();

        orderDetailAdapter = new OrderDetailAdapter(getActivity(), productsList);
        recyclerView.setAdapter(orderDetailAdapter);

        return view;
    }

    public void loadProducts() {

        String URL = "http://antonserver.ddns.net/gamezen/orderDetail.php?id=" + id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject products = array.getJSONObject(i);

                        productsList.add(new Products(

                                products.getInt("code"),
                                products.getString("name"),
                                products.getInt("price"),
                                products.getString("image"),
                                products.getInt("quantity")
                        ));
                    }

                    orderDetailAdapter = new OrderDetailAdapter(getActivity(), productsList);
                    recyclerView.setAdapter(orderDetailAdapter);

                    orderDetailAdapter.setOnItemClickListener(new OrderDetailAdapter.OnItemClickListener() {
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
}