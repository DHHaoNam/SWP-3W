/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.CommentDTO;
import DB.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Feedback;
import model.Product;
import model.Voucher;

/**
 *
 * @author Dinh_Hau
 */
public class FeedbackDAO {

    private final DBContext dbcontext = new DBContext();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private static final String SELECT_ALL_FEEDBACL = "SELECT * FROM Feedback";
    private String SELECT_ALL_FEEDBACK;
    private String SELECT_FEEDBACK_BY_ID;

//    public List<Feedback> VỉewFeedback(String productID) {
//        List<Feedback> list = new ArrayList<>(); // Initialize the list
//
//        String sql = "SELECT \n"
//                + "    c.fullName AS [Customer Name],\n"
//                + "    f.comment AS [Comment],\n"
//                + "    f.reply AS [Reply],\n"
//                + "    f.feedback_date AS [Feedback Date]\n"
//                + "FROM \n"
//                + "    [Feedback] f\n"
//                + "JOIN \n"
//                + "    [Customer] c ON f.customerID = c.CustomerID\n"
//                + "WHERE \n"
//                + "    f.comment IS NOT NULL\n"
//                + "    AND f.status = 'approved'\n"
//                + "	AND f.productID = ?\n"
//                + "ORDER BY \n"
//                + "    f.feedback_date DESC;";
//
//        Connection conn = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//
//        try {
//            conn = dbcontext.getConnection(); // Get connection
//            if (conn != null) { // Check if connection is successful
//                ps = conn.prepareStatement(sql); // Prepare statement
//                ps.setString(1, productID);
//                rs = ps.executeQuery(); // Execute query
//                // Use while loop to iterate through ALL rows
//                while (rs.next()) {
//                    Feedback f = new Feedback();
//                    f.setCustomerName(rs.getString("Customer Name")); // Use column names for clarity
//                    f.setComment(rs.getString("Comment"));
//                    f.setReply(rs.getString("Reply"));
//                    f.setFeedback_date(rs.getDate("Feedback Date"));
//
//                    list.add(f); // *** Add the created voucher to the list ***
//                }
//            } else {
//                System.err.println("Database connection failed!"); // Add error handling
//            }
//        } catch (SQLException e) {
//            System.err.println("SQL Error in ViewVoucher: " + e.getMessage());
//        }
//        return list; // Return the populated (or empty) list
//    }
//
//    public List<Feedback> getAllFeedback() {
//        List<Feedback> feedbackList = new ArrayList<>();
//        try (
//                 Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement("SELECT \n"
//                + "    [feedbackID],\n"
//                + "    [productID],\n"
//                + "    [customerId],\n"
//                + "    [comment],\n"
//                + "    [reply],\n"
//                + "    [status],\n"
//                + "    [feedback_date]\n"
//                + "FROM [demo2].[dbo].[Feedback]\n"
//                + "ORDER BY feedbackID DESC;")) {
//            ResultSet rs = preparedStatement.executeQuery();
//            while (rs.next()) {
//                Feedback f = new Feedback();
//                f.setFeedbackID(rs.getInt("feedbackID"));
//                f.setCustomerID(rs.getInt("customerID"));
//                f.setProductID(rs.getInt("productID"));
//                f.setComment(rs.getString("comment"));
//                f.setReply(rs.getString("reply"));
//                f.setFeedback_date(rs.getDate("feedback_date"));
//                f.setStatus(rs.getString("status"));
//                feedbackList.add(f);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return feedbackList;
//    }
//
//    // chọn feedback theo id 
//    public Feedback selectFeedback(int feedbackID) {
//        Feedback feedback = null;
//        String sql = "SELECT * FROM Feedback WHERE feedbackID = ?";
//
//        try (
//                 Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setInt(1, feedbackID);
//            ResultSet rs = preparedStatement.executeQuery();
//
//            if (rs.next()) {
//                feedback = new Feedback();
//                feedback.setFeedbackID(rs.getInt("feedbackID"));
//                feedback.setCustomerID(rs.getInt("customerId"));
//                feedback.setProductID(rs.getInt("productID"));
//                feedback.setComment(rs.getString("comment"));
//                feedback.setReply(rs.getString("reply"));
//                feedback.setFeedback_date(rs.getDate("feedback_date"));
//                feedback.setStatus(rs.getString("status"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return feedback;
//    }
// 3. Lấy tất cả sản phẩm
//    public List<Feedback> selectAllFeedback() {
//        List<Feedback> feedbackList = new ArrayList<>();
//        String sql = "SELECT * FROM Feedback";
//        try (
//                 Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//            ResultSet rs = preparedStatement.executeQuery();
//            while (rs.next()) {
//                Feedback f = new Feedback();
//                f.setFeedbackID(rs.getInt("feedbackID"));
//                f.setCustomerID(rs.getInt("customerID"));
//                f.setProductID(rs.getInt("productID"));
//                f.setComment(rs.getString("comment"));
//                f.setReply(rs.getString("reply"));
//                f.setFeedback_date(rs.getDate("feedback_date"));
//                f.setStatus(rs.getString("status"));
//                feedbackList.add(f);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return feedbackList;
//    }
//
//    public Feedback getFeedbackById(int id) {
//        Feedback feedback = null;
//        String sql = "SELECT * FROM Feedback WHERE feedbackID = ?";
//        try (
//                 Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                feedback = new Feedback();
//                feedback.setFeedbackID(rs.getInt("feedbackID"));
//                feedback.setCustomerID(rs.getInt("customerID"));
//                feedback.setProductID(rs.getInt("productID"));
//                feedback.setComment(rs.getString("comment"));
//                feedback.setReply(rs.getString("reply"));
//                feedback.setFeedback_date(rs.getDate("feedback_date"));
//                feedback.setStatus(rs.getString("status"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return feedback;
//    }
//
//    public void updateFeedback(int id, String reply, String status) {
//        String sql = "UPDATE Feedback SET reply = ?, status = ? WHERE feedbackID = ?";
//        try (
//                 Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, reply);
//            ps.setString(2, status);
//            ps.setInt(3, id);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    public List<Feedback> filterFeedback(String productID, String status, String reply, String sort) {
//        List<Feedback> feedbackList = new ArrayList<>();
//        StringBuilder sql = new StringBuilder("SELECT [feedbackID], [productID], [customerID], [comment], [reply], [status], [feedback_date] FROM [dbo].[Feedback] WHERE 1=1");
//
//        // Lọc theo productID
//        if (productID != null && !productID.isEmpty() && !productID.equals("Enter Product ID")) {
//            sql.append(" AND [productID] = ?");
//        }
//
//        // Lọc theo status (pending, approved, rejected) bằng switch-case
//        if (status != null && !status.equals("ALL Status")) {
//            switch (status.toLowerCase()) {
//                case "pending":
//                    sql.append(" AND [status] = 'pending'");
//                    break;
//                case "approved":
//                    sql.append(" AND [status] = 'approved'");
//                    break;
//                case "rejected":
//                    sql.append(" AND [status] = 'rejected'");
//                    break;
//                default:
//                    // Không thêm điều kiện nếu status không hợp lệ
//                    break;
//            }
//        }
//
//        // Lọc theo reply (hasReply: not null, noReply: null)
//        if (reply != null && !reply.equals("ALL")) {
//            if (reply.equals("hasReply")) {
//                sql.append(" AND [reply] IS NOT NULL");
//            } else if (reply.equals("noReply")) {
//                sql.append(" AND [reply] = ''");
//            }
//        }
//
//        // Sắp xếp theo feedback_date (oldest, latest)
//        if (sort != null && !sort.equals("Default")) {
//            if (sort.equals("oldest")) {
//                sql.append(" ORDER BY [feedback_date] ASC");
//            } else if (sort.equals("latest")) {
//                sql.append(" ORDER BY [feedback_date] DESC");
//            }
//        }
//
//        try (
//                 Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql.toString())) {
//            int paramIndex = 1;
//
//            // Gán giá trị cho productID
//            if (productID != null && !productID.isEmpty() && !productID.equals("Enter Product ID")) {
//                ps.setString(paramIndex++, productID);
//            }
//
//            // Thực thi truy vấn và xử lý kết quả
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                Feedback f = new Feedback();
//                f.setFeedbackID(rs.getInt("feedbackID"));
//                f.setCustomerID(rs.getInt("customerID"));
//                f.setProductID(rs.getInt("productID"));
//                f.setComment(rs.getString("comment"));
//                f.setReply(rs.getString("reply"));
//                f.setFeedback_date(rs.getDate("feedback_date"));
//                f.setStatus(rs.getString("status"));
//                feedbackList.add(f);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return feedbackList;
//    }
    public List<Feedback> findAllByProductId(int productId) {
        List<Feedback> feedbacks = new LinkedList<>();
        String sql = "SELECT * FROM Feedback WHERE productID = ? and isDeleted = 0";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Feedback feedback = new Feedback();
                    feedback.setFeedbackID(rs.getInt("feedbackID"));
                    feedback.setProductID(rs.getInt("productID"));
                    feedback.setCustomerID(rs.getInt("customerID"));
                    feedback.setRating(rs.getInt("rating"));
                    feedback.setComment(rs.getString("comment"));
                    feedback.setDeleted(rs.getBoolean("isDeleted"));
                    feedback.setCreateAt(rs.getDate("createAt"));
                    feedback.setCustomer(customerDAO.getCustomerById(feedback.getCustomerID()));
                    feedbacks.add(feedback);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return feedbacks;
    }

