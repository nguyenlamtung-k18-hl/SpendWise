package vn.edu.fpt.model;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Transaction {
    private long id;
    private String type; // "income" or "expense"
    private double amount;
    private String description;
    private long categoryId;
    private String categoryName; // For display purposes
    private String categoryColor; // For UI colors
    private long date; // timestamp
    
    // Constructors
    public Transaction() {
    }
    
    public Transaction(String type, double amount, String description, long categoryId, long date) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.categoryId = categoryId;
        this.date = date;
    }
    
    public Transaction(long id, String type, double amount, String description, 
                      long categoryId, String categoryName, String categoryColor, long date) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.date = date;
    }
    
    // Getters and Setters
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getCategoryColor() {
        return categoryColor;
    }
    
    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }
    
    public long getDate() {
        return date;
    }
    
    public void setDate(long date) {
        this.date = date;
    }
    
    // Helper methods cho UI display
    public String getFormattedAmount() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String prefix = isExpense() ? "-" : "+";
        return prefix + formatter.format(amount);
    }
    
    public String getFormattedDate() {
        Date dateObj = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return formatter.format(dateObj);
    }
    
    public String getFormattedDateTime() {
        Date dateObj = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return formatter.format(dateObj);
    }
    
    public boolean isExpense() {
        return "expense".equals(type);
    }
    
    public boolean isIncome() {
        return "income".equals(type);
    }
    
    public Date getDateAsObject() {
        return new Date(date);
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", date=" + getFormattedDate() +
                '}';
    }
}