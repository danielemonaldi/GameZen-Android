package com.example.gamezen;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformationFragment extends Fragment {

    Client client = new Client();

    ImageView image;
    Button add, increment, decrement;
    TextView productName, productAvailable, productPrice, description, productCode, producer, productCategory, language, year, quantity;

    JSONObject products;

    // AlterDialog
    AlertDialog.Builder dialog;
    Dialog dialog2;

    int code;
    int count = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment informationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InformationFragment newInstance(String param1, String param2) {
        InformationFragment fragment = new InformationFragment();
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
        View view = inflater.inflate(R.layout.fragment_information, container, false);

        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        Bundle bundle = this.getArguments();
        code = bundle.getInt("code");

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

        dialog2 = new Dialog(getActivity());

        image = view.findViewById(R.id.productImage);
        productName = view.findViewById(R.id.ProductName);
        productAvailable = view.findViewById(R.id.productAvailable);
        productPrice = view.findViewById(R.id.productPrice);
        productCode = view.findViewById(R.id.productCode);
        producer = view.findViewById(R.id.producer);
        description = view.findViewById(R.id.description);
        productCategory = view.findViewById(R.id.productCategory);
        language = view.findViewById(R.id.language);
        year = view.findViewById(R.id.year);
        quantity = view.findViewById(R.id.quantity);

        add = view.findViewById(R.id.returnHome);
        increment = view.findViewById(R.id.cartIncrement);
        decrement = view.findViewById(R.id.cartDecrement);

        increment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                count++;
                quantity.setText("" + count);
            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(count <= 1) {

                    count = 1;

                } else {

                    count--;
                }

                quantity.setText("" + count);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(80);
                addProduct();
            }
        });

        loadInformaion();

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

    public void dialog2() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                dialog2.setContentView(R.layout.dialog);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.show();

                final Timer t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        dialog2.cancel();
                        t.cancel();
                    }
                }, 1000);
            }
        });
    }

    private void loadInformaion() {

        String URL = "http://antonserver.ddns.net/gamezen/information.php?code=" + code;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONArray array = new JSONArray(response);

                    products = array.getJSONObject(0);

                    Glide.with(getActivity()).load(products.getString("image")).into(image);
                    productName.setText(products.getString("name"));

                    if (products.getInt("available") == 1) {

                        productAvailable.setText("Disponibile");
                        productAvailable.setTextColor(Color.GREEN);

                    } else {

                        productAvailable.setText("Non disponibile");
                        productAvailable.setTextColor(Color.RED);
                        add.setEnabled(false);
                        increment.setEnabled(false);
                        decrement.setEnabled(false);
                    }

                    float price = (float)products.getInt("price")/100;

                    productPrice.setText("€ " + price);
                    description.setText(products.getString("description"));
                    productCode.setText(HtmlCompat.fromHtml("<b>Codice: </b>" + products.getString("code"), HtmlCompat.FROM_HTML_MODE_LEGACY));
                    producer.setText(HtmlCompat.fromHtml("<b>Produttore: </b>" + products.getString("producer"), HtmlCompat.FROM_HTML_MODE_LEGACY));
                    productCategory.setText(HtmlCompat.fromHtml("<b>Categoria: </b>" + products.getString("category"), HtmlCompat.FROM_HTML_MODE_LEGACY));
                    language.setText(HtmlCompat.fromHtml("<b>Lingua: </b>" + products.getString("language"), HtmlCompat.FROM_HTML_MODE_LEGACY));
                    year.setText(HtmlCompat.fromHtml("<b>Anno: </b>" + products.getString("year"), HtmlCompat.FROM_HTML_MODE_LEGACY));

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

    public void addProduct() {

        SharedPreferences settings = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        String id = settings.getString("id", "");

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    client.addProduct(id, products.getString("code"), quantity.getText().toString());

                    if(client.response.code() == 200) {

                        dialog2();

                    } else if (client.response.code() == 401) {

                        dialog.setMessage("Il prodotto è già presente nel carrello.");

                        dialog();
                    }

                } catch (UnknownHostException | JSONException e) {

                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}