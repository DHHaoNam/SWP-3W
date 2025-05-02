/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author HP
 */
public class OrderInfo {

    private int orderID;
    private int customerID;
    private int orderStatus;
    private Date orderDate;
    private int managerID;
    private int paymentMethodID;
    private double totalPrice;
    private String deliveryAddress;
    private String fullName;
    private String phone;
    private String optionName;
    private String methodName;

    public OrderInfo() {
    }

    public OrderInfo(int orderID, int customerID, int orderStatus, Date orderDate, int managerID, int paymentMethodID, double totalPrice, String deliveryAddress, String fullName, String phone) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.managerID = managerID;
        this.paymentMethodID = paymentMethodID;
        this.totalPrice = totalPrice;
        this.deliveryAddress = deliveryAddress;
        this.fullName = fullName;
        this.phone = phone;
    }

    public OrderInfo(int orderID, int customerID, int orderStatus, Date orderDate, int managerID, int paymentMethodID, double totalPrice, String deliveryAddress) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.managerID = managerID;
        this.paymentMethodID = paymentMethodID;
        this.totalPrice = totalPrice;
        this.deliveryAddress = deliveryAddress;
    }

    public OrderInfo(int orderID, Customer customer, Date orderDate, int orderStatus, String deliveryAddress) {
        this.orderID = orderID;
        this.fullName = customer.getFullName();
        this.phone = customer.getPhone();
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.deliveryAddress = deliveryAddress;
    }

    public int getOrderID() {
        return orderID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getManagerID() {
        return managerID;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    public int getPaymentMethodID() {
        return paymentMethodID;
    }

    public void setPaymentMethodID(int paymentMethodID) {
        this.paymentMethodID = paymentMethodID;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

}
