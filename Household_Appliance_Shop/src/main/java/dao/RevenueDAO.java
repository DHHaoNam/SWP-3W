/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import DB.DBContext;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.RevenueDTO;
import model.TopCustomerDTO;
import model.TopProductDTO;

/**
 *
 * @author HP
 */
public class RevenueDAO {

    private DBContext dbcontext = new DBContext();

    public List<RevenueDTO> getRevenueByDateRange(java.util.Date startDate, java.util.Date endDate) {
        List<RevenueDTO> revenueList = new ArrayList<>();

        String sql = "SELECT \n"
                + "    oi.orderDate AS order_date, \n"
                + "    SUM(oi.totalPrice) AS daily_revenue \n"
                + "FROM OrderInfo oi\n"
                + "JOIN OrderStatus os ON oi.orderStatus = os.orderStatusID\n"
                + "WHERE oi.orderDate BETWEEN ? AND ? \n"
                + "  AND os.statusName = 'Delivered'\n"
                + "GROUP BY oi.orderDate \n"
                + "ORDER BY order_date";

        try ( PreparedStatement stmt = dbcontext.getConnection().prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(startDate.getTime()));
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                revenueList.add(new RevenueDTO(
                        rs.getDate("order_date"),
                        rs.getDouble("daily_revenue")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return revenueList;
    }

    public boolean decreaseRevenueByDate(Date orderDate, double amount) {
        String sql = "SELECT orderID, totalPrice FROM OrderInfo WHERE orderDate = ? AND orderStatus != ?";
        String updateSql = "UPDATE OrderInfo SET totalPrice = totalPrice - ? WHERE orderID = ?";

        try ( PreparedStatement checkStmt = dbcontext.getConnection().prepareStatement(sql)) {
            checkStmt.setDate(1, new java.sql.Date(orderDate.getTime()));
            checkStmt.setInt(2, 4); // Set giá trị cho tham số thứ hai (5 là mã trạng thái "Đã hủy")
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("No orders found or all orders are cancelled on " + orderDate);
                return false;
            }

            // Lặp qua các đơn hàng không bị hủy
            do {
                double totalPrice = rs.getDouble("totalPrice");
                int orderID = rs.getInt("orderID");

                // Kiểm tra lại nếu giá trị tổng giá trị đơn hàng là hợp lệ trước khi giảm
                if (totalPrice >= amount) {
                    try ( PreparedStatement updateStmt = dbcontext.getConnection().prepareStatement(updateSql)) {
                        updateStmt.setDouble(1, amount);
                        updateStmt.setInt(2, orderID); // ID của đơn hàng cần cập nhật
                        if (updateStmt.executeUpdate() > 0) {
                            System.out.println("Revenue decreased for orderID: " + orderID);
                            return true;
                        } else {
                            System.out.println("Failed to update revenue for orderID: " + orderID);
                        }
                    }
                } else {
                    System.out.println("Not enough revenue to decrease for orderID: " + orderID);
                }
            } while (rs.next());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<TopCustomerDTO> getTopCustomers(Date startDate, Date endDate) {
        List<TopCustomerDTO> list = new ArrayList<>();
        String sql = "SELECT TOP 5 c.fullName AS customerName, SUM(o.totalPrice) AS totalSpent "
                + "FROM OrderInfo o "
                + "JOIN Customer c ON o.customerID = c.customerID "
                + "WHERE o.orderDate BETWEEN ? AND ? "
                + "GROUP BY c.fullName "
                + "ORDER BY totalSpent DESC";

        try ( PreparedStatement ps = dbcontext.getConnection().prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(startDate.getTime()));
            ps.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new TopCustomerDTO(rs.getString("customerName"), rs.getDouble("totalSpent")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<TopProductDTO> getTopProducts(Date startDate, Date endDate) {
        List<TopProductDTO> list = new ArrayList<>();
        String sql = "SELECT TOP 5 p.productName, SUM(od.quantity) AS totalSold "
                + "FROM OrderDetail od "
                + "JOIN Product p ON od.productID = p.productID "
                + "JOIN OrderInfo o ON od.orderID = o.orderID "
                + "WHERE o.orderDate BETWEEN ? AND ? "
                + "GROUP BY p.productName "
                + "ORDER BY totalSold DESC";

        try ( PreparedStatement ps = dbcontext.getConnection().prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(startDate.getTime()));
            ps.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new TopProductDTO(rs.getString("productName"), rs.getInt("totalSold")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
