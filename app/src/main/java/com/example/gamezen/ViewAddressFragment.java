package com.example.gamezen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamezen.adapter.AddressesAdapter;
import com.example.gamezen.adapter.ViewAddressesAdapter;
import com.example.gamezen.classes.Addresses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAddressFragment extends Fragment {

    List<Addresses> addressesList;

    RecyclerView recyclerView;

    ViewAddressesAdapter addressesAdapter;

    Button addAddress;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewAddressFragment newInstance(String param1, String param2) {
        ViewAddressFragment fragment = new ViewAddressFragment();
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
        View view = inflater.inflate(R.layout.fragment_view_address, container, false);

        addressesList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.viewAddressesRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        addAddress = view.findViewById(R.id.addAddress);

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewAddressFragment nextFrag= new NewAddressFragment();

                (getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        loadAddresses();

        addressesAdapter = new ViewAddressesAdapter(getActivity(), addressesList);
        recyclerView.setAdapter(addressesAdapter);

        return view;
    }

    private void loadAddresses() {

        SharedPreferences settings = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        String id = settings.getString("id", "");

        String URL = "http://antonserver.ddns.net/gamezen/getAddresses.php?id="+ id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject addresses = array.getJSONObject(i);

                        addressesList.add(new Addresses(

                                addresses.getInt("id"),
                                addresses.getString("address"),
                                addresses.getInt("civic"),
                                addresses.getString("city"),
                                addresses.getString("CAP"),
                                addresses.getString("province"),
                                addresses.getString("phone")
                        ));
                    }

                    addressesAdapter = new ViewAddressesAdapter(getActivity(), addressesList);
                    recyclerView.setAdapter(addressesAdapter);

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