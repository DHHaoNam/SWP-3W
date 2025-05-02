/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import DB.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CancelReason;
import model.OrderInfo;

/**
 *
 * @author Nam
 */
public class OrderHistoryDAO {

    private DBContext dbcontext = new DBContext();

    public List<OrderInfo> getOrdersByStatus(int customerID, int orderStatus) {
        List<OrderInfo> orders = new ArrayList<>();
        String sql = "SELECT * FROM OrderInfo WHERE customerID = ? AND orderStatus = ? ORDER BY orderDate DESC";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerID);
            stmt.setInt(2, orderStatus);

            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(new OrderInfo(
                            rs.getInt("orderID"),
                            rs.getInt("customerID"),
                            rs.getInt("orderStatus"),
                            rs.getDate("orderDate"),
                            rs.getInt("managerID"),
                            rs.getInt("paymentMethodID"),
                            rs.getDouble("totalPrice"),
                            rs.getString("deliveryAddress")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public boolean cancelOrder(int orderId, String reason) {
        String updateOrderInfoSQL = "UPDATE OrderInfo SET orderStatus = 5 WHERE orderID = ?";
        String insertCancelReasonSQL = "INSERT INTO CancelReason (orderID, reason) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = dbcontext.getConnection();
            conn.setAutoCommit(false);

            try ( PreparedStatement ps = conn.prepareStatement(updateOrderInfoSQL)) {
                ps.setInt(1, orderId);
                int rowsUpdated = ps.executeUpdate();

                if (rowsUpdated == 0) {
                    conn.rollback();
                    return false;
                }
            }

            try ( PreparedStatement ps = conn.prepareStatement(insertCancelReasonSQL)) {
                ps.setInt(1, orderId);
                ps.setString(2, reason);
                ps.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public CancelReason getCancelReasonByOrderID(int orderID) {
        CancelReason cancelReason = null;
        String sql = "SELECT * FROM CancelReason WHERE orderID = ?";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderID);

            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cancelReason = new CancelReason(
                            rs.getInt("cancelID"),
                            rs.getInt("orderID"),
                            rs.getString("reason")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cancelReason;
    }
}
