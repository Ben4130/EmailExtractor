/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. old
 */
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author benphillips
 */
public class DBManagement {
    public static void createNewDatabase(String fileName)
    {
        String url = "jdbc:sqlite:/Users" + fileName;
        
        try (Connection conn = DriverManager.getConnection(url)){
            if (conn != null){
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println(("The driver name is " + meta.getDriverName()));
                System.out.println("A new db has been created");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
