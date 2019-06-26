package com.example.puskesmassumbersari;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.puskesmassumbersari.config.Server;
import com.example.puskesmassumbersari.controllers.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AntreanAndaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    //URL REST API Antrian
    public static final String URL = Server.URL + "api/app_antrian_anda/index_post";

    // manajemen session untuk log in, log out dan manajemen data pasien
    SessionManager SessionManager;

    // deklarasi variabel
    ListView listViewAntreanAnda;
    private List<AntreanAndaItem> antreanAndaItemList;

    String index_pasien;

    // variabel handler dan refresh untuk menangani refresh nomor antrean
    Handler handler = new Handler();
    Runnable refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antrian_anda);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout_antrian_anda);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        SessionManager = new SessionManager(getApplicationContext());
        SessionManager.checkLogin();

        //menampilkan user data
        HashMap<String, String> user = SessionManager.getUserDetails();
        index_pasien = user.get(SessionManager.KEY_INDEX_PASIEN);

        // refresh activity
        refresh = new Runnable() {
            @Override
            public void run() {
                // inisiasi nomor antrean
                listViewAntreanAnda = findViewById(R.id.ViewAntrianAnda);
                antreanAndaItemList = new ArrayList<>();

                // tampilkan nomor antrean
                loadAntreanAnda(index_pasien);
                handler.postDelayed(refresh, 5000);
            }
        };

        handler.post(refresh);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_antrian_anda);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.logout:
                SessionManager.logoutUser();
                break;
            case R.id.antrian_anda:
                startActivity(new Intent(this, AntreanAndaActivity.class));
                break;
            case R.id.profil:
                startActivity(new Intent(this, ProfilActivity.class));
                break;
            case R.id.antrian:
                startActivity(new Intent(this, MainActivity.class));
                break;

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_antrian_anda);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadAntreanAnda(final String index_pasien) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Ambil data JSON
                    JSONObject object = new JSONObject(response);
                    JSONArray antreanArray = object.getJSONArray("result");
                    JSONObject antreanObject = antreanArray.getJSONObject(0);
                    AntreanAndaItem antreanAndaItem = new AntreanAndaItem(
                            antreanObject.getString("running_nomor"),
                            antreanObject.getString("last_nomor"),
                            antreanObject.getString("anda_nomor"),
                            antreanObject.getString("anda_poli"),
                            antreanObject.getString("anda_status"),
                            antreanObject.getString("antrian_tanggal")
                    );

                    antreanAndaItemList.add(antreanAndaItem);

                    android.widget.ListAdapter adapter = new com.example.puskesmassumbersari.ListAdapterAntrianAnda(antreanAndaItemList, getApplicationContext());
                    listViewAntreanAnda.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            // Mengirim data yang sudah diringkas dengan teknik Hashing dengan method post
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("session_index", index_pasien);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
