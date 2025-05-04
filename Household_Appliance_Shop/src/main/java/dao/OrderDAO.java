/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import DB.DBContext;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Address;
import model.Cart;

import model.OrderDetail;
import model.OrderInfo;
import model.OrderStatus;
import model.PaymentMethod;

/**
 *
 * @author HP
 */
public class OrderDAO {

    private DBContext dbcontext = new DBContext();

    private Connection conn;
    private static final String SELECT_ORDERINFO_BY_ID = "SELECT * FROM OrderInfo WHERE orderID = ?";
    private static final String SELECT_ORDERINFO_BY_STATUS = "SELECT * FROM Orders WHERE orderStatus = ?";
    private static final String SELECT_ALL_ORDERINFO = "SELECT * FROM OrderInfo";
    private static final String DELETE_ORDERINFO_SQL = "DELETE FROM OrderInfo WHERE orderID = ?";
    private static final String UPDATE_ORDERINFO_SQL = "UPDATE OrderInfo SET customerID = ?, managerID = ?, paymentMethodID = ?, totalPrice = ?, deliveryAddress = ? WHERE orderID = ?";

    public List<OrderDetail> getOrderDetailsByOrderID(int orderID) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT * FROM OrderDetail WHERE orderID = ?";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderID(rs.getInt("orderID"));
                detail.setProductID(rs.getInt("productID"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setTotalPrice(rs.getInt("totalPrice"));
                orderDetails.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetails;
    }

    public List<Integer> selectedStatus() {
        List<Integer> statusList = new ArrayList<>();
        String query = "SELECT DISTINCT orderStatus FROM OrderInfo";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                statusList.add(rs.getInt("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statusList;
    }

    public List<OrderInfo> getOrdersByStatus(int status) {
        List<OrderInfo> orders = new ArrayList<>();

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(SELECT_ORDERINFO_BY_STATUS)) {
            ps.setInt(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderInfo order = new OrderInfo();
                order.setOrderID(rs.getInt("orderID"));
                order.setCustomerID(rs.getInt("customerID"));
                order.setOrderStatus(rs.getInt("orderStatus"));
                order.setOrderDate(rs.getDate("orderDate"));
                order.setManagerID(rs.getInt("managerID"));
                order.setPaymentMethodID(rs.getInt("paymentMethodID"));
                order.setTotalPrice(rs.getDouble("totalPrice"));
                order.setDeliveryAddress(rs.getString("deliveryAddress"));

                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public OrderInfo selectOrder(int orderID) {
        OrderInfo order = null;
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ORDERINFO_BY_ID)) {
            preparedStatement.setInt(1, orderID);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                order = new OrderInfo();
                order.setOrderID(rs.getInt("orderID"));
                order.setCustomerID(rs.getInt("customerID"));
                order.setOrderStatus(rs.getInt("orderStatus"));
                order.setOrderDate(rs.getDate("orderDate"));
                order.setManagerID(rs.getInt("managerID"));
                order.setPaymentMethodID(rs.getInt("paymentMethodID"));
                order.setTotalPrice(rs.getDouble("totalPrice"));
                order.setDeliveryAddress(rs.getString("deliveryAddress"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public List<PaymentMethod> getAllPaymentMethods() {
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        String sql = "SELECT * FROM PaymentMethod";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                paymentMethods.add(new PaymentMethod(
                        rs.getInt("paymentMethodID"),
                        rs.getString("methodName")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentMethods;
    }

    public List<Address> selectAllAddress() {
        List<Address> address = null;
        String query = "SELECT * FROM Address";
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                address.add(new Address(
                        rs.getInt("addressID"),
                        rs.getString("addressDetail"),
                        rs.getInt("CustomerID")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return address;
    }

    public List<OrderInfo> selectAllOrder() {
        List<OrderInfo> or = new ArrayList<>();
        try (
                 Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ORDERINFO)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                OrderInfo order = new OrderInfo();
                order.setOrderID(rs.getInt("orderID"));
                order.setCustomerID(rs.getInt("customerID"));
                order.setOrderStatus(rs.getInt("orderStatus"));
                order.setOrderDate(rs.getDate("orderDate"));
                order.setManagerID(rs.getInt("managerID"));
                order.setPaymentMethodID(rs.getInt("paymentMethodID"));
                order.setTotalPrice(rs.getDouble("totalPrice"));
                order.setDeliveryAddress(rs.getString("deliveryAddress"));

                or.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return or;
    }

    /**
     * Tìm đơn hàng theo ID
     *
     * @param order
     * @return
     * @throws java.sql.SQLException
     */
    public boolean updateOrder(OrderInfo order) throws SQLException {
        boolean rowUpdated;
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDERINFO_SQL)) {
            preparedStatement.setInt(1, order.getOrderID());
            preparedStatement.setInt(2, order.getCustomerID());
            preparedStatement.setInt(3, order.getOrderStatus());
            preparedStatement.setDate(4, order.getOrderDate());
            preparedStatement.setInt(5, order.getManagerID());
            preparedStatement.setInt(6, order.getPaymentMethodID());
            preparedStatement.setDouble(7, order.getTotalPrice());
            preparedStatement.setString(8, order.getDeliveryAddress());
            rowUpdated = preparedStatement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    public boolean deleteOrder(int orderID) {
        boolean rowDeleted = false;
        String deleteOrderDetailsSQL = "DELETE FROM OrderDetail WHERE orderID = ?";
        String deleteOrderSQL = "DELETE FROM OrderInfo WHERE orderID = ?";

        try ( Connection connection = dbcontext.getConnection()) {
            connection.setAutoCommit(false);

            try ( PreparedStatement psDetails = connection.prepareStatement(deleteOrderDetailsSQL)) {
                psDetails.setInt(1, orderID);
                psDetails.executeUpdate();
            }

            try ( PreparedStatement psOrder = connection.prepareStatement(deleteOrderSQL)) {
                psOrder.setInt(1, orderID);
                rowDeleted = psOrder.executeUpdate() > 0;
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowDeleted;
    }

    /**
     * Tìm kiếm đơn hàng qua số điện thoại khách hàng (giả sử bảng Customer có
     * phone, bạn JOIN hoặc thay đổi logic tuỳ cấu trúc)
     *
     * @param phone
     * @return
     */
    public List<OrderInfo> searchByPhone(String phone) {
        List<OrderInfo> list = new ArrayList<>();
        // Ví dụ: SELECT * FROM OrderInfo o JOIN Customer c ON o.customerID = c.customerID WHERE c.phone = ?
        String sql = "SELECT o.* FROM OrderInfo o JOIN Customer c ON o.customerID = c.customerID WHERE c.phone = ?";
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + phone + "%");
            try ( ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    OrderInfo order = new OrderInfo();
                    order.setOrderID(rs.getInt("orderID"));
                    order.setCustomerID(rs.getInt("customerID"));
                    order.setOrderStatus(rs.getInt("orderStatus"));
                    order.setOrderDate(rs.getDate("orderDate"));
                    order.setManagerID(rs.getInt("managerID"));
                    order.setPaymentMethodID(rs.getInt("paymentMethodID"));
                    order.setTotalPrice(rs.getDouble("totalPrice"));
                    order.setDeliveryAddress(rs.getString("deliveryAddress"));
                    order.setFullName(rs.getString("fullName"));
                    order.setPhone(rs.getString("phone"));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<OrderStatus> getAllStatuses() {
        List<OrderStatus> list = new ArrayList<>();
        String sql = "select * from OrderStatus";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int statusId = rs.getInt("orderStatus");
                String statusName = rs.getString("statusName");
                list.add(new OrderStatus(statusId, statusName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Cập nhật trạng thái đơn hàng
     *
     * @param orderID
     * @param newStatus
     * @return
     */
    public boolean updateOrderStatus(int orderID, int newStatus) {
        String sql = "UPDATE OrderInfo SET orderStatus = ? WHERE orderID = ?";
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, newStatus);
            ps.setInt(2, orderID);
            int row = ps.executeUpdate();
            return row > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int createOrder(OrderInfo order) {
        String sql = "INSERT INTO OrderInfo (customerID, orderStatus, orderDate, managerID, paymentMethodID, totalPrice, deliveryAddress, fullName, phone) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int orderID = -1;

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getCustomerID());
            ps.setInt(2, order.getOrderStatus());
            ps.setDate(3, new java.sql.Date(order.getOrderDate().getTime()));
            //ps.setInt(4, order.getDeliveryOptionID());
            ps.setInt(4, order.getManagerID());
            ps.setInt(5, order.getPaymentMethodID());
            ps.setDouble(6, order.getTotalPrice());
            ps.setString(7, order.getDeliveryAddress());
            ps.setString(8, order.getFullName());
            ps.setString(9, order.getPhone());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try ( ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderID = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderID;
    }

    public boolean addOrderItemsFromCart(int orderID, int cartID) {
        String selectCartSql = "SELECT productID, quantity, totalPrice FROM CartItem WHERE cartID = ?";
        String insertOrderSql = "INSERT INTO OrderDetail (orderID, productID, quantity, totalPrice) "
                + "VALUES (?, ?, ?, ?)";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement selectPs = conn.prepareStatement(selectCartSql);  PreparedStatement insertPs = conn.prepareStatement(insertOrderSql)) {

            conn.setAutoCommit(false); // Bắt đầu transaction

            selectPs.setInt(1, cartID);
            ResultSet rs = selectPs.executeQuery();

            while (rs.next()) {
                int productID = rs.getInt("productID");
                int quantity = rs.getInt("quantity");
                double totalPrice = rs.getDouble("totalPrice");

                insertPs.setInt(1, orderID);
                insertPs.setInt(2, productID);
                insertPs.setInt(3, quantity);
                insertPs.setDouble(4, totalPrice);
                insertPs.addBatch(); // Thêm vào batch
            }

            insertPs.executeBatch(); // Thực thi tất cả INSERT cùng lúc
            conn.commit(); // Xác nhận transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy thông tin đơn hàng theo ID
    public OrderInfo getOrderById(int orderID) {
        String sql = "SELECT * FROM OrderInfo WHERE orderID = ?";
        OrderInfo order = null;

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                order = new OrderInfo();
                order.setOrderID(rs.getInt("orderID"));
                order.setCustomerID(rs.getInt("customerID"));
                order.setOrderStatus(rs.getInt("orderStatus"));
                order.setOrderDate(rs.getDate("orderDate"));
                order.setManagerID(rs.getInt("managerID"));
                order.setPaymentMethodID(rs.getInt("paymentMethodID"));
                order.setTotalPrice(rs.getDouble("totalPrice"));
                order.setDeliveryAddress(rs.getString("deliveryAddress"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public List<OrderDetail> getOrderDetails(int orderID) {
        String sql = "SELECT * FROM OrderDetail WHERE orderID = ?";
        List<OrderDetail> orderItems = new ArrayList<>();

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderDetail item = new OrderDetail(
                        rs.getInt("orderID"),
                        rs.getInt("productID"),
                        rs.getInt("quantity"),
                        rs.getDouble("totalPrice")
                );
                orderItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    public boolean decrementProductQuantity(int productId, int quantity) {
        String sql = "UPDATE Product SET stock_Quantity = stock_Quantity - ? WHERE productID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Return true if the update was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error
        }
    }

    public void incrementProductQuantity(int productId, int quantity) {
        String sql = "UPDATE Product SET stock_Quantity = stock_Quantity + ? WHERE productID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean canDecrementProductQuantity(int productId, int quantity) {
        String sql = "SELECT stock_Quantity FROM Product WHERE productID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int currentStock = rs.getInt("stock_Quantity");
                return currentStock >= quantity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Nếu lỗi xảy ra, trả về false để không trừ số lượng
    }

    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT od.productID, od.quantity, p.price FROM OrderDetail od "
                + "JOIN Product p ON od.productID = p.productID WHERE od.orderID = ?";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("productID");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price"); // Lấy giá sản phẩm từ bảng Product

                OrderDetail detail = new OrderDetail();
                detail.setProductID(productId);
                detail.setQuantity(quantity);
                detail.setTotalPrice(price * quantity); // Tính lại tổng giá tiền

                orderDetails.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDetails;
    }

    public Date getOrderDateByOrderId(int orderId) {
        String sql = "SELECT orderDate FROM OrderInfo WHERE orderID = ?";
        Date orderDate = null;

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                orderDate = rs.getDate("orderDate");  // Lấy ngày đặt hàng
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDate; // Trả về null nếu không tìm thấy
    }

    public int getOrderStatusById(int orderId) {
        String sql = "SELECT orderStatus FROM OrderInfo WHERE orderID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("orderStatus"); // Trả về trạng thái đơn hàng
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu không tìm thấy đơn hàng hoặc có lỗi xảy ra
    }

//    public boolean addOrderItem(int orderID, int customerID) {
//        String insertSQL = "INSERT INTO OrderDetail (orderID, productID, quantity, totalPrice) "
//                + "SELECT ?, productID, quantity, totalPrice "
//                + "FROM CartItem "
//                + "WHERE cartID IN (SELECT cartID FROM Cart WHERE customerID = ?)";
//
//        String updateTotalPriceSQL = "UPDATE OrderInfo "
//                + "SET totalPrice = (SELECT SUM(totalPrice) FROM OrderDetail WHERE orderID = ?) "
//                + "WHERE orderID = ?";
//
//        try ( Connection conn = getConnection()) {
//            conn.setAutoCommit(false);
//
//            // Chèn toàn bộ CartItem vào OrderDetail
//            try ( PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
//                insertStmt.setInt(1, orderID);
//                insertStmt.setInt(2, customerID);
//                insertStmt.executeUpdate();
//            }
//
//            // Cập nhật tổng tiền trong OrderInfo
//            try ( PreparedStatement updateStmt = conn.prepareStatement(updateTotalPriceSQL)) {
//                updateStmt.setInt(1, orderID);
//                updateStmt.setInt(2, orderID);
//                updateStmt.executeUpdate();
//            }
//
//            conn.commit();
//            return true;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//    public boolean addOrderItem(int orderID, int customerID) {
//        String insertSQL = "INSERT INTO OrderDetail (orderID, productID, quantity, totalPrice) "
//                + "VALUES (?, ?, ?, ?)";
//
//        String updateTotalPriceSQL = "UPDATE OrderInfo "
//                + "SET totalPrice = (SELECT SUM(totalPrice) FROM OrderDetail WHERE orderID = ?) "
//                + "WHERE orderID = ?";
//
//        try ( Connection conn = dbcontext.getConnection()) {
//            conn.setAutoCommit(false);
//            CartDAO cartDAO = new CartDAO();
//
//            // Lấy các item trong giỏ hàng
//            List<Cart> cartItems = cartDAO.getcart(customerID);
//
//            try ( PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
//                for (Cart item : cartItems) {
//                    insertStmt.setInt(1, orderID);
//                    insertStmt.setInt(2, item.getProduct().getProductID());
//                    insertStmt.setInt(3, item.getQuantity());
//                    insertStmt.setDouble(4, item.getTotalPrice()); // Tổng tiền cho mỗi sản phẩm
//                    insertStmt.addBatch(); // dùng batch để tối ưu
//                }
//                insertStmt.executeBatch(); // Thực thi một lần
//            }
//
//            // Cập nhật tổng tiền đơn hàng
//            try ( PreparedStatement updateStmt = conn.prepareStatement(updateTotalPriceSQL)) {
//                updateStmt.setInt(1, orderID);
//                updateStmt.setInt(2, orderID);
//                updateStmt.executeUpdate();
//            }
//
//            conn.commit();
//            return true;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }
    public boolean addOrderItem(int orderID, int customerID, double discount) {
        String insertSQL = "INSERT INTO OrderDetail (orderID, productID, quantity, totalPrice) "
                + "VALUES (?, ?, ?, ?)";

        String updateTotalPriceSQL = "UPDATE OrderInfo "
                + "SET totalPrice = (SELECT SUM(totalPrice) FROM OrderDetail WHERE orderID = ?) "
                + "WHERE orderID = ?";

        try ( Connection conn = dbcontext.getConnection()) {
            conn.setAutoCommit(false);
            CartDAO cartDAO = new CartDAO();

            // Lấy các item trong giỏ hàng
            List<Cart> cartItems = cartDAO.getcart(customerID);

            try ( PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                for (Cart item : cartItems) {
                    double discountedPrice = item.getProduct().getPrice() * (1 - discount); // Tính giá đã giảm
                    insertStmt.setInt(1, orderID);
                    insertStmt.setInt(2, item.getProduct().getProductID());
                    insertStmt.setInt(3, item.getQuantity());
                    insertStmt.setDouble(4, discountedPrice * item.getQuantity()); // Lưu giá đã giảm cho sản phẩm
                    insertStmt.addBatch(); // dùng batch để tối ưu
                }
                insertStmt.executeBatch(); // Thực thi một lần
            }

            // Cập nhật tổng tiền đơn hàng sau khi tính giảm giá
            try ( PreparedStatement updateStmt = conn.prepareStatement(updateTotalPriceSQL)) {
                updateStmt.setInt(1, orderID);
                updateStmt.setInt(2, orderID);
                updateStmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean hasCustomerPurchasedProduct(int customerID, int productID) {
        String sql = "SELECT COUNT(*) FROM OrderInfo oi "
                + "JOIN OrderDetail od ON oi.orderID = od.orderID "
                + "WHERE oi.customerID = ? AND od.productID = ? AND oi.orderStatus = 4";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            ps.setInt(2, productID);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
