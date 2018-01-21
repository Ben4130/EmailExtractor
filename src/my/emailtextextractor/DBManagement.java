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
import java.text.DateFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.JComboBox;
import org.sqlite.jdbc4.JDBC4PreparedStatement;
/**
 *
 * @author benphillips
 */

class DBManagement {
   
    private static String DBUrl = "jdbc:sqlite:/Users/benphillips/testlitex.db";
    private static final String DATE_FORMAT = "yy/MM/dd";
    
    //Tables
    private static final String TABLE_USER = "user";
    private static final String COL_USERNAME = "username";
    private static final String COL_HOSTNAME = "hostname";
    private static final String COL_PROTOCOL = "protocol";
    private static final String COL_PORT = "port";
    
    private static final String TABLE_SEARCHFILTER = "search_filter";
    private static final String COL_FILTERNAME = "filter_name";
    private static final String COL_DATEFROM = "date_from";
    private static final String COL_DATETO = "date_to";
    private static final String COL_ADDRESS = "address";
    private static final String COL_FOLDER = "folder";
    private static final String COL_SUBJECT = "sibject_text";
    private static final String COL_BODY = "body_text";
    
    private static final String DATATYPE_TEXT = "text";
    private static final String DATATYPE_INT = "integer";
    
    private static String convertDateToDBString(Date date){
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.format(date);
    }
    
