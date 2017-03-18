package org.jeecgframework.web.system.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.web.system.service.MysqlImportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service("mysqlImportDataService")
@Transactional
public class MysqlImportDataServiceImpl extends CommonServiceImpl implements MysqlImportDataService {
	
	@Autowired
	private AccessExportDataServiceImpl accessExportDataService;
	private static final Logger logger = Logger.getLogger(MysqlImportDataServiceImpl.class);
	
	/**access考勤数据取出
	 * @throws SQLException */
	public String  addMysql() throws Exception{
		Connection conn = null;  
		try{
			conn = SessionFactoryUtils.getDataSource(
					getSession().getSessionFactory()).getConnection(); 
			
			String batchDate = queryBatchDate(conn);//查询批处理日期
			
			if(batchDate.equals((new SimpleDateFormat("yyyy-MM-dd")).format(new Date()))){
				return batchDate;
			}
			
			deleteCheckinout(conn,batchDate);//清除批处理的考勤流水信息
			
			String batchDate1 = batchDate.replace("-", "/");
			String [] batchDate2 = batchDate1.split("/");
			if(batchDate2[1].startsWith("0"))
				batchDate1 = batchDate2[0]+"/"+batchDate2[1].substring(1);
			else
				batchDate1 = batchDate2[0]+"/"+batchDate2[1];
			
			if(batchDate2[2].startsWith("0"))
				batchDate1 += "/"+batchDate2[2].substring(1);
			else
				batchDate1 += "/"+batchDate2[2];
			//获取考勤流水
			String query="select u.Badgenumber,c.CHECKTIME,c.CHECKTYPE,"
					+ "c.VERIFYCODE,c.SENSORID"
					+ " from  CHECKINOUT c,USERINFO u where c.USERID = u.USERID and c.checktime like '"+batchDate1+"%'"; 
			Statement stmt = null;
			ResultSet rs = null;
			try{
				stmt=accessExportDataService.getStatement(); 
			    rs=accessExportDataService.executeQuery(stmt, query); 
			    insertPCheckInOut(rs,conn);		//插入考勤流水
			}catch(Exception e){
				logger.error("考勤信息落地失败，失败原因：",e);
				throw new BusinessException("考勤信息落地失败，失败原因："+e);
			}finally{
				try{
					if(rs != null) rs.close();
					if(stmt != null) stmt.close();
					accessExportDataService.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
			return batchDate;
		}catch(Exception e){
			throw new BusinessException("考勤信息落地失败，失败原因："+e);
		}finally{
			if(conn != null) conn.close();
		}
	}
	
	/**
	 * 考勤统计数据插入Mysql数据库
	 * @param rs
	 * @throws ParseException 
	 */
	public void insertPCheckInOut(ResultSet rs,Connection conn) throws Exception {
		String sql = "insert into p_checkinout (USERID,checktime,checktype,verifycode,sensorid) values (?,?,?,?,?)";
		
		PreparedStatement pst = null; 
		try {  
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			int i=0;
			while(rs.next()){ 
				i++;
				pst.setInt(1,rs.getInt(1));   
				pst.setString(2,rs.getString(2));   
				pst.setString(3, rs.getString(3));   
		        pst.setInt(4, rs.getInt(4));
		        pst.setString(5,rs.getString(5));   
		        pst.addBatch();
			    // 500条记录插入一次
			    if (i % 500 == 0){
			    	pst.executeBatch();
			         conn.commit();
			     }
		   }
			// 最后插入不足500条的数据
			pst.executeBatch();
			conn.commit();
	    } catch (SQLException e) {  
	    	logger.error("批处理之导入考勤记录失败，失败原因："+e);  
	        throw new BusinessException("批处理失败，失败原因：导入考勤记录失败，");
	    }finally {
	    	 try {  
	         	if(pst != null) pst.close();  
	         } catch (SQLException e) {  
	             e.printStackTrace();  
	         }  
	    }
	}

	/**考勤数据插入考勤统计表*/
	public void deleteCheckinout(Connection conn,String batchDate){
		String delete="delete from p_checkinout "
					+ " where date_format(checktime,'%Y-%m-%d')='"+batchDate+"'"; 
		PreparedStatement pst = null; 
		try{
			pst = conn.prepareStatement(delete);
			pst.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(pst != null) pst.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}    
	}
	
	/** 获取批处理日期 */
	public String queryBatchDate(Connection conn) {
		String query = "select cur_values from s_xy_sys_conf where sys_code ='100001'"; // 插入批处理日期
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()){
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
}
