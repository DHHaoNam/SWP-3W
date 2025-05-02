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
import model.Category;
import model.Product;
import model.ProductAttribute;
import model.SubCategory;

public class ProductDAO {

    private DBContext dbcontext = new DBContext();

    private static final String INSERT_PRODUCT_SQL = "INSERT INTO Product (productName, description, subCategoryID, price, stock_Quantity, brandID, image) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_PRODUCT_BY_ID = "SELECT * FROM Product WHERE productID = ?";
    private static final String SELECT_ALL_PRODUCTS = "SELECT * FROM Product";
    private static final String DELETE_PRODUCT_SQL = "DELETE FROM Product WHERE productID = ?";
    private static final String UPDATE_PRODUCT_SQL = "UPDATE Product SET productName=?, description=?, subCategoryID=?, price=?, stock_Quantity=?, brandID=?, image=? WHERE productID=?";
    private static final String SELECT_PRODUCT_BY_CATEGORYID = "SELECT * FROM Product WHERE subCategoryID = ?";
    private static final String SEARCH_PRODUCT = "SELECT * FROM Product WHERE productName LIKE ?";

    private static final String SELECT_ALL_CATEGORYS = "SELECT * FROM Category";
    private static final String SELECT_ALL_BRANDS = "SELECT * FROM Brand";
    private static final String SELECT_ALL_PRODUCTS_BY_NAME = "SELECT * FROM Product WHERE productName LIKE ?";
    private static final String SELECT_ALL_PRODUCTS_BY_CATEGORYID = "SELECT * FROM Product WHERE subCategoryID = ?";

