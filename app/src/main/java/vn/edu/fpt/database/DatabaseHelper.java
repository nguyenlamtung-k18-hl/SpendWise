package vn.edu.fpt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import vn.edu.fpt.model.Category;
import vn.edu.fpt.model.CategoryStats;
import vn.edu.fpt.model.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
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

    // calculate total income
    public double getTotalIncome() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT SUM(" + SpendWiseDatabase.COLUMN_AMOUNT + ") FROM " + 
                      SpendWiseDatabase.TABLE_TRANSACTIONS + 
                      " WHERE " + SpendWiseDatabase.COLUMN_TYPE + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{"income"});
        double total = 0.0;
        
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        
        cursor.close();
        return total;
    }
    
    // calculate total expense
    public double getTotalExpense() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT SUM(" + SpendWiseDatabase.COLUMN_AMOUNT + ") FROM " + 
                      SpendWiseDatabase.TABLE_TRANSACTIONS + 
                      " WHERE " + SpendWiseDatabase.COLUMN_TYPE + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{"expense"});
        double total = 0.0;
        
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        
        cursor.close();
        return total;
    }
    
    // calculate current balance
    public double getCurrentBalance() {
        return getTotalIncome() - getTotalExpense();
    }
    
    // get recent transactions
    public List<Transaction> getRecentTransactions(int limit) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Transaction> transactions = new ArrayList<>();
        
        String query = "SELECT t.*, c." + SpendWiseDatabase.COLUMN_CATEGORY_NAME + 
                      ", c." + SpendWiseDatabase.COLUMN_CATEGORY_COLOR + 
                      " FROM " + SpendWiseDatabase.TABLE_TRANSACTIONS + " t" +
                      " LEFT JOIN " + SpendWiseDatabase.TABLE_CATEGORIES + " c" +
                      " ON t." + SpendWiseDatabase.COLUMN_CATEGORY_ID + " = c." + SpendWiseDatabase.COLUMN_ID +
                      " ORDER BY t." + SpendWiseDatabase.COLUMN_DATE + " DESC" +
                      " LIMIT ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(limit)});
        
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
    
    // Helper method to get month timestamp range
    private long[] getMonthTimestampRange(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        
        // Start of month
        calendar.set(year, month - 1, 1, 0, 0, 0); // month is 0-based
        calendar.set(Calendar.MILLISECOND, 0);
        long startTimestamp = calendar.getTimeInMillis();
        
        // End of month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long endTimestamp = calendar.getTimeInMillis();
        
        return new long[]{startTimestamp, endTimestamp};
    }
    
    // get monthly income
    public double getMonthlyIncome(int year, int month) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        long[] dateRange = getMonthTimestampRange(year, month);
        long startTimestamp = dateRange[0];
        long endTimestamp = dateRange[1];
        
        String query = "SELECT SUM(" + SpendWiseDatabase.COLUMN_AMOUNT + ") FROM " + 
                      SpendWiseDatabase.TABLE_TRANSACTIONS + 
                      " WHERE " + SpendWiseDatabase.COLUMN_TYPE + " = ?" +
                      " AND " + SpendWiseDatabase.COLUMN_DATE + " >= ?" +
                      " AND " + SpendWiseDatabase.COLUMN_DATE + " <= ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{"income", String.valueOf(startTimestamp), String.valueOf(endTimestamp)});
        double total = 0.0;
        
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        
        cursor.close();
        return total;
    }
    
        // get monthly expense
    public double getMonthlyExpense(int year, int month) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        long[] dateRange = getMonthTimestampRange(year, month);
        long startTimestamp = dateRange[0];
        long endTimestamp = dateRange[1];
        
        String query = "SELECT SUM(" + SpendWiseDatabase.COLUMN_AMOUNT + ") FROM " + 
                      SpendWiseDatabase.TABLE_TRANSACTIONS + 
                      " WHERE " + SpendWiseDatabase.COLUMN_TYPE + " = ?" +
                      " AND " + SpendWiseDatabase.COLUMN_DATE + " >= ?" +
                      " AND " + SpendWiseDatabase.COLUMN_DATE + " <= ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{"expense", String.valueOf(startTimestamp), String.valueOf(endTimestamp)});
        double total = 0.0;
        
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        
        cursor.close();
        return total;
    }
    
    // get category statistics
    public List<CategoryStats> getCategoryStats(String type, int year, int month) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<CategoryStats> stats = new ArrayList<>();
        
        long[] dateRange = getMonthTimestampRange(year, month);
        long startTimestamp = dateRange[0];
        long endTimestamp = dateRange[1];
        
        String query = "SELECT c." + SpendWiseDatabase.COLUMN_CATEGORY_NAME + 
                      ", c." + SpendWiseDatabase.COLUMN_CATEGORY_COLOR +
                      ", SUM(t." + SpendWiseDatabase.COLUMN_AMOUNT + ") as total" +
                      ", COUNT(t." + SpendWiseDatabase.COLUMN_ID + ") as count" +
                      " FROM " + SpendWiseDatabase.TABLE_TRANSACTIONS + " t" +
                      " LEFT JOIN " + SpendWiseDatabase.TABLE_CATEGORIES + " c" +
                      " ON t." + SpendWiseDatabase.COLUMN_CATEGORY_ID + " = c." + SpendWiseDatabase.COLUMN_ID +
                      " WHERE t." + SpendWiseDatabase.COLUMN_TYPE + " = ?" +
                      " AND t." + SpendWiseDatabase.COLUMN_DATE + " >= ?" +
                      " AND t." + SpendWiseDatabase.COLUMN_DATE + " <= ?" +
                      " GROUP BY c." + SpendWiseDatabase.COLUMN_ID +
                      " ORDER BY total DESC";
        
        Cursor cursor = db.rawQuery(query, new String[]{type, String.valueOf(startTimestamp), String.valueOf(endTimestamp)});
        
        if (cursor.moveToFirst()) {
            do {
                CategoryStats stat = new CategoryStats(
                    cursor.getString(0), // name
                    cursor.getString(1), // color
                    cursor.getDouble(2), // total
                    cursor.getInt(3)     // count
                );
                stats.add(stat);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return stats;
    }
} 