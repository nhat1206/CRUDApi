package com.example.apicrud;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private List<User> userList = new ArrayList<>();
    private UserAdapter userAdapter;
    private RecyclerView recyclerView;
    public static final String URL = "https://60c026edb8d36700175545a1.mockapi.io/api/users";
    private EditText nameEd,emailEd;
    private Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();
    }

    private void addEvents() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEd.getText().toString().trim();
                String email = emailEd.getText().toString().trim();
                User user = new User(name,email);
                postData(URL,user);

            }
        });
    }

    private void postData(String url, User user) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error by Post data!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Nullable

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("name",user.getName());
                map.put("email",user.getEmail());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addControls() {
        nameEd = findViewById(R.id.nam_ed);
        emailEd = findViewById(R.id.email_ed);

        saveBtn = findViewById(R.id.save_btn);

        loadDataFromApi(URL,userList);
        userAdapter = new UserAdapter(this);
        userAdapter.setData(userList);
        recyclerView = findViewById(R.id.user_rcv);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadDataFromApi(String url, List<User> userList) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0;i < response.length();i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        User user = new User();
                        user.setId(jsonObject.getInt("id"));
                        user.setName(jsonObject.getString("name"));
                        user.setEmail(jsonObject.getString("email"));
                        userList.add(user);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                userAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "That didn't work", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}