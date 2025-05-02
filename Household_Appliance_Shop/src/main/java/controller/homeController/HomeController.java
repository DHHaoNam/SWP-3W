/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.homeController;

import dao.CommentDAO;
import dao.FeedbackDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import model.Brand;
import model.Category;
import model.Comment;
import model.Customer;
import model.Feedback;
import model.Product;
import model.ProductAttribute;
import model.SubCategory;

/**
 *
 * @author HP
 */
public class HomeController extends HttpServlet {

    private ProductDAO ProductDAO;
    private FeedbackDAO feedbackDAO;
    private CommentDAO commentDAO;
    private OrderDAO orderDAO;

    @Override
    public void init() {
        ProductDAO = new ProductDAO();
        feedbackDAO = new FeedbackDAO();
        commentDAO = new CommentDAO();
        orderDAO = new OrderDAO();
    }

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
            out.println("<title>Servlet HomeController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet HomeController at " + request.getContextPath() + "</h1>");
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
            //loadCart(request);
            switch (action) {
                case "/home-search":
                    getProductByName(request, response);
                    break;
                case "/product-detail":
                    detailProduct(request, response);
                    break;
                default:
                    getData(request, response);
                    break;
            }
        } catch (ServletException | IOException e) {
            System.out.println(e);
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

    protected void getData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String indexStr = request.getParameter("index");
        String categoryIdStr = request.getParameter("categoryid");
        String subCategoryIdStr = request.getParameter("subcategoryid"); // NEW
        String brandIdStr = request.getParameter("brandid");

        Integer categoryid = null;
        Integer subcategoryid = null; // NEW
        Integer brandid = null;

        if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            categoryid = Integer.parseInt(categoryIdStr);
        }

        if (subCategoryIdStr != null && !subCategoryIdStr.isEmpty()) {
            subcategoryid = Integer.parseInt(subCategoryIdStr);
        }

        if (brandIdStr != null && !brandIdStr.isEmpty()) {
            brandid = Integer.parseInt(brandIdStr);
        }

        // Lấy danh sách chính
        List<Category> categorys = ProductDAO.selectAllCategorys();
        List<Brand> brands = ProductDAO.selectAllBrand();
        List<SubCategory> subcategories = ProductDAO.selectAllSubCategories(); // NEW

        int index = (indexStr == null || indexStr.isEmpty()) ? 1 : Integer.parseInt(indexStr);

        // Lấy danh sách sản phẩm có lọc
        List<Product> products = ProductDAO.pagingProducts(index, categoryid, subcategoryid, brandid);

        // Đếm số lượng sản phẩm
        int count = ProductDAO.countProductByFilters(categoryid, subcategoryid, brandid);
        int endPage = (count % 12 == 0) ? count / 12 : count / 12 + 1;

        // Đẩy dữ liệu sang JSP
        request.setAttribute("currentPage", index);
        request.setAttribute("endPage", endPage);
        request.setAttribute("products", products);
        request.setAttribute("categorys", categorys);
        request.setAttribute("brands", brands);
        request.setAttribute("subcategories", subcategories); // NEW
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }

    protected void getProductByName(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nameInput = request.getParameter("search");
        List<Product> products = ProductDAO.selectProductByName(nameInput);
        List<Category> categorys = ProductDAO.selectAllCategorys();
        request.setAttribute("products", products);
        request.setAttribute("categorys", categorys);
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }

    protected void detailProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("productID");

        // Kiểm tra id có null hoặc rỗng không
        if (id == null || id.isEmpty()) { // **HungLHT** - Thêm kiểm tra productID null/rỗng
            // Chuyển hướng về trang chủ hoặc hiển thị thông báo lỗi
            request.setAttribute("errorMessage", "Product ID is required"); // **HungLHT** - Đặt thông báo lỗi
            getData(request, response); // **HungLHT** - Điều hướng về trang chủ với dữ liệu
            return;
        }

        List<ProductAttribute> pa = new ArrayList<>();
        Product product = ProductDAO.getProductBYID(id);

        // Kiểm tra product có null không
        if (product == null) { // **HungLHT** - Thêm kiểm tra product null
            request.setAttribute("errorMessage", "Product not found"); // **HungLHT** - Đặt thông báo lỗi
            getData(request, response); // **HungLHT** - Điều hướng về trang chủ với dữ liệu
            return;
        }

        Brand brand = ProductDAO.getBrandById(product.getBrandID());
        pa = ProductDAO.getProductAttributesById(product.getProductID());
        List<Feedback> feedbacks = feedbackDAO.findAllByProductId(Integer.parseInt(id));
        List<Comment> comments = commentDAO.findAllByProductId(Integer.parseInt(id));
        Double avgRating = feedbackDAO.getAvgRatingByProductID(Integer.parseInt(id));
        int ratingCount = feedbackDAO.getCountRatingByProductID(Integer.parseInt(id));

        // Check if customer can leave feedback
        boolean hasPurchased = false;
        boolean canLeaveFeedback = false;

        Customer customer = (Customer) request.getSession().getAttribute("customer");
        if (customer != null) {
            // Check if customer has purchased this product
            hasPurchased = orderDAO.hasCustomerPurchasedProduct(customer.getCustomerID(), product.getProductID());

            // Check if customer has already given feedback for this product
            boolean hasGivenFeedback = feedbackDAO.hasCustomerFeedbackForProduct(customer.getCustomerID(), product.getProductID());

            // Customer can leave feedback if they purchased the product and haven't given feedback yet
            canLeaveFeedback = hasPurchased && !hasGivenFeedback;
        }

        request.setAttribute("hasPurchased", hasPurchased);
        request.setAttribute("canLeaveFeedback", canLeaveFeedback);
        request.setAttribute("avgRating", avgRating);
        request.setAttribute("ratingCount", ratingCount);
        request.setAttribute("feedbacks", feedbacks);
        request.setAttribute("comments", comments);
        request.setAttribute("brand", brand);
        request.setAttribute("details", product);
        request.setAttribute("pa", pa);
        request.getRequestDispatcher("product_detail.jsp").forward(request, response);
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

}
