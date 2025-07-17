package vn.edu.fpt.model;

import android.graphics.Color;

public class Category {
    private long id;
    private String name;
    private String type; // "income" or "expense"
    private String color; // hex color code

    public Category() {
    }
    
    public Category(String name, String type, String color) {
        this.name = name;
        this.type = type;
        this.color = color;
    }
    
    public Category(long id, String name, String type, String color) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.color = color;
    }

    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }

    public boolean isExpenseCategory() {
        return "expense".equals(type);
    }
    
    public boolean isIncomeCategory() {
        return "income".equals(type);
    }
    
    public int getColorInt() {
        try {
            return Color.parseColor(color);
        } catch (Exception e) {
            // Default color
            return Color.parseColor("#2196F3");
        }
    }
    
    public String getDisplayName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return id == category.id;
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}