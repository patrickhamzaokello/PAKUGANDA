package com.example.pakuganda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;
    NavController navController;
    private String[] PERMISSIONS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AskPermissions();
        setUpNavigation();

    }

    private void setUpNavigation() {
        navView = findViewById(R.id.bottomNav_view);
        navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navView, navController);
    }


    private void AskPermissions() {

        PERMISSIONS = new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };

        if(!hasPermissions(MainActivity.this, PERMISSIONS)){
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, 1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){


            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "ACCESS_FINE_LOCATION granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "ACCESS_FINE_LOCATION denied", Toast.LENGTH_SHORT).show();
            }
            if(grantResults[1]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "ACCESS_COARSE_LOCATION granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "ACCESS_COARSE_LOCATION denied", Toast.LENGTH_SHORT).show();
            }
            if(grantResults[2]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "CAMERA granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "CAMERA denied", Toast.LENGTH_SHORT).show();
            }
            if(grantResults[3]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "READ_EXTERNAL_STORAGEgranted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "READ_EXTERNAL_STORAGE denied", Toast.LENGTH_SHORT).show();
            }
            if(grantResults[4]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "WRITE_EXTERNAL_STORAGE granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "WRITE_EXTERNAL_STORAGE denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean hasPermissions(Context context, String... PERMISSIONS){
        if(context != null && PERMISSIONS != null){
            for (String permission: PERMISSIONS){
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }
}