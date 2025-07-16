package vn.edu.fpt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import vn.edu.fpt.model.Category;
import vn.edu.fpt.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    
    private SpendWiseDatabase dbHelper;
    private static DatabaseHelper instance;
    
    private DatabaseHelper(Context context) {
        dbHelper = SpendWiseDatabase.getInstance(context);
    }
    
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }
    
    // get category by type
    public List<Category> getCategoriesByType(String type) {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String query = "SELECT * FROM " + SpendWiseDatabase.TABLE_CATEGORIES + 
                      " WHERE " + SpendWiseDatabase.COLUMN_CATEGORY_TYPE + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{type});
        
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category(
                    cursor.getLong(cursor.getColumnIndexOrThrow(SpendWiseDatabase.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SpendWiseDatabase.COLUMN_CATEGORY_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SpendWiseDatabase.COLUMN_CATEGORY_TYPE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SpendWiseDatabase.COLUMN_CATEGORY_COLOR))
                );
                categories.add(category);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return categories;
    }
    
    // get category by ID
    public Category getCategoryById(long categoryId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String query = "SELECT * FROM " + SpendWiseDatabase.TABLE_CATEGORIES + 
                      " WHERE " + SpendWiseDatabase.COLUMN_ID + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(categoryId)});
        
        Category category = null;
        if (cursor.moveToFirst()) {
            category = new Category(
                cursor.getLong(cursor.getColumnIndexOrThrow(SpendWiseDatabase.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(SpendWiseDatabase.COLUMN_CATEGORY_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(SpendWiseDatabase.COLUMN_CATEGORY_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(SpendWiseDatabase.COLUMN_CATEGORY_COLOR))
            );
        }
        
        cursor.close();
        return category;
    }
    
    // add transaction
    public long addTransaction(Transaction transaction) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(SpendWiseDatabase.COLUMN_TYPE, transaction.getType());
        values.put(SpendWiseDatabase.COLUMN_AMOUNT, transaction.getAmount());
        values.put(SpendWiseDatabase.COLUMN_DESCRIPTION, transaction.getDescription());
        values.put(SpendWiseDatabase.COLUMN_CATEGORY_ID, transaction.getCategoryId());
        values.put(SpendWiseDatabase.COLUMN_DATE, transaction.getDate());
        
        long id = db.insert(SpendWiseDatabase.TABLE_TRANSACTIONS, null, values);
        transaction.setId(id);
        
        return id;
    }
    
    // get all transactions with category info
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String query = "SELECT t.*, c." + SpendWiseDatabase.COLUMN_CATEGORY_NAME + 
                      ", c." + SpendWiseDatabase.COLUMN_CATEGORY_COLOR + 
                      " FROM " + SpendWiseDatabase.TABLE_TRANSACTIONS + " t" +
                      " LEFT JOIN " + SpendWiseDatabase.TABLE_CATEGORIES + " c" +
                      " ON t." + SpendWiseDatabase.COLUMN_CATEGORY_ID + " = c." + SpendWiseDatabase.COLUMN_ID +
                      " ORDER BY t." + SpendWiseDatabase.COLUMN_DATE + " DESC";
        
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction(
                    cursor.getLong(cursor.getColumnIndexOrThrow(SpendWiseDatabase.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SpendWiseDatabase.COLUMN_TYPE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(SpendWiseDatabase.COLUMN_AMOUNT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SpendWiseDatabase.COLUMN_DESCRIPTION)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(SpendWiseDatabase.COLUMN_CATEGORY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SpendWiseDatabase.COLUMN_CATEGORY_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SpendWiseDatabase.COLUMN_CATEGORY_COLOR)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(SpendWiseDatabase.COLUMN_DATE))
                );
                transactions.add(transaction);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return transactions;
    }
    
    // update transaction
    public int updateTransaction(Transaction transaction) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(SpendWiseDatabase.COLUMN_TYPE, transaction.getType());
        values.put(SpendWiseDatabase.COLUMN_AMOUNT, transaction.getAmount());
        values.put(SpendWiseDatabase.COLUMN_DESCRIPTION, transaction.getDescription());
        values.put(SpendWiseDatabase.COLUMN_CATEGORY_ID, transaction.getCategoryId());
        values.put(SpendWiseDatabase.COLUMN_DATE, transaction.getDate());
        
        return db.update(SpendWiseDatabase.TABLE_TRANSACTIONS, values, 
                        SpendWiseDatabase.COLUMN_ID + " = ?", 
                        new String[]{String.valueOf(transaction.getId())});
    }
    
    // delete transaction
    public int deleteTransaction(long transactionId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        return db.delete(SpendWiseDatabase.TABLE_TRANSACTIONS, 
                        SpendWiseDatabase.COLUMN_ID + " = ?", 
                        new String[]{String.valueOf(transactionId)});
    }
} 