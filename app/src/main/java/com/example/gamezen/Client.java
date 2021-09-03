package com.example.gamezen;

import android.util.Log;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Client {

    // JSON type
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // APIKEY
    final static String APIKEY = "3dc5ba8d30e7631037e34ccb53579db2";

    OkHttpClient client;
    Response response;

    //
    public Client () {}

    // Ping server
    public void ping () throws IOException {

        // HTTP Client creation
        client = new OkHttpClient();

        // HTTP Request creation
        Request request = new Request.Builder()
                .url("http://antonserver.ddns.net:8080/")
                .header("apikey", APIKEY)
                .build();

        // Send request
        response = client.newCall(request).execute();
    }

    // Login
    public void login(String email, String password) throws IOException {

        //
        String json = "{\"email\": \""+email+"\",\"password\": \""+password+"\"}";

        // HTTP Client creation
        client = new OkHttpClient();

        // HTTP body creation
        RequestBody body = RequestBody.create(json, JSON);

        // HTTP Request creation
        Request request = new Request.Builder()
                .url("http://antonserver.ddns.net/gamezen/login.php")
                .post(body)
                .build();

        // Send request
        response = client.newCall(request).execute();
    }

    // Login
    public void register(String name, String surname, String email, String date, String password) throws IOException {

        //
        String json = "{\"name\": \""+name+"\",\"surname\": \""+surname+"\",\"email\": \""+email+"\",\"date\": \""+date+"\",\"password\": \""+password+"\"}";

        // HTTP Client creation
        client = new OkHttpClient();

        // HTTP body creation
        RequestBody body = RequestBody.create(json, JSON);

        // HTTP Request creation
        Request request = new Request.Builder()
                .url("http://antonserver.ddns.net/gamezen/register.php")
                .post(body)
                .build();

        // Send request
        response = client.newCall(request).execute();
    }

    public void addProduct(String id, String code, String quantity) throws IOException {

        //
        String json = "{\"id\": \""+id+"\",\"code\": \""+code+"\",\"quantity\": \""+quantity+"\"}";

        // HTTP Client creation
        client = new OkHttpClient();

        // HTTP body creation
        RequestBody body = RequestBody.create(json, JSON);

        // HTTP Request creation
        Request request = new Request.Builder()
                .url("http://antonserver.ddns.net/gamezen/addCart.php")
                .post(body)
                .build();

        // Send request
        response = client.newCall(request).execute();
    }

    public void setQuantity(String id, String code, int quantity) throws IOException {

        //
        String json = "{\"id\": \""+id+"\",\"code\": \""+code+"\",\"quantity\": \""+quantity+"\"}";

        // HTTP Client creation
        client = new OkHttpClient();

        // HTTP body creation
        RequestBody body = RequestBody.create(json, JSON);

        // HTTP Request creation
        Request request = new Request.Builder()
                .url("http://antonserver.ddns.net/gamezen/setQuantity.php")
                .post(body)
                .build();

        // Send request
        response = client.newCall(request).execute();
    }

    public void deleteProduct(String id, String code) throws IOException {

        //
        String json = "{\"id\": \""+id+"\",\"code\": \""+code+"\"}";

        // HTTP Client creation
        client = new OkHttpClient();

        // HTTP body creation
        RequestBody body = RequestBody.create(json, JSON);

        // HTTP Request creation
        Request request = new Request.Builder()
                .url("http://antonserver.ddns.net/gamezen/deleteProduct.php")
                .post(body)
                .build();

        // Send request
        response = client.newCall(request).execute();
    }

    public void sendOrder(String username, String address, String total) throws IOException {

        //
        String json = "{\"username\": \""+username+"\",\"address\": \""+address+"\",\"total\": \""+total+"\"}";

        // HTTP Client creation
        client = new OkHttpClient();

        // HTTP body creation
        RequestBody body = RequestBody.create(json, JSON);

        // HTTP Request creation
        Request request = new Request.Builder()
                .url("http://antonserver.ddns.net/gamezen/order.php")
                .post(body)
                .build();

        // Send request
        response = client.newCall(request).execute();
    }

    public void addAddress(String id, String addressValue, String civicValue, String cityValue, String capValue, String provinceValue, String phoneValue) throws IOException {

        //
        String json = "{\"id\": \""+id+"\",\"address\": \""+addressValue+"\",\"civic\": \""+civicValue+"\",\"city\": \""+cityValue+"\",\"cap\": \""+capValue+"\",\"province\": \""+provinceValue+"\",\"phone\": \""+phoneValue+"\" }";

        Log.d("1", json);

        // HTTP Client creation
        client = new OkHttpClient();

        // HTTP body creation
        RequestBody body = RequestBody.create(json, JSON);

        // HTTP Request creation
        Request request = new Request.Builder()
                .url("http://antonserver.ddns.net/gamezen/addAddress.php")
                .post(body)
                .build();

        // Send request
        response = client.newCall(request).execute();

    }
}
