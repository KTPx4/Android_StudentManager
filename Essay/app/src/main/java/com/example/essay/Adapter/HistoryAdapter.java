package com.example.essay.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.essay.R;
import com.example.essay.models.account.User;
import com.example.essay.models.account.UserHistory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.text.ParseException;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
public class HistoryAdapter  extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    private List<UserHistory> userList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public HistoryAdapter(List<UserHistory> userList)
    {
        this.userList = userList;
    }
    // Phương thức sắp xếp theo ngày mới nhất
    public void sortByDate() {
        Collections.sort(userList, new Comparator<UserHistory>() {
            @Override
            public int compare(UserHistory u1, UserHistory u2) {
                try {
                    Date date1 = dateFormat.parse(u1.getDate());
                    Date date2 = dateFormat.parse(u2.getDate());
                    return date2.compareTo(date1); // Giảm dần (mới nhất lên đầu)
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        notifyDataSetChanged(); // Cập nhật lại giao diện sau khi sắp xếp
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        UserHistory user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView avtUser;
        private TextView txtName, txtPhone, txtRole, txtDate, txtUser;
        private UserHistory User;

        public void bind(UserHistory user) {
            this.User = user;
            txtUser.setText(user.getUser());
            txtName.setText(user.getName());
            txtPhone.setText(user.getPhone());
            txtRole.setText(user.getRole());
            txtDate.setText(user.getDate());

            Glide.with(itemView.getContext())
                    .load(user.getLinkAvt()) // Đường dẫn tới ảnh
                    .error(R.drawable.ic_person_foreground)
                    .into(avtUser); // ImageButton bạn muốn thiết lập ảnh

        }

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            avtUser = itemView.findViewById(R.id.history_im_user);
            txtName = itemView.findViewById(R.id.history_txtUser);
            txtPhone = itemView.findViewById(R.id.history_txtPhone);
            txtRole = itemView.findViewById(R.id.history_txtRole);
            txtDate = itemView.findViewById(R.id.history_txtTime);
            txtUser = itemView.findViewById(R.id.history_txtName);
        }
    }
}
