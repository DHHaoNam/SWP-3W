/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer.profile;

import dao.CustomerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Customer;

/**
 *
 * @author Nam
 */
public class UpdateProfile extends HttpServlet {

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
            out.println("<title>Servlet UpdateProfile</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateProfile at " + request.getContextPath() + "</h1>");
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
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    HttpSession session = request.getSession();
    Customer customer = (Customer) session.getAttribute("customer");

    // Kiểm tra xem customer có trong session hay không
    if (customer == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String fullName = request.getParameter("fullName");
    String email = request.getParameter("email");
    String phone = request.getParameter("phone");

    // Kiểm tra các tham số
    if (fullName == null || fullName.isEmpty() || email == null || email.isEmpty() || phone == null || phone.isEmpty()) {
        request.setAttribute("errorMessage", "Information cannot be empty.");
        request.getRequestDispatcher("update-account.jsp").forward(request, response);
        return;
    }

    // Kiểm tra số điện thoại
    if (phone.length() != 10 || !phone.matches("\\d+")) {
        request.setAttribute("errorMessage", "Invalid phone number! The phone number must be exactly 10 digits and contain no letters.");
        request.getRequestDispatcher("update-account.jsp").forward(request, response);
        return;
    }

    CustomerDAO customerDAO = new CustomerDAO();
    boolean updateSuccess = customerDAO.updateCustomerInfo(customer.getCustomerID(), fullName, email, phone);

    if (updateSuccess) {
        customer.setFullName(fullName);
        customer.setEmail(email);
        customer.setPhone(phone);
        session.setAttribute("customer", customer);
        request.setAttribute("successMessage", "Information updated successfully!");
    } else {
        request.setAttribute("errorMessage", "Update fail, phone already existed");
    }

    request.getRequestDispatcher("update-account.jsp").forward(request, response);
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
