package org.jeecgframework.web.system.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jeecgframework.web.system.service.AccessExportDataService;
import org.springframework.stereotype.Service;

@Service("AccessExportDataService")
public class AccessExportDataServiceImpl implements AccessExportDataService {
	public Connection conn=null; 

	@Override
	public Statement getStatement() throws Exception {
		System.out.println("1");
		Class.forName("org.objectweb.rmijdbc.Driver").newInstance();
    	String url = "jdbc:rmi://192.168.2.32/jdbc:odbc:xy_access_db";
        conn = DriverManager.getConnection(url); 
        System.out.println(conn);
        return conn.createStatement(); 
	}

	@Override
	public Statement getStatement(String dbPath) throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); 
        String dburl = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};" + 
                "DBQ="+dbPath;// 此为NO-DSN方式 
        // String dburl ="jdbc:odbc:odbcName";//此为ODBC连接方式 
        conn = DriverManager.getConnection(dburl); 
        return conn.createStatement(); 
	}

	@Override
	public Statement getStatement(String dbPath, String password) throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); 
        String dburl = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};" + 
                "pwd="+password+";DBQ="+dbPath;// 此为NO-DSN方式 
        // String dburl ="jdbc:odbc:odbcName";//此为ODBC连接方式 
        conn = DriverManager.getConnection(dburl); 
        return conn.createStatement(); 
	}

	@Override
	public ResultSet executeQuery(Statement stmt, String query) throws Exception {
		  ResultSet rs=stmt.executeQuery(query); 
	        return rs; 
	}

	@Override
	public void executeUpdate(Statement stmt, String query) throws SQLException {
		stmt.executeUpdate(query);
	}

	@Override
	public void close() throws SQLException {
		 if(conn!=null) 
	            conn.close(); 
	}
	

}
