/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import model.Cart;

/**
 *
 * @author HP
 */
public class Main {

    public static void main(String[] args) {
        // Tạo đối tượng CartDAO
        CartDAO cartDAO = new CartDAO();

        // Thử cập nhật số lượng sản phẩm trong giỏ hàng
        int customerID = 25;  // Giả sử customerID là 1
        int productId = 4; // Giả sử productId là 101
        int newQuantity = 5; // Cập nhật số lượng mới

        boolean result = cartDAO.updateCartItemQuantity(customerID, productId, newQuantity);

        if (result) {
            System.out.println("Cart item quantity updated successfully!");
        } else {
            System.out.println("Failed to update cart item quantity.");
        }
    }
}
