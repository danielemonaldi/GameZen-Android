package com.example.gamezen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewAddressFragment extends Fragment {

    Client client = new Client();

    EditText address, civic, city, cap, province, phone;

    Button confirm, cancel;

    // AlterDialog
    AlertDialog.Builder dialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewAddressFragment newInstance(String param1, String param2) {
        NewAddressFragment fragment = new NewAddressFragment();
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
        View view = inflater.inflate(R.layout.fragment_new_address, container, false);

        // AlertDialog builder
        dialog = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        dialog.setCancelable(false);
        dialog.setTitle("Attenzione!");
        dialog.setIcon(R.drawable.ic_error);

        dialog.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        address = view.findViewById(R.id.address);
        civic = view.findViewById(R.id.civic);
        city = view.findViewById(R.id.city);
        cap = view.findViewById(R.id.cap);
        province = view.findViewById(R.id.province);
        phone = view.findViewById(R.id.phone);
        confirm = view.findViewById(R.id.confirm);
        cancel = view.findViewById(R.id.cancel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAddress();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().popBackStack();
            }
        });

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

    public void newAddress() {

        SharedPreferences settings = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        String id = settings.getString("id", "");

        String addressValue = address.getText().toString();
        String civicValue = civic.getText().toString();
        String cityValue = city.getText().toString();
        String capValue = cap.getText().toString();
        String provinceValue = province.getText().toString();
        String phoneValue = phone.getText().toString();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    if(addressValue.length() == 0 || civicValue.length() == 0 || cityValue.length() == 0 || capValue.length() == 0 || provinceValue.length() == 0 || phoneValue.length() == 0) {

                        dialog.setMessage("Inserisci i campi richiesti.");

                        dialog();

                    } else {

                        client.addAddress(id, addressValue, civicValue, cityValue, capValue, provinceValue, phoneValue);

                        if (client.response.code() == 200) {

                            getFragmentManager().popBackStack();

                        }

                    }

                } catch (UnknownHostException e) {

                    e.printStackTrace();

                } catch (SocketTimeoutException e) {

                    e.printStackTrace();

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        });

        thread.start();
    }
}