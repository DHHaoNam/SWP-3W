/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author TRUNG NHAN
 */
import DB.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Category;

public class CategoryDAO {

    private DBContext dbcontext = new DBContext();

    private static final String INSERT_CATEGORY_SQL = "INSERT INTO Category (categoryName) VALUES (?)";
    private static final String SELECT_CATEGORY_BY_ID = "SELECT * FROM Category WHERE categoryID = ?";
    private static final String SELECT_ALL_CATEGORIES = "SELECT * FROM Category";
    private static final String DELETE_CATEGORY_SQL = "DELETE FROM Category WHERE categoryID = ?";
    private static final String UPDATE_CATEGORY_SQL = "UPDATE Category SET categoryName = ? WHERE categoryID = ?";

    // 1. Thêm danh mục
    public void insertCategory(Category category) throws SQLException {
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATEGORY_SQL)) {
            preparedStatement.setString(1, category.getCategoryName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. Lấy danh mục theo ID
    public Category selectCategory(int categoryID) {
        Category category = null;
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CATEGORY_BY_ID)) {
            preparedStatement.setInt(1, categoryID);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                category = new Category(
                        rs.getInt("categoryID"),
                        rs.getString("categoryName")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    // 3. Lấy tất cả danh mục
    public List<Category> selectAllCategories() {
        List<Category> categories = new ArrayList<>();
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CATEGORIES)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("categoryID"),
                        rs.getString("categoryName")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    // 4. Cập nhật danh mục
    public boolean updateCategory(Category category) throws SQLException {
        boolean rowUpdated;
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CATEGORY_SQL)) {
            preparedStatement.setString(1, category.getCategoryName());
            preparedStatement.setInt(2, category.getCategoryID());
            rowUpdated = preparedStatement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    // 5. Xóa danh mục
    public boolean deleteCategory(int categoryID) throws SQLException {
        boolean rowDeleted;
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CATEGORY_SQL)) {
            preparedStatement.setInt(1, categoryID);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    //check category name
    public boolean isCategoryExists(String name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Category WHERE categoryName = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu số lượng > 0, tức là danh mục đã tồn tại
            }
        }
        return false;
    }

    //check
    public boolean isCategoryExistsForUpdate(int id, String name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Category WHERE categoryName = ? AND categoryID != ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu số lượng > 0, tức là có danh mục khác đang dùng tên này
            }
        }
        return false;
    }

    public int getCategoryIDBySubCategoryID(int subCategoryID) {
        String sql = "SELECT categoryID FROM SubCategory WHERE subCategoryID = ?";
        try (
                 Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subCategoryID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("categoryID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // hoặc throw exception nếu không tìm thấy
    }

}
