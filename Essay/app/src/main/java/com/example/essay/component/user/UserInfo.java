package com.example.essay.component.user;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.essay.R;
import com.example.essay.services.AccountService;
import com.example.essay.services.ServiceCallback;
import com.example.essay.services.SvgLoader;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;

public class UserInfo extends AppCompatActivity implements View.OnClickListener {
    private static String typeStart = "create";
    AutoCompleteTextView txtRole;
    Button btnSave, btnClose;
    ImageButton btnEditName;
    TextView txtErr, txtName;
    EditText  txtUser, txtPhone, txtBirth, txtEmail;
    ImageView btnAvt;
    private static String selectAVT = "https://api.dicebear.com/9.x/adventurer/svg?seed=Brian";

    private String[] imageUrls = {
            "https://api.dicebear.com/9.x/adventurer/svg?seed=Brian",
            "https://api.dicebear.com/9.x/adventurer/svg?seed=Ryan",
            "https://api.dicebear.com/9.x/adventurer/svg?seed=Emery",
            "https://api.dicebear.com/9.x/adventurer/svg?seed=Easton",
            "https://api.dicebear.com/9.x/adventurer/svg?seed=Oliver",
            "https://api.dicebear.com/9.x/adventurer/svg?seed=Sawyer",
            "https://api.dicebear.com/9.x/adventurer/svg?seed=Avery",
            "https://api.dicebear.com/9.x/adventurer/svg?seed=Aiden",
            "https://api.dicebear.com/9.x/avataaars-neutral/svg?seed=Sadie",
            "https://api.dicebear.com/9.x/avataaars-neutral/svg?seed=Eliza",
            "https://api.dicebear.com/9.x/avataaars-neutral/svg?seed=Ryan",
            "https://api.dicebear.com/9.x/avataaars-neutral/svg?seed=Luis",
            "https://api.dicebear.com/9.x/fun-emoji/svg?seed=Sadie",
            "https://api.dicebear.com/9.x/fun-emoji/svg?seed=Jack",
            "https://api.dicebear.com/9.x/fun-emoji/svg?seed=Kimberly",
            "https://api.dicebear.com/9.x/fun-emoji/svg?seed=Brooklynn"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.component_user_info);
        btnAvt = findViewById(R.id.avt_info_user);
        txtRole = findViewById(R.id.txt_info_role);
        btnSave = findViewById(R.id.btn_info_save);
        btnClose = findViewById(R.id.btn_info_close);
        btnEditName = findViewById(R.id.bnt_info_editName);
        txtErr = findViewById(R.id.txt_info_err);
        txtName = findViewById(R.id.txt_info_name);
        txtUser = findViewById(R.id.txt_info_user);
        txtPhone = findViewById(R.id.txt_info_phone);
        txtBirth = findViewById(R.id.txt_info_birthDay);
        txtEmail = findViewById(R.id.txt_info_email);

        btnAvt.setOnClickListener(this);
        txtRole.setOnClickListener(this);
        txtBirth.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnEditName.setOnClickListener(this);

        Intent itent = getIntent();
        String type = itent.getStringExtra("type");

        if(type != null && type.equals("edit"))
        {
            typeStart = "edit";
        }
        else if(type != null && type.equals("view"))
        {
            typeStart = "view";
        }
        else if(type != null && type.equals("profile"))
        {
            typeStart ="profile";
        }
        else typeStart = "create";

