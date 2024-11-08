package com.example.essay.component.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.essay.R;

public class HistoryInfo extends AppCompatActivity {
    private static String typeStart = "all";
    private EditText txtSearch;
    private RecyclerView rvHistory;
    private Handler searchHandler = new Handler();
    private Runnable searchRunnable;
    private String UserName;
    private ProgressBar loadingProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.component_user_history);
        txtSearch = findViewById(R.id.txt_info_search);
        rvHistory = findViewById(R.id.rv_info_history);
        loadingProcess = findViewById(R.id.loading_info_history);

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



                };

                // Đặt độ trễ 300ms trước khi bắt đầu tìm kiếm
                searchHandler.postDelayed(searchRunnable, 600);
            }
        });

        Intent itent = getIntent();
        String type = itent.getStringExtra("type");

        if(type != null && type.equals("all"))
        {
            typeStart = "all";
        }
        else if(type != null && type.equals("only"))
        {
            typeStart = "only";
            UserName= itent.getStringExtra("user");
        }

        intiStart(itent);
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
    }



}