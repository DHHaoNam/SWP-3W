/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author HP
 */
public class TopCustomerDTO {

    private String customerName;
    private double totalSpent;

    public TopCustomerDTO() {
    }

    public TopCustomerDTO(String customerName, double totalSpent) {
        this.customerName = customerName;
        this.totalSpent = totalSpent;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }
    
    
    
}
