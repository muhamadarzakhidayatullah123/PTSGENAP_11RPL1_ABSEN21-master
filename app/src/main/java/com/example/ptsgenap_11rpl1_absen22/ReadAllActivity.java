package com.example.ptsgenap_11rpl1_absen22;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReadAllActivity extends AppCompatActivity {
    //inisialisasi variabel
    private static final String TAG = "ReadAllActivity";
    private List<DataBarang> dataBarangs;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_all_activity);

        recyclerView = findViewById(R.id.recyclerReadAllData); //findId recyclerView yg ada pada activity_read_all.xml

        recyclerView.setHasFixedSize(true); //agar recyclerView tergambar lebih cepat
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //menset layout manager sebagai LinearLayout(scroll kebawah)
        dataBarangs = new ArrayList<>(); //arraylist untuk menyimpan data mahasiswa
        AndroidNetworking.initialize(getApplicationContext()); //inisialisasi FAN
        getData(); // pemanggilan fungsi get data
    }

    public void getData(){
        //koneksi ke file read_all.php, jika menggunakan localhost gunakan ip sesuai dengan ip kamu
        AndroidNetworking.get("http://192.168.43.145/PTSGENAP_11RPL1_ABSEN22/crud/read_all.php")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse: " + response); //untuk log pada onresponse
                        // do anything with response
                        {
                            //mengambil data dari JSON array pada read_all.php
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject data = response.getJSONObject(i);
                                    //adding the product to product list
                                    DataBarang item = new DataBarang();
                                    item.setIdBarang(data.getString("b_id"));
                                    item.setKodeBarang(data.getString("b_kode"));
                                    item.setNamaBarang(data.getString("b_nama"));
                                    item.setJenisBarang(data.getString("b_jenis"));

                                    dataBarangs.add(item);
                                }
                                //men inisialisasi adapter RecyclerView yang sudah kita buat sebelumnya
                                ListBarangAdapter adapter = new ListBarangAdapter(ReadAllActivity.this, dataBarangs , ReadAllActivity.this);
                                recyclerView.setAdapter(adapter); //menset adapter yang akan digunakan pada recyclerView
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } @Override
                    public void onError(ANError error) {
                        Log.d(TAG, "onError: " + error); //untuk log pada onerror
                        // handle error
                    }
                });
    }

}