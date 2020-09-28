package com.example.interview;

        import androidx.appcompat.app.AppCompatActivity;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.Bundle;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonArrayRequest;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<DataModel> list;
    DataAdapter dataAdapter;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    ProgressDialog p;
    Button fetch, fetchRetro;
    JsonArrayRequest jsonArrayRequest;
    String url = "https://loopintechies.com/homeservice/android/index.php";
    String retroUrl = "https://loopintechies.com/homeservice/android/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        fetch = findViewById(R.id.fetchdata);
        fetchRetro = findViewById(R.id.fetchdataRetro);
        list = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDataVolley();
            }
        });
        fetchRetro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDataRetrofit();
            }
        });
    }

    public void fetchDataVolley(){
        list.clear();
        p = new ProgressDialog(MainActivity.this);
        p.setTitle("Loading...");
        p.show();
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                p.dismiss();
                Log.d("serviceres", "onResponse: "+ response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jsonObject = array.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String image = jsonObject.getString("image");

                        DataModel model = new DataModel(name, image, id);
                        list.add(model);
                    }
                    dataAdapter = new DataAdapter(list, MainActivity.this, new DataAdapter.ClickListener() {
                        @Override
                        public void onClick(DataModel item) {
                            Intent i = new Intent(MainActivity.this, ViewActivity.class);
                            i.putExtra("name", item.getName());
                            i.putExtra("image", item.getImage());
                            startActivity(i);
                        }
                    });
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setAdapter(dataAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                p.dismiss();
                Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("errorrrrr", "onErrorResponse: " +error.toString() );
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> stringHashMap = new HashMap<>();
                stringHashMap.put("API", "SERVICE");
                return stringHashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void fetchDataRetrofit(){
        list.clear();
        p = new ProgressDialog(MainActivity.this);
        p.setTitle("Loading...");
        p.show();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(retroUrl)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<PojoModel> call = apiInterface.getAllData("SERVICE");
        call.enqueue(new Callback<PojoModel>() {
            @Override
            public void onResponse(Call<PojoModel> call, retrofit2.Response<PojoModel> response) {
                if (response.isSuccessful()){
                    p.dismiss();
                    list = (ArrayList<DataModel>)response.body().getData();
                    dataAdapter = new DataAdapter(list, MainActivity.this, new DataAdapter.ClickListener() {
                        @Override
                        public void onClick(DataModel item) {
                            Intent i = new Intent(MainActivity.this, ViewActivity.class);
                            i.putExtra("name", item.getName());
                            i.putExtra("image", item.getImage());
                            startActivity(i);
                        }
                    });
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setAdapter(dataAdapter);
                }

            }

            @Override
            public void onFailure(Call<PojoModel> call, Throwable t) {
                p.dismiss();
                Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
