package com.example.stockmonitor3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {
    Button nappi;
    ListView stockList;
    EditText idEdit;
    EditText nameEdit;
    String stockIDString;
    double newStock;
    String stockNameString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nappi = findViewById(R.id.btnHit);
        stockList = findViewById(R.id.user_list);
        idEdit= findViewById(R.id.editText);
        nameEdit = findViewById(R.id.editText2);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://financialmodelingprep.com/api/company/price/AAPL,INTC,IBM,GOOGL,FB,NOK,RHT,MSFT,AMZN,BRK-B,BABA,JNJ,JPM,XOM,BAC,WMT,WFC,RDS-B,V,PG,BUD,T,TWX,CVX,UNH,PFE,CHL,HD,TSM,VZ,ORCL,C,NVS?datatype=json",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        final ArrayList<String> stockDatas = parseStockData(response);

                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, stockDatas);
                        stockList.setAdapter(arrayAdapter);
                        nappi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                stockNameString = nameEdit.getText().toString();
                                stockIDString = idEdit.getText().toString();
                                newStock = parseStockDataUser(response);
                                if (nameEdit.getText().toString().trim().equals("")) {
                                    Toast.makeText(MainActivity.this, "ADD STOCK NAME", Toast.LENGTH_SHORT).show();
                                } else if (idEdit.getText().toString().trim().equals("")) {
                                    Toast.makeText(MainActivity.this, "ADD STOCK ID", Toast.LENGTH_SHORT).show();
                                }


                                for (int i = 0; i < parseStockDataUser(response); i++) {
                                    if (idEdit.getText().toString().equals(stockIDString)) {
                                        Toast.makeText(MainActivity.this, "STOCK " + stockNameString + " ADDED SUCCESFULLY", Toast.LENGTH_SHORT).show();
                                        stockDatas.add(stockNameString + " " + newStock);
                                        break;
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "STOCK NOT FOUND", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error ["+error+"]");

            }
        });
        requestQueue.add(stringRequest);





    }




        private ArrayList<String> parseStockData(String response) {
            ArrayList<String> stockDatas = new ArrayList<>();
            try {

                JSONObject jsonObject = new JSONObject(response);
                Iterator<String> it = jsonObject.keys();
                int i = 0;
                while (it.hasNext()) {
                    String key = it.next();
                    JSONObject stock = jsonObject.getJSONObject(key);
                    double stockPrice = stock.getDouble("price");
                    stockDatas.add(" " + key + " " + stockPrice);
                    i++;
                    if(i > 6){
                        break;
                    }

                }
            } catch (Exception e){e.printStackTrace();} return stockDatas;
        }

    private double parseStockDataUser(String response){
        double newStock = 0;
        try{
            JSONObject jsonObject2 = new JSONObject(response);
            JSONObject added = jsonObject2.getJSONObject(stockIDString);
            newStock = added.getDouble("price");
        }   catch (JSONException e) {
            e.printStackTrace();
        }   return newStock;
    }

}
