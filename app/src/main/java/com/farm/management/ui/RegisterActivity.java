package com.farm.management.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.farm.management.R;
import com.farm.management.database.AppDatabase;
import com.farm.management.models.User;
import com.farm.management.util.PasswordUtils;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText farmNameEditText;
    private EditText phoneNumberEditText;
    private EditText locationEditText;
    private Button registerButton;
    private Button backButton;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = AppDatabase.getInstance(this);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        farmNameEditText = findViewById(R.id.farmNameEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        locationEditText = findViewById(R.id.locationEditText);
        registerButton = findViewById(R.id.registerButton);
        backButton = findViewById(R.id.backButton);

        registerButton.setOnClickListener(v -> performRegistration());
        backButton.setOnClickListener(v -> finish());
    }

    private void performRegistration() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String farmName = farmNameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || 
            farmName.isEmpty() || phoneNumber.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "جميع الحقول مطلوبة", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "كلمات المرور غير متطابقة", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            User existingUser = db.userDao().getUserByEmail(email);
            if (existingUser != null) {
                runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "هذا البريد مسجل بالفعل", Toast.LENGTH_SHORT).show());
                return;
            }

            String hashedPassword = PasswordUtils.hashPassword(password);
            User newUser = new User(email, hashedPassword, farmName, phoneNumber, location);
            db.userDao().insertUser(newUser);

            runOnUiThread(() -> {
                Toast.makeText(RegisterActivity.this, "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            });
        }).start();
    }
}
