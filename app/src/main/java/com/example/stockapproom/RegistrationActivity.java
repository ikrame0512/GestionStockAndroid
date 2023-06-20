package com.example.stockapproom;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private UserDao userDao;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnRegister;
    private TextView login;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize userDao object using your Room database instance
        this.userDao = AppDatabase.getInstance(this).userDao();


        ;

        etUsername = findViewById(R.id.Username);
        etPassword = findViewById(R.id.Password);
        btnRegister = findViewById(R.id.btnRegister);
        login = findViewById(R.id.tvLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }
    private void registerUser() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (!username.isEmpty() && !password.isEmpty()) {
            // Check if the username already exists
            new Thread(new Runnable() {
                @Override
                public void run() {
                    User existingUser = userDao.getUserByUsername(username);
                    if (existingUser == null) {
                        User newUser = new User(username, password);
                        userDao.insert(newUser);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegistrationActivity.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        } else {
            Toast.makeText(RegistrationActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
        }
    }

    /**private void registerUser() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (!username.isEmpty() && !password.isEmpty()) {
            // Check if the username already exists
            User existingUser = userDao.getUserByUsername(username);
            if (existingUser == null) {
                User newUser = new User(username, password);
                userDao.insert(newUser);
                Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(RegistrationActivity.this, "Username already exists!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(RegistrationActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
        }
    }*/
    }