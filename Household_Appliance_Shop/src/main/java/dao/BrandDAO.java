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
import model.Brand;

public class BrandDAO {

    private DBContext dbcontext = new DBContext();

    private static final String INSERT_BRAND_SQL = "INSERT INTO Brand (brandName) VALUES (?)";
    private static final String SELECT_BRAND_BY_ID = "SELECT * FROM Brand WHERE brandID = ?";
    private static final String SELECT_ALL_BRANDS = "SELECT * FROM Brand";
    private static final String DELETE_BRAND_SQL = "DELETE FROM Brand WHERE brandID = ?";
    private static final String UPDATE_BRAND_SQL = "UPDATE Brand SET brandName = ? WHERE brandID = ?";

    // 1. Thêm thương hiệu mới
    public void insertBrand(Brand brand) throws SQLException {
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BRAND_SQL)) {
            preparedStatement.setString(1, brand.getBrandName());
            preparedStatement.executeUpdate();
        }
    }

    // 2. Lấy thương hiệu theo ID
    public Brand selectBrand(int brandID) {
        Brand brand = null;
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BRAND_BY_ID)) {
            preparedStatement.setInt(1, brandID);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                brand = new Brand(
                        rs.getInt("brandID"),
                        rs.getString("brandName")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brand;
    }

    // 3. Lấy tất cả thương hiệu
    public List<Brand> selectAllBrands() {
        List<Brand> brands = new ArrayList<>();
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BRANDS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                brands.add(new Brand(
                        rs.getInt("brandID"),
                        rs.getString("brandName")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brands;
    }

    // 4. Cập nhật thương hiệu
    public boolean updateBrand(Brand brand) throws SQLException {
        boolean rowUpdated;
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BRAND_SQL)) {
            preparedStatement.setString(1, brand.getBrandName());
            preparedStatement.setInt(2, brand.getBrandID());
            rowUpdated = preparedStatement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    // 5. Xóa thương hiệu
    public boolean deleteBrand(int brandID) throws SQLException {
        boolean rowDeleted;
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BRAND_SQL)) {
            preparedStatement.setInt(1, brandID);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        }
        return rowDeleted;
    }
}
