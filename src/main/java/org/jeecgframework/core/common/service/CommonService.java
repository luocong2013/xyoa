package org.jeecgframework.core.common.service;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.hibernate.qbc.HqlQuery;
import org.jeecgframework.core.common.hibernate.qbc.PageList;
import org.jeecgframework.core.common.model.common.DBTable;
import org.jeecgframework.core.common.model.common.UploadFile;
import org.jeecgframework.core.common.model.json.ComboTree;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.model.json.DataGridReturn;
import org.jeecgframework.core.common.model.json.ImportFile;
import org.jeecgframework.core.common.model.json.TreeGrid;
import org.jeecgframework.tag.vo.datatable.DataTableReturn;
import org.jeecgframework.tag.vo.easyui.Autocomplete;
import org.jeecgframework.tag.vo.easyui.ComboTreeModel;
import org.jeecgframework.tag.vo.easyui.TreeGridModel;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.dao.DataAccessException;

import com.xy.oa.activiti.util.CommentBean;
import com.xy.oa.util.ApplyTypeEnum;

public interface CommonService {
	/**
	 * 获取所有数据库表
	 * 
	 * @return
	 */
	public List<DBTable> getAllDbTableName();
	
	public Integer getAllDbTableSize();

	public <T> Serializable save(T entity);

	public <T> void saveOrUpdate(T entity);

	public <T> void delete(T entity);

	public <T> void batchSave(List<T> entitys);

	/**
	 * 根据实体名称和主键获取实体
	 * 
	 * @param <T>
	 * @param entityName
	 * @param id
	 * @return
	 */
	public <T> T get(Class<T> class1, Serializable id);

	/**
	 * 根据实体名称和主键获取实体
	 * 
	 * @param <T>
	 * @param entityName
	 * @param id
	 * @return
	 */
	public <T> T getEntity(Class entityName, Serializable id);

	/**
	 * 根据实体名称和字段名称和字段值获取唯一记录
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> T findUniqueByProperty(Class<T> entityClass,
			String propertyName, Object value);

	/**
	 * 按属性查找对象列表.
	 */
	public <T> List<T> findByProperty(Class<T> entityClass,
			String propertyName, Object value);

	/**
	 * 加载全部实体
	 * 
	 * @param <T>
	 * @param entityClass
	 * @return
	 */
	public <T> List<T> loadAll(final Class<T> entityClass);

	/**
	 * 删除实体主键删除
	 * 
	 * @param <T>
	 * @param entities
	 */
	public <T> void deleteEntityById(Class entityName, Serializable id);

	/**
	 * 删除实体集合
	 * 
	 * @param <T>
	 * @param entities
	 */
	public <T> void deleteAllEntitie(Collection<T> entities);

	/**
	 * 更新指定的实体
	 * 
	 * @param <T>
	 * @param pojo
	 */
	public <T> void updateEntitie(T pojo);

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T> List<T> findByQueryString(String hql);

	/**
	 * 根据sql更新
	 * 
	 * @param query
	 * @return
	 */
	public int updateBySqlString(String sql);

	/**
	 * 根据sql查找List
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T> List<T> findListbySql(String query);

	/**
	 * 通过属性称获取实体带排序
	 * 
	 * @param <T>
	 * @param clas
	 * @return
	 */
	public <T> List<T> findByPropertyisOrder(Class<T> entityClass,
			String propertyName, Object value, boolean isAsc);

	public <T> List<T> getList(Class clas);

	public <T> T singleResult(String hql);
	
	public <T> T singleResult(String hql, Object... params);

	/**
	 * 
	 * cq方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList getPageList(final CriteriaQuery cq, final boolean isOffset);

	/**
	 * 返回DataTableReturn模型
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public DataTableReturn getDataTableReturn(final CriteriaQuery cq,
			final boolean isOffset);

	/**
	 * 返回easyui datagrid模型
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public DataGridReturn getDataGridReturn(final CriteriaQuery cq,
			final boolean isOffset);

	/**
	 * 
	 * hqlQuery方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList getPageList(final HqlQuery hqlQuery,
			final boolean needParameter);

	/**
	 * 
	 * sqlQuery方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList getPageListBySql(final HqlQuery hqlQuery,
			final boolean isToEntity);

	public Session getSession();

	public List findByExample(final String entityName,
			final Object exampleEntity);

	/**
	 * 通过cq获取全部实体
	 * 
	 * @param <T>
	 * @param cq
	 * @return
	 */
	public <T> List<T> getListByCriteriaQuery(final CriteriaQuery cq,
			Boolean ispage);