        intiStart(itent);
    }
    private void setAvt(String linkAvt)
    {
        SvgLoader.loadSvgFromUrl(linkAvt, btnAvt);
    }
    private void intiStart(Intent itent)
    {
        if(typeStart.equals("create")) return;

        txtErr.setText(""); ;
        txtRole.setText(itent.getStringExtra("role"));
        txtName.setText(itent.getStringExtra("name"))  ;
        txtUser.setText(itent.getStringExtra("user")) ;
        txtPhone.setText(itent.getStringExtra("phone"));
        txtBirth.setText(itent.getStringExtra("birth"));
        txtEmail.setText(itent.getStringExtra("email"));

        setAvt(itent.getStringExtra("linkAvt"));

        switch (typeStart)
        {
            case "edit":
                txtUser.setEnabled(false);
                break;

            case "view":
                btnEditName.setVisibility(View.INVISIBLE);
                btnSave.setVisibility(View.INVISIBLE);
                txtRole.setEnabled(false);
                txtErr.setText(""); ;
                txtName.setEnabled(false);
                txtUser.setEnabled(false);
                txtPhone.setEnabled(false);
                txtBirth.setEnabled(false);
                txtEmail.setEnabled(false);
                break;

            case "profile":
                txtUser.setEnabled(false);
                txtRole.setEnabled(false);

            default:
                break;
        }
    }

    private boolean isCanSave()
    {
        if(txtName.getText().toString().isEmpty()) txtErr.setText("Please input name");

        else if(txtUser.getText().toString().isEmpty()) txtErr.setText("Please input user");

        else if(txtPhone.getText().toString().isEmpty()) txtErr.setText("Please input phone");

        else if(txtBirth.getText().toString().isEmpty()) txtErr.setText("Please choose birthday");

        else if( txtRole.getText().toString().isEmpty()) txtErr.setText("Please choose role");
        else if( txtEmail.getText().toString().isEmpty()) txtErr.setText("Please input email");
        else if(!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches())txtErr.setText("Email not invalid");

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
        else if(id == R.id.btn_info_save && typeStart.equals("create"))
        {
            if(isCanSave())
            {
                String user = txtUser.getText().toString();
                String pass = user;
                String name = txtName.getText().toString();
                String phone = txtPhone.getText().toString();
                String birth = txtBirth.getText().toString();
                String role = txtRole.getText().toString();
                String email = txtEmail.getText().toString();

                AccountService userService = new AccountService();
                userService.createUser(user, pass, name, phone, birth, role, email, selectAVT, new ServiceCallback() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Show success message
                  //      Log.d("UserService", "User created with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(),
                                "Create user success", Toast.LENGTH_SHORT).show();

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("success", "ok"); // Thêm dữ liệu cần trả về
                        setResult(RESULT_OK, resultIntent); // Đặt mã kết quả và Intent
                        finish(); // Kết thúc Activity B
                    }

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Show error message
                   //     Log.e("UserService", "Error creating user", e);
                        Toast.makeText(getApplicationContext(),
                                        e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResult(boolean exists) {

                    }
                });
            }
        }
        else if(id == R.id.btn_info_save && (typeStart.equals("edit") || typeStart.equals("profile")))
        {
            if(isCanSave())
            {
                String user = txtUser.getText().toString();

                String pass = user;
                String name = txtName.getText().toString();
                String phone = txtPhone.getText().toString();
                String birth = txtBirth.getText().toString();
                String role = txtRole.getText().toString();
                String email = txtEmail.getText().toString();

                AccountService userService = new AccountService();
                userService.updateUser(user, pass, name, phone, birth, role, email, selectAVT, new ServiceCallback() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Show success message
                        //      Log.d("UserService", "User created with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(),
                                "Update user success", Toast.LENGTH_SHORT).show();

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("success", "ok"); // Thêm dữ liệu cần trả về
                        resultIntent.putExtra("name", name);
                        resultIntent.putExtra("phone", phone);
                        resultIntent.putExtra("birth", birth);
                        resultIntent.putExtra("email", email);
                        resultIntent.putExtra("role", role);
                        resultIntent.putExtra("linkAvt", selectAVT);

                        setResult(RESULT_OK, resultIntent); // Đặt mã kết quả và Intent
                        finish(); // Kết thúc Activity B
                    }

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Show error message
                        //     Log.e("UserService", "Error creating user", e);
                        Toast.makeText(getApplicationContext(),
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResult(boolean exists) {

                    }
                });
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
        else if(id == R.id.avt_info_user)
        {
            showImageSelectionDialog();
        }
    }

    private void showImageSelectionDialog() {
        // Tạo Dialog tùy chỉnh
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_image_selection);
        dialog.setTitle("Select an Avatar");

        // Lấy container để hiển thị danh sách ảnh
        GridLayout imageContainer = dialog.findViewById(R.id.imageContainer);
        imageContainer.setColumnCount(4); // Thiết lập số cột cho GridLayout

        // Duyệt qua danh sách URL và tạo ImageView cho từng ảnh
        for (String url : imageUrls) {
            ImageView imageView = new ImageView(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 200;
            params.height = 200;
            params.setMargins(16, 16, 16, 16);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            SvgLoader.loadSvgFromUrl(url, imageView);

            // Thiết lập sự kiện click để chọn ảnh
            imageView.setOnClickListener(v -> {
                setAvt(url);
                selectAVT = url;
                if(!typeStart.equals("create"))
                {
                    AccountService accountService = new AccountService();
                    accountService.updateAvatar(txtUser.getText().toString(), url, new ServiceCallback() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                        }

                        @Override
                        public void onSuccess() {
                            Toast.makeText(getApplicationContext(),"Update avatar success", Toast.LENGTH_SHORT).show();
                            dialog.dismiss(); // Đóng Dialog
                        }

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResult(boolean exists) {

                        }
                    });
                }
                else {
                    dialog.dismiss(); // Đóng Dialog

                }

            });

            imageContainer.addView(imageView); // Thêm ImageView vào container
        }

        dialog.show(); // Hiển thị Dialog
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