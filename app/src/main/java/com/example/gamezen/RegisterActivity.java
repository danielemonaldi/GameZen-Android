package com.example.gamezen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigInteger;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {

    // Client instance
    Client client = new Client();

    TextView name, surname, email, date, password, password2;
    String nameVal, surnameVal, emailVal, dateVal, passwordVal, password2Val;
    Button registerButton;

    // AlterDialog
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // UI widgets
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        email = findViewById(R.id.email);
        date = findViewById(R.id.date);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        registerButton = findViewById(R.id.registerButton);

        // AlertDialog builder
        dialog = new AlertDialog.Builder(RegisterActivity.this, R.style.CustomAlertDialog);
        dialog.setCancelable(false);
        dialog.setTitle("Registrazione fallita!");
        dialog.setIcon(R.drawable.ic_error);

        dialog.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Login button listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    register();

                } catch (NoSuchAlgorithmException e) {

                    e.printStackTrace();
                }
            }
        });
    }

    // Dialog show
    public void dialog() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                dialog.show();
            }
        });
    }

    public void register() throws NoSuchAlgorithmException {

        // Get data
        nameVal = name.getText().toString();
        surnameVal = surname.getText().toString();
        emailVal = email.getText().toString();
        dateVal = date.getText().toString();
        passwordVal = password.getText().toString();
        password2Val = password2.getText().toString();

        if(nameVal.length() == 0 || surnameVal.length() == 0 || emailVal.length() == 0 || dateVal.length() == 0 || passwordVal.length() == 0 || password2Val.length() == 0) {

            dialog.setMessage("Inserisci le credenziali per registrarti.");

            dialog();

        } else if(!passwordVal.equals(password2Val)) {

            dialog.setMessage("Le due password non corrispondono.");

            dialog();

        } else {

            // MD5 Converter
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(passwordVal.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(16);

            while (hashtext.length() < 32) {

                hashtext = "0" + hashtext;
            }

            String MD5password = hashtext;

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {

                    try {
                        client.register(nameVal, surnameVal, emailVal, dateVal, MD5password);

                        if (client.response.code() == 200) {

                            Intent openLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(openLogin);

                            finish();

                        } else if (client.response.code() == 401) {

                            dialog.setMessage("L'email è già stata utilizzata.");

                            dialog();

                        }

                    } catch (UnknownHostException e) {

                        dialog.setMessage("Il servizio non è disponibile.");
                        dialog();

                    } catch (SocketTimeoutException e) {

                        dialog.setMessage("Il servizio non è disponibile.");
                        dialog();

                    } catch (Exception e) {

                        dialog.setMessage("Il servizio non è disponibile.");
                        dialog();
                    }

                }
            });

            thread.start();
        }
    }
}