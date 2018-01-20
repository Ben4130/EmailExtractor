/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. COPIED
 */
package my.emailtextextractor;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sqlite.jdbc4.JDBC4PreparedStatement;
/**
 *
 * @author benphillips
 */
class DBManagement {
    public static void createNewDatabase(String fileName)
    {
        String url = "jdbc:sqlite:/Users/benphillips/" + fileName;
        
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
    
    private static Connection connect (){
        String url = "jdbc:sqlite:/Users/benphillips/testlitex.db";
        
        Connection conn = null;
        
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return conn;
    }
    
    public static void createUserTable(String dbURL)
    throws SQLException{
        Connection conn = connect();
        Statement createStmt = null;
        String createSQL = "CREATE TABLE USER (" +
                "username text PRIMARY KEY," +
                "hostname text," +
                "protocol text," +
                "port interger);";
        
        doQuery(conn, createSQL, createStmt, true);
        
        if (createStmt != null) {createStmt.close();}
        if (conn != null) {conn.close();}
    }
    
    public static void createSearchFilterTable(String dbURL)
    throws SQLException{
        Connection conn = connect();
        Statement createStmt = null;
        String createSQL = "CREATE TABLE search_filter (" +
                "filter_name text ," +
                "date_from date," +
                "date_to date," +
                "addresses text," +
                "folder text," +
                "subject_text text," +
                "body_text text," +
                "username text," +
                "PRIMARY KEY (filter_name, username)," +
                "FOREIGN KEY(username) REFERENCES user(username)"+
                ");";
        
        doQuery(conn, createSQL, createStmt, true);
        
        if (createStmt != null) {createStmt.close();}
        if (conn != null) {conn.close();}
    }
    
    private static ResultSet doQuery (Connection conn, 
                                        String query,
                                        Statement stmt,
                                        Boolean isUpdate)
    throws SQLException {
        ResultSet rs = null;
        //Statement selectStmt = conn.createStatement();
        stmt = conn.createStatement();
        
        if (isUpdate){
            int executeUpdate = stmt.executeUpdate(query);
            System.out.println(executeUpdate);
        }else{
            rs = stmt.executeQuery(query);
        }
        
        
        return rs;
    }
    
    private static void doQuery (Connection conn,
                                    String sql,
                                    PreparedStatement pstmt,
                                    String param1,
                                    String param2,
                                    String param3,
                                    String param4)
    {//throws SQLException{
        try{
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, param1);
        pstmt.setString(2, param2);
        //pstmt.setInt(3, Integer.parseInt(param3));
        pstmt.setString(3, param3);
        pstmt.setString(4, param4);
        
        System.out.println(pstmt.toString());
        
        System.out.println(pstmt.executeUpdate());
        pstmt.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    public static UserConfiguration getUserConfiguration (String userName)
    throws SQLException {
        UserConfiguration userConfig = null;
        ResultSet rs = null;
        String selectStr = "SELECT * from user Where username = '" + 
                userName + "';";
        
        Statement selectStmt = null;
        Connection conn = null;
        try{
            //conn = DriverManager.getConnection(url);
            conn = connect();
            
            rs = doQuery(conn, selectStr, selectStmt, false);
            rs.next();
            
            if (rs.getRow() > 0){
                userConfig = new UserConfiguration(userName,
                        rs.getString("hostname"),
                        rs.getString("protocol"),
                        Integer.parseInt(rs.getString("port")));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }finally{
            if (rs != null){rs.close();}
            if (selectStmt != null) {selectStmt.close();}
            if (conn != null){conn.close();}
        }
        return userConfig;
    }
    
    public static void getUserDetails (String userName)
        throws SQLException {
        //Connection conn = connect();
        Connection conn = null;
        
        
        Statement selectStmt = null;
        //String selectStr = "SELECT * FROM user";
        String selectStr = "SELECT * from user;";
        
        //String selectStr = "INSERT INTO user (username,hostname,protocol,port) " +
                            //"VALUES ('test@test.com','gator','pop3',995);";
        
        String url = "jdbc:sqlite:/Users/benphillips/testlitex.db";
        
        try{
            conn = DriverManager.getConnection(url);
            //conn = connect();
            selectStmt = conn.createStatement();
            System.out.println(conn.getCatalog());
            ResultSet rs = selectStmt.executeQuery(selectStr);
            
            System.out.println(rs.getFetchSize());
            //rs size should be one
            //if (rs.getFetchSize() == 1)
            //{
            rs.next();
            System.out.println(rs.getRow());
            System.out.println(rs.getString("hostname"));
            //}
            
        } catch (SQLException e){
            //System.out.println(conn.getCatalog());
            System.out.println(e.getMessage());
        } finally {
            if (selectStmt != null) {selectStmt.close();}
        }
               
    }
    
    public static void updateUserDetails(UserConfiguration userConfig)
    throws SQLException{
        Connection conn = connect();
        Statement selectStmt = null;
        PreparedStatement pstmt = null;
        String queryStr = "UPDATE user " +
                            "SET hostname = '" + userConfig.hostName + "'," +
                            "protocol = '" + userConfig.protocol + "'," +
                            "port = " + userConfig.port.toString() +
                " WHERE username = '" + userConfig.userName + "';";
        System.out.println(queryStr);
        String updateStr = "UPDATE user " +
                            "SET hostname = ?," +
                            "protocol = ?," +
                            "port = ? " +
                            "WHERE hostname = ? ";
        
        //try {
            
            
            /*doQuery(conn, 
                    updateStr, 
                    pstmt, 
                    userConfig.hostName, 
                    userConfig.protocol,
                    userConfig.port.toString(),
                    userConfig.userName);*/
            doQuery(conn,queryStr, selectStmt,true);
        //} catch (SQLException ex) {
          ///  Logger.getLogger(DBManagement.class.getName()).log(Level.SEVERE, null, ex);
        //}finally{
            if (pstmt != null){pstmt.close();}
            if (conn != null){conn.close();}
        //}
        
    }
    
    public static void updateSearchFilter(Filter searchFilter, UserConfiguration userConfig)
    throws SQLException{
        Connection conn = null;
        Statement selectStmt = null;
        
        String queryString = "UPDATE filter " +
                "SET filter_name = '" + searchFilter.filterName + "'," +
                "date_from = '" + searchFilter.dateFrom.toString() + "'," +
                "date_to = '" + searchFilter.dateTo.toString() + "'," +
                "addresses = '" + searchFilter.addresses + "'," +
                "email_folder = '" + searchFilter.emailFolder + "'," +
                "subeject_text = '" + searchFilter.subjectText + "' " +
                "body_text = '" + searchFilter.bodyText + "' " +
                "WHERE username = '" + userConfig.userName + "'; ";
                
    }
}