    // 1. Thêm sản phẩm
    public int insertProduct(Product product) {
        String sql = "INSERT INTO Product (productName, description, price, stock_Quantity, subCategoryID, brandID, image) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int productID = -1;

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, product.getProductName());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getStock_Quantity());
            ps.setInt(5, product.getSubCategoryID());
            ps.setInt(6, product.getBrandID());
            ps.setString(7, product.getImage());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    productID = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productID;
    }

    // 2. Lấy sản phẩm theo ID
    public Product selectProduct(int productID) {
        Product product = null;
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_ID)) {
            preparedStatement.setInt(1, productID);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                product = new Product(
                        rs.getInt("productID"),
                        rs.getString("productName"),
                        rs.getString("description"),
                        rs.getInt("subCategoryID"),
                        rs.getDouble("price"),
                        rs.getInt("stock_Quantity"),
                        rs.getInt("brandID"),
                        rs.getString("image")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    // 3. Lấy tất cả sản phẩm
    public List<Product> selectAllProducts() {
        List<Product> products = new ArrayList<>();
        try (
                 Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("productID"),
                        rs.getString("productName"),
                        rs.getString("description"),
                        rs.getInt("subCategoryID"),
                        rs.getDouble("price"),
                        rs.getInt("stock_Quantity"),
                        rs.getInt("brandID"),
                        rs.getString("image")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // 4. Cập nhật sản phẩm
    public boolean updateProduct(Product product) throws SQLException {
        boolean rowUpdated;
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_SQL)) {
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setInt(3, product.getSubCategoryID());
            preparedStatement.setDouble(4, product.getPrice());
            preparedStatement.setInt(5, product.getStock_Quantity());
            preparedStatement.setInt(6, product.getBrandID());
            preparedStatement.setString(7, product.getImage());
            preparedStatement.setInt(8, product.getProductID());
            rowUpdated = preparedStatement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    // 5. Xóa sản phẩm
    public boolean deleteProduct(int id) {
        boolean rowDeleted = false;
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRODUCT_SQL)) {

            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0; // Kiểm tra số dòng bị ảnh hưởng

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowDeleted;
    }

    public List<Product> selectProductBycategory(int categoryID) {
        List<Product> list = new ArrayList<>();
        try {
            Connection connection = dbcontext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_CATEGORYID);
            preparedStatement.setInt(1, categoryID);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("productID"),
                        rs.getString("productName"),
                        rs.getString("description"),
                        rs.getInt("subCategoryID"),
                        rs.getDouble("price"),
                        rs.getInt("stock_Quantity"),
                        rs.getInt("brandID"),
                        rs.getString("image")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
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

    //7. Seach product by name
    public List<Product> searchProduct(String name) {
        List<Product> list = new ArrayList<>();
        try {
            Connection connection = dbcontext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_PRODUCT);
            preparedStatement.setString(1, "%" + name + "%");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("productID"),
                        rs.getString("productName"),
                        rs.getString("description"),
                        rs.getInt("subCategoryID"),
                        rs.getDouble("price"),
                        rs.getInt("stock_Quantity"),
                        rs.getInt("brandID"),
                        rs.getString("image")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> filterProductsByPrice(Double minPrice, Double maxPrice) throws SQLException {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE 1=1";

        if (minPrice != null) {
            sql += " AND price >= ?";
        }
        if (maxPrice != null) {
            sql += " AND price <= ?";
        }

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;
            if (minPrice != null) {
                ps.setDouble(index++, minPrice);
            }
            if (maxPrice != null) {
                ps.setDouble(index++, maxPrice);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("productID"),
                        rs.getString("productName"),
                        rs.getString("description"),
                        rs.getInt("subCategoryID"),
                        rs.getDouble("price"),
                        rs.getInt("stock_Quantity"),
                        rs.getInt("brandID"),
                        rs.getString("image")
                );
                productList.add(product);
            }
        }
        return productList;
    }

    public int getProductStock(int productId) {
        int stockQuantity = 0;
        String query = "SELECT stock_Quantity FROM Product WHERE productID = ?";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                stockQuantity = rs.getInt("stock_quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stockQuantity;
    }

    public Product getProductBYID(String productID) {
        String sql = "SELECT * FROM Product WHERE productID = ?";
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setString(1, productID);
            ResultSet pr = prepareStatement.executeQuery();
            if (pr.next()) {
                return new Product(pr.getInt("productID"),
                        pr.getString("productName"),
                        pr.getString("description"),
                        pr.getInt("subCategoryID"),
                        pr.getDouble("price"),
                        pr.getInt("stock_Quantity"),
                        pr.getInt("brandID"),
                        pr.getString("image"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Product getProductById(int productID) {
        String sql = "SELECT * FROM Product WHERE productID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productID);
            ResultSet pr = ps.executeQuery();
            if (pr.next()) {
                return new Product(pr.getInt("productID"),
                        pr.getString("productName"),
                        pr.getString("description"),
                        pr.getInt("subCategoryID"),
                        pr.getDouble("price"),
                        pr.getInt("stock_Quantity"),
                        pr.getInt("brandID"),
                        pr.getString("image"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateProductQuantity(int productID, int stockQuantity) throws SQLException {
        String sql = "UPDATE Product SET stock_Quantity = ? WHERE productID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, stockQuantity);
            stmt.setInt(2, productID);
            stmt.executeUpdate();
        }
    }

    public boolean updateStock(int productID, int quantity) {
        String sql = "UPDATE Product SET stock_Quantity = stock_Quantity - ? WHERE productID = ? AND stock_Quantity >= ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productID);
            ps.setInt(3, quantity);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Product> getProductsByOrderId(int orderID) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT p.productID, p.productName, p.description, p.subCategoryID, p.price,\n"
                + "p.stock_Quantity, p.brandID, p.image \n"
                + "FROM Product p\n"
                + "JOIN OrderDetail od ON p.productID = od.productID\n"
                + "WHERE od.orderID = ?";

        try {
            Connection connection = dbcontext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, orderID);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("productID"),
                        rs.getString("productName"),
                        rs.getString("description"),
                        rs.getInt("subCategoryID"),
                        rs.getDouble("price"),
                        rs.getInt("stock_Quantity"),
                        rs.getInt("brandID"),
                        rs.getString("image")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteProductAttributes(int productID) {
        String sql = "DELETE FROM ProductAttribute WHERE productID = ?";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productID);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int countProduct() {
        String sql = "SELECT COUNT(*) FROM Product";
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Product> selectProductsByBrand(int brandId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE BrandID = ?";

        try ( Connection connection = dbcontext.getConnection();  PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, brandId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ProductID");
                String name = rs.getString("productName");
                String description = rs.getString("Description");
                int categoryId = rs.getInt("subCategoryID");
                double price = rs.getDouble("Price");
                int stockQuantity = rs.getInt("Stock_Quantity");
                String imageUrl = rs.getString("image");
                int brandID = rs.getInt("BrandID");
                products.add(new Product(id, name, description, categoryId, price, stockQuantity, brandId, imageUrl));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> pagingProducts(int index, Integer categoryid, Integer subcategoryid, Integer brandid) {
        StringBuilder sql = new StringBuilder("SELECT p.* FROM Product p ");

        boolean hasJoin = false;
        boolean hasWhereClause = false;

        if (categoryid != null) {
            sql.append("JOIN SubCategory s ON p.SubCategoryID = s.SubCategoryID ");
            hasJoin = true;
        }

        if (categoryid != null) {
            sql.append("WHERE s.CategoryID = ? ");
            hasWhereClause = true;
        }

        if (subcategoryid != null) {
            sql.append(hasWhereClause ? "AND " : "WHERE ");
            sql.append("p.SubCategoryID = ? ");
            hasWhereClause = true;
        }

        if (brandid != null) {
            sql.append(hasWhereClause ? "AND " : "WHERE ");
            sql.append("p.BrandID = ? ");
        }

        sql.append("ORDER BY p.ProductID OFFSET ? ROWS FETCH NEXT 12 ROWS ONLY");

        List<Product> products = new ArrayList<>();
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (categoryid != null) {
                ps.setInt(paramIndex++, categoryid);
            }
            if (subcategoryid != null) {
                ps.setInt(paramIndex++, subcategoryid);
            }
            if (brandid != null) {
                ps.setInt(paramIndex++, brandid);
            }
            ps.setInt(paramIndex, (index - 1) * 12);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("productID"),
                        rs.getString("productName"),
                        rs.getString("Description"),
                        rs.getInt("subCategoryID"),
                        rs.getDouble("price"),
                        rs.getInt("stock_Quantity"),
                        rs.getInt("brandID"),
                        rs.getString("image")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<SubCategory> selectAllSubCategories() {
        List<SubCategory> subcategories = new ArrayList<>();
        String sql = "SELECT * FROM SubCategory";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("subCategoryID");
                String name = rs.getString("subCategoryName");
                int categoryId = rs.getInt("categoryID");
                subcategories.add(new SubCategory(id, name, categoryId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subcategories;
    }

    public int countProductByFilters(Integer categoryid, Integer subcategoryid, Integer brandid) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Product p ");
        boolean hasJoin = false;
        boolean hasWhereClause = false;

        if (categoryid != null) {
            sql.append("JOIN SubCategory s ON p.SubCategoryID = s.SubCategoryID ");
        }

        if (categoryid != null) {
            sql.append("WHERE s.CategoryID = ? ");
            hasWhereClause = true;
        }

        if (subcategoryid != null) {
            sql.append(hasWhereClause ? "AND " : "WHERE ");
            sql.append("p.SubCategoryID = ? ");
            hasWhereClause = true;
        }

        if (brandid != null) {
            sql.append(hasWhereClause ? "AND " : "WHERE ");
            sql.append("p.BrandID = ? ");
        }

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (categoryid != null) {
                ps.setInt(paramIndex++, categoryid);
            }
            if (subcategoryid != null) {
                ps.setInt(paramIndex++, subcategoryid);
            }
            if (brandid != null) {
                ps.setInt(paramIndex++, brandid);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<Category> selectAllCategorys() {
        List<Category> categorys = new ArrayList<>();
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement prepareStatement = connection.prepareStatement(SELECT_ALL_CATEGORYS)) {
            ResultSet rs = prepareStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("categoryID");
                String name = rs.getString("categoryName");
                categorys.add(new Category(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorys;
    }

    public List<Brand> selectAllBrand() {
        List<Brand> brands = new ArrayList<>();
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement prepareStatement = connection.prepareStatement(SELECT_ALL_BRANDS)) {
            ResultSet rs = prepareStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("BrandID");
                String name = rs.getString("brandName");
                brands.add(new Brand(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brands;
    }

    public Brand getBrandById(int brandId) {
        Brand brand = null;
        String sql = "SELECT * FROM Brand WHERE BrandID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, brandId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                brand = new Brand(rs.getInt("BrandID"), rs.getString("brandName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brand;
    }

    public List<Product> selectProductByName(String nameInput) {
        List<Product> products = new ArrayList<>();
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS_BY_NAME)) {
            preparedStatement.setString(1, "%" + nameInput + "%");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ProductID");
                String name = rs.getString("productName");
                String description = rs.getString("Description");
                int categoryId = rs.getInt("subCategoryID");
                double price = rs.getDouble("Price");
                int stockQuantity = rs.getInt("Stock_Quantity");
                String imageUrl = rs.getString("image");
                int brandId = rs.getInt("BrandID");
                products.add(new Product(id, name, description, categoryId, price, stockQuantity, brandId, imageUrl));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public boolean isProductNameExists(String name) {
        String sql = "SELECT COUNT(*) FROM Product WHERE productName = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int insertProductAndReturnID(Product product) throws SQLException {
        String sql = "INSERT INTO Product (productName, description, subCategoryID, price, stock_Quantity, brandID, image) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, product.getProductName());
            ps.setString(2, product.getDescription());
            ps.setInt(3, product.getSubCategoryID());
            ps.setDouble(4, product.getPrice()); // BIGINT
            ps.setInt(5, product.getStock_Quantity());
            ps.setInt(6, product.getBrandID());
            ps.setString(7, product.getImage());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // Trả về productID mới
                }
            }
        }

        return -1; // Lỗi
    }

    public boolean insertProductAttribute(ProductAttribute attribute) {
        String sql = "INSERT INTO ProductAttribute (productID, attributeID, value) VALUES (?, ?, ?)";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, attribute.getProductID());
            ps.setInt(2, attribute.getAttributeID());
            ps.setString(3, attribute.getValue());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasAttributes(int productID) {
        String sql = "SELECT COUNT(*) FROM ProductAttribute WHERE productID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productID);
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

    public List<ProductAttribute> getProductAttributesById(int productID) {
        List<ProductAttribute> list = new ArrayList<>();

        String sql = "SELECT pa.productAttributeID, pa.productID, pa.attributeID, pa.Value, a.attributeName "
                + "FROM ProductAttribute pa "
                + "JOIN Attribute a ON pa.attributeID = a.attributeID "
                + "WHERE pa.productID = ?";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productID);

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int productAttributeID = rs.getInt("productAttributeID");
                    int prodID = rs.getInt("productID");
                    int attrID = rs.getInt("attributeID");
                    String value = rs.getString("value");
                    String attrName = rs.getString("attributeName");

                    ProductAttribute attr = new ProductAttribute(productAttributeID, prodID, attrID, value, attrName);
                    list.add(attr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
