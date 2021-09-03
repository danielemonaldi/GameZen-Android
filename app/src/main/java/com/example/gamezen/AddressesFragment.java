package com.example.gamezen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Vibrator;
import android.util.Log;
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
import com.example.gamezen.adapter.CartAdapter;
import com.example.gamezen.adapter.CategoriesAdapter;
import com.example.gamezen.adapter.ProductsAdapter;
import com.example.gamezen.classes.Addresses;
import com.example.gamezen.classes.Categories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddressesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressesFragment extends Fragment {

    List<Addresses> addressesList;

    RecyclerView recyclerView;

    AddressesAdapter addressesAdapter;

    Button confirm, addAddress, cancel;

    AlertDialog.Builder dialog;

    Client client = new Client();

    String total, username;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddressesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddressesFragment newInstance(String param1, String param2) {
        AddressesFragment fragment = new AddressesFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_addresses, container, false);

        Bundle bundle = this.getArguments();
        total = bundle.getString("total");

        // AlertDialog builder
        dialog = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        dialog.setCancelable(false);
        dialog.setTitle("Attenzione!");
        dialog.setMessage("Devi selezionare l'indirizzo di spedizione.");
        dialog.setIcon(R.drawable.ic_error);

        dialog.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        confirm = view.findViewById(R.id.confirm);
        addAddress = view.findViewById(R.id.addAddress);
        cancel = view.findViewById(R.id.cancel);

        addressesList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.addressesRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().popBackStack();
            }
        });

        loadAddresses();

        addressesAdapter = new AddressesAdapter(getActivity(), addressesList);
        recyclerView.setAdapter(addressesAdapter);

        return view;
    }

    // Dialog show
    public void dialog() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                dialog.show();
            }
        });
    }

    private void loadAddresses() {

        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

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

                    addressesAdapter = new AddressesAdapter(getActivity(), addressesList);
                    recyclerView.setAdapter(addressesAdapter);

                    addressesAdapter.setOnItemClickListener(new AddressesAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {

                            addressesList.get(position);
                        }
                    });

                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            vibrator.vibrate(80);

                            if(addressesAdapter.selectedAddress == "") {

                                dialog();

                            } else {

                                sendOrder();

                            }

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

    public void sendOrder() {

        SharedPreferences settings = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        username = settings.getString("id", "");

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    client.sendOrder(username, addressesAdapter.selectedId, total);

                    Log.d("1", username);
                    Log.d("1", addressesAdapter.selectedId);
                    Log.d("1", total);

                    if (client.response.code() == 200) {

                        ConfirmFragment nextFrag= new ConfirmFragment();

                        (getActivity()).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                                .addToBackStack(null)
                                .commit();

                    }

                } catch (UnknownHostException e) {

                    dialog.setMessage("Il server non risponde!");
                    dialog();

                } catch (SocketTimeoutException e) {

                    dialog.setMessage("Il server non risponde!");
                    dialog();

                } catch (Exception e) {

                    dialog.setMessage("Il server non risponde!");
                    dialog();
                }

            }
        });

        thread.start();
    }

}