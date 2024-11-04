package com.example.essay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.essay.component.user.UserInfo;
import com.example.essay.services.AccountService;
import com.example.essay.services.ServiceCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener
{
    private static String UserName ="";
    private static String Role = "";
    private static String Name ="";
    private Toolbar toolbar;
    private static String action ="users";

    BottomNavigationView bottomNavigationView;

    Button btnAdd;


    private ActivityResultLauncher<Intent> userInfoLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        getFromIntent();
        toolbar = findViewById(R.id.toolbar);
        btnAdd = findViewById(R.id.btnAdd);

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.users);
        btnAdd.setOnClickListener( v -> {
            AddButton(v);
        });

        // Initialize the user info launcher
        userInfoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Handle the result for UserInfo activity

                        String rs = result.getData().getStringExtra("success");

                        if(rs != null && rs.equals("ok"))
                        {
                            Log.d("HomeActivity", "it run: ");
                             // reload User Fragment
                            reloadUserFragment();
                        }
                    }
                }
        );
    }

    private void AddButton(View view)
    {
        int id = view.getId();
        if(action.equals("users"))
        {
            // Tạo AlertDialog với hai lựa chọn dọc
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Create User");

            // Set layout của AlertDialog để tạo các nút vertical
            builder.setItems(new CharSequence[]{"Full Create", "Fast Create"}, (dialog, which) -> {
                if (which == 0) {
                    Intent intent = new Intent(this, UserInfo.class);
                    intent.putExtra("type", "create");
                    userInfoLauncher.launch(intent);
                }
                else if (which == 1) {
                    fastAddUserDialog();
                }
            });

            // Hiển thị AlertDialog
            builder.create().show();
        }
        else if(action.equals("students"))
        {

        }
        else
        {

        }

    }

    private void fastAddUserDialog()
    {

        // Tạo AlertDialog mới cho Fast Create với hai trường nhập User và Name
        AlertDialog.Builder fastCreateDialog = new AlertDialog.Builder(this);
        fastCreateDialog.setTitle("Fast Create");

        // Tạo layout chứa các EditText và Spinner
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);


        // Tạo EditText cho User
        EditText userInput = new EditText(this);
        userInput.setHint("User");
        layout.addView(userInput);

        // Tạo EditText cho Name
        EditText nameInput = new EditText(this);
        nameInput.setHint("Name");
        layout.addView(nameInput);

        // Tạo Spinner cho lựa chọn Role
        Spinner roleSpinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Manager", "Employee"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
        layout.addView(roleSpinner);


        fastCreateDialog.setView(layout);

        // Nút Xác nhận
        fastCreateDialog.setPositiveButton("Confirm", (dialogInterface, i) -> {
            String user = userInput.getText().toString();
            String name = nameInput.getText().toString();
            String role = roleSpinner.getSelectedItem().toString();

            if (user.isEmpty() || name.isEmpty() || role.isEmpty())
            {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            AccountService accountService = new AccountService();
            accountService.fastCreate(user, name, role,new ServiceCallback () {

                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(HomeActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                    // Reload User Fragment
                    reloadUserFragment();
                }
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),
                            e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess() {

                }

                @Override
                public void onResult(boolean exists) {

                }
            });
        });

        // Nút Hủy
        fastCreateDialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });

        // Hiển thị AlertDialog Fast Create
        fastCreateDialog.create().show();
    }
    // load data auth account
    private void getFromIntent()
    {
        Intent intent = getIntent();
        UserName = intent.getStringExtra("user");
        Role = intent.getStringExtra("role");
        Name = intent.getStringExtra("name");
        if(UserName.isEmpty() || Role.isEmpty())
        {
            Intent intent1 = new Intent(this, LoginActivity.class);
            startActivity(intent1);
            this.finish();
        }
    }

    UserFragment userFragment;
    StudentFragment studentFragment;
    SettingFragment settingFragment;

    private void reloadUserFragment()
    {
        if(userFragment != null) {
            userFragment.initUser_v2("", "");
        }
    }

    // click on bottom navigation
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int idPerson  =R.id.users;
        int idStudent = R.id.students;
        int idSetting = R.id.settings;

        if(Role.toLowerCase().equals("admin")) btnAdd.setVisibility(View.VISIBLE);
        else btnAdd.setVisibility(View.INVISIBLE);


        if(item.getItemId() == idPerson)
        {
            action = "users";

            toolbar.setTitle("User Management");
            userFragment =  UserFragment.newInstance(UserName, Role);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, userFragment)
                    .commit();
        }
        else if(item.getItemId() == idStudent)
        {
            action = "students";

            toolbar.setTitle("Student Management");
            studentFragment = StudentFragment.newInstance(UserName, Role);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, studentFragment)
                    .commit();
        }
        else if(item.getItemId() == idSetting)
        {

            toolbar.setTitle("Setting");
            btnAdd.setVisibility(View.INVISIBLE);
            settingFragment = SettingFragment.newInstance(UserName, Role);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, settingFragment)
                    .commit();
        }
        else return false;


        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}