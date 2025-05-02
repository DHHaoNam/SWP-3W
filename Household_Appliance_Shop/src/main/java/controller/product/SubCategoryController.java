package controller.product;

import dao.CategoryDAO;
import dao.SubCategoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Category;
import model.SubCategory;

public class SubCategoryController extends HttpServlet {

    SubCategoryDAO sdao = new SubCategoryDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SubCategoryController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SubCategoryController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getServletPath();
            switch (action) {
                case "/new-subcategory":
                    showNewForm(request, response);
                    break;
                case "/insert-subcategory":
                    addSubCategory(request, response);
                    break;
                case "/newedit-subcategory":
                    neweditSubCategory(request, response);
                    break;
                case "/delete-subcategory":
                    deleteSubCategory(request, response);
                    break;
                case "/edit-subcategory":
                    editSubCategory(request, response);
                    break;
                default:
                    adminSubCategory(request, response);
                    break;
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(SubCategoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "SubCategory management controller";
    }
    SubCategoryDAO scdao = new SubCategoryDAO();
    CategoryDAO cdao = new CategoryDAO();

    private void adminSubCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int categoryID = Integer.parseInt(request.getParameter("id"));
        List<SubCategory> scList = sdao.selectSubCategoriesByCategoryID(categoryID);
        request.setAttribute("categoryID", categoryID);
        request.setAttribute("subCategories", scList);
        request.getRequestDispatcher("admin_subcategory_crud.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int categoryID = Integer.parseInt(request.getParameter("categoryID"));
        Category category = cdao.selectCategory(categoryID);
        request.setAttribute("category", category);    // Gửi sang JSP
        request.getRequestDispatcher("admin_add_subcategory.jsp").forward(request, response);
    }

    private void addSubCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String name = request.getParameter("name").trim();
        int categoryID = Integer.parseInt(request.getParameter("categoryID"));

        try {
            if (scdao.isSubCategoryExists(name, categoryID)) {
                request.setAttribute("errorMessage", "SubCategory already exists in this category!");
                Category category = cdao.selectCategory(categoryID);
                request.setAttribute("category", category);
                request.getRequestDispatcher("admin_add_subcategory.jsp").forward(request, response);
            } else {
                scdao.insertSubCategory(new SubCategory(name, categoryID));
                response.sendRedirect("SubCategoryController?id=" + categoryID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }

    private void neweditSubCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            SubCategory subcategory = sdao.selectSubCategory(id); // lấy subcategory theo ID
            List<Category> categoryList = cdao.selectAllCategories(); // lấy danh sách category

            request.setAttribute("subcategory", subcategory);
            request.setAttribute("categoryList", categoryList);
            request.getRequestDispatcher("admin_edit_subcategory.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editSubCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        int subCategoryID = Integer.parseInt(request.getParameter("subCategoryID"));
        String subCategoryName = request.getParameter("subCategoryName").trim();  // Loại bỏ khoảng trắng
        int categoryID = Integer.parseInt(request.getParameter("categoryID"));

        // Kiểm tra nếu tên danh mục con đã tồn tại trong cơ sở dữ liệu (trường hợp cập nhật)
        if (sdao.isSubCategoryExistsForUpdate(subCategoryID, subCategoryName, categoryID)) {
            request.setAttribute("subcategory", new SubCategory(subCategoryID, subCategoryName, categoryID));
            request.setAttribute("categoryList", cdao.selectAllCategories());
            request.setAttribute("errorMessage", "Subcategory name already exists in this category. Please choose another name.");
            request.getRequestDispatcher("admin_edit_subcategory.jsp").forward(request, response);
            return;
        }

        // Cập nhật danh mục con nếu không trùng tên
        SubCategory updated = new SubCategory(subCategoryID, subCategoryName, categoryID);
        boolean success = sdao.updateSubCategory(updated);

        if (success) {
            response.sendRedirect("SubCategoryController?id=" + categoryID);
        } else {
            request.setAttribute("subcategory", updated);
            request.setAttribute("categoryList", cdao.selectAllCategories());
            request.setAttribute("errorMessage", "Update failed. Please try again.");
            request.getRequestDispatcher("admin_edit_subcategory.jsp").forward(request, response);
        }
    }

    private void deleteSubCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        int categoryID = Integer.parseInt(request.getParameter("categoryID"));

        try {
            boolean success = sdao.deleteSubCategory(id);
            if (!success) {
                request.setAttribute("errorMessage", "Cannot delete subcategory. It may be in use.");
                // Load lại danh sách để hiển thị trên JSP
                List<SubCategory> subCategories = sdao.selectAllSubCategories();
                request.setAttribute("subCategories", subCategories);
                request.getRequestDispatcher("admin_subcategory_crud.jsp").forward(request, response);
            } else {
                response.sendRedirect("SubCategoryController?id=" + categoryID);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "A database error occurred.");
            List<SubCategory> subCategories = sdao.selectAllSubCategories();
            request.setAttribute("subCategories", subCategories);
            request.getRequestDispatcher("/admin_subcategory_crud.jsp").forward(request, response);
        }
    }

}
