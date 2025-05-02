package dao;

import DB.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Attribute;
import model.ProductAttribute;

public class AttributeDAO {

    private DBContext dbcontext = new DBContext();

    // CREATE
    public int insertAttribute(String name) {
        String sql = "INSERT INTO Attribute (attributeName) VALUES (?)";
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the generated ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // READ (get all)
    public List<Attribute> getAllAttributes() {
        List<Attribute> list = new ArrayList<>();
        String sql = "SELECT * FROM Attribute";
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement ps = connection.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Attribute attr = new Attribute();
                attr.setAttributeID(rs.getInt("attributeID"));
                attr.setAttributeName(rs.getString("attributeName"));
                list.add(attr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // READ (get by ID)
    public Attribute getAttributeByID(int id) {
        String sql = "SELECT * FROM Attribute WHERE attributeID = ?";
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Attribute attr = new Attribute();
                    attr.setAttributeID(rs.getInt("attributeID"));
                    attr.setAttributeName(rs.getString("attributeName"));
                    return attr;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    public boolean updateAttribute(int id, String name) {
        String sql = "UPDATE Attribute SET attributeName = ? WHERE attributeID = ?";
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Link attribute to category
    public void linkAttributeToCategory(int attributeID, int categoryID) {
        String sql = "INSERT INTO categoryAttribute (attributeID, categoryID) VALUES (?, ?)";
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, attributeID);
            ps.setInt(2, categoryID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get attribute by category
    public List<Attribute> getAttributeByCategoryID(int categoryID) {
        List<Attribute> attributeList = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "SELECT a.attributeID, a.attributeName "
                + "FROM categoryAttribute ca "
                + "JOIN Attribute a ON ca.attributeID = a.attributeID "
                + "WHERE ca.categoryID = ?";

        try {
            Connection conn = dbcontext.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, categoryID);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("attributeID");
                String name = rs.getString("attributeName");
                attributeList.add(new Attribute(id, name));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignored) {
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ignored) {
            }
        }

        return attributeList;
    }

    //check same name
    public boolean isAttributeInCategory(String attributeName, int categoryID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM CategoryAttribute ca "
                + "JOIN Attribute a ON ca.AttributeID = a.AttributeID "
                + "WHERE a.AttributeName = ? AND ca.CategoryID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, attributeName);
            ps.setInt(2, categoryID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

// check for update
    public boolean isDuplicateNameInCategory(String name, int currentAttrId, int categoryID) {
        String sql = "SELECT COUNT(*) FROM Attribute a "
                + "JOIN categoryAttribute ca ON a.attributeID = ca.attributeID "
                + "WHERE a.attributeName = ? AND ca.categoryID = ? AND a.attributeID != ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, categoryID);
            ps.setInt(3, currentAttrId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCategoryAttributeByAttributeID(int attributeID) {

        Connection conn = dbcontext.getConnection();
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM categoryAttribute WHERE attributeID = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, attributeID);
            int rows = ps.executeUpdate();
            return rows >= 0; // Có thể = 0 nếu không có liên kết, vẫn coi là thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAttribute(int attributeID) {
        String sql = "DELETE FROM Attribute WHERE attributeID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, attributeID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertProductAttribute(int productID, int attributeID, String value) {
        String sql = "INSERT INTO ProductAttribute (productID, attributeID, Value) VALUES (?, ?, ?)";
        try (
                 Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productID);
            ps.setInt(2, attributeID);
            ps.setString(3, value);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //get by product id
    public List<ProductAttribute> getAttributesByProductID(int productID) {
        List<ProductAttribute> list = new ArrayList<>();
        String sql = "SELECT productID, attributeID, value FROM ProductAttribute WHERE productID = ?";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProductAttribute pa = new ProductAttribute(
                        rs.getInt("productID"),
                        rs.getInt("attributeID"),
                        rs.getString("value")
                );
                list.add(pa);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}



