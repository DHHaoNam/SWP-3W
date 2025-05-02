/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author admin
 */
public class Address {

    private int addressID;
    private String addressDetail;
    private int CustomerID;
    private int isDefault;

    public Address() {
    }
    
    public Address(int addressID, String addressDetail, int CustomerID) {
        this.addressID = addressID;
        this.addressDetail = addressDetail;
        this.CustomerID = CustomerID;
    }

    public Address(int addressID, String addressDetail, int CustomerID, int isDefault) {
        this.addressID = addressID;
        this.addressDetail = addressDetail;
        this.CustomerID = CustomerID;
        this.isDefault = isDefault;

    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int CustomerID) {
        this.CustomerID = CustomerID;
    }

    public int isDefault() {
        return isDefault;
    }

    public void setDefault(int isDefault) {
        this.isDefault = isDefault;
    }

}
