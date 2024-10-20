package com.example.essay;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener
{
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.users);
    }
    UserFragment firstFragment = new UserFragment();
    StudentFragment secondFragment = new StudentFragment();
    SettingFragment thirdFragment = new SettingFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int idPerson  =R.id.users;
        int idStudent = R.id.students;
        int idSetting = R.id.settings;
        if(item.getItemId() == idPerson)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, firstFragment)
                    .commit();
        }
        else if(item.getItemId() == idStudent) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, secondFragment)
                    .commit();
        }
        else if(item.getItemId() == idSetting) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, thirdFragment)
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