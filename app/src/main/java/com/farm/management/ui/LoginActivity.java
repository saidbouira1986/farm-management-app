package com.farm.management.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.farm.management.R;
import com.farm.management.database.AppDatabase;
import com.farm.management.models.User;
import com.farm.management.util.PasswordUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = AppDatabase.getInstance(this);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);

        loginButton.setOnClickListener(v -> performLogin());
        registerTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void performLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "البريد والكلمة مطلوبان", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            User user = db.userDao().getUserByEmail(email);
            runOnUiThread(() -> {
                if (user != null && PasswordUtils.verifyPassword(password, user.password)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userId", user.id);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "البريد أو كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
