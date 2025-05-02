/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Nam
 */
public class CancelReason {

    private int cancelID;
    private int orderID;
    private String reason;

    public CancelReason() {
    }

    public CancelReason(int cancelID, int orderID, String reason) {
        this.cancelID = cancelID;
        this.orderID = orderID;
        this.reason = reason;
    }

    public int getCancelID() {
        return cancelID;
    }

    public void setCancelID(int cancelID) {
        this.cancelID = cancelID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
