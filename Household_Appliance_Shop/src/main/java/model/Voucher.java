/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

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
    private Timestamp startTime;
    private Timestamp endTime;

    public Voucher() {
    }

    public Voucher(int voucherID, String title, double discount, int categoryID, String status, String categoryName, Timestamp startTime, Timestamp endTime) {
        this.voucherID = voucherID;
        this.title = title;
        this.discount = discount;
        this.categoryID = categoryID;
        this.status = status;
        this.categoryName = categoryName;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Voucher{" + "voucherID=" + voucherID + ", title=" + title + ", discount=" + discount + ", categoryID=" + categoryID + ", status=" + status + ", categoryName=" + categoryName + ", startTime=" + startTime + ", endTime=" + endTime + '}';
    }

}
