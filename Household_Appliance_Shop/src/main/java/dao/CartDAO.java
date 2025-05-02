package dao;

import DB.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cart;
import model.Product;
import dao.ProductDAO;

public class CartDAO {

    private DBContext dbcontext = new DBContext();

//    public List<Cart> getcart(int customerID) {
//        String sql = "SELECT \n"
//                + "    Cart.cartID, \n"
//                + "    Cart.quantity, \n"
//                + "    Product.productID, \n"
//                + "    Product.productName, \n"
//                + "    Product.description, \n"
//                + "    Product.image, \n"
//                + "    Product.price, \n"
//                + "    Product.subCategoryID, \n"
//                + "    Product.stock_Quantity, \n"
//                + "    Product.brandID, \n"
//                + "    (Cart.quantity * Product.price) AS totalPrice\n"
//                + "FROM Cart\n"
//                + "INNER JOIN Product ON Cart.productID = Product.productID\n"
//                + "WHERE Cart.customerID = ?";
//
//        List<Cart> list = new ArrayList<>();
//
//        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, customerID);
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                Product product = new Product(
//                        rs.getInt("productID"),
//                        rs.getString("productName"),
//                        rs.getString("description"),
//                        rs.getInt("subCategoryID"),
//                        rs.getDouble("price"),
//                        rs.getInt("stock_Quantity"),
//                        rs.getInt("brandID"),
//                        rs.getString("image")
//                );
//
//                Cart cartItem = new Cart(
//                        rs.getInt("cartID"),
//                        rs.getInt("productID"),
//                        rs.getInt("quantity"),
//                        rs.getInt("customerID"),
//                        product
//                );
//                list.add(cartItem);
//            }
//        } catch (SQLException e) {
//            System.out.println("Lỗi lấy giỏ hàng: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return list;
//    }
    public List<Cart> getcart(int customerID) {
        List<Cart> list = new ArrayList<>();
        String sql = "SELECT * FROM Cart WHERE customerID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int cartID = rs.getInt("cartID");
                int productID = rs.getInt("productID");
                int quantity = rs.getInt("quantity");

                Product product = new ProductDAO().getProductById(productID);

                Cart cartItem = new Cart(cartID, productID, quantity, customerID, product);
                list.add(cartItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean isProductInCart(int customerID, int productID) {
        String query = "SELECT TOP 1 1 FROM Cart WHERE customerID = ? AND productID = ?";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, customerID);
            ps.setInt(2, productID);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi kiểm tra sản phẩm trong giỏ hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCartItemQuantity(int customerID, int productId, int quantity) {
        String sql = "UPDATE Cart\n"
                + "SET quantity = ?\n"
                + "WHERE customerID = ? AND productID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            // Chỉ cần cập nhật quantity và truyền tham số đúng
            ps.setInt(1, quantity);  // Cập nhật số lượng
            ps.setInt(2, customerID); // Điều kiện customerID
            ps.setInt(3, productId);  // Điều kiện productID

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating product quantity: " + e.getMessage());
            return false;
        }
    }

//    public boolean addCartItem(int customerID, int productId, int quantity) {
//        if (isProductInCart(customerID, productId)) {
//            int currentQuantity = getCartItemQuantity(customerID, productId);
//            return updateCartItemQuantity(customerID, productId, currentQuantity + quantity);
//        }
//        // Kiểm tra xem giỏ hàng đã tồn tại hay chưa
//        String getCartIdQuery = "SELECT cartID FROM Cart WHERE customerID = ?";
//        String createCartQuery = "INSERT INTO Cart (customerID) VALUES (?)";
//        String priceQuery = "SELECT price FROM Product WHERE productID = ?";
//        String insertQuery = "INSERT INTO CartItem (cartID, productID, quantity, totalPrice) VALUES (?, ?, ?, ?)";
//        try ( Connection conn = dbcontext.getConnection();  PreparedStatement cartStmt = conn.prepareStatement(getCartIdQuery);  PreparedStatement createCartStmt = conn.prepareStatement(createCartQuery);  PreparedStatement priceStmt = conn.prepareStatement(priceQuery);  PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
//            // Lấy cartID
//            cartStmt.setInt(1, customerID);
//            ResultSet cartRs = cartStmt.executeQuery();
//            int cartID;
//
//            if (!cartRs.next()) {
//                // Nếu không có giỏ hàng, tạo giỏ hàng mới
//                createCartStmt.setInt(1, customerID);
//                createCartStmt.executeUpdate();
//                ResultSet newCartRs = cartStmt.executeQuery();
//                if (!newCartRs.next()) {
//                    return false;
//                }
//                cartID = newCartRs.getInt("cartID");
//            } else {
//                cartID = cartRs.getInt("cartID");
//            }
//            // Lấy giá sản phẩm
//            priceStmt.setInt(1, productId);
//            ResultSet rs = priceStmt.executeQuery();
//            if (!rs.next()) {
//                return false;
//            }
//            double price = rs.getDouble("price");
//            // Thêm sản phẩm vào giỏ hàng
//            insertStmt.setInt(1, cartID);
//            insertStmt.setInt(2, productId);
//            insertStmt.setInt(3, quantity);
//            insertStmt.setDouble(4, price * quantity);
//            return insertStmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.out.println("Lỗi thêm sản phẩm vào giỏ hàng: " + e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }
    public boolean addCartItem(int customerID, int productId, int quantity) {
        if (isProductInCart(customerID, productId)) {
            int currentQuantity = getCartItemQuantity(customerID, productId);
            return updateCartItemQuantity(customerID, productId, currentQuantity + quantity);
        }

        String insertQuery = "INSERT INTO Cart (productID, quantity, customerID) VALUES (?, ?, ?)";

        try (
                 Connection conn = dbcontext.getConnection();  PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            insertStmt.setInt(1, productId);
            insertStmt.setInt(2, quantity);
            insertStmt.setInt(3, customerID);

            return insertStmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Lỗi thêm sản phẩm vào giỏ hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeCartItem(int customerID, int productId) {
        String sql = "DELETE FROM Cart WHERE customerID = ? AND productID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa sản phẩm khỏi giỏ hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public int getCartItemQuantity(int customerID, int productId) {
        String sql = "SELECT quantity FROM Cart WHERE customerID = ? AND productID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            ps.setInt(2, productId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("quantity") : 0;
        } catch (SQLException e) {
            System.out.println("Lỗi lấy số lượng sản phẩm: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public boolean clearCart(int customerID) {
        String sql = "DELETE FROM Cart WHERE customerID = ?";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerID);
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0; // Trả về true nếu có sản phẩm bị xóa
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa giỏ hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
