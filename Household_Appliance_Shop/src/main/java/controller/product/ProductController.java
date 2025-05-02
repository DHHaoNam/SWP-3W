/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dao.AttributeDAO;
import dao.BrandDAO;
import dao.CategoryDAO;
import dao.ProductDAO;
import dao.SubCategoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Attribute;
import model.Brand;
import model.Category;
import model.Product;
import model.ProductAttribute;
import model.SubCategory;

/**
 *
 * @author TRUNG NHAN
 */
public class ProductController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProductController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProductController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        try {
            switch (action) {
                case "/delete":
                    delete(request, response);
                    break;
                case "/insert":
                    addProduct(request, response);
                    break;
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/newedit":
                    newedit(request, response);
                    break;
                case "/edit":
                    edit(request, response);
                    break;
                case "/searchProduct":
                    search(request, response);
                    break;
                default:
                    adminProduct(request, response);
                    break;
            }
        } catch (IOException e) {
            e.getStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    ProductDAO pdao = new ProductDAO();
    CategoryDAO cdao = new CategoryDAO();
    BrandDAO bdao = new BrandDAO();
    SubCategoryDAO scdao = new SubCategoryDAO();
    AttributeDAO adao = new AttributeDAO();

    private void adminProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Product> product;
        String minPrice_raw = request.getParameter("minPrice");
        String maxPrice_raw = request.getParameter("maxPrice");
        Double minPrice = (minPrice_raw != null && !minPrice_raw.isEmpty()) ? Double.parseDouble(minPrice_raw) : null;
        Double maxPrice = (maxPrice_raw != null && !maxPrice_raw.isEmpty()) ? Double.parseDouble(maxPrice_raw) : null;
        if (minPrice != null || maxPrice != null) {
            product = pdao.filterProductsByPrice(minPrice, maxPrice);
        } else {
            product = pdao.selectAllProducts();
        }
        List<Brand> brand = bdao.selectAllBrands();
        List<SubCategory> subcategories = scdao.selectAllSubCategories();
        request.setAttribute("brand", brand);
        request.setAttribute("subcategories", subcategories);
        request.setAttribute("p", product);
        request.getRequestDispatcher("admin_product_crud.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        List<Category> categories = cdao.selectAllCategories();
        List<Brand> brand = bdao.selectAllBrands();

        int categoryID = request.getParameter("categoryID") != null
                ? Integer.parseInt(request.getParameter("categoryID"))
                : 0;

        List<SubCategory> subCategories = new ArrayList<>();
        List<Attribute> attributes = new ArrayList<>();

        if (categoryID > 0) {
            subCategories = scdao.selectSubCategoriesByCategoryID(categoryID);
            attributes = adao.getAttributeByCategoryID(categoryID);
        }
        request.setAttribute("category", categories);
        request.setAttribute("brand", brand);
        request.setAttribute("categoryID", categoryID);
        request.setAttribute("subCategories", subCategories);
        request.setAttribute("attributes", attributes);

        request.getRequestDispatcher("admin_add_product.jsp").forward(request, response);
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        try {
            String name = request.getParameter("name") != null ? request.getParameter("name").trim() : null;
            String priceStr = request.getParameter("price") != null ? request.getParameter("price").trim() : null;
            String description = request.getParameter("description") != null ? request.getParameter("description").trim() : null;
            String subCategoryIDStr = request.getParameter("subcategoryID") != null ? request.getParameter("subcategoryID").trim() : null;
            String quantityStr = request.getParameter("quantity") != null ? request.getParameter("quantity").trim() : null;
            String brandIDStr = request.getParameter("brandID") != null ? request.getParameter("brandID").trim() : null;
            String image = request.getParameter("image") != null ? request.getParameter("image").trim() : null;

            if (name == null || name.isEmpty() || priceStr == null || quantityStr == null
                    || subCategoryIDStr == null || brandIDStr == null || description == null || description.isEmpty()) {
                request.setAttribute("error", "All fields are required!");
                // Gọi lại showNewForm để truyền lại dữ liệu cho form
                showNewForm(request, response);
                return;
            }

            if (pdao.isProductNameExists(name)) {
                request.setAttribute("error", "Product name already exists!");
                // Gọi lại showNewForm để truyền lại dữ liệu cho form
                showNewForm(request, response);
                return;
            }

            // Xử lý các giá trị của form
            double price;
            int subCategoryID, stockQuantity, brandID;

            try {
                price = Double.parseDouble(priceStr);
                subCategoryID = Integer.parseInt(subCategoryIDStr);
                stockQuantity = Integer.parseInt(quantityStr);
                brandID = Integer.parseInt(brandIDStr);

                if (price < 0 || stockQuantity < 0) {
                    int categoryID = cdao.getCategoryIDBySubCategoryID(subCategoryID);
                    request.setAttribute("error", "Price and stock quantity must be non-negative!");
                    // Gọi lại showNewForm để truyền lại dữ liệu cho form
                    showNewForm(request, response);
                    return;
                }

            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid number format!");
                // Gọi lại showNewForm để truyền lại dữ liệu cho form
                showNewForm(request, response);
                return;
            }

            if (description.length() > 1000) {
                request.setAttribute("error", "Description cannot exceed 1000 characters!");
                // Gọi lại showNewForm để truyền lại dữ liệu cho form
                showNewForm(request, response);
                return;
            }

            // Lưu sản phẩm vào cơ sở dữ liệu
            Product product = new Product(name, description, subCategoryID, price, stockQuantity, brandID, image);
            int productID = pdao.insertProductAndReturnID(product);

            if (productID > 0) {
                // Lưu các thuộc tính sản phẩm
                int categoryID = cdao.getCategoryIDBySubCategoryID(subCategoryID);
                List<Attribute> attributes = adao.getAttributeByCategoryID(categoryID);
                for (Attribute attr : attributes) {
                    String value = request.getParameter("attribute_" + attr.getAttributeID());
                    if (value != null && !value.trim().isEmpty()) {
                        adao.insertProductAttribute(productID, attr.getAttributeID(), value.trim());
                    }
                }

                response.sendRedirect(request.getContextPath() + "/ProductController?success=Product added successfully");
            } else {
                request.setAttribute("error", "Could not add product.");
                // Gọi lại showNewForm để truyền lại dữ liệu cho form
                showNewForm(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while adding the product. Please try again.");
            // Gọi lại showNewForm để truyền lại dữ liệu cho form
            showNewForm(request, response);
        }
    }

    protected void newedit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy productId từ tham số request
        int productId = Integer.parseInt(request.getParameter("id"));

        // Lấy sản phẩm theo productId
        Product product = pdao.selectProduct(productId);

        // Khai báo biến selectedCategoryID
        int selectedCategoryID;

        // Lấy tham số categoryID từ request
        String categoryParam = request.getParameter("categoryID");
        if (categoryParam != null && !categoryParam.isEmpty()) {
            // Nếu người dùng chọn lại category từ dropdown
            selectedCategoryID = Integer.parseInt(categoryParam);
        } else {
            // Nếu không có categoryID, lấy category của sản phẩm dựa trên subCategoryID
            int subCategoryID = product.getSubCategoryID();
            selectedCategoryID = scdao.getCategoryIDBySubCategoryID(subCategoryID);
        }

        // Lấy lại danh sách subCategories theo category đã chọn
        List<SubCategory> subCategoryList = scdao.selectSubCategoriesByCategoryID(selectedCategoryID);

        // Lấy danh sách brand, category, và attributes cho product
        List<Brand> brandList = bdao.selectAllBrands();
        List<Category> categories = cdao.selectAllCategories();
        List<Attribute> attributes = adao.getAttributeByCategoryID(selectedCategoryID);

        // Lấy các thuộc tính đã gán cho sản phẩm
        List<ProductAttribute> productAttributes = adao.getAttributesByProductID(productId);

        // Map để lưu giá trị các thuộc tính của sản phẩm
        Map<Integer, String> productAttributeMap = new HashMap<>();
        for (ProductAttribute pa : productAttributes) {
            productAttributeMap.put(pa.getAttributeID(), pa.getValue());
        }

        // Truyền các dữ liệu vào request để JSP sử dụng
        request.setAttribute("p", product);
        request.setAttribute("brand", brandList);
        request.setAttribute("categories", categories);
        request.setAttribute("subCategories", subCategoryList);
        request.setAttribute("attributes", attributes);
        request.setAttribute("productAttributeMap", productAttributeMap);
        request.setAttribute("selectedCategoryID", selectedCategoryID); // Đảm bảo selected category được hiển thị trong form

        // Chuyển tiếp tới trang JSP để hiển thị form chỉnh sửa
        request.getRequestDispatcher("/admin_edit_product.jsp").forward(request, response);
    }

    private void edit(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            Integer managerRole = (Integer) request.getSession().getAttribute("managerRole");
            if (managerRole == null) {
                response.sendRedirect("login-manager.jsp");
                return;
            }

            int productID = Integer.parseInt(request.getParameter("id"));
            int stockQuantity = Integer.parseInt(request.getParameter("quantity"));

            if (stockQuantity < 0) {
                request.setAttribute("error", "Quantity cannot be negative!");
                newedit(request, response);
                return;
            }

            if (managerRole == 2) {
                pdao.updateProductQuantity(productID, stockQuantity);
            } else if (managerRole == 1) {
                // Get all parameters
                String name = request.getParameter("name").trim();
                String description = request.getParameter("description").trim();
                String priceStr = request.getParameter("price").trim();
                String subCategoryIDStr = request.getParameter("subCategoryID").trim();
                String brandIDStr = request.getParameter("brandID").trim();
                String image = request.getParameter("image").trim();

                if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty()
                        || subCategoryIDStr.isEmpty() || brandIDStr.isEmpty() || image.isEmpty()) {
                    request.setAttribute("error", "All fields must be filled!");
                    newedit(request, response);
                    return;
                }

                double price;
                int subCategoryID, brandID;

                try {
                    price = Double.parseDouble(priceStr);
                    subCategoryID = Integer.parseInt(subCategoryIDStr);
                    brandID = Integer.parseInt(brandIDStr);
                    if (price < 0) {
                        request.setAttribute("error", "Price cannot be negative!");
                        newedit(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid number format!");
                    newedit(request, response);
                    return;
                }

                if (description.length() > 1000) {
                    request.setAttribute("error", "Description cannot exceed 1000 characters!");
                    newedit(request, response);
                    return;
                }

                // Update product
                Product updatedProduct = new Product(productID, name, description, subCategoryID, price, stockQuantity, brandID, image);
                pdao.updateProduct(updatedProduct);

                // Handle attributes
                String[] attributeIDs = request.getParameterValues("attributeID");
                String[] attributeValues = request.getParameterValues("attributeValue");

                if (attributeIDs != null && attributeValues != null && attributeIDs.length == attributeValues.length) {
                    pdao.deleteProductAttributes(productID);
                    Set<Integer> usedAttr = new HashSet<>();

                    for (int i = 0; i < attributeIDs.length; i++) {
                        String idStr = attributeIDs[i].trim();
                        String value = attributeValues[i].trim();

                        if (!idStr.isEmpty() && !value.isEmpty()) {
                            int attrID = Integer.parseInt(idStr);

                            if (usedAttr.contains(attrID)) {
                                request.setAttribute("error", "Duplicate attribute ID: " + attrID);
                                newedit(request, response);
                                return;
                            }

                            usedAttr.add(attrID);
                            ProductAttribute attr = new ProductAttribute(productID, attrID, value);
                            pdao.insertProductAttribute(attr);
                        }
                    }
                }
            }

            response.sendRedirect("ProductController");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid number format!");
            newedit(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            newedit(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Unexpected error: " + e.getMessage());
            newedit(request, response);
        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean hasAttributes = pdao.hasAttributes(id); // Bạn sẽ cần tạo phương thức này trong DAO
        if (hasAttributes) {
            request.setAttribute("error", "Cannot delete product because it has associated attributes.");
            adminProduct(request, response);
            return;
        }
        pdao.deleteProduct(id);
        response.sendRedirect(request.getContextPath() + "/ProductController");
    }

    private void search(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        List<Product> products = new ArrayList<>();
        String search = request.getParameter("search") != null ? request.getParameter("search").trim() : "";
        try {
            if (search == null || search.trim().isEmpty()) {
                request.setAttribute("error", "Please enter valid product!");
                request.getRequestDispatcher("admin_product_crud.jsp").forward(request, response);
                return;
            }
            products = pdao.searchProduct(search);

            if (products.isEmpty()) {
                request.setAttribute("error", "No valid products found!");
            } else {
                request.setAttribute("p", products);
            }
            List<Category> category = cdao.selectAllCategories();
            List<Brand> brand = bdao.selectAllBrands();
            request.setAttribute("categories", category);
            request.setAttribute("brand", brand);
            request.getRequestDispatcher("admin_product_crud.jsp").forward(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



