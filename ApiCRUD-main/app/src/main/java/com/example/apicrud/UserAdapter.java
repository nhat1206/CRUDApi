package com.example.apicrud;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    private Context mContext;
    private List<User> userList;

    public UserAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<User> userList){
        this.userList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User user = userList.get(position);
        if(user == null){
            return;
        }
        else
        {
            holder.nameTv.setText("Name: " + user.getName());
            holder.emailTv.setText("Email: " + user.getEmail());
        }
    }

    @Override
    public int getItemCount() {
        if(userList != null){
            return userList.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTv,emailTv;
        private ImageButton deleteBtn,editBtn;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.user_name_tv);
            emailTv = itemView.findViewById(R.id.user_email_ed);
            deleteBtn = itemView.findViewById(R.id.user_delete_btn);
            editBtn = itemView.findViewById(R.id.user_edit_btn);


            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = userList.get(getAdapterPosition());
                    int id = user.getId();

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    // Setting Alert Dialog Title
                    alertDialogBuilder.setTitle("Confirm Delete..!!!");
                    // Icon Of Alert Dialog

                    // Setting Alert Dialog Message
                    alertDialogBuilder.setMessage("Are you sure delete this product?");
                    alertDialogBuilder.setCancelable(false);

                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, MainActivity.URL +"/" +id,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Toast.makeText(mContext, "Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(mContext,mContext.getClass());
                                            mContext.startActivity(intent);
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(mContext, "Error by Post data!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                            requestQueue.add(stringRequest);
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                }
            });
        }
    }
}
