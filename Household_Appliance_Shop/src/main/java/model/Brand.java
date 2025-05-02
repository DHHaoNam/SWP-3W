/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author HP
 */
public class Brand {
    private int BrandID;
    private String brandName;

    public Brand() {
    }

    public Brand(int BrandID, String brandName) {
        this.BrandID = BrandID;
        this.brandName = brandName;
    }

    public int getBrandID() {
        return BrandID;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandID(int BrandID) {
        this.BrandID = BrandID;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    
}