	/**
	 * 文件上传
	 * 
	 * @param request
	 */
	public <T> T uploadFile(UploadFile uploadFile);

	public HttpServletResponse viewOrDownloadFile(UploadFile uploadFile);

	/**
	 * 生成XML文件
	 * 
	 * @param fileName
	 *            XML全路径
	 */
	public HttpServletResponse createXml(ImportFile importFile);

	/**
	 * 解析XML文件
	 * 
	 * @param fileName
	 *            XML全路径
	 */
	public void parserXml(String fileName);

	public List<ComboTree> comTree(List<TSDepart> all, ComboTree comboTree);

	/**
	 * 根据模型生成JSON
	 * 
	 * @param all 全部对象
	 * @param in 已拥有的对象
	 * @param recursive 是否递归加载所有子节点
     * @return List<ComboTree>
	 */
	public List<ComboTree> ComboTree(List all, ComboTreeModel comboTreeModel, List in, boolean recursive);


    /**
     * 构建树形数据表
	 * 
	 * @param all
	 * @param treeGridModel
	 * @return
	 */
	public List<TreeGrid> treegrid(List all, TreeGridModel treeGridModel);

	/**
	 * 获取自动完成列表
	 * 
	 * @param <T>
	 * @return
	 */
	public <T> List<T> getAutoList(Autocomplete autocomplete);

	/**
	 * 执行SQL
	 */
	public Integer executeSql(String sql, List<Object> param);

	/**
	 * 执行SQL
	 */
	public Integer executeSql(String sql, Object... param);

	/**
	 * 执行SQL 使用:name占位符
	 */
	public Integer executeSql(String sql, Map<String, Object> param);
	/**
	 * 执行SQL 使用:name占位符,并返回执行后的主键值
	 */
	public Object executeSqlReturnKey(String sql, Map<String, Object> param);
	/**
	 * 通过JDBC查找对象集合 使用指定的检索标准检索数据返回数据
	 */
	public List<Map<String, Object>> findForJdbc(String sql, Object... objs);

	/**
	 * 通过JDBC查找对象集合 使用指定的检索标准检索数据返回数据
	 */
	public Map<String, Object> findOneForJdbc(String sql, Object... objs);

	/**
	 * 通过JDBC查找对象集合,带分页 使用指定的检索标准检索数据并分页返回数据
	 */
	public List<Map<String, Object>> findForJdbc(String sql, int page, int rows);

	/**
	 * 通过JDBC查找对象集合,带分页 使用指定的检索标准检索数据并分页返回数据
	 */
	public <T> List<T> findObjForJdbc(String sql, int page, int rows,
			Class<T> clazz);

	/**
	 * 使用指定的检索标准检索数据并分页返回数据-采用预处理方式
	 * 
	 * @param criteria
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String, Object>> findForJdbcParam(String sql, int page,
			int rows, Object... objs);

	/**
	 * 使用指定的检索标准检索数据并分页返回数据For JDBC
	 */
	public Long getCountForJdbc(String sql);

	/**
	 * 使用指定的检索标准检索数据并分页返回数据For JDBC-采用预处理方式
	 * 
	 */
	public Long getCountForJdbcParam(String sql, Object[] objs);

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T> List<T> findHql(String hql, Object... param);

	public <T> List<T> pageList(DetachedCriteria dc, int firstResult,
			int maxResult);

	public <T> List<T> findByDetached(DetachedCriteria dc);

	/**
	 * 执行存储过程
	 * @param executeSql
	 * @param params
	 * @return
	 */
	public <T> List<T> executeProcedure(String procedureSql,Object... params);
	