    public void insert(Feedback feedback) {
        String sql = "INSERT INTO Feedback values (?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            int seqIdx = 1;
            ps.setInt(seqIdx++, feedback.getProductID());
            ps.setInt(seqIdx++, feedback.getCustomerID());
            ps.setInt(seqIdx++, feedback.getRating());
            ps.setString(seqIdx++, feedback.getComment());
            ps.setBoolean(seqIdx++, feedback.isDeleted());
            ps.setDate(seqIdx++, feedback.getCreateAt());
            ps.setInt(seqIdx++, feedback.getOrderDetailID());

            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    public double getAvgRatingByProductID(int productId) {
        String sql = "SELECT AVG(rating) FROM Feedback WHERE productID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return 0;
    }

    public boolean hasCustomerFeedbackForProduct(int customerId, int productId) {
        String sql = "SELECT COUNT(*) FROM Feedback WHERE customerID = ? AND productID = ? AND isDeleted = 0";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.setInt(2, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return false;
    }

    public int getCountRatingByProductID(int productId) {
        String sql = "SELECT COUNT(*) FROM Feedback WHERE productID = ? AND isDeleted = 0";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * Get a feedback by its ID
     *
     * @param feedbackId The ID of the feedback to retrieve
     * @return The feedback with the specified ID, or null if not found
     */
    public Feedback getFeedbackById(int feedbackId) {
        String sql = "SELECT * FROM Feedback WHERE feedbackID = ? AND isDeleted = 0";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, feedbackId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Feedback feedback = new Feedback();
                    feedback.setFeedbackID(rs.getInt("feedbackID"));
                    feedback.setProductID(rs.getInt("productID"));
                    feedback.setCustomerID(rs.getInt("customerID"));
                    feedback.setRating(rs.getInt("rating"));
                    feedback.setComment(rs.getString("comment"));
                    feedback.setDeleted(rs.getBoolean("isDeleted"));
                    feedback.setCreateAt(rs.getDate("createAt"));
                    return feedback;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Update an existing feedback
     *
     * @param feedback The feedback to update
     * @return true if the update was successful, false otherwise
     */
    public boolean update(Feedback feedback) {
        String sql = "UPDATE Feedback SET rating = ?, comment = ? WHERE feedbackID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, feedback.getRating());
            ps.setString(2, feedback.getComment());
            ps.setInt(3, feedback.getFeedbackID());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Soft delete a feedback by setting isDeleted to true
     *
     * @param feedbackId The ID of the feedback to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean delete(int feedbackId) {
        String sql = "UPDATE Feedback SET isDeleted = 1 WHERE feedbackID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, feedbackId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return false;
    }

//    lay product chua cmt
    public List<Product> getProductsWithComments() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT DISTINCT\n"
                + "    p.productID,\n"
                + "    p.productName,\n"
                + "    p.image\n"
                + "FROM \n"
                + "    Product p\n"
                + "JOIN \n"
                + "    OrderDetail od ON p.productID = od.productID\n"
                + "JOIN \n"
                + "    Feedback f ON od.orderDetailID = f.orderDetailID\n"
                + "WHERE \n"
                + "    f.comment IS NOT NULL AND f.isDeleted = 0;";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("productID");
                String name = rs.getString("productName");
                String image = rs.getString("image");  // Get image field
                list.add(new Product(id, name, image));  // Updated constructor to include image
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

//  LAY CMT THEO PRODUCT
    public List<CommentDTO> getCommentByProduct(int productId) {
        List<CommentDTO> comments = new ArrayList<>();

        String sql = "SELECT "
                + "f.feedbackID, f.comment, f.createAt, "
                + "c.customerID, c.fullName AS customerName "
                + "FROM Feedback f "
                + "JOIN OrderDetail od ON f.orderDetailID = od.orderDetailID "
                + "JOIN OrderInfo o ON od.orderID = o.orderID "
                + "JOIN Customer c ON o.customerID = c.customerID "
                + "WHERE od.productID = ? AND f.isDeleted = 0";

        try (
                 Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CommentDTO comment = new CommentDTO(
                            rs.getInt("feedbackID"),
                            rs.getString("comment"),
                            rs.getDate("createAt"),
                            rs.getInt("customerID"),
                            rs.getString("customerName")
                    );
                    comments.add(comment);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving feedback list: " + e.getMessage());
        }

        return comments;
    }

//    xóa mềm
    public void softDeleteFeedback(int feedbackID) {
        String sql = "UPDATE Feedback SET isDeleted = 1 WHERE feedbackID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, feedbackID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
