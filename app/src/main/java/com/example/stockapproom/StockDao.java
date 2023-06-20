package com.example.stockapproom;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StockDao {

    @Insert
    void insert(StockItem stockItem);

    @Update
    void update(StockItem stockItem);

    @Delete
    void delete(StockItem stockItem);

    @Query("SELECT * FROM stocks")
    List<StockItem> getAllStockItems();

    @Query("SELECT * FROM stocks WHERE item = :item")
    StockItem getStockItemByItem(String item);
}