	public <T> T singleResultSQL(String sql, Object...params);
	
	
	/**==================自定义操作接口==========================*/
	/**
	 * 查询当前用户的角色编码
	 * @param tsUser：当前用户对象
	 * @return
	 */
	public List<String> findHqlRoleCodes(TSUser tsUser);
	/**
	 * 查询当前用户所属的部门
	 * @param tsUser：当前用户对象
	 * @return
	 */
	public TSDepart findHqlTSDepart(TSUser tsUser);
	/**
	 * 查询当前用户所属部门的上一级部门
	 * @param tsUser：当前用户对象
	 * @return
	 */
	public TSDepart findHqlTSPDepart(TSUser tsUser);
	/**
	 * 查询当前用户所属部门的上一级部门的上一级部门
	 * @param tsUser
	 * @return
	 */
	public TSDepart findHqlTSPPDepart(TSUser tsUser);
	/**
	 * 通过角色编码查询属于该角色编码的所有用户
	 * @param roleCode：角色编码
	 * @return
	 */
	public List<TSUser> findHqlTSUsersByRoleCode(String roleCode);
	/**
	 * 通过角色编码和部门查询属于该角色编码和部门下的所有用户
	 * @param roleCode：角色编码
	 * @param tsDepart：所属部门对象
	 * @return
	 */
	public List<TSUser> findHqlTSUsersByRD(String roleCode, TSDepart tsDepart);
	/**
	 * 部门DM查询该部门下的所有用户（不包括自己）以及各种组的所有用户
	 * @param tsUser：当前用户对象
	 * @return
	 */
	public List<TSUser> findHqlTSUsersByUser(TSUser tsUser);
	/**
	 * 查询普通员工、直接上级、人事专员、部门DM、人事DM和副总裁（不包括自己）
	 * @param tsUser：当前用户对象
	 * @return
	 */
	public List<TSUser> findHqlTSUsersByUR(TSUser tsUser);
	
	
	/**
	 * 添加流程的服务接口
	 */
	
	/**
	 * 部署流程定义
	 * @param inputStream
	 * @param fileName
	 */
	public void deploy(InputStream inputStream, String fileName) throws Exception;
	/**
	 * 查询流程定义信息，对应（act_re_procdef）表
	 * @return
	 */
	public List<ProcessDefinition> findProcessDefinitions();
	/**
	 * 查看流程定义图片
	 * @param pdid
	 */
	public InputStream viewImage(String pdid);
	/**
	 * 删除流程定义
	 */
	public void deleteProcessDefinition(String deploymentId);
	/**
	 * 启动流程，返回流程实例ID
	 * @param processDefinitionKey：流程KEY
	 * @param Id：业务表ID
	 * @param applyUserId：申请人编号(userName)
	 * @return
	 * @throws Exception
	 */
	public String startFlow(String processDefinitionKey, String Id, String applyUserId) throws Exception;
	/**
	 * 查询下一连线名称集合
	 * @param flowInstId
	 * @param assignee
	 * @return
	 */
	public List<String> findNextLineNames(String flowInstId, String assignee);
	/**
	 * 查询下一审批人角色编码
	 * @param flowInstId
	 * @param lineName
	 * @param assignee
	 * @return
	 * @throws Exception
	 */
	public String findNextApproverRoleCode(String flowInstId, String lineName, String assignee);
	/**
	 * 按照指定的前缀查询连线名
	 * @param flowInstId
	 * @param fixLineName
	 * @param assignee
	 * @return
	 */
	public String findLineName(String flowInstId, String fixLineName, String assignee);
	/**
	 * 撤销流程
	 * @param flowInstId：流程实例ID
	 * @throws Exception
	 */
	public void delFlow(String flowInstId) throws Exception;
	/**
	 * 查看当前用户的任务（未完成的任务）
	 * @param assignee：当前登录用户编号(userName)
	 * @return
	 * @throws Exception
	 */
	public List<Task> queryTask(String assignee) throws Exception;
	/**
	 * 查看当前用户的历史任务（已完成的任务）
	 * @param assignee：当前登录用户编号(userName)
	 * @return
	 * @throws Exception
	 */
	public List<HistoricTaskInstance> queryHisTask(String assignee) throws Exception;
	/**
	 * 分页查询 当前用户正在进行的任务对应的流程实例ID
	 * @param tableName：表名
	 * @param assignee：当前登录用户编号(userName)
	 * @param isHr：是否是人事部人员
	 * @param curPage：当前页
	 * @param pageSize：每页显示条数
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryRunFlowInstId(String tableName, String assignee, boolean isHr, int curPage, int pageSize) throws Exception;
	/**
	 * 查询当前用户正在进行的任务总条数
	 * @param tableName：表名
	 * @param assignee：当前登录用户编号(userName)
	 * @param isHr：是否是人事部人员
	 * @return
	 * @throws Exception
	 */
	public int queryRunTotal(String tableName, String assignee, boolean isHr) throws Exception;
	/**
	 * 分页查询 当前用户的历史任务对应的流程实例ID（去除重复项）
	 * @param tableName：表名
	 * @param assignee：当前登录用户编号(userName)
	 * @param isHr：是否是人事部人员
	 * @param curPage：当前页
	 * @param pageSize：每页显示条数
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryHisFlowInstId(String tableName, String assignee, boolean isHr, int curPage, int pageSize) throws Exception;
	/**
	 * 查询当前用户的历史任务总条数（去除重复项）
	 * @param tableName：表名
	 * @param assignee：当前登录用户编号(userName)
	 * @param isHr：是否是人事部人员
	 * @return
	 * @throws Exception
	 */
	public int queryHisTotal(String tableName, String assignee, boolean isHr) throws Exception;
	
