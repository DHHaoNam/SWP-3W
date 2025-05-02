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
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Comment;
import model.Feedback;

/**
 *
 * @author Acer
 */
public class CommentDAO {

    private final DBContext dbcontext = new DBContext();
    private final CustomerDAO customerDAO = new CustomerDAO();

    public List<Comment> findAllByProductId(int productId) {
        List<Comment> comments = new LinkedList<>();
        String sql = "SELECT * FROM Comment WHERE productID = ? and deleted = 0";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment();
                    comment.setCommentID(rs.getInt("commentID"));
                    comment.setContent(rs.getString("content"));
                    comment.setCustomerID(rs.getInt("customerID"));
                    comment.setCustomer(customerDAO.getCustomerById(comment.getCustomerID()));
                    comment.setProductID(rs.getInt("productID"));
                    comment.setDeleted(rs.getBoolean("deleted"));
                    comment.setCreatedAt(rs.getTimestamp("createdAt"));
                    comment.setUpdatedAt(rs.getTimestamp("updatedAt"));
                    comments.add(comment);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return comments;
    }

    public void insert(Comment model) {
        String sql = "INSERT INTO Comment values (?, ?, ?, ?, ?, ?)";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            int seqIdx = 1;
            ps.setString(seqIdx++, model.getContent());
            ps.setInt(seqIdx++, model.getCustomerID());
            ps.setInt(seqIdx++, model.getProductID());
            ps.setBoolean(seqIdx++, model.isDeleted());
            ps.setTimestamp(seqIdx++, model.getCreatedAt());
            ps.setTimestamp(seqIdx++, model.getUpdatedAt());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    public void update(Comment model) {
        String sql = "  UPDATE Comment SET content = ?, updatedAt = ? WHERE commentID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            int seqIdx = 1;
            ps.setString(seqIdx++, model.getContent());
            ps.setTimestamp(seqIdx++, model.getUpdatedAt());
            ps.setInt(seqIdx++, model.getCommentID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    public void updateDeleted(Comment model) {
        String sql = "UPDATE Comment SET deleted = ? WHERE commentID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            int seqIdx = 1;
            ps.setBoolean(seqIdx++, model.isDeleted());
            ps.setInt(seqIdx++, model.getCommentID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    /**
     * Lấy comment theo ID
     *
     * @param commentId ID của comment cần lấy
     * @return Comment object hoặc null nếu không tìm thấy
     */
    public Comment getCommentById(int commentId) {
        Comment comment = null;
        String sql = "SELECT * FROM Comment WHERE commentID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commentId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    comment = new Comment();
                    comment.setCommentID(rs.getInt("commentID"));
                    comment.setContent(rs.getString("content"));
                    comment.setCustomerID(rs.getInt("customerID"));
                    comment.setCustomer(customerDAO.getCustomerById(comment.getCustomerID()));
                    comment.setProductID(rs.getInt("productID"));
                    comment.setDeleted(rs.getBoolean("deleted"));
                    comment.setCreatedAt(rs.getTimestamp("createdAt"));
                    comment.setUpdatedAt(rs.getTimestamp("updatedAt"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommentDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return comment;
    }
}
