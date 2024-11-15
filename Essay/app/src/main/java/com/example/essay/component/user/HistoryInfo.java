package com.example.essay.component.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.essay.Adapter.HistoryAdapter;
import com.example.essay.Adapter.UserAdapter;
import com.example.essay.R;
import com.example.essay.UserFragment;
import com.example.essay.models.account.User;
import com.example.essay.models.account.UserHistory;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryInfo extends AppCompatActivity {
    private static String typeStart = "all";
    private EditText txtSearch;
    private RecyclerView rvHistory;
    private Button btnClose;
    private Handler searchHandler = new Handler();
    private Runnable searchRunnable;
    private String UserName = "";
    private ProgressBar loadingProcess;
    private final String COLECTION_NAME = "histories";
    private FirebaseFirestore db;
    public HistoryInfo()
    {
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.component_user_history);
        txtSearch = findViewById(R.id.txt_info_search);
        rvHistory = findViewById(R.id.rv_info_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        loadingProcess = findViewById(R.id.loading_info_history);
        btnClose = findViewById(R.id.btn_history_close);
        btnClose.setOnClickListener(v -> finish());

        inputSearchListen();

        Intent itent = getIntent();
        String type = itent.getStringExtra("type");

        if(type != null && type.equals("all"))
        {
            typeStart = "all";
        }
        else if(type != null && type.equals("only"))
        {
            typeStart = "only";
            UserName = itent.getStringExtra("user");
            txtSearch.setText(UserName);
        }

        intiStart(itent);
    }
    private void inputSearchListen()
    {
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Hủy tìm kiếm trước đó nếu người dùng tiếp tục nhập
                searchHandler.removeCallbacks(searchRunnable);


                // Tạo Runnable cho tìm kiếm
                searchRunnable = () -> {
                    // Thực hiện tìm kiếm với chuỗi nhập
                    String searchText = s.toString().trim();
                    loadingProcess.setVisibility(View.VISIBLE);
                    queryUserHistory(searchText, new QueryCallback() {
                        @Override
                        public void onQueryComplete(List<UserHistory> userHistories) {
                            loadingProcess.setVisibility(View.GONE);
                            HistoryAdapter historyAdapter = new HistoryAdapter(userHistories);
                            historyAdapter.sortByDate();
                            rvHistory.setAdapter(historyAdapter);
                            // Cập nhật adapter
                            historyAdapter.notifyDataSetChanged();
                        }
                    });


                };

                // Đặt độ trễ 300ms trước khi bắt đầu tìm kiếm
                searchHandler.postDelayed(searchRunnable, 600);
            }
        });

    }
    private void intiStart(Intent itent)
    {
        loadingProcess.setVisibility(View.VISIBLE);
        switch (typeStart)
        {
            case "all":
                break;

            case "only":
                break;

            default:
                break;
        }

        queryUserHistory(UserName, new QueryCallback() {
            @Override
            public void onQueryComplete(List<UserHistory> userHistories) {
                loadingProcess.setVisibility(View.GONE);
                HistoryAdapter historyAdapter = new HistoryAdapter(userHistories);
                historyAdapter.sortByDate();
                rvHistory.setAdapter(historyAdapter);
            }
        });
    }

    public void queryUserHistory(String search, QueryCallback callback) {
        List<UserHistory> result = new ArrayList<>();

        db.collection("histories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot historyDocs = task.getResult();
                        int totalHistories = historyDocs.size();

                        // Nếu không có lịch sử nào, gọi callback ngay lập tức
                        if (totalHistories == 0) {
                            callback.onQueryComplete(result);
                            return;
                        }

                        // Đếm số lượng truy vấn còn lại
                        int[] pendingTasks = {totalHistories};

                        for (QueryDocumentSnapshot historyDoc : historyDocs) {
                            String historyUser = historyDoc.getString("user");
                            String historyDate = historyDoc.getString("date");

                            db.collection("accounts")
                                    .whereEqualTo("user", historyUser)
                                    .get()
                                    .addOnCompleteListener(accountTask -> {
                                        if (accountTask.isSuccessful()) {
                                            for (QueryDocumentSnapshot accountDoc : accountTask.getResult()) {
                                                String name = accountDoc.getString("name");
                                                String phone = accountDoc.getString("phone");
                                                String role = accountDoc.getString("role");
                                                String linkAvt = accountDoc.getString("linkAvt");
                                                String user = accountDoc.getString("user");

                                                boolean matches = true;

                                                // Kiểm tra điều kiện tìm kiếm nếu có
                                                if (search != null && !search.isEmpty()) {
                                                    matches = (name != null && name.contains(search)) ||
                                                            (phone != null && phone.contains(search)) ||
                                                            (user != null && user.contains(search)) ||
                                                            (historyDate != null && historyDate.contains(search));
                                                }

                                                // Nếu khớp điều kiện, thêm vào kết quả
                                                if (matches) {
                                                    result.add(new UserHistory(name, historyUser, phone, role, linkAvt, historyDate));
                                                }
                                            }
                                        }

                                        // Giảm đếm số lượng yêu cầu còn lại
                                        pendingTasks[0]--;

                                        // Khi tất cả các yêu cầu đã hoàn tất, gọi callback với dữ liệu kết quả
                                        if (pendingTasks[0] == 0) {
                                            callback.onQueryComplete(result);
                                        }
                                    });
                        }
                    }
                });
    }

    // Interface để trả kết quả truy vấn khi hoàn tất
    public interface QueryCallback {
        void onQueryComplete(List<UserHistory> userHistories);
    }

}