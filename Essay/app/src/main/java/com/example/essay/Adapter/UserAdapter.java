package com.example.essay.Adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.essay.R;
import com.example.essay.UserFragment;

import com.example.essay.models.account.User;
import com.example.essay.services.AccountService;
import com.example.essay.services.ServiceCallback;
import com.example.essay.services.SvgLoader;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private String role;
    private Runnable refreshListener;
    private UserFragment.OnUserClickListener listener; // Listener cho sự kiện click


    public UserAdapter(List<User> userList, String role,  UserFragment.OnUserClickListener listener) {
        this.userList = userList;
        this.role = role;
        this.listener = listener;
    }

    public void setRefreshListener(Runnable listener){
        this.refreshListener = listener;
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
        holder.bind(user);
        holder.nameTextView.setText(user.getName());
        holder.txtRole.setText(user.getRole().toLowerCase() + " - " + user.getUser());
        holder.txtUser.setText(user.getUser());

        SvgLoader.loadSvgFromUrl(user.getLinkAvt(), holder.avtUser);

//        Glide.with(holder.itemView.getContext())
//                .load(user.getLinkAvt()) // Đường dẫn tới ảnh
//                .error(R.drawable.ic_person_foreground)
//                .into(holder.avtUser); // ImageButton bạn muốn thiết lập ảnh

        holder.setRefreshListener(refreshListener);
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if(id == R.id.btn_more)
                {

                    // Tạo PopupMenu tại vị trí của nút btn_more
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);

                    // Inflate menu từ file XML hoặc tạo các mục menu trực tiếp
                    popupMenu.getMenu().add("View");
                    popupMenu.getMenu().add("Edit");
                    popupMenu.getMenu().add("History");

                    // Thiết lập lắng nghe sự kiện khi người dùng chọn mục

                    popupMenu.setOnMenuItemClickListener(menuItem -> {

                        String title = menuItem.getTitle().toString();
                        switch (title) {
                            case "Edit":
                                // Thực hiện logic chỉnh sửa người dùng
                                Log.d("UserAdapter", "Edit user selected");
                                listener.onUserClick(user, "edit");
                                break;
                            case "View":
                                // Thực hiện logic xem chi tiết người dùng
                                Log.d("UserAdapter", "View details selected");
                                listener.onUserClick(user, "view");
                                break;
                            case "History":
                                listener.onUserClick(user, "history");
                                // Xử lý trường hợp không hợp lệ
                                break;

                        }

                        return true;
                    });

                    // Hiển thị PopupMenu
                    popupMenu.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       private ImageView avtUser;
        private TextView nameTextView;
        private TextView txtRole;
        private ImageButton btnDelete;
        private ImageButton btnMore;
        private TextView txtUser;
        private Runnable refreshListener;
        private boolean isAdmin = false;
        private User User;
        public void setRefreshListener(Runnable listener){
            this.refreshListener = listener;
        }

        public UserViewHolder(@NonNull View itemView, boolean isAdmin) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.txtName);
            txtRole = itemView.findViewById(R.id.txtRole);
            btnDelete = itemView.findViewById(R.id.btn_del);
            btnMore = itemView.findViewById(R.id.btn_more);
            txtUser = itemView.findViewById(R.id.txtUser);
            avtUser = itemView.findViewById(R.id.im_user);
            btnDelete.setOnClickListener(this);

            this.isAdmin = isAdmin;
            if(!this.isAdmin){
                btnDelete.setVisibility(View.INVISIBLE);
            }
        }

        public void bind(User user) {
            this.User = user;
        }


        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id == R.id.btn_del && isAdmin)
            {
                String user = txtUser.getText().toString();
                if(user.isEmpty())
                {
                    Toast.makeText(itemView.getContext(),
                            "Error!. Please try again", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Tạo AlertDialog xác nhận xóa user
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(itemView.getContext());
                deleteDialog.setTitle("Confirm Delete");
                deleteDialog.setMessage("Are you sure you want to delete '" + user + "' ?");

                // Nút Delete
                deleteDialog.setPositiveButton("Delete", (dialog, which) -> {
                    // Thực hiện hành động xóa user ở đây
                    AccountService accountService = new AccountService();
                    accountService.deleteAccount(user, new ServiceCallback() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                        }

                        @Override
                        public void onSuccess() {
                            Toast.makeText(itemView.getContext(),
                                    "Deleted '" + user +"' successfully", Toast.LENGTH_SHORT).show();
                            refreshListener.run();  // Gọi hàm refresh từ lớp cha
                        }

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(itemView.getContext(),
                                    e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResult(boolean exists) {

                        }
                    });


                });

                // Nút Cancel
                deleteDialog.setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });

                // Hiển thị AlertDialog xác nhận xóa
                deleteDialog.create().show();
            }
        }
    }
}