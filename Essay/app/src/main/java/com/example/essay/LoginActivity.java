package com.example.essay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.essay.models.account.AccountRepository;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicReference;

public class LoginActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private TextView txtUser;
    private TextView txtPass;
    private Button btnLogin;

    private TextView txtErr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        getFromSharedPreferences();
        initProgram();
    }

    private void initProgram()
    {
        // Khởi tạo các thành phần giao diện
        txtUser = findViewById(R.id.txtuser);
        txtPass = findViewById(R.id.txtpass);
        btnLogin = findViewById(R.id.btnlogin);
        txtErr = findViewById(R.id.txterror);
        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        txtUser.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtErr.setText("");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtErr.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        btnLogin.setOnClickListener(v -> {
            String User = txtUser.getText().toString();
            String Pass = txtPass.getText().toString();
            String err = "";



            if(User == null || User.isEmpty())  err = "Vui lòng nhập tài khoản";
            else if (Pass == null || Pass.isEmpty()) err = "Vui lòng nhập mật khẩu";

            if (!err.isEmpty())
            {
                txtErr.setText(err);
                return;
            }

            try{
                AccountRepository accountRepository = new AccountRepository(db);
                Login(User, Pass);
            }
            catch (Exception e)
            {

            }
        });
    }

    public boolean Login(String user, String pass) throws Exception
    {
        AtomicReference<String> mess = new AtomicReference<>("");

        db.collection("accounts")
                .whereEqualTo("user", user)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // Lấy tài liệu đầu tiên (vì user là duy nhất)
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);

                            // Lấy mật khẩu và role từ Firestore
                            String storedPassword = document.getString("pass");
                            String userRole = document.getString("role");
                            String name = document.getString("name");
                            String phone = document.getString("phone");
                            String birth = document.getString("birthDay");
                            String email = document.getString("email");

                            // So sánh password
                            if (pass.equals(storedPassword))
                            {
                                Log.d("Login", "Login successful");
                                //txtErr.setText("đăng nhập thành công");
                                saveTokenLocally(user, name, userRole);
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.putExtra("user", user);
                                intent.putExtra("name", name);
                                intent.putExtra("role", userRole);
                                intent.putExtra("phone", phone);
                                intent.putExtra("birth", birth);
                                intent.putExtra("email", email);

                                startActivity(intent);

                                this.finish();
                            }
                            else
                            {
                                Log.d("Login", "Incorrect password");
                                txtErr.setText("Mật khẩu không đúng");
                            }
                        }
                        else
                        {
                            Log.d("Login", "User not found");
                            txtErr.setText("Tài khoản không tồn tại");

                        }
                    } else {
                        txtErr.setText("Lỗi đăng nhập!");

                        Log.w("Login", "Error getting documents.", task.getException());
                    }
                });

        return true;

    }

    private void getFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("appPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("user", null);
        String role = sharedPreferences.getString("role", null);
        String name = sharedPreferences.getString("name", null);

        if (username != null && role != null && name != null && !username.isEmpty() && !role.isEmpty() && !name.isEmpty())
        {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("user", username);
            intent.putExtra("name", name);
            intent.putExtra("role", role);

            startActivity(intent);

            this.finish();
        }


    }
    private void saveTokenLocally(String user, String name , String role) {
        SharedPreferences sharedPreferences = getSharedPreferences("appPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", user);
        editor.putString("role", role);  // Lưu vai trò của người dùng
        editor.putString("name", name);  // Lưu vai trò của người dùng
        editor.apply();
    }

}