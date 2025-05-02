package controller.product;

import dao.ManagerDAO;
import dao.RoleDAO;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Manager;
import model.Role;

public class ManagerController extends HttpServlet {

    ManagerDAO managerDAO = new ManagerDAO();
    RoleDAO roleDAO = new RoleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        switch (action) {
            case "/admin-edit-manager":
                editForm(request, response);
                break;
            case "/admin-delete-manager":
                deleteManager(request, response);
                break;
            case "/admin-toggle-manager-status":
                toggleStatus(request, response);
                break;
            case "/admin-update-manager":
                updateManager(request, response);
                break;
            case "/admin-view-manager":
                viewDetail(request, response);
                break;
            case "/admin-add-manager":
                manager_showNewForm(request, response);
                break;
            case "/admin-insert-manager":
                addManager(request, response);
                break;
            case "/admin-search-manager":
                searchManager(request, response);
                break;
            default:
                adminManagerList(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void adminManagerList(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        List<Manager> managers = managerDAO.getAllManagers();
        List<Role> roles = roleDAO.getAllRoles();
        request.setAttribute("roleList", roles);
        request.setAttribute("managers", managers);
        request.getRequestDispatcher("admin_manager_crud.jsp").forward(request, response);
    }

    private void editForm(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        Manager manager = managerDAO.getManagerById(id);
        List<Role> roles = roleDAO.getAllRoles();

        request.setAttribute("manager", manager);
        request.setAttribute("roleList", roles);
        request.getRequestDispatcher("admin_edit_manager.jsp").forward(request, response);
    }

    private void updateManager(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id").trim());
            String name = request.getParameter("name").trim();
            String username = request.getParameter("username").trim();
            String password_temp = request.getParameter("password").trim();
            String password = managerDAO.hashPasswordMD5(password_temp);
            String email = request.getParameter("email").trim();
            String phone = request.getParameter("phone_number").trim();
            int roleID = Integer.parseInt(request.getParameter("roleID"));
            boolean status = request.getParameter("status").equals("1");

            if (managerDAO.isUsernameTaken(username, id)) {
                request.setAttribute("error", "Username already taken.");
                editForm(request, response);
                return;
            }

            Manager updatedManager = new Manager(id, name, email, phone, username, password, LocalDate.now(), status, roleID);
            boolean success = managerDAO.updateManager(updatedManager);

            request.setAttribute(success ? "message" : "error", success ? "Updated successfully!" : "Update failed.");
            editForm(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("admin_edit_manager.jsp").forward(request, response);
        }
    }

    private void viewDetail(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        Manager manager = managerDAO.getManagerById(id);

        // Gán role cho manager để tránh lỗi JSP
        int roleId = manager.getRoleID();
        Role role = roleDAO.getRoleById(roleId);
        manager.setRole(role);

        List<Role> roles = roleDAO.getAllRoles();
        request.setAttribute("roleList", roles);
        request.setAttribute("manager", manager);
        request.getRequestDispatcher("admin_view_manager.jsp").forward(request, response);
    }

    private void manager_showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Role> roles = roleDAO.getAllRoles();
        request.setAttribute("roles", roles);
        request.getRequestDispatcher("admin_add_manager.jsp").forward(request, response);
    }

    private void addManager(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String name = request.getParameter("name");
            String username = request.getParameter("username");
            String rawPassword = request.getParameter("password");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String statusStr = request.getParameter("status");

            // Parse roleID và status
            int roleID = Integer.parseInt(request.getParameter("roleID"));
            boolean status = statusStr != null && statusStr.equals("1");

            // Validate trống
            if (name == null || name.trim().isEmpty()
                    || username == null || username.trim().isEmpty()
                    || rawPassword == null || rawPassword.trim().isEmpty()) {
                List<Role> roles = roleDAO.getAllRoles();
                request.setAttribute("roleList", roles);
                request.setAttribute("error", "Name, Username and Password must not be empty.");
                manager_showNewForm(request, response);
                return;
            }

            // Validate số điện thoại
            if (phone != null && !phone.matches("\\d{10}")) {
                List<Role> roles = roleDAO.getAllRoles();
                request.setAttribute("roleList", roles);
                request.setAttribute("error", "Phone number must be exactly 10 digits.");
                manager_showNewForm(request, response);

                return;
            }

            // Kiểm tra username đã tồn tại
            if (managerDAO.isUsernameTaken(username, -1)) {
                List<Role> roles = roleDAO.getAllRoles();
                request.setAttribute("roleList", roles);
                request.setAttribute("error", "Username is already taken.");
                manager_showNewForm(request, response);

                return;
            }

            // Mã hóa password
            String password = managerDAO.hashPasswordMD5(rawPassword);

            // Tạo đối tượng Manager
            Manager newManager = new Manager(name, email, phone, username, password, LocalDate.now(), status, roleID);

            // Insert DB
            boolean success = managerDAO.insertManager(newManager);
            List<Role> roles = roleDAO.getAllRoles();
            request.setAttribute("roleList", roles);
            request.setAttribute(success ? "message" : "error", success ? "Added successfully!" : "Addition failed.");
            manager_showNewForm(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            List<Role> roles = roleDAO.getAllRoles();
            request.setAttribute("roleList", roles);
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            manager_showNewForm(request, response);

        }
    }

    private void deleteManager(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = managerDAO.deleteManager(id);

            if (success) {
                request.getSession().setAttribute("message", "Manager deleted successfully.");
            } else {
                request.getSession().setAttribute("error", "Failed to delete manager.");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Invalid manager ID.");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error deleting manager: " + e.getMessage());
        }
        response.sendRedirect("ManagerController");
    }

    private void searchManager(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String keyword = request.getParameter("search");

        if (keyword == null || keyword.trim().isEmpty()) {
            request.setAttribute("error", "Please enter a keyword.");
            request.setAttribute("roleList", roleDAO.getAllRoles());
            request.getRequestDispatcher("admin_manager_crud.jsp").forward(request, response);
            return;
        }

        keyword = keyword.trim();
        List<Manager> list = managerDAO.searchManagerByPhone(keyword);

        if (list.isEmpty()) {
            request.setAttribute("error", "No matching manager found.");
        } else {
            request.setAttribute("managers", list);
        }

        List<Role> roles = roleDAO.getAllRoles();
        request.setAttribute("roleList", roles);
        request.setAttribute("search", keyword); // giữ lại từ khóa người dùng tìm

        request.getRequestDispatcher("admin_manager_crud.jsp").forward(request, response);
    }

    private void toggleStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Manager manager = managerDAO.getManagerById(id);

        if (manager != null) {
            boolean newStatus = !manager.isStatus();
            managerDAO.updateManagerStatus(id, newStatus);
        }

        response.sendRedirect("ManagerController");
    }
}



