package com.example.stockapproom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private UserDao userDao;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize the Room database
        this.userDao = AppDatabase.getInstance(this).userDao();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        signUp = findViewById(R.id.tvSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }
    /*private void loginUser() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (!username.isEmpty() && !password.isEmpty()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    User user = userDao.getUser(username, password);
                    if (user != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                // Redirect to the main activity (stock app)
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        } else {
            Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void loginUser() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (!username.isEmpty() && !password.isEmpty()) {
            // Execute the database query on a background thread
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    // Retrieve the user from the database
                    User user = userDao.getUserByUsername(username);

                    // Perform UI updates on the main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (user != null && user.getPassword().equals(password)) {
                                // Login successful
                                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                // Username or password is incorrect
                                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        } else {
            // Username or password is empty
            Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
        }
    }



}