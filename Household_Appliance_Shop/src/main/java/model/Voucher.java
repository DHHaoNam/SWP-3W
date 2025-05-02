/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Dinh_Hau
 */
public class Voucher {

    private int voucherID;
    private String title;
    private double discount;
    private int categoryID;
    private String status;
    private String categoryName;
    
    public Voucher() {
    }

    public Voucher(int voucherID, String title, double discount, int categoryID, String status) {
        this.voucherID = voucherID;
        this.title = title;
        this.discount = discount;
        this.categoryID = categoryID;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(int voucherID) {
        this.voucherID = voucherID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    @Override
    public String toString() {
        return "Voucher{" + "voucherID=" + voucherID + ", title=" + title + ", discount=" + discount + ", categoryID=" + categoryID + ", status=" + status + '}';
    }

}
