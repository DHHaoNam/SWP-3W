package model;

public class ProductAttribute {

    private int productAttributeID;
    private int productID;
    private int attributeID;
    private String value;
    private String attributeName;

    public ProductAttribute() {
    }

    public ProductAttribute(int productAttributeID, int productID, int attributeID, String value, String attributeName) {
        this.productAttributeID = productAttributeID;
        this.productID = productID;
        this.attributeID = attributeID;
        this.value = value;
        this.attributeName = attributeName;
    }
    

    public ProductAttribute(int productAttributeID, int productID, int attributeID, String value) {
        this.productAttributeID = productAttributeID;
        this.productID = productID;
        this.attributeID = attributeID;
        this.value = value;
    }

    public ProductAttribute(int productID, int attributeID, String value) {
        this.productID = productID;
        this.attributeID = attributeID;
        this.value = value;
    }

    public int getProductAttributeID() {
        return productAttributeID;
    }

    public void setProductAttributeID(int productAttributeID) {
        this.productAttributeID = productAttributeID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(int attributeID) {
        this.attributeID = attributeID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    
    
}
