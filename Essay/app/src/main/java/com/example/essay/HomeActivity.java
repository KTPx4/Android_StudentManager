package com.example.essay;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener
{
    private static String UserName ="";
    private static String Role = "";
    private static String Name ="";
    private Toolbar toolbar;
    private static String action ="users";

    BottomNavigationView bottomNavigationView;

    Button btnAdd;

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
    }

    private void AddButton(View view)
    {
        int id = view.getId();
        if(action.equals("users"))
        {

        }
        else if(action.equals("students"))
        {

        }
        else
        {

        }

    }
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
        else if(item.getItemId() == idStudent) {
            action = "students";

            toolbar.setTitle("Student Management");
            studentFragment = StudentFragment.newInstance(UserName, Role);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, studentFragment)
                    .commit();
        }
        else if(item.getItemId() == idSetting) {

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