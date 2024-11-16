package com.example.essay.component.student;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.essay.R;
import com.example.essay.services.AccountService;
import com.example.essay.services.StudentCallback;
import com.example.essay.services.StudentService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CertificateInfo extends AppCompatActivity {
    private Button btnClose, btnSave;
    private EditText etCertificateName, etId;
    private TextView tvIssueDate, tvExpDate;

    private String certificateId;
    private String studentId;
    private String certificateName;
    private String issueDate;
    private String expDate;
    private  boolean isEdit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.component_certificate_info_2);

        // Ánh xạ các view
        btnClose = findViewById(R.id.btnCloseCertificate);
        btnSave = findViewById(R.id.btnSaveCertificate);
        etCertificateName = findViewById(R.id.etCertificateName);
        tvIssueDate = findViewById(R.id.tvIssueDate);
        tvExpDate = findViewById(R.id.tvExpDate);
        etId = findViewById(R.id.etCertificateId);

        // Gán DatePicker cho tvIssueDate và tvExpDate
        tvIssueDate.setOnClickListener(v -> showDatePicker(tvIssueDate));
        tvExpDate.setOnClickListener(v -> showDatePicker(tvExpDate));

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        studentId = intent.getStringExtra("studentId");

        if (type != null && type.equals("edit")) {
            isEdit = true;
            certificateId = intent.getStringExtra("id");
            etId.setText(certificateId);
            etId.setEnabled(false);
            certificateName = intent.getStringExtra("certificateName");
            issueDate = intent.getStringExtra("issueDate");
            expDate = intent.getStringExtra("expDate");

            // Gán dữ liệu vào các trường tương ứng
            etCertificateName.setText(certificateName);
            tvIssueDate.setText(issueDate);
            tvExpDate.setText(expDate);
        } else if (type != null && type.equals("create")) {
            isEdit = false;
            // Nếu là "create", để các giá trị mặc định là null hoặc rỗng
            certificateId = null;

            certificateName = "";
            issueDate = "";
            expDate = "";

            // Hiển thị các trường trống
            etCertificateName.setText("");
            tvIssueDate.setText("Select issue date");
            tvExpDate.setText("Select expiration date");
        } else {
            // Trường hợp không có type hoặc type không hợp lệ
            Toast.makeText(this, "Invalid type!", Toast.LENGTH_SHORT).show();
            finish(); // Đóng activity
        }

        // Xử lý sự kiện cho nút "Save"
        btnSave.setOnClickListener(v -> saveCertificate());

        // Xử lý sự kiện cho nút "Close"
        btnClose.setOnClickListener(v -> finish());
    }

    private void saveCertificate() {
        // Lấy dữ liệu từ các trường
        String name = etCertificateName.getText().toString();
        String issue = tvIssueDate.getText().toString();
        String exp = tvExpDate.getText().toString();
        String id = etId.getText().toString();
        // Kiểm tra dữ liệu hợp lệ
        if (name.isEmpty() || id.isEmpty() || issue.equals("Select issue date") || exp.equals("Select expiration date")) {
            Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        StudentService studentService = new StudentService();

        if(isEdit)
        {
            Log.d("CERTIFICATE", "editCertificate: " + studentId +" " + name +" " + issue  + " "+ exp);

            studentService.editCertificate(id, name, issue, exp, new StudentCallback(){

                @Override
                public void onSuccess() {
                    // Lưu dữ liệu (gửi về server hoặc database, tùy logic của bạn)
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("id", certificateId);
                    resultIntent.putExtra("studentId", studentId);
                    resultIntent.putExtra("certificateName", name);
                    resultIntent.putExtra("issueDate", issue);
                    resultIntent.putExtra("expDate", exp);
                    setResult(RESULT_OK, resultIntent);
                    Toast.makeText(getApplicationContext(), "Edit Success", Toast.LENGTH_SHORT).show();

                    finish();
                }

                @Override
                public void onFailure(String mess) {

                }
            });

        }
        else{
            Log.d("CERTIFICATE", "saveCertificate: " + studentId +" " + name +" " + issue  + " "+ exp);
            studentService.addCertificate(studentId,id, name, issue, exp, new StudentCallback() {
                @Override
                public void onSuccess() {
                    // Lưu dữ liệu (gửi về server hoặc database, tùy logic của bạn)
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("id", certificateId);
                    resultIntent.putExtra("studentId", studentId);
                    resultIntent.putExtra("certificateName", name);
                    resultIntent.putExtra("issueDate", issue);
                    resultIntent.putExtra("expDate", exp);
                    setResult(RESULT_OK, resultIntent);
                    Toast.makeText(getApplicationContext(), "Add Success", Toast.LENGTH_SHORT).show();

                    finish();
                }

                @Override
                public void onFailure(String mess) {
                    Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
                }
            });
        }




    }

    private void showDatePicker(TextView textView) {
        // Lấy ngày hiện tại
        final Calendar calendar = Calendar.getInstance();

        // Hiển thị DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    // Chuyển đổi ngày đã chọn thành định dạng MM/DD/YYYY
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                    String formattedDate = sdf.format(calendar.getTime());

                    // Gán ngày đã chọn vào TextView
                    textView.setText(formattedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

}
