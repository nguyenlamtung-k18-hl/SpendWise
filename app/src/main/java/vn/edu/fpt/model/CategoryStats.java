package vn.edu.fpt.model;

public class CategoryStats {
    private String categoryName;
    private String categoryColor;
    private double totalAmount;
    private int transactionCount;
    
    public CategoryStats(String categoryName, String categoryColor, double totalAmount, int transactionCount) {
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.totalAmount = totalAmount;
        this.transactionCount = transactionCount;
    }
    
    // Getters
    public String getCategoryName() { return categoryName; }
    public String getCategoryColor() { return categoryColor; }
    public double getTotalAmount() { return totalAmount; }
    public int getTransactionCount() { return transactionCount; }
    
    public String getFormattedAmount() {
        return String.format("%,.0f VNĐ", totalAmount);
    }
    
    public double getPercentage(double totalSum) {
        return totalSum > 0 ? (totalAmount / totalSum) * 100 : 0;
    }
} 