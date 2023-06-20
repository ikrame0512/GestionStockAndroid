package com.example.stockapproom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    private StockDao stockDao;
    private List<StockItem> stockItemList;
    private StockAdapter adapter;
    private EditText etItem;
    private EditText etQuantity;
    private ExecutorService executorService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        AppDatabase stockDatabase = AppDatabase.getInstance(this);
        stockDao = stockDatabase.stockDao();
        executorService = Executors.newSingleThreadExecutor();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StockAdapter(stockItemList, this);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((OnItemClickListener) this);



        etItem = findViewById(R.id.et_item);
        etQuantity = findViewById(R.id.et_quantity);

        Button btnAdd = findViewById(R.id.btn_add);
        Button btnRemove = findViewById(R.id.btn_remove);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItemWithQuantity();
            }
        });
        Button btnModify = findViewById(R.id.btn_modify);

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyItemWithQuantity();
            }
        });

        loadStockItems();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);

        return true;
    }
    public void onItemClick(StockItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete Item");
        builder.setMessage("Are you sure you want to delete this item?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeItem(item);
            }
        });

        builder.setNegativeButton("No", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void removeItem(StockItem item) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                stockDao.delete(item);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadStockItems();
                        Toast.makeText(MainActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void addItem() {
        String item = etItem.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();

        if (!item.isEmpty() && !quantityStr.isEmpty()) {
            int quantity = Integer.parseInt(quantityStr);

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    StockItem existingItem = stockDao.getStockItemByItem(item);

                    if (existingItem == null) {
                        StockItem stockItem = new StockItem(item, quantity);
                        stockDao.insert(stockItem);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                etItem.setText("");
                                etQuantity.setText("");
                                loadStockItems();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Item already exists", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please enter item and quantity", Toast.LENGTH_SHORT).show();
        }
    }
    private void modifyItemWithQuantity() {
        String item = etItem.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();

        if (!item.isEmpty() && !quantityStr.isEmpty()) {
            int quantity = Integer.parseInt(quantityStr);

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    StockItem stockItem = stockDao.getStockItemByItem(item);

                    if (stockItem != null) {
                        stockItem.setQuantity(quantity);
                        stockDao.update(stockItem);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                etItem.setText("");
                                etQuantity.setText("");
                                loadStockItems();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Item not found", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please enter item and quantity", Toast.LENGTH_SHORT).show();
        }
    }
    private void removeItemWithQuantity() {
        String item = etItem.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();

        if (!item.isEmpty() && !quantityStr.isEmpty()) {
            int quantity = Integer.parseInt(quantityStr);

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    StockItem stockItem = stockDao.getStockItemByItem(item);

                    if (stockItem != null) {
                        if (stockItem.getQuantity() >= quantity) {
                            stockItem.setQuantity(stockItem.getQuantity() - quantity);
                            stockDao.update(stockItem);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    etItem.setText("");
                                    etQuantity.setText("");
                                    loadStockItems();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Insufficient quantity", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Item not found", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please enter item and quantity", Toast.LENGTH_SHORT).show();
        }
    }



    private void loadStockItems() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                stockItemList = stockDao.getAllStockItems();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setStockItemList(stockItemList);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
    public void logout(MenuItem item) {
        // Effacer les informations d'identification de l'utilisateur
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("password");
        editor.apply();

        // Terminer toutes les activités ouvertes et revenir à l'écran de connexion
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}