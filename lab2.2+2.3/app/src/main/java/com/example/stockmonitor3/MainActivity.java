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
    Button button;
    ListView listView;
    EditText editText;
    EditText editText2;
    String stockID = "";
    String stockName = "";
    Toast AddID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.btnHit);
        listView = findViewById(R.id.user_list);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        AddID.makeText(MainActivity.this, "ADD VALUES", Toast.LENGTH_SHORT).show();



        RequestQueue requestQueue = Volley.newRequestQueue(this);


        //final String url = loadFromWeb("https://financialmodelingprep.com/api/company/price/AAPL,INTC,IBM,GOOGL,FB,NOK,RHT,MSFT,AMZN,BRK-B,BABA,JNJ,JPM,XOM,BAC,WMT,WFC,RDS-B,V,PG,BUD,T,TWX,CVX,UNH,PFE,CHL,HD,TSM,VZ,ORCL,C,NVS?datatype=json");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://financialmodelingprep.com/api/company/price/AAPL,INTC,IBM,GOOGL,FB,NOK,RHT,MSFT,AMZN,BRK-B,BABA,JNJ,JPM,XOM,BAC,WMT,WFC,RDS-B,V,PG,BUD,T,TWX,CVX,UNH,PFE,CHL,HD,TSM,VZ,ORCL,C,NVS?datatype=json",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final ArrayList<String> stockDatas = parseStockData(response);

                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, stockDatas);
                        listView.setAdapter(arrayAdapter);
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

        public String loadFromWeb(String urlString) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(connection.getInputStream());
                String htmlText = Utilities.fromStream(in);
                return htmlText;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
}
