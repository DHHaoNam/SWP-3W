package dao;

import DB.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Category;
import model.Voucher;

/**
 *
 * @author Dinh_Hau
 */
public class VoucherDAO {

    private final DBContext dbcontext = new DBContext();

    public List<Voucher> ViewVoucher() {
        List<Voucher> list = new ArrayList<>(); // Initialize the list

        String sql = "SELECT TOP (1000)\n"
                + "    v.voucherID,\n"
                + "    v.title,\n"
                + "    v.discount,\n"
                + "    v.status,\n"
                + "    v.categoryID,\n"
                + "    c.categoryName\n"
                + "FROM \n"
                + "    Voucher v\n"
                + "JOIN \n"
                + "    Category c ON v.categoryID = c.categoryID\n"
                + "WHERE \n"
                + "    v.status = 'active';";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dbcontext.getConnection(); // Get connection
            if (conn != null) { // Check if connection is successful
                ps = conn.prepareStatement(sql); // Prepare statement
                rs = ps.executeQuery(); // Execute query

                // Use while loop to iterate through ALL rows
                while (rs.next()) {
                    Voucher v = new Voucher(); // Create a new Voucher object for each row
                    v.setVoucherID(rs.getInt("voucherID")); // Use column names for clarity
                    v.setTitle(rs.getString("title"));
                    v.setDiscount(rs.getDouble("discount"));
                    v.setStatus(rs.getString("status"));
                    v.setCategoryID(rs.getInt("categoryID"));
                    v.setCategoryName(rs.getString("categoryName"));

                    list.add(v); // *** Add the created voucher to the list ***
                }
            } else {
                System.err.println("Database connection failed!"); // Add error handling
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in ViewVoucher: " + e.getMessage());
        }
        return list; // Return the populated (or empty) list
    }

    public List<Voucher> filterVoucher(String voucherID, String status, String categoryID, String sort) {
        List<Voucher> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT v.voucherID, v.title, v.discount, v.status, v.categoryID, c.categoryName "
                + "FROM Voucher v JOIN Category c ON v.categoryID = c.categoryID WHERE 1=1");

        if (voucherID != null && !voucherID.isEmpty()) {
            sql.append(" AND v.voucherID = ?");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND v.status = ?");
        }
        if (categoryID != null && !categoryID.isEmpty()) {
            sql.append(" AND v.categoryID = ?");
        }
        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "oldest":
                    sql.append(" ORDER BY v.voucherID ASC");
                    break;
                case "latest":
                    sql.append(" ORDER BY v.voucherID DESC");
                    break;
                case "discount_asc":
                    sql.append(" ORDER BY v.discount ASC");
                    break;
                case "discount_desc":
                    sql.append(" ORDER BY v.discount DESC");
                    break;
                default:
                    break;
            }
        }

        try (
                 Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (voucherID != null && !voucherID.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(voucherID));
            }
            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }
            if (categoryID != null && !categoryID.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(categoryID));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Voucher v = new Voucher();
                v.setVoucherID(rs.getInt("voucherID"));
                v.setTitle(rs.getString("title"));
                v.setDiscount(rs.getDouble("discount"));
                v.setStatus(rs.getString("status"));
                v.setCategoryID(rs.getInt("categoryID"));
                v.setCategoryName(rs.getString("categoryName"));
                list.add(v);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in filterVoucher: " + e.getMessage());
        }
        return list;
    }

    public List<Category> getAllCategory() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT [categoryID], [categoryName] FROM [Category]";

        try (
                 Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category c = new Category();
                c.setCategoryID(rs.getInt("categoryID"));
                c.setCategoryName(rs.getString("categoryName"));
                list.add(c);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getAllCategory: " + e.getMessage());
        }
        return list;
    }

    public Voucher getVoucherById(int voucherID) {
        Voucher voucher = null;
        String sql = "SELECT v.voucherID, v.title, v.discount, v.status, v.categoryID, c.categoryName "
                + "FROM Voucher v JOIN Category c ON v.categoryID = c.categoryID WHERE v.voucherID = ?";

        try (
                 Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, voucherID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                voucher = new Voucher();
                voucher.setVoucherID(rs.getInt("voucherID"));
                voucher.setTitle(rs.getString("title"));
                voucher.setDiscount(rs.getDouble("discount"));
                voucher.setStatus(rs.getString("status"));
                voucher.setCategoryID(rs.getInt("categoryID"));
                voucher.setCategoryName(rs.getString("categoryName"));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getVoucherById: " + e.getMessage());
        }
        return voucher;
    }

    public void updateVoucher(int voucherID, String title, double discount, int categoryID, String status) {
        String sql = "UPDATE Voucher SET title = ?, discount = ?, categoryID = ?, status = ? WHERE voucherID = ?";

        try (
                 Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setDouble(2, discount);
            ps.setInt(3, categoryID);
            ps.setString(4, status);
            ps.setInt(5, voucherID);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL Error in updateVoucher: " + e.getMessage());
        }
    }

    public void addVoucher(String title, double discount, int categoryID, String status) {
        String sql = "INSERT INTO Voucher (title, discount, categoryID, status) VALUES (?, ?, ?, ?)";

        try (
                 Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setDouble(2, discount);
            ps.setInt(3, categoryID);
            ps.setString(4, status);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL Error in addVoucher: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        VoucherDAO dao = new VoucherDAO();
        List<Category> categoryList = dao.getAllCategory();
        for (Category category : categoryList) {
            System.out.println(category);
        }
    }
}
