package org.jeecgframework.web.system.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jeecgframework.core.common.service.CommonService;

public interface AccessExportDataService {
	Statement getStatement() throws Exception;
	Statement getStatement(String dbPath) throws Exception;
	Statement getStatement(String dbPath,String password) throws Exception;
	ResultSet executeQuery(Statement stmt,String query) throws Exception;
	void executeUpdate(Statement stmt,String query) throws SQLException;
	void close() throws SQLException;
}
