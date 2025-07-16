package vn.edu.fpt.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SpendWiseDatabase extends SQLiteOpenHelper {
    
    // Database info
    private static final String DATABASE_NAME = "spendwise.db";
    private static final int DATABASE_VERSION = 1;
    
    // Table names
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String TABLE_CATEGORIES = "categories";
    
    // Transactions
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type"; // "income" or "expense"
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_DATE = "date"; // timestamp
    
    // Categories
    public static final String COLUMN_CATEGORY_NAME = "name";
    public static final String COLUMN_CATEGORY_TYPE = "type"; // "income" or "expense"
    public static final String COLUMN_CATEGORY_COLOR = "color"; // cho UI
    
    // Singleton instance
    private static SpendWiseDatabase instance;
    
    private SpendWiseDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public static synchronized SpendWiseDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new SpendWiseDatabase(context.getApplicationContext());
        }
        return instance;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo table categories
        String createCategoriesTable = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY_NAME + " TEXT NOT NULL, " +
                COLUMN_CATEGORY_TYPE + " TEXT NOT NULL, " +
                COLUMN_CATEGORY_COLOR + " TEXT DEFAULT '#2196F3'" +
                ")";
        
        // Tạo table transactions
        String createTransactionsTable = "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TYPE + " TEXT NOT NULL, " +
                COLUMN_AMOUNT + " REAL NOT NULL, " +
                COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                COLUMN_CATEGORY_ID + " INTEGER, " +
                COLUMN_DATE + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " + 
                TABLE_CATEGORIES + "(" + COLUMN_ID + ")" +
                ")";
        
        // Tạo các bảng
        db.execSQL(createCategoriesTable);
        db.execSQL(createTransactionsTable);
        
        // Chèn các category mặc định
        insertDefaultCategories(db);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xoá các bảng cũ nếu có
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    private void insertDefaultCategories(SQLiteDatabase db) {
        // Expense categories
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + 
                   " (" + COLUMN_CATEGORY_NAME + ", " + COLUMN_CATEGORY_TYPE + ", " + COLUMN_CATEGORY_COLOR + ") " +
                   "VALUES ('Food', 'expense', '#FF5722')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + 
                   " (" + COLUMN_CATEGORY_NAME + ", " + COLUMN_CATEGORY_TYPE + ", " + COLUMN_CATEGORY_COLOR + ") " +
                   "VALUES ('Transport', 'expense', '#FF9800')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + 
                   " (" + COLUMN_CATEGORY_NAME + ", " + COLUMN_CATEGORY_TYPE + ", " + COLUMN_CATEGORY_COLOR + ") " +
                   "VALUES ('Shopping', 'expense', '#E91E63')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + 
                   " (" + COLUMN_CATEGORY_NAME + ", " + COLUMN_CATEGORY_TYPE + ", " + COLUMN_CATEGORY_COLOR + ") " +
                   "VALUES ('Bills', 'expense', '#9C27B0')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + 
                   " (" + COLUMN_CATEGORY_NAME + ", " + COLUMN_CATEGORY_TYPE + ", " + COLUMN_CATEGORY_COLOR + ") " +
                   "VALUES ('Entertainment', 'expense', '#3F51B5')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + 
                   " (" + COLUMN_CATEGORY_NAME + ", " + COLUMN_CATEGORY_TYPE + ", " + COLUMN_CATEGORY_COLOR + ") " +
                   "VALUES ('Healthcare', 'expense', '#009688')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + 
                   " (" + COLUMN_CATEGORY_NAME + ", " + COLUMN_CATEGORY_TYPE + ", " + COLUMN_CATEGORY_COLOR + ") " +
                   "VALUES ('Other', 'expense', '#607D8B')");
        
        // Income categories
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + 
                   " (" + COLUMN_CATEGORY_NAME + ", " + COLUMN_CATEGORY_TYPE + ", " + COLUMN_CATEGORY_COLOR + ") " +
                   "VALUES ('Salary', 'income', '#4CAF50')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + 
                   " (" + COLUMN_CATEGORY_NAME + ", " + COLUMN_CATEGORY_TYPE + ", " + COLUMN_CATEGORY_COLOR + ") " +
                   "VALUES ('Business', 'income', '#8BC34A')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + 
                   " (" + COLUMN_CATEGORY_NAME + ", " + COLUMN_CATEGORY_TYPE + ", " + COLUMN_CATEGORY_COLOR + ") " +
                   "VALUES ('Freelance', 'income', '#CDDC39')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + 
                   " (" + COLUMN_CATEGORY_NAME + ", " + COLUMN_CATEGORY_TYPE + ", " + COLUMN_CATEGORY_COLOR + ") " +
                   "VALUES ('Investment', 'income', '#FFC107')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + 
                   " (" + COLUMN_CATEGORY_NAME + ", " + COLUMN_CATEGORY_TYPE + ", " + COLUMN_CATEGORY_COLOR + ") " +
                   "VALUES ('Gift', 'income', '#FF5722')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + 
                   " (" + COLUMN_CATEGORY_NAME + ", " + COLUMN_CATEGORY_TYPE + ", " + COLUMN_CATEGORY_COLOR + ") " +
                   "VALUES ('Other', 'income', '#795548')");
    }
} 