package com.example.gamezen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    AlertDialog.Builder dialog;

    Client client = new Client();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Dialog message setup
        dialog = new AlertDialog.Builder(MainActivity.this, R.style.CustomAlertDialog);
        dialog.setCancelable(false);
        dialog.setTitle("Errore");
        dialog.setIcon(R.drawable.ic_error);

        // Dialog button
        dialog.setNegativeButton("Riprova",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ping();
                    }
                });

        // Ping
        ping();
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

    // Ping server
    public void ping() {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try
                {
                    // Ping request
                    client.ping();

                    if(client.response.code() == 200) {

                        // Open LoginActivity
                        Intent openHome = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(openHome);

                        finish();
                    }
                    else {

                        dialog.setMessage("Il servizio non è disponibile.");
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