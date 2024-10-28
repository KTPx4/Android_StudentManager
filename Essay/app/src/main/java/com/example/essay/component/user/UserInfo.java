package com.example.essay.component.user;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.essay.R;

import java.util.Calendar;

public class UserInfo extends AppCompatActivity implements View.OnClickListener {
    private static boolean isEdit = false;
    AutoCompleteTextView txtRole;
    Button btnSave, btnClose;
    ImageButton btnEditName;
    TextView txtErr, txtName, txtUser, txtPhone, txtBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.component_user_info);


        txtRole = findViewById(R.id.txt_info_role);
        btnSave = findViewById(R.id.btn_info_save);
        btnClose = findViewById(R.id.btn_info_close);
        btnEditName = findViewById(R.id.bnt_info_editName);
        txtErr = findViewById(R.id.txt_info_err);
        txtName = findViewById(R.id.txt_info_name);
        txtUser = findViewById(R.id.txt_info_user);
        txtPhone = findViewById(R.id.txt_info_phone);
        txtBirth = findViewById(R.id.txt_info_birthDay);

        Intent itent = getIntent();
        String type = itent.getStringExtra("type");
        if(type != null && type.equals("edit"))
        {
            isEdit = true;
            intiEdit(itent);
        }

        txtRole.setOnClickListener(this);
        txtBirth.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnEditName.setOnClickListener(this);

    }
    private void intiEdit(Intent itent)
    {

    }

    private boolean isCanSave()
    {
        if(txtName.getText().toString().isEmpty()) txtErr.setText("Please input Name");

        else if(txtUser.getText().toString().isEmpty()) txtErr.setText("Please input User");

        else if(txtPhone.getText().toString().isEmpty()) txtErr.setText("Please input Phone");

        else if(txtBirth.getText().toString().isEmpty()) txtErr.setText("Please choise Birth Day");

        else if( txtRole.getText().toString().isEmpty()) txtErr.setText("Please choise Role");

        else return true;

        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        txtErr.setText("");

        if (id == R.id.btn_info_close)
        {
            finish();
        }
        else if(id == R.id.btn_info_save)
        {
            if(isCanSave())
            {

            }
        }
        else if(id == R.id.bnt_info_editName)
        {
            // Tạo EditText
            final EditText nameEditText = new EditText(this);
            nameEditText.setHint("Enter your name");
            nameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            nameEditText.setMaxLines(1);

            // Bao quanh EditText bằng FrameLayout để thêm khoảng cách
            FrameLayout container = new FrameLayout(this);
            container.setPadding(15, 80, 10, 15);
            container.addView(nameEditText);

            // Tạo AlertDialog với FrameLayout chứa EditText
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setCancelable(false)
                    .setView(container) // Sử dụng container để chứa EditText
                    .setPositiveButton("OK", (dialog, which) -> {
                        String name = nameEditText.getText().toString().trim();
                        if (!name.isEmpty()) {
                            txtName.setText(name);
                        } else {
                            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    });


            // Hiển thị AlertDialog
            builder.create().show();


        }
        else if(id == R.id.txt_info_role)
        {
            // Khởi tạo mảng các lựa chọn
            String[] roles = {"Manager", "Employee"};
            // Tạo ArrayAdapter để gắn các lựa chọn vào AutoCompleteTextView
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, roles);
            txtRole.setAdapter(adapter);
            txtRole.showDropDown();
        }
        else if(id == R.id.txt_info_birthDay)
        {
            showDatePickerDialog();
        }
    }

    private void showDatePickerDialog() {
        // Lấy ngày hiện tại để đặt ngày mặc định cho DatePicker
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            // Cập nhật EditText với ngày đã chọn
            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            txtBirth.setText(selectedDate);
        }, 2003, 1, 1);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }
}