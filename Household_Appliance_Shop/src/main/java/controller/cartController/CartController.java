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
import java.util.Arrays;
import model.Cart;
import model.Customer;
import model.Product;

/**
 *
 * @author HP
 */
public class CartController extends HttpServlet {

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
            out.println("<title>Servlet CartController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CartController at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        CartDAO cartDAO = new CartDAO();
        int customerId = customer.getCustomerID();

        List<Cart> cartList = cartDAO.getcart(customer.getCustomerID());

        if (cartList != null) {
            for (Cart item : cartList) {
            }
        }
        request.setAttribute("cartlists", cartList);
        request.getRequestDispatcher("cart.jsp").forward(request, response);
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

        CartDAO cartDAO = new CartDAO();
        ProductDAO productDAO = new ProductDAO();
        int customerID = customer.getCustomerID();
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        String removeProductIdStr = request.getParameter("removeProductId");
        System.out.println("Product IDs: " + Arrays.toString(productIds));
        System.out.println("Quantities: " + Arrays.toString(quantities));

        if (removeProductIdStr != null) {
            int removeProductId = Integer.parseInt(removeProductIdStr);
            cartDAO.removeCartItem(customerID, removeProductId);

            // Cập nhật session sau khi xóa sản phẩm
            List<Cart> updatedCartList = cartDAO.getcart(customerID);
            request.setAttribute("cartlists", updatedCartList);
        } else if (productIds != null && quantities != null) {
            StringBuilder errorMessage = new StringBuilder();
            for (int i = 0; i < productIds.length; i++) {
                int productId = Integer.parseInt(productIds[i]);
                int quantity = Integer.parseInt(quantities[i]);
                Product p = productDAO.getProductById(productId);
                // Kiểm tra số lượng tồn kho
                int stockQuantity = productDAO.getProductStock(productId); // Hàm này lấy số lượng tồn kho

                if (quantity > stockQuantity) {
                    errorMessage.append("Only ")
                            .append(stockQuantity)
                            .append(" left in stock for: \"")
                            .append(p.getProductName())
                            .append("\".<br>");
                }
            }

            if (errorMessage.length() > 0) {
                session.setAttribute("error", errorMessage);

            } else {
                for (int i = 0; i < productIds.length; i++) {
                    int productId = Integer.parseInt(productIds[i]);
                    int quantity = Integer.parseInt(quantities[i]);
                    cartDAO.updateCartItemQuantity(customerID, productId, quantity);
                }
                request.setAttribute("cartlists", cartDAO.getcart(customerID));
            }
        }

        response.sendRedirect("cart"); // Quay lại trang giỏ hàng sau khi cập nhật
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
