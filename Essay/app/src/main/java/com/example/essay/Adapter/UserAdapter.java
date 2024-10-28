package com.example.essay.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.essay.R;
import com.example.essay.models.account.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private String role;

    public UserAdapter(List<User> userList, String role) {
        this.userList = userList;
        this.role = role;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        Boolean isAdmin = role.toLowerCase().equals("admin");
        return new UserViewHolder(view, isAdmin);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameTextView.setText(user.getName());
        holder.txtRole.setText(user.getRole() + " - " + user.getUser());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nameTextView;
        private TextView txtRole;
        private ImageButton btnDelete;
        private ImageButton btnMore;

        public UserViewHolder(@NonNull View itemView, boolean isAdmin) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.txtName);
            txtRole = itemView.findViewById(R.id.txtRole);
            btnDelete = itemView.findViewById(R.id.btn_del);
            btnMore = itemView.findViewById(R.id.btn_more);
            btnDelete.setOnClickListener(this);
            btnMore.setOnClickListener(this);
            if(!isAdmin){
                btnDelete.setVisibility(View.INVISIBLE);
            }
        }

        public void bind(User user) {
            nameTextView.setText(user.getName());
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id == R.id.btn_more)
            {
                Log.d("UserAdapter", "onClick: btn more - "+ nameTextView.getText().toString());
            }
            else if(id == R.id.btn_del)
            {
                Log.d("UserAdapter", "onClick: btn delete - " + nameTextView.getText().toString());

            }
        }
    }
}