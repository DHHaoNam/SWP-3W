/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import DB.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Address;

/**
 *
 * @author Nam
 */
public class AddressDAO {

    private DBContext dbcontext = new DBContext();

    public List<Address> getAddressesByCustomerID(int customerID) {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT * FROM Address WHERE CustomerID = ?";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                addresses.add(new Address(
                        rs.getInt("addressID"),
                        rs.getString("addressDetail"),
                        rs.getInt("CustomerID"),
                        rs.getInt("isDefault")
                ));
            }
            return addresses;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addresses;
    }

    public boolean deleteAddress(int addressID) {
        String sql = "DELETE FROM Address WHERE addressID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, addressID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removeDefaultAddress(int customerID) {
        String sql = "UPDATE Address SET IsDefault = 0 WHERE CustomerID = ?";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerID);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateAddress(Address address) {
        String sql = "UPDATE Address SET addressDetail = ?, isDefault = ? WHERE addressID = ? AND CustomerID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, address.getAddressDetail());
            ps.setInt(2, address.isDefault());
            ps.setInt(3, address.getAddressID());
            ps.setInt(4, address.getCustomerID());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addAddress(Address address) {
        String sql = "INSERT INTO Address (addressDetail, CustomerID, isDefault) VALUES (?, ?, ?)";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            // Nếu thêm địa chỉ là mặc định, cần xóa địa chỉ mặc định trước đó
            if (address.isDefault() == 1) {
                removeDefaultAddress(address.getCustomerID());
            }

            ps.setString(1, address.getAddressDetail());
            ps.setInt(2, address.getCustomerID());
            ps.setInt(3, address.isDefault());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Address getAddressById(int addressID) {
        String sql = "SELECT * FROM Address WHERE addressID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, addressID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Address(
                        rs.getInt("addressID"),
                        rs.getString("addressDetail"),
                        rs.getInt("customerID"),
                        rs.getInt("isDefault")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Address getDefaultAddress(int customerID) {
        String sql = "SELECT * FROM Address WHERE CustomerID = ? AND isDefault = 1";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Address(
                        rs.getInt("addressID"),
                        rs.getString("addressDetail"),
                        rs.getInt("CustomerID"),
                        rs.getInt("isDefault")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