	/**
	 * 根据流程实例ID和流程任务节点ID查询历史流程节点变量值
	 * @param flowInstId：流程实例ID
	 * @param taskId：流程任务节点ID
	 * @return：历史流程节点变量值
	 * @throws Exception
	 */
	public Object findHistoryVarInst(String flowInstId, String taskId) throws Exception;
	/**
	 * 完成任务
	 * @param flowInstId：流程实例ID
	 * @param variables：传入的参数
	 * @param assignee：当前登录用户编号(userName)
	 * @param comment：批注信息
	 * @throws Exception
	 */
	public void completeTask(String flowInstId, Map<String, Object> variables, String assignee, String comment) throws Exception;
	/**
	 * 查看流程图片
	 * @param flowInstId：流程实例ID
	 * @return
	 * @throws Exception
	 */
	public InputStream graphics(String flowInstId) throws Exception;
	/**
	 * 查询历史任务节点批注信息
	 * @param flowInstId：流程实例ID
	 * @return
	 * @throws Exception
	 */
	public List<CommentBean> findComment(String flowInstId) throws Exception;
	
	/**
	 * 生成享宇相关流程的申请编号
	 * @param applyType：申请类型
	 * @param tableName：申请类型对应的表
	 * @return 申请编号
	 * @throws Exception
	 */
	public String getNextApplyNo(ApplyTypeEnum applyType,String tableName) throws Exception;
	
	
	public DataGrid getDataGridForHSql(final Class<?> className, final String hSql, final DataGrid dataGrid,final boolean isOffset);
	
	public <T> DataGrid getDataGridRunForHQL(final Class<T> className, final DataGrid dataGrid, final boolean isHr, final String paramHql, final String orgId, final int beginNum, final int pageSize, final Object... param);
	
	public <T> DataGrid getDataGridHisForHQL(final Class<T> className, final DataGrid dataGrid, final boolean isHr, final String paramHql, final String orgId, final int beginNum, final int pageSize, final Object... param);
	
	
	public <T> int getRunTotal(final Class<T> className, final boolean isHr, final String paramHql, final String orgId, final Object... param);
	
	public <T> int getHisTotal(final Class<T> className, final boolean isHr, final String paramHql, final String orgId, final Object... param);

	public List<String> getSSDeptId(String deptId) ;
	
	public List<TSDepart> getAll();
	
	public String getTSDepartAllStr();
	
	public String getSSDeptIdForString(String orgId);
	
	public String getDeptAllStr();
}
