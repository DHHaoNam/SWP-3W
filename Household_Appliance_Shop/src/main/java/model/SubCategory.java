/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author HP
 */
public class SubCategory {

    private int subCategoryID;
    private String subCategoryName;
    private int categoryID;

    public SubCategory() {
    }

    public SubCategory(int subCategoryID, String subCategoryName, int categoryID) {
        this.subCategoryID = subCategoryID;
        this.subCategoryName = subCategoryName;
        this.categoryID = categoryID;
    }

    public SubCategory(String subCategoryName, int categoryID) {
        this.subCategoryName = subCategoryName;
        this.categoryID = categoryID;
    }
    
    

    public int getSubCategoryID() {
        return subCategoryID;
    }

    public void setSubCategoryID(int subCategoryID) {
        this.subCategoryID = subCategoryID;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    
    
}
