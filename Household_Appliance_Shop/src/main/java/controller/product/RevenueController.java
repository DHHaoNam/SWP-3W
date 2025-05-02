/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import com.google.gson.Gson;
import dao.OrderDAO;
import dao.OrderHistoryDAO;
import dao.ProductDAO;
import dao.RevenueDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.OrderDetail;
import model.RevenueDTO;
import model.TopCustomerDTO;
import model.TopProductDTO;

/**
 *
 * @author HP
 */
public class RevenueController extends HttpServlet {

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
            out.println("<title>Servlet RevenueController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RevenueController at " + request.getContextPath() + "</h1>");
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
        try {
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate;
            Date endDate;

            // Default to last 30 days if no dates are provided
            if (startDateStr != null && !startDateStr.isEmpty()
                    && endDateStr != null && !endDateStr.isEmpty()) {
                startDate = dateFormat.parse(startDateStr);
                endDate = dateFormat.parse(endDateStr);
            } else {
                Calendar cal = Calendar.getInstance();
                endDate = cal.getTime();
                cal.add(Calendar.DATE, -30); // Default to 30 days prior
                startDate = cal.getTime();
            }

            RevenueDAO revenueDAO = new RevenueDAO();
            List<RevenueDTO> revenueData = revenueDAO.getRevenueByDateRange(startDate, endDate);
            List<TopCustomerDTO> topCustomers = revenueDAO.getTopCustomers(startDate, endDate);
            List<TopProductDTO> topProducts = revenueDAO.getTopProducts(startDate, endDate);

            double totalRevenue = 0;
            for (RevenueDTO item : revenueData) {
                totalRevenue += item.getRevenue();
            }

            if (request.getHeader("X-Requested-With") != null) {
                // Handle AJAX request by returning JSON data
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                Map<String, Object> responseData = new HashMap<>();
                responseData.put("revenueData", revenueData);
                responseData.put("totalRevenue", totalRevenue);
                responseData.put("topCustomers", topCustomers);
                responseData.put("topProducts", topProducts);

                Gson gson = new Gson();
                response.getWriter().write(gson.toJson(responseData));
            } else {
                // Regular page request
                request.setAttribute("revenueData", revenueData);
                request.setAttribute("totalRevenue", totalRevenue);
                request.setAttribute("topCustomers", topCustomers);
                request.setAttribute("topProducts", topProducts);
                request.setAttribute("startDate", dateFormat.format(startDate));
                request.setAttribute("endDate", dateFormat.format(endDate));
                request.getRequestDispatcher("admin_chart.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error: " + e.getMessage());
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
     @Override
    public String getServletInfo() {
        return "Revenue management servlet.";
    }
}
