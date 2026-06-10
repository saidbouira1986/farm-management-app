package com.farm.management.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.farm.management.R;

public class MainActivity extends AppCompatActivity {

    private int userId;
    private Button sheepManagementBtn;
    private Button expenseManagementBtn;
    private Button incomeManagementBtn;
    private Button dashboardBtn;
    private Button healthManagementBtn;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId = getIntent().getIntExtra("userId", -1);

        sheepManagementBtn = findViewById(R.id.sheepManagementBtn);
        expenseManagementBtn = findViewById(R.id.expenseManagementBtn);
        incomeManagementBtn = findViewById(R.id.incomeManagementBtn);
        dashboardBtn = findViewById(R.id.dashboardBtn);
        healthManagementBtn = findViewById(R.id.healthManagementBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        sheepManagementBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SheepManagementActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        expenseManagementBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExpenseManagementActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        incomeManagementBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, IncomeManagementActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        dashboardBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        healthManagementBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HealthManagementActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }
}
