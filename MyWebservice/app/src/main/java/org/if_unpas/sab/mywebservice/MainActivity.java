package org.if_unpas.sab.mywebservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listAkademik;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listAkademik = (RecyclerView) findViewById(R.id.listAkademik);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        listAkademik.setLayoutManager(linearLayoutManager);
        new GetAkademikAsyncTask().execute();

    }

    private class GetAkademikAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground (String... params){
            String respon = "";
            try {
                String url =
                        "http://sab.if-unpas.org/pertemuan_07/akademik.php?action=get_akademik";
//                respon = CustomeHttp.executeHttpGet(url);
            } catch (Exception e) {
                respon = e.toString();
            }
            return respon;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            progressDialog.dismiss();
            try {
                Log.e("Masuk", "Respon Unit ->" + result) ;
                JSONObject jsonObject = new JSONObject(result);
                ArrayList<HashMap<String, String>> arr = new ArrayList<>();
                if(jsonObject.getString("success").equalsIgnoreCase("1")){
                    JSONArray array = jsonObject.getJSONArray("data");
                    HashMap<String, String> map;
                    for (int i = 0; 1 < array.length(); i++) {
                        JSONObject jsonObject1 = array.getJSONObject(i);
                        map = new HashMap<String, String>();
                        map.put("img_url", jsonObject1.getString("img_url"));
                        map.put("singkatan", jsonObject1.getString("singkatan"));
                        map.put("nama", jsonObject1.getString("nama"));
                        map.put("url", jsonObject1.getString("url"));
                    }
                }
                listAkademik.setAdapter(new AkademikAdapter(arr,
                        MainActivity.this));
            } catch (Exception e) {
                Log.e("masuk", "->" + e.getMessage());
            }
        }
    }
}

