/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer.profile;

import dao.AddressDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Address;
import model.Customer;

/**
 *
 * @author Nam
 */
public class UpdateAddress extends HttpServlet {

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
            out.println("<title>Servlet UpdateAddress</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateAddress at " + request.getContextPath() + "</h1>");
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
        int addressID = Integer.parseInt(request.getParameter("addressID"));
       

        AddressDAO dao = new AddressDAO();
        Address address = dao.getAddressById(addressID);

        if (address != null) {
            request.setAttribute("updateAddress", address);
            request.getRequestDispatcher("edit_address.jsp").forward(request, response);
        } else {
            response.sendRedirect("listAddress");
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
        
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        int customerID = customer.getCustomerID(); // Lấy customerID từ đối tượng Customer
        int addressID = Integer.parseInt(request.getParameter("addressID"));

        String addressDetail = request.getParameter("addressDetail");
        boolean isDefault = request.getParameter("isDefault") != null;
        AddressDAO dao = new AddressDAO();

        if (isDefault) {
            dao.removeDefaultAddress(customerID); // Xóa địa chỉ mặc định cũ
        }

        Address address = new Address(addressID, addressDetail, customerID, isDefault ? 1 : 0);
        boolean success = dao.updateAddress(address);

        if (success) {
            response.sendRedirect("listAddress"); // Quay về danh sách sau khi cập nhật thành công
        } else {
            request.setAttribute("errorMessage", "Failed to update the address!");
            request.setAttribute("updateAddress", address);
            request.getRequestDispatcher("edit_address.jsp").forward(request, response);
        }
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
