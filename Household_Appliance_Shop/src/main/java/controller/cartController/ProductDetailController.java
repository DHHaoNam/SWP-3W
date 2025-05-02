/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.cartController;

import dao.CartDAO;
import dao.ProductDAO;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Brand;
import model.Cart;

import model.Customer;
import model.Product;

/**
 *
 * @author HP
 */
public class ProductDetailController extends HttpServlet {

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
            out.println("<title>Servlet ProductDetailController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProductDetailController at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
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
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int customerID = customer.getCustomerID();
        CartDAO dao = new CartDAO();

        ProductDAO productDAO = new ProductDAO();
        Brand brand = productDAO.getBrandById(customerID);

        int productId = Integer.parseInt(request.getParameter("productID"));
        int quantity = Integer.parseInt(request.getParameter("stock_Quantity"));

        // Lấy số lượng tồn kho từ database
        int stockQuantity = productDAO.getProductStock(productId);

        // Kiểm tra nếu số lượng đặt hàng vượt quá số lượng tồn kho
        if (quantity > stockQuantity) {
            // Tạo DAO lấy dữ liệu sản phẩm
            Product details = productDAO.selectProduct(productId);
            request.setAttribute("details", details);
            request.setAttribute("brand", brand);
            session.setAttribute("errorMessage", "The requested quantity exceeds the available stock.");
            request.getRequestDispatcher("product_detail.jsp").forward(request, response);
            return;
        }

        if (dao.isProductInCart(customerID, productId)) {
            int currentQuantity = dao.getCartItemQuantity(customerID, productId);
            int newQuantity = currentQuantity + quantity;
            if (newQuantity > stockQuantity) {
                Product details = productDAO.selectProduct(productId);
                request.setAttribute("details", details);
                request.setAttribute("brand", brand);
                int remaining = stockQuantity - currentQuantity;
                session.setAttribute("errorMessage", "Only " + stockQuantity + " items are available. You’ve already added " + currentQuantity + " to your cart.");
                request.getRequestDispatcher("product_detail.jsp").forward(request, response);
                return;
            }

            dao.updateCartItemQuantity(customerID, productId, newQuantity);
        } else {
            dao.addCartItem(customerID, productId, quantity);
        }

        List<Cart> list = dao.getcart(customerID);
        request.setAttribute("cartlists", list);
        response.sendRedirect("cart");

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
