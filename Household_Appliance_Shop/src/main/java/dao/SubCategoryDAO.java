/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import DB.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.SubCategory;

public class SubCategoryDAO {

    private DBContext dbcontext = new DBContext();

    private static final String INSERT_SUBCATEGORY_SQL = "INSERT INTO SubCategory (subCategoryName, categoryID) VALUES (?, ?)";
    private static final String SELECT_SUBCATEGORY_BY_ID = "SELECT * FROM SubCategory WHERE subCategoryID = ?";
    private static final String SELECT_ALL_SUBCATEGORIES = "SELECT * FROM SubCategory";
    private static final String SELECT_SUBCATEGORIES_BY_CATEGORY_ID = "SELECT * FROM SubCategory WHERE categoryID = ?";
    private static final String DELETE_SUBCATEGORY_SQL = "DELETE FROM SubCategory WHERE subCategoryID = ?";
    private static final String UPDATE_SUBCATEGORY_SQL = "UPDATE SubCategory SET subCategoryName = ?, categoryID = ? WHERE subCategoryID = ?";

    // 1. Thêm SubCategory
    public void insertSubCategory(SubCategory subCategory) throws SQLException {
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SUBCATEGORY_SQL)) {
            preparedStatement.setString(1, subCategory.getSubCategoryName());
            preparedStatement.setInt(2, subCategory.getCategoryID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. Lấy SubCategory theo ID
    public SubCategory selectSubCategory(int subCategoryID) {
        SubCategory subCategory = null;
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SUBCATEGORY_BY_ID)) {
            preparedStatement.setInt(1, subCategoryID);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                subCategory = new SubCategory(
                        rs.getInt("subCategoryID"),
                        rs.getString("subCategoryName"),
                        rs.getInt("categoryID")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subCategory;
    }

    // 3. Lấy tất cả SubCategory
    public List<SubCategory> selectAllSubCategories() {
        List<SubCategory> list = new ArrayList<>();
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SUBCATEGORIES)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(new SubCategory(
                        rs.getInt("subCategoryID"),
                        rs.getString("subCategoryName"),
                        rs.getInt("categoryID")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 4. Lấy danh sách SubCategory theo CategoryID
    public List<SubCategory> selectSubCategoriesByCategoryID(int categoryID) {
        List<SubCategory> list = new ArrayList<>();
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SUBCATEGORIES_BY_CATEGORY_ID)) {
            preparedStatement.setInt(1, categoryID);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(new SubCategory(
                        rs.getInt("subCategoryID"),
                        rs.getString("subCategoryName"),
                        rs.getInt("categoryID")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 5. Cập nhật SubCategory
    public boolean updateSubCategory(SubCategory subCategory) throws SQLException {
        boolean rowUpdated;
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SUBCATEGORY_SQL)) {
            preparedStatement.setString(1, subCategory.getSubCategoryName());
            preparedStatement.setInt(2, subCategory.getCategoryID());
            preparedStatement.setInt(3, subCategory.getSubCategoryID());
            rowUpdated = preparedStatement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    // 6. Xóa SubCategory
    public boolean deleteSubCategory(int subCategoryID) throws SQLException {
        boolean rowDeleted;
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SUBCATEGORY_SQL)) {
            preparedStatement.setInt(1, subCategoryID);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    // 7. Kiểm tra tên SubCategory có tồn tại chưa
    public boolean isSubCategoryExists(String name, int categoryID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM SubCategory WHERE subCategoryName = ? AND categoryID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, categoryID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // 8. Kiểm tra SubCategory trùng tên (khi update)
    public boolean isSubCategoryExistsForUpdate(int id, String name, int categoryID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM SubCategory WHERE subCategoryName = ? AND categoryID = ? AND subCategoryID != ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, categoryID);
            stmt.setInt(3, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    //get category by sub id
    public int getCategoryIDBySubCategoryID(int subCategoryID) {
        int categoryID = -1;
        String sql = "SELECT categoryID FROM SubCategory WHERE subCategoryID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subCategoryID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                categoryID = rs.getInt("categoryID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryID;
    }

}



