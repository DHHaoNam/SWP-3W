/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author TRUNG NHAN
 */
public class Attribute {

    private int attributeID;
    private String attributeName;

    public Attribute() {
    }

    public Attribute(int attributeID, String attributeName) {
        this.attributeID = attributeID;
        this.attributeName = attributeName;
    }

    public Attribute(String attributeName) {
        this.attributeName = attributeName;
    }

    public int getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(int attributeID) {
        this.attributeID = attributeID;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
}
