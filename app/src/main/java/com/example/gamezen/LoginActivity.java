package com.example.gamezen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigInteger;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    //
    private static final String PREFS_NAME = "preferences";
    private static final String PREF_USERNAME = "Email";
    private static final String PREF_PASSWORD = "Password";

    private final String DefaultUnameValue = "";
    private String UnameValue;

    private final String DefaultPasswordValue = "";
    private String PasswordValue;

    private boolean saved;

    // Client instance
    Client client = new Client();

    // UI widgets
    EditText emaillog, passwordlog;
    TextView register;
    CheckBox remember;
    Button login;

    // AlterDialog
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // UI widgets
        emaillog = findViewById(R.id.emaillog);
        passwordlog = findViewById(R.id.passwordlog);
        remember = findViewById(R.id.remember);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        // AlertDialog builder
        dialog = new AlertDialog.Builder(LoginActivity.this, R.style.CustomAlertDialog);
        dialog.setCancelable(false);
        dialog.setTitle("Login fallito!");
        dialog.setIcon(R.drawable.ic_error);

        dialog.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        try {
            loadPreferences();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (compoundButton.isChecked()) {

                    saved = true;

                } else if (!compoundButton.isChecked()) {

                    saved = false;

                }
            }
        });

        // Login button listener
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    login();

                } catch (NoSuchAlgorithmException e) {

                    e.printStackTrace();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(openRegister);
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

    // Login function
    public void login() throws NoSuchAlgorithmException {

        // Get username and password
        UnameValue = emaillog.getText().toString();
        PasswordValue = passwordlog.getText().toString();

        if(UnameValue.length() == 0 || PasswordValue.length() == 0) {

            dialog.setMessage("Inserisci le credenziali per accedere.");

            dialog();

        } else {

            // MD5 Converter
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(PasswordValue.getBytes());
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
                        client.login(UnameValue, MD5password);

                        if (client.response.code() == 200) {

                            if (saved == true) {

                                SavedPreferences(UnameValue, PasswordValue);

                            } else {

                                SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                                settings.edit().clear().apply();

                            }

                            SaveUser(UnameValue, client.response.body().string());

                            Intent openHome = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(openHome);

                            finish();

                        } else if (client.response.code() == 401) {

                            dialog.setMessage("Le credenziali sono errate.");

                            dialog();

                        } else if (client.response.code() == 404) {

                            dialog.setMessage("L'utente non esiste.");

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

    private void SaveUser(String email, String id) {

        SharedPreferences settings = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("email", email);
        editor.putString("id", id);
        editor.apply();
    }

    private void SavedPreferences(String email, String password) {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = settings.edit();

        editor.putString(PREF_USERNAME, email);
        editor.putString(PREF_PASSWORD, password);
        editor.apply();
    }

    private void loadPreferences() throws NoSuchAlgorithmException {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        UnameValue = settings.getString(PREF_USERNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);

        if(UnameValue != "" || PasswordValue != "") {

            emaillog.setText(UnameValue);
            passwordlog.setText(PasswordValue);
            remember.setChecked(true);

            // MD5 Converter
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(PasswordValue.getBytes());
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
                        client.login(UnameValue, MD5password);

                        if (client.response.code() == 200) {

                            SaveUser(UnameValue, client.response.body().string());

                            Intent openHome = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(openHome);

                            finish();

                        } else if (client.response.code() == 401) {

                            dialog.setMessage("Le credenziali sono errate.");

                            dialog();

                        } else if (client.response.code() == 404) {

                            dialog.setMessage("L'utente non esiste.");

                            dialog();
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
}