    private static Date convertDBStringToDate(String dbString){
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        Date date = null;
        try {
            date = df.parse(dbString);
        } catch (ParseException ex) {
            Logger.getLogger(DBManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }
    
    public static void createNewDatabase(String fileName)
    {
        try (Connection conn = DriverManager.getConnection(DBUrl)){
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
        String url = DBUrl;
        
        Connection conn = null;
        
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return conn;
    }
    
    public static void createUserTable()
    throws SQLException{
        Connection conn = connect();
        Statement createStmt = null;
        String createSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_USER +
                "\n(" +
                COL_USERNAME + " " + DATATYPE_TEXT + " PRIMARY KEY,\n" +
                COL_HOSTNAME + " " + DATATYPE_TEXT + ",\n" +
                COL_PROTOCOL + " " + DATATYPE_TEXT + ",\n" +
                COL_PORT + " " + DATATYPE_INT +
                "\n);";
        System.out.println(createSQL);
        doQuery(conn, createSQL, createStmt, true);
        
        if (createStmt != null) {createStmt.close();}
        if (conn != null) {conn.close();}
    }
    
    public static void createPersistentUserConfiguration(UserConfiguration userConfig)
            throws SQLException{
        Connection conn = connect();
        Statement insertStmt = null;
        
        String insertSql = "INSERT INTO" + TABLE_USER +
                " VALUES (" +
                "'" + userConfig.userName + "'," +
                "'" + userConfig.hostName + "'," +
                "'" + userConfig.protocol + "'," +
                "'" + userConfig.port.toString() + "'" +
                ");";
        doQuery(conn, insertSql, insertStmt, true);
        
        if (insertStmt != null){insertStmt.close();}
        if (conn != null){conn.close();}
    }
    
    public static void createSearchFilterTable()
    throws SQLException{
        Connection conn = connect();
        Statement createStmt = null;
        String createSQL = "CREATE TABLE IF NOT EXISTS " + 
                TABLE_SEARCHFILTER +
                "\n (" +
                COL_FILTERNAME + " " + DATATYPE_TEXT + " ,\n" +
                COL_DATEFROM + " " + DATATYPE_TEXT + ",\n" +
                COL_DATETO + " " + DATATYPE_TEXT + ",\n" +
                COL_ADDRESS + " " + DATATYPE_TEXT + ",\n" +
                COL_FOLDER + " " + DATATYPE_TEXT + ",\n" +
                COL_SUBJECT + " " + DATATYPE_TEXT + ",\n" +
                COL_BODY + " " + DATATYPE_TEXT + ",\n" +
                COL_USERNAME + " " + DATATYPE_TEXT + ",\n" +
                "PRIMARY KEY (\n" + 
                COL_FILTERNAME + ",\n " + 
                COL_USERNAME + 
                "\n),\n" +
                "FOREIGN KEY(" + 
                COL_USERNAME + ")\n REFERENCES " +
                TABLE_USER + "(\n" + COL_USERNAME + 
                ")\n"+
                "\n);";
        System.out.println(createSQL);
        doQuery(conn, createSQL, createStmt, true);
        
        if (createStmt != null) {createStmt.close();}
        if (conn != null) {conn.close();}
    }
    
    public static void createPersistentSearchFilter(Filter filter, UserConfiguration userConfig)
    throws SQLException{
        Connection conn = connect();
        Statement insertStmt = null;
        String insertSql = "INSERT INTO " + TABLE_SEARCHFILTER + " " +
                "VALUES (" + 
                "'" + filter.filterName + "'," +
                "'" + filter.dateFrom.toString() + "'," +
                "'" + filter.dateTo.toString() + "'," +
                "'" + filter.addresses + "'," +
                "'" + filter.emailFolder + "'," +
                "'" + filter.subjectText + "'," +
                "'" + filter.bodyText + "'," +
                "'" + userConfig.userName + "');";
        System.out.println(insertSql);
        doQuery(conn, insertSql, insertStmt, true);
        if (insertStmt != null){insertStmt.close();}
        if (conn != null){conn.close();}
        
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
        String selectStr = "SELECT * FROM " + TABLE_USER +
                " WHERE " + COL_USERNAME + " = '" + 
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
                        rs.getString(COL_HOSTNAME),
                        rs.getString(COL_PROTOCOL),
                        Integer.parseInt(rs.getString(COL_PORT)));
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
        String selectStr = "SELECT * FROM " + TABLE_USER + ";";
        
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
        Statement updateStmt = null;
        
        String queryStr = "UPDATE " + 
                TABLE_USER + " " +
                "SET " + 
                COL_HOSTNAME + " = '" + userConfig.hostName + "'," +
                COL_PROTOCOL + " = '" + userConfig.protocol + "'," +
                COL_PORT + " = " + userConfig.port.toString() + " " +
                "WHERE " + 
                COL_USERNAME + " = '" + userConfig.userName + "';";
                
            doQuery(conn,queryStr, updateStmt,true);
            if (updateStmt != null){updateStmt.close();}
            if (conn != null){conn.close();}
    }
    
    public static void updateSearchFilter(Filter searchFilter, UserConfiguration userConfig)
    throws SQLException{
        Connection conn = connect();
        Statement selectStmt = null;
        
        String queryString = "UPDATE " + 
                TABLE_SEARCHFILTER + " " +
                "SET " + 
                COL_FILTERNAME + " = '" + searchFilter.filterName + "'," +
                COL_DATEFROM + " = '" + searchFilter.dateFrom.toString() + "'," +
                COL_DATETO + " = '" + searchFilter.dateTo.toString() + "'," +
                COL_ADDRESS + " = '" + searchFilter.addresses + "'," +
                COL_FOLDER + " = '" + searchFilter.emailFolder + "'," +
                COL_SUBJECT + " = '" + searchFilter.subjectText + "' " +
                COL_BODY + " = '" + searchFilter.bodyText + "' " +
                "WHERE " +
                COL_USERNAME + " = '" + userConfig.userName + "'; ";
        
        doQuery(conn,queryString, selectStmt, true);
        if(selectStmt != null){selectStmt.close();}
        if(conn != null){conn.close();}        
    }
    
    public static Filter getSearchFilter(String filterName, String username)
            throws SQLException, ParseException{
        Connection conn = connect();
        Statement selectStmt = null;
        Filter searchFilter = null;
        
        String selectSql = "SELECT * FROM " +
                TABLE_SEARCHFILTER + " search_filter " +
                "WHERE " +
                COL_FILTERNAME + " = '" + filterName + "' " +
                "AND " +
                COL_USERNAME + " = '" + username + "';";
        
        ResultSet rs = doQuery(conn, selectSql, selectStmt, false);
        
        rs.next();
        if(rs.getRow() > 0){
            searchFilter = new Filter(filterName, 
                    convertDBStringToDate(rs.getString(COL_DATEFROM)), 
                    convertDBStringToDate(rs.getString(COL_DATETO)),
                    null,//Direction
                    rs.getString(COL_ADDRESS), 
                    rs.getString(COL_FOLDER),
                    rs.getString(COL_SUBJECT),
                    rs.getString(COL_BODY));
        }
        
        if(selectStmt != null){selectStmt.close();}
        if(rs != null){rs.close();}
        if(conn != null){conn.close();}
        return searchFilter;
    }

    static void populateFilterNames(JComboBox<String> cmbFilterName, UserConfiguration userConfig) 
    throws SQLException{
        Connection conn = null;
        Statement selectStmt = null;
        
        String queryString = "Select " + 
                COL_FILTERNAME + " " +
                "FROM " + 
                TABLE_SEARCHFILTER + " " +
                "WHERE " + COL_USERNAME + " = '" + userConfig.userName + "';";
        conn = connect();
        ResultSet rs = doQuery(conn, queryString, selectStmt, false);
        
        
        
        while (rs.next()){
            cmbFilterName.addItem(rs.getString(COL_FILTERNAME));
        }
    }
}
