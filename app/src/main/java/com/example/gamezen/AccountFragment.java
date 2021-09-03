package com.example.gamezen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
import com.example.gamezen.adapter.FaqAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // Declaration of UI widgets variables

    Button logout, viewAddresses, viewOrders, faq;

    TextView username, userEmail, birth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // UI widgets declaration

        username = view.findViewById(R.id.username);
        userEmail = view.findViewById(R.id.delivery);
        birth = view.findViewById(R.id.birth);
        viewAddresses= view.findViewById(R.id.viewAddresses);
        viewOrders= view.findViewById(R.id.viewOrders);
        logout = view.findViewById(R.id.logout);
        faq = view.findViewById(R.id.faq);

        // Load user information
        loadUser();

        // Listener Orders button
        viewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewOrdersFragment nextFrag= new ViewOrdersFragment();

                (getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Listener Addresses button
        viewAddresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewAddressFragment nextFrag= new ViewAddressFragment();

                (getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FaqFragment nextFrag= new FaqFragment();

                (getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Listener Logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences settings = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
                settings.edit().clear().apply();

                SharedPreferences user = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                user.edit().clear().apply();

                Intent openLogin = new Intent(getActivity(), LoginActivity.class);
                startActivity(openLogin);

                getActivity().finish();
            }
        });

        return view;
    }

    public void loadUser() {

        SharedPreferences settings = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        String id = settings.getString("id", "");

        String URL = "http://antonserver.ddns.net/gamezen/user.php?id="+ id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONArray array = new JSONArray(response);

                    JSONObject user = array.getJSONObject(0);

                    username.setText(user.getString("name") + " " + user.getString("surname"));
                    userEmail.setText(user.getString("email"));
                    birth.setText(user.getString("birth"));

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