package org.jeecgframework.core.common.service.impl;

import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.jeecgframework.core.common.dao.ICommonDao;
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
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.util.ProcessInstanceDiagramCmd;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.vo.datatable.DataTableReturn;
import org.jeecgframework.tag.vo.easyui.Autocomplete;
import org.jeecgframework.tag.vo.easyui.ComboTreeModel;
import org.jeecgframework.tag.vo.easyui.TreeGridModel;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xy.oa.activiti.util.CommentBean;
import com.xy.oa.util.ApplyNoUtils;
import com.xy.oa.util.ApplyTypeEnum;
import com.xy.oa.util.Constants;

@Service("commonService")
@Transactional
public class CommonServiceImpl implements CommonService {
	@Autowired
	public ICommonDao commonDao = null;
	// 引入流程的各种服务
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ManagementService managementService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private HistoryService historyService;

	/**
	 * 获取所有数据库表
	 * 
	 * @return
	 */
	public List<DBTable> getAllDbTableName() {
		return commonDao.getAllDbTableName();
	}

	public Integer getAllDbTableSize() {
		return commonDao.getAllDbTableSize();
	}

	public void setCommonDao(ICommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public <T> Serializable save(T entity) {
		return commonDao.save(entity);
	}

	public <T> void saveOrUpdate(T entity) {
		commonDao.saveOrUpdate(entity);

	}

	public <T> void delete(T entity) {
		commonDao.delete(entity);

	}

	/**
	 * 删除实体集合
	 * 
	 * @param <T>
	 * @param entities
	 */
	public <T> void deleteAllEntitie(Collection<T> entities) {
		commonDao.deleteAllEntitie(entities);
	}

	/**
	 * 根据实体名获取对象
	 */
	public <T> T get(Class<T> class1, Serializable id) {
		return commonDao.get(class1, id);
	}

	/**
	 * 根据实体名返回全部对象
	 * 
	 * @param <T>
	 * @param hql
	 * @param size
	 * @return
	 */
	public <T> List<T> getList(Class clas) {
		return commonDao.loadAll(clas);
	}

	/**
	 * 根据实体名获取对象
	 */
	public <T> T getEntity(Class entityName, Serializable id) {
		return commonDao.getEntity(entityName, id);
	}

	/**
	 * 根据实体名称和字段名称和字段值获取唯一记录
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value) {
		return commonDao.findUniqueByProperty(entityClass, propertyName, value);
	}

	/**
	 * 按属性查找对象列表.
	 */
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value) {

		return commonDao.findByProperty(entityClass, propertyName, value);
	}

	/**
	 * 加载全部实体
	 * 
	 * @param <T>
	 * @param entityClass
	 * @return
	 */
	public <T> List<T> loadAll(final Class<T> entityClass) {
		return commonDao.loadAll(entityClass);
	}

	public <T> T singleResult(String hql) {
		return commonDao.singleResult(hql);
	}

	public <T> T singleResult(String hql, Object... params) {
		return commonDao.singleResult(hql, params);
	}

	/**
	 * 删除实体主键ID删除对象
	 * 
	 * @param <T>
	 * @param entities
	 */
	public <T> void deleteEntityById(Class entityName, Serializable id) {
		commonDao.deleteEntityById(entityName, id);
	}

	/**
	 * 更新指定的实体
	 * 
	 * @param <T>
	 * @param pojo
	 */
	public <T> void updateEntitie(T pojo) {
		commonDao.updateEntitie(pojo);

	}

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T> List<T> findByQueryString(String hql) {
		return commonDao.findByQueryString(hql);
	}

	/**
	 * 根据sql更新
	 * 
	 * @param query
	 * @return
	 */
	public int updateBySqlString(String sql) {
		return commonDao.updateBySqlString(sql);
	}

	/**
	 * 根据sql查找List
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T> List<T> findListbySql(String query) {
		return commonDao.findListbySql(query);
	}

	/**
	 * 通过属性称获取实体带排序
	 * 
	 * @param <T>
	 * @param clas
	 * @return
	 */
	public <T> List<T> findByPropertyisOrder(Class<T> entityClass, String propertyName, Object value, boolean isAsc) {
		return commonDao.findByPropertyisOrder(entityClass, propertyName, value, isAsc);
	}

	/**
	 * 
	 * cq方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList getPageList(final CriteriaQuery cq, final boolean isOffset) {
		return commonDao.getPageList(cq, isOffset);
	}

	/**
	 * 返回DataTableReturn模型
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public DataTableReturn getDataTableReturn(final CriteriaQuery cq, final boolean isOffset) {
		return commonDao.getDataTableReturn(cq, isOffset);
	}

	/**
	 * 返回easyui datagrid模型
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public DataGridReturn getDataGridReturn(final CriteriaQuery cq, final boolean isOffset) {
		return commonDao.getDataGridReturn(cq, isOffset);
	}

	/**
	 * 
	 * hqlQuery方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList getPageList(final HqlQuery hqlQuery, final boolean needParameter) {
		return commonDao.getPageList(hqlQuery, needParameter);
	}

	/**
	 * 
	 * sqlQuery方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList getPageListBySql(final HqlQuery hqlQuery, final boolean isToEntity) {
		return commonDao.getPageListBySql(hqlQuery, isToEntity);
	}

	public Session getSession()

	{
		return commonDao.getSession();
	}

	public List findByExample(final String entityName, final Object exampleEntity) {
		return commonDao.findByExample(entityName, exampleEntity);
	}

	/**
	 * 通过cq获取全部实体
	 * 
	 * @param <T>
	 * @param cq
	 * @return
	 */
	public <T> List<T> getListByCriteriaQuery(final CriteriaQuery cq, Boolean ispage) {
		return commonDao.getListByCriteriaQuery(cq, ispage);
	}

	/**
	 * 文件上传
	 * 
	 * @param request
	 */
	public <T> T uploadFile(UploadFile uploadFile) {
		return commonDao.uploadFile(uploadFile);
	}

	public HttpServletResponse viewOrDownloadFile(UploadFile uploadFile)

	{
		return commonDao.viewOrDownloadFile(uploadFile);
	}

	/**
	 * 生成XML文件
	 * 
	 * @param fileName
	 *            XML全路径
	 * @return
	 */
	public HttpServletResponse createXml(ImportFile importFile) {
		return commonDao.createXml(importFile);
	}

	/**
	 * 解析XML文件
	 * 
	 * @param fileName
	 *            XML全路径
	 */
	public void parserXml(String fileName) {
		commonDao.parserXml(fileName);
	}

	public List<ComboTree> comTree(List<TSDepart> all, ComboTree comboTree) {
		return commonDao.comTree(all, comboTree);
	}

	public List<ComboTree> ComboTree(List all, ComboTreeModel comboTreeModel, List in, boolean recursive) {
		return commonDao.ComboTree(all, comboTreeModel, in, recursive);
	}

	/**
	 * 构建树形数据表
	 */
	public List<TreeGrid> treegrid(List all, TreeGridModel treeGridModel) {
		return commonDao.treegrid(all, treeGridModel);
	}

	/**
	 * 获取自动完成列表
	 * 
	 * @param <T>
	 * @return
	 */
	public <T> List<T> getAutoList(Autocomplete autocomplete) {
		StringBuffer sb = new StringBuffer("");
		for (String searchField : autocomplete.getSearchField().split(",")) {
			sb.append("  or " + searchField + " like '%" + autocomplete.getTrem() + "%' ");
		}
		String hql = "from " + autocomplete.getEntityName() + " where 1!=1 " + sb.toString();
		return commonDao.getSession().createQuery(hql).setFirstResult(autocomplete.getCurPage() - 1)
				.setMaxResults(autocomplete.getMaxRows()).list();
	}

	public Integer executeSql(String sql, List<Object> param) {
		return commonDao.executeSql(sql, param);
	}

	public Integer executeSql(String sql, Object... param) {
		return commonDao.executeSql(sql, param);
	}

	public Integer executeSql(String sql, Map<String, Object> param) {
		return commonDao.executeSql(sql, param);
	}

	public Object executeSqlReturnKey(String sql, Map<String, Object> param) {
		return commonDao.executeSqlReturnKey(sql, param);
	}

	public List<Map<String, Object>> findForJdbc(String sql, int page, int rows) {
		return commonDao.findForJdbc(sql, page, rows);
	}

	public List<Map<String, Object>> findForJdbc(String sql, Object... objs) {
		return commonDao.findForJdbc(sql, objs);
	}

	public List<Map<String, Object>> findForJdbcParam(String sql, int page, int rows, Object... objs) {
		return commonDao.findForJdbcParam(sql, page, rows, objs);
	}

	public <T> List<T> findObjForJdbc(String sql, int page, int rows, Class<T> clazz) {
		return commonDao.findObjForJdbc(sql, page, rows, clazz);
	}

	public Map<String, Object> findOneForJdbc(String sql, Object... objs) {
		return commonDao.findOneForJdbc(sql, objs);
	}

	public Long getCountForJdbc(String sql) {
		return commonDao.getCountForJdbc(sql);
	}

	public Long getCountForJdbcParam(String sql, Object[] objs) {
		return commonDao.getCountForJdbcParam(sql, objs);
	}

	public <T> void batchSave(List<T> entitys) {
		this.commonDao.batchSave(entitys);
	}

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T> List<T> findHql(String hql, Object... param) {
		return this.commonDao.findHql(hql, param);
	}

	public <T> List<T> pageList(DetachedCriteria dc, int firstResult, int maxResult) {
		return this.commonDao.pageList(dc, firstResult, maxResult);
	}

	public <T> List<T> findByDetached(DetachedCriteria dc) {
		return this.commonDao.findByDetached(dc);
	}

	/**
	 * 调用存储过程
	 */
	public <T> List<T> executeProcedure(String procedureSql, Object... params) {
		return this.commonDao.executeProcedure(procedureSql, params);
	}

	/**
	 * SQL语句查询
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	@Override
	public <T> T singleResultSQL(String sql, Object... params) {
		return this.commonDao.singleResultSQL(sql, params);
	}

	/** ==================自定义操作接口========================== */
	/**
	 * 查询当前用户的角色编码
	 * 
	 * @param tsUser：当前用户对象
	 * @return
	 */
	@Override
	public List<String> findHqlRoleCodes(TSUser tsUser) {
		String rHql = "select ru.TSRole.roleCode from TSRoleUser ru where ru.TSUser = ?";
		return this.findHql(rHql, tsUser);
	}

	/**
	 * 查询当前用户所属的部门
	 * 
	 * @param tsUser：当前用户对象
	 * @return
	 */
	@Override
	public TSDepart findHqlTSDepart(TSUser tsUser) {
		String dHql = "select uo.tsDepart from TSUserOrg uo where uo.tsUser = ?";
		return this.singleResult(dHql, tsUser);
	}

	/**
	 * 查询当前用户所属部门的上一级部门
	 * 
	 * @param tsUser：当前用户对象
	 * @return
	 */
	@Override
	public TSDepart findHqlTSPDepart(TSUser tsUser) {
		String hql = "select uo.tsDepart.TSPDepart from TSUserOrg uo where uo.tsUser = ?";
		return this.singleResult(hql, tsUser);
	}

	/**
	 * 查询当前用户所属部门的上一级部门的上一级部门
	 * 
	 * @param tsUser
	 * @return
	 */
	@Override
	public TSDepart findHqlTSPPDepart(TSUser tsUser) {
		String hql = "select uo.tsDepart.TSPDepart.TSPDepart from TSUserOrg uo where uo.tsUser = ?";
		return this.singleResult(hql, tsUser);
	}

	/**
	 * 查询是某一角色编码的所有用户
	 * 
	 * @param roleCode：角色编码
	 * @return
	 */
	@Override
	public List<TSUser> findHqlTSUsersByRoleCode(String roleCode) {
		String hql = "select ru.TSUser from TSRoleUser ru where ru.TSRole.roleCode = ?";
		return this.findHql(hql, roleCode);
	}

	/**
	 * 通过角色编码和部门查询属于该角色编码和部门下的所有用户
	 * 
	 * @param roleCode：角色编码
	 * @param tsDepart：所属部门对象
	 * @return
	 */
	@Override
	public List<TSUser> findHqlTSUsersByRD(String roleCode, TSDepart tsDepart) {
		String hql = "select ru.TSUser from TSRoleUser ru, TSUserOrg uo where ru.TSUser = uo.tsUser and ru.TSRole.roleCode = ? and uo.tsDepart = ?";
		return this.findHql(hql, roleCode, tsDepart);
	}

	/**
	 * 部门DM查询该部门下的所有用户（不包括自己）以及各种组的所有用户
	 * 
	 * @param tsUser：当前用户对象
	 * @return
	 */
	@Override
	public List<TSUser> findHqlTSUsersByUser(TSUser tsUser) {
		TSDepart tsDepart = tsUser.getCurrentDepart();
		String hql = "select uo1.tsUser from TSUserOrg uo1 where uo1.tsUser <> ? and uo1.tsDepart in (select d from TSDepart d where "
				+ "d.orgCode like '" + tsDepart.getOrgCode() + "%')";
		return this.findHql(hql, tsUser);
	}

	/**
	 * 查询普通员工、直接上级、人事专员、部门DM、人事DM和副总裁（不包括自己）
	 * 
	 * @param tsUser：当前用户对象
	 * @return
	 */
	@Override
	public List<TSUser> findHqlTSUsersByUR(TSUser tsUser) {
		String hql = "select ru.TSUser from TSRoleUser ru where ru.TSUser <> ? and ru.TSRole.roleCode in ('employee', 'headman', 'hr', 'dm', 'hrdm', 'vice')";
		return this.findHql(hql, tsUser);
	}

	/**
	 * 添加流程的服务接口实现
	 */

	/**
	 * 部署流程定义
	 * 
	 * @param inputStream
	 * @param fileName
	 */
	@Override
	public void deploy(InputStream inputStream, String fileName) throws Exception {
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		repositoryService.createDeployment()// 创建部署对象
				.name(fileName)// 添加部署名称
				.addZipInputStream(zipInputStream).deploy();// 完成部署
	}

	/**
	 * 查询流程定义信息，对应（act_re_procdef）表
	 * 
	 * @return
	 */
	@Override
	public List<ProcessDefinition> findProcessDefinitions() {
		List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()// 创建流程定义查询
				.orderByProcessDefinitionKey().asc().orderByProcessDefinitionVersion().desc()// 按照版本的降序排列
				.list();
		return processDefinitions;
	}

	/**
	 * 查看流程定义图片
	 */
	@Override
	public InputStream viewImage(String pdid) {
		return repositoryService.getProcessDiagram(pdid);
	}

	/**
	 * 删除流程定义
	 */
	@Override
	public void deleteProcessDefinition(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);
	}

	/**
	 * 启动流程，返回流程实例ID
	 * 
	 * @param processDefinitionKey：流程KEY
	 * @param Id：业务表ID
	 * @param applyUserId：申请人编号(userName)
	 * @return
	 * @throws Exception
	 */
	@Override
	public String startFlow(String processDefinitionKey, String Id, String applyUserId) throws Exception {
		// 设置流程启动人
		identityService.setAuthenticatedUserId(applyUserId);
		// 通过key启动流程，传入数据，并将业务表的ID作为BUSINESS_KEY_
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, Id);
		return processInstance.getId();
	}

	/**
	 * 查询下一连线名称集合
	 * 
	 * @param flowInstId
	 * @param assignee
	 * @return
	 */
	@Override
	public List<String> findNextLineNames(String flowInstId, String assignee) {
		List<String> nextLineNames = new ArrayList<String>();
		Task task = taskService.createTaskQuery().processInstanceId(flowInstId).taskAssignee(assignee).singleResult();
		String processDefinitionId = task.getProcessDefinitionId();
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processDefinitionId);
		String activityId = task.getTaskDefinitionKey();
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
		// 获取当前活动完成之后的连线
		List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
		if (pvmList != null && !pvmList.isEmpty()) {
			for (PvmTransition pvm : pvmList) {
				Object obj = pvm.getProperty("name");
				if (obj == null) {
					// 获取连线的终端路径
					PvmActivity pa = pvm.getDestination();
					List<PvmTransition> pvmList2 = pa.getOutgoingTransitions();
					if (pvmList2 != null && !pvmList2.isEmpty()) {
						for (PvmTransition pvm2 : pvmList2) {
							nextLineNames.add(pvm2.getProperty("name").toString());
						}
					}
				} else {
					nextLineNames.add(obj.toString());
				}
			}
		}
		return nextLineNames;
	}

	/**
	 * 查询下一审批人角色编码
	 * 
	 * @param flowInstId
	 * @param lineName
	 * @param assignee
	 * @return
	 * @throws Exception
	 */
	@Override
	public String findNextApproverRoleCode(String flowInstId, String lineName, String assignee) {
		String roleCode = null;
		Task task = taskService.createTaskQuery().processInstanceId(flowInstId).taskAssignee(assignee).singleResult();
		String processDefinitionId = task.getProcessDefinitionId();
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processDefinitionId);
		String activityId = task.getTaskDefinitionKey();
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
		// 获取当前活动完成之后的连线
		List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
		if (pvmList != null && !pvmList.isEmpty()) {
			for (PvmTransition pvm : pvmList) {
				// 获取连线的终端路径
				PvmActivity pa = pvm.getDestination();
				if (pa.getProperty("type").equals("userTask")) {
					if (pvm.getProperty("name").toString().equals(lineName)) {
						roleCode = pa.getId();
						break;
					}
				} else {
					List<PvmTransition> pvmList2 = pa.getOutgoingTransitions();
					if (pvmList2 != null && !pvmList2.isEmpty()) {
						for (PvmTransition pvm2 : pvmList2) {
							PvmActivity pa2 = pvm2.getDestination();
							if (pa2.getProperty("type").equals("userTask")) {
								if (pvm2.getProperty("name").toString().equals(lineName)) {
									roleCode = pa2.getId();
									break;
								}
							}
						}
					}
				}
			}
		}
		return roleCode;
	}

	/**
	 * 按照指定的前缀查询连线名
	 * 
	 * @param flowInstId
	 * @param fixLineName
	 * @param assignee
	 * @return
	 */
	@Override
	public String findLineName(String flowInstId, String fixLineName, String assignee) {
		String lineName = null;
		Task task = taskService.createTaskQuery().processInstanceId(flowInstId).taskAssignee(assignee).singleResult();
		String processDefinitionId = task.getProcessDefinitionId();
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processDefinitionId);
		String activityId = task.getTaskDefinitionKey();
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
		// 获取当前活动完成之后的连线
		List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
		if (pvmList != null && !pvmList.isEmpty()) {
			for (PvmTransition pvm : pvmList) {
				// 获取连线的终端路径
				PvmActivity pa = pvm.getDestination();
				List<PvmTransition> pvmList2 = pa.getOutgoingTransitions();
				if (pvmList2 != null && !pvmList2.isEmpty()) {
					for (PvmTransition pvm2 : pvmList2) {
						Object obj = pvm2.getProperty("name");
						if (obj.toString().startsWith(fixLineName)) {
							lineName = obj.toString();
						}
					}
				}
			}
		}
		return lineName;
	}

	/**
	 * 撤销流程
	 * 
	 * @param flowInstId：流程实例ID
	 * @throws Exception
	 */
	@Override
	public void delFlow(String flowInstId) throws Exception {
		runtimeService.deleteProcessInstance(flowInstId, "");
	}

	/**
	 * 查看当前用户的任务（未完成的任务）
	 * 
	 * @param assignee：当前登录用户编号(userName)
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Task> queryTask(String assignee) throws Exception {
		List<Task> tasks = null;
		tasks = taskService.createNativeTaskQuery()
				.sql("SELECT * FROM " + managementService.getTableName(Task.class) + " T "
						+ "WHERE T.TASK_DEF_KEY_ <> #{taskDefKey} " + "AND T.ASSIGNEE_ = #{assignee} "
						+ "ORDER BY T.CREATE_TIME_ ASC")
				.parameter("taskDefKey", "applyUserId").parameter("assignee", assignee).list();
		return tasks;
	}

	/**
	 * 查看当前用户的历史任务（已完成的任务）
	 * 
	 * @param assignee：当前登录用户编号(userName)
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<HistoricTaskInstance> queryHisTask(String assignee) throws Exception {
		List<HistoricTaskInstance> historicTaskInstances = null;
		historicTaskInstances = historyService.createNativeHistoricTaskInstanceQuery()
				.sql("SELECT * FROM " + managementService.getTableName(HistoricTaskInstance.class) + " H "
						+ "WHERE H.TASK_DEF_KEY_ <> #{taskDefKey} " + "AND H.ASSIGNEE_ = #{assignee} "
						+ "AND H.END_TIME_ IS NOT NULL " + "ORDER BY H.END_TIME_ DESC")
				.parameter("taskDefKey", "applyUserId").parameter("assignee", assignee).list();
		return historicTaskInstances;
	}

	/**
	 * 分页查询 当前用户正在进行的任务对应的流程实例ID
	 * 
	 * @param tableName：表名
	 * @param assignee：当前登录用户编号(userName)
	 * @param isHr：是否是人事部人员
	 * @param curPage：当前页
	 * @param pageSize：每页显示条数
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> queryRunFlowInstId(String tableName, String assignee, boolean isHr, int curPage,
			int pageSize) throws Exception {
		List<Map<String, Object>> lMaps = null;
		String sql = null;
		if (isHr) {
			sql = "SELECT art.PROC_INST_ID_ " + "FROM act_ru_task art " + "INNER JOIN " + tableName
					+ " s ON art.PROC_INST_ID_ = s.flow_inst_id " + "WHERE art.NAME_ = '人事部' AND art.ASSIGNEE_ = ? "
					+ "ORDER BY s.c_time DESC " + "LIMIT ?, ?";
		} else {
			sql = "SELECT art.PROC_INST_ID_ " + "FROM act_ru_task art " + "INNER JOIN " + tableName
					+ " s ON art.PROC_INST_ID_ = s.flow_inst_id " + "WHERE art.NAME_ <> '人事部' AND art.ASSIGNEE_ = ? "
					+ "ORDER BY s.c_time DESC " + "LIMIT ?, ?";
		}
		lMaps = this.findForJdbc(sql, assignee, curPage, pageSize);
		return lMaps;
	}

	/**
	 * 查询当前用户正在进行的任务总条数
	 * 
	 * @param tableName：表名
	 * @param assignee：当前登录用户编号(userName)
	 * @param isHr：是否是人事部人员
	 * @return
	 * @throws Exception
	 */
	@Override
	public int queryRunTotal(String tableName, String assignee, boolean isHr) throws Exception {
		String sql = null;
		if (isHr) {
			sql = "SELECT COUNT(art.PROC_INST_ID_) " + "FROM act_ru_task art " + "INNER JOIN " + tableName
					+ " s ON art.PROC_INST_ID_ = s.flow_inst_id " + "WHERE art.NAME_ = '人事部' AND art.ASSIGNEE_ = ?";
		} else {
			sql = "SELECT COUNT(art.PROC_INST_ID_) " + "FROM act_ru_task art " + "INNER JOIN " + tableName
					+ " s ON art.PROC_INST_ID_ = s.flow_inst_id " + "WHERE art.NAME_ <> '人事部' AND art.ASSIGNEE_ = ?";
		}
		Long total = this.getCountForJdbcParam(sql, new Object[] { assignee });
		return total.intValue();
	}

	/**
	 * 分页查询 当前用户的历史任务对应的流程实例ID（去除重复项）
	 * 
	 * @param tableName：表名
	 * @param assignee：当前登录用户编号(userName)
	 * @param isHr：是否是人事部人员
	 * @param curPage：当前页
	 * @param pageSize：每页显示条数
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> queryHisFlowInstId(String tableName, String assignee, boolean isHr, int curPage,
			int pageSize) throws Exception {
		List<Map<String, Object>> lMaps = null;
		String sql = null;
		if (isHr) {
			sql = "SELECT DISTINCT aht.PROC_INST_ID_ " + "FROM act_hi_taskinst aht " + "INNER JOIN " + tableName
					+ " s ON aht.PROC_INST_ID_ = s.flow_inst_id "
					+ "WHERE aht.NAME_ = '人事部' AND aht.ASSIGNEE_ = ? AND aht.END_TIME_ IS NOT NULL "
					+ "AND aht.DELETE_REASON_ <> 'deleted' " + "ORDER BY s.c_time DESC " + "LIMIT ?, ?";
		} else {
			sql = "SELECT DISTINCT aht.PROC_INST_ID_ " + "FROM act_hi_taskinst aht " + "INNER JOIN " + tableName
					+ " s ON aht.PROC_INST_ID_ = s.flow_inst_id "
					+ "WHERE aht.NAME_ <> '人事部' AND aht.ASSIGNEE_ = ? AND aht.END_TIME_ IS NOT NULL "
					+ "AND aht.DELETE_REASON_ <> 'deleted' " + "ORDER BY s.c_time DESC " + "LIMIT ?, ?";
		}
		lMaps = this.findForJdbc(sql, assignee, curPage, pageSize);
		return lMaps;
	}

	/**
	 * 查询当前用户的历史任务总条数（去除重复项）
	 * 
	 * @param tableName：表名
	 * @param assignee：当前登录用户编号(userName)
	 * @param isHr：是否是人事部人员
	 * @return
	 * @throws Exception
	 */
	@Override
	public int queryHisTotal(String tableName, String assignee, boolean isHr) throws Exception {
		String sql = null;
		if (isHr) {
			sql = "SELECT COUNT(DISTINCT aht.PROC_INST_ID_) " + "FROM act_hi_taskinst aht " + "INNER JOIN " + tableName
					+ " s ON aht.PROC_INST_ID_ = s.flow_inst_id "
					+ "WHERE aht.NAME_ = '人事部' AND aht.ASSIGNEE_ = ? AND aht.END_TIME_ IS NOT NULL "
					+ "AND aht.DELETE_REASON_ <> 'deleted'";
		} else {
			sql = "SELECT COUNT(DISTINCT aht.PROC_INST_ID_) " + "FROM act_hi_taskinst aht " + "INNER JOIN " + tableName
					+ " s ON aht.PROC_INST_ID_ = s.flow_inst_id "
					+ "WHERE aht.NAME_ <> '人事部' AND aht.ASSIGNEE_ = ? AND aht.END_TIME_ IS NOT NULL "
					+ "AND aht.DELETE_REASON_ <> 'deleted'";
		}
		Long total = this.getCountForJdbcParam(sql, new Object[] { assignee });
		return total.intValue();
	}

	/**
	 * 根据流程实例ID和流程任务节点ID查询历史流程节点变量值
	 * 
	 * @param flowInstId：流程实例ID
	 * @param taskId：流程任务节点ID
	 * @return：历史流程节点变量值
	 * @throws Exception
	 */
	@Override
	public Object findHistoryVarInst(String flowInstId, String taskId) throws Exception {
		Object val = null;
		HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
				.processInstanceId(flowInstId).taskId(taskId).variableName("isPass").singleResult();
		val = historicVariableInstance.getValue();
		return val;
	}

	/**
	 * 完成任务
	 * 
	 * @param flowInstId：流程实例ID
	 * @param variables：传入的参数
	 * @param assignee：当前登录用户编号(userName)
	 * @param comment：批注信息
	 * @throws Exception
	 */
	@Override
	public void completeTask(String flowInstId, Map<String, Object> variables, String assignee, String comment)
			throws Exception {
		Task task = taskService.createTaskQuery().processInstanceId(flowInstId).taskAssignee(assignee)// 保证并发流程可以得到唯一的Task
				.singleResult();
		if (comment != null) {
			// 添加批注时候的审批人，应该是完成任务的人（一定要写，不然查看的时候不知道人物信息）
			Authentication.setAuthenticatedUserId(assignee);
			// 添加批注信息
			taskService.addComment(task.getId(), flowInstId, comment);
		}
		// 将完成该任务的变量保存在该Task下
		taskService.setVariablesLocal(task.getId(), variables);
		// 完成任务
		taskService.complete(task.getId(), variables);
	}

	/**
	 * 查看流程图片
	 * 
	 * @param flowInstId：流程实例ID
	 * @return
	 * @throws Exception
	 */
	@Override
	public InputStream graphics(String flowInstId) throws Exception {
		Command<InputStream> cmd = null;
		InputStream inputStream = null;
		if (flowInstId != null) {
			cmd = new ProcessInstanceDiagramCmd(flowInstId);
		}
		if (cmd != null) {
			inputStream = managementService.executeCommand(cmd);
		}
		return inputStream;
	}

	/**
	 * 查询历史任务节点批注信息
	 * 
	 * @param flowInstId：流程实例ID
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<CommentBean> findComment(String flowInstId) throws Exception {
		List<Comment> comments = new ArrayList<Comment>();
		List<CommentBean> commentBeans = new ArrayList<CommentBean>();
		// 使用流程实例ID，查询历史任务，获取历史任务对应的每个任务ID
		List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(flowInstId).orderByHistoricTaskInstanceEndTime()// 以结束时间排序
				.desc()// 降序排序
				.finished()// 查询已完成的任务节点
				.list();
		if (historicTaskInstances != null && !historicTaskInstances.isEmpty()) {
			for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
				// 历史任务ID
				String htaskId = historicTaskInstance.getId();
				List<Comment> comments2 = taskService.getTaskComments(htaskId);
				comments.addAll(comments2);
			}
		}
		if (comments != null && !comments.isEmpty()) {
			for (Comment comment : comments) {
				CommentBean commentBean = new CommentBean();
				TSUser tsUser = this.findUniqueByProperty(TSUser.class, "userName", comment.getUserId());
				String isPass = (String) this.findHistoryVarInst(comment.getProcessInstanceId(), comment.getTaskId());
				commentBean.setRealName(tsUser.getRealName());
				commentBean.setFullMessage(comment.getFullMessage());
				commentBean.setIsAgree(isPass);
				commentBeans.add(commentBean);
			}
		}
		return commentBeans;
	}

	/**
	 * 生成享宇相关流程的申请编号
	 * 
	 * @param applyType：申请类型
	 * @param tableName：申请类型对应的表
	 * @return 申请编号
	 * @throws Exception
	 */
	@Override
	public String getNextApplyNo(ApplyTypeEnum applyType, String tableName) throws Exception {
		SimpleDateFormat time = new SimpleDateFormat("yyMMdd");
		Map<String, Object> maxApplyNo = findOneForJdbc("select max(apply_no) as applyNo from " + tableName
				+ " where apply_no like '%" + time.format(new Date()) + "%'");
		if (maxApplyNo == null || maxApplyNo.get("applyNo") == null
				|| maxApplyNo.get("applyNo").toString().trim().length() == 0)
			return ApplyNoUtils.getApplyNo(applyType);
		else
			return ApplyNoUtils.getNextApplyNo(maxApplyNo.get("applyNo").toString());
	}

	public DataGrid getDataGridForHSql(final Class<?> className, final String hSql, final DataGrid dataGrid,
			final boolean isOffset) {
		return this.commonDao.getDataGridForHSql(className, hSql, dataGrid, isOffset);
	}

	@Override
	public <T> DataGrid getDataGridRunForHQL(final Class<T> className, final DataGrid dataGrid, final boolean isHr,
			final String paramHql, final String orgId, final int beginNum, final int pageSize, final Object... param) {
		String hql = null;
		if (isHr) {
			hql = "SELECT S FROM " + className.getSimpleName()
					+ " S, TaskInstanceEntity H where S.flowInstId = H.procInstId  "
					+ "AND H.name = '人事部'  AND H.assignee = ?";
		} else {
			hql = "SELECT S FROM " + className.getSimpleName()
					+ " S, TaskInstanceEntity H where S.flowInstId = H.procInstId  "
					+ "AND H.name <> '人事部'  AND H.assignee = ? AND H.assignee <> S.applySttaffId";
		}
		if (StringUtil.isNotEmpty(orgId)) {

			String deptStr = getSSDeptIdForString(orgId);
			if (StringUtil.isNotEmpty(deptStr))
				hql += " AND S.tsDept.id in( " + deptStr + ")";
		}
		if (StringUtil.isNotEmpty(paramHql)) {
			hql += " AND " + paramHql;
		}
		hql += " ORDER BY S.createTime DESC";
		return this.commonDao.getDataGridForHQL(hql, dataGrid, beginNum, pageSize, param);
	}

	@Override
	public <T> DataGrid getDataGridHisForHQL(final Class<T> className, final DataGrid dataGrid, final boolean isHr,
			final String paramHql, final String orgId, final int beginNum, final int pageSize, final Object... param) {
		String hql = null;
		if (isHr) {
			hql = "SELECT DISTINCT S FROM " + className.getSimpleName()
					+ " S, HisTaskInstanceEntity H where S.flowInstId = H.procInstId  "
					+ "AND H.name = '人事部'  AND H.assignee = ? AND H.endTime IS NOT NULL AND H.deleteReason <> 'deleted'";
		} else {
			hql = "SELECT DISTINCT S FROM " + className.getSimpleName()
					+ " S, HisTaskInstanceEntity H where S.flowInstId = H.procInstId  "
					+ "AND H.name <> '人事部'  AND H.assignee = ? AND H.assignee <> S.applySttaffId AND H.endTime IS NOT NULL AND H.deleteReason <> 'deleted'";
		}
		if (StringUtil.isNotEmpty(orgId)) {
			String deptStr = getSSDeptIdForString(orgId);
			if (StringUtil.isNotEmpty(deptStr))
				hql += " AND S.tsDept.id in( " + deptStr + ")";
		}
		if (StringUtil.isNotEmpty(paramHql)) {
			hql += " AND " + paramHql;
		}
		hql += " ORDER BY S.createTime DESC";
		return this.commonDao.getDataGridForHQL(hql, dataGrid, beginNum, pageSize, param);
	}

	@Override
	public <T> int getRunTotal(final Class<T> className, final boolean isHr, final String paramHql, final String orgId,
			final Object... param) {
		String hql = null;
		if (isHr) {
			hql = "SELECT COUNT(S) FROM " + className.getSimpleName()
					+ " S, TaskInstanceEntity H where S.flowInstId = H.procInstId  "
					+ "AND H.name = '人事部'  AND H.assignee = ?";
		} else {
			hql = "SELECT COUNT(S) FROM " + className.getSimpleName()
					+ " S, TaskInstanceEntity H where S.flowInstId = H.procInstId  "
					+ "AND H.name <> '人事部'  AND H.assignee = ? AND H.assignee <> S.applySttaffId";
		}
		if (StringUtil.isNotEmpty(orgId)) {
			String deptStr = getSSDeptIdForString(orgId);
			if (StringUtil.isNotEmpty(deptStr))
				hql += " AND S.tsDept.id in( " + deptStr + ")";
		}
		if (StringUtil.isNotEmpty(paramHql)) {
			hql += " AND " + paramHql;
		}
		return this.commonDao.getTotalForHQL(hql, param);
	}

	@Override
	public <T> int getHisTotal(final Class<T> className, final boolean isHr, final String paramHql, final String orgId,
			final Object... param) {
		String hql = null;
		if (isHr) {
			hql = "SELECT COUNT(DISTINCT S) FROM " + className.getSimpleName()
					+ " S, HisTaskInstanceEntity H where S.flowInstId = H.procInstId  "
					+ "AND H.name = '人事部'  AND H.assignee = ? AND H.endTime IS NOT NULL AND H.deleteReason <> 'deleted'";
		} else {
			hql = "SELECT COUNT(DISTINCT S) FROM " + className.getSimpleName()
					+ " S, HisTaskInstanceEntity H where S.flowInstId = H.procInstId  "
					+ "AND H.name <> '人事部'  AND H.assignee = ? AND H.assignee <> S.applySttaffId AND H.endTime IS NOT NULL AND H.deleteReason <> 'deleted'";
		}
		if (StringUtil.isNotEmpty(orgId)) {
			String deptStr = getSSDeptIdForString(orgId);
			if (StringUtil.isNotEmpty(deptStr))
				hql += " AND S.tsDept.id in( " + deptStr + ")";
		}
		if (StringUtil.isNotEmpty(paramHql)) {
			hql += " AND " + paramHql;
		}
		return this.commonDao.getTotalForHQL(hql, param);
	}

	@Override
	public List<String> getSSDeptId(String deptId) {
		TSDepart dePart = this.commonDao.getEntity(TSDepart.class, deptId);
		List<TSDepart> dePartList = null;
		List<String> deptList = new ArrayList<String>();
		deptList.add(dePart.getId());

		StringBuffer hql = new StringBuffer(" from TSDepart t where 1=1 ");
		hql.append(" and t.orgCode like '" + dePart.getOrgCode() + "%'  order by t.orgCode ");

		dePartList = this.commonDao.findHql(hql.toString());

		for (TSDepart subDept : dePartList) {
			deptList.add(subDept.getId());
		}
		return deptList;
	}

	@Override
	public String getSSDeptIdForString(String orgId) {
		if (StringUtil.isEmpty(orgId))
			return null;
		List<String> deptIdList = getSSDeptId(orgId);
		String deptIdStr = "";
		for (String deptId : deptIdList) {
			deptIdStr += "'" + deptId + "',";
		}
		if (StringUtil.isEmpty(deptIdStr))
			return null;
		return deptIdStr.substring(0, deptIdStr.length() - 1);
	}

	@Override
	public List<TSDepart> getAll() {
		List<TSDepart> tSDeparts = new ArrayList<TSDepart>();
		// 当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		// 查询当前用户的角色编码
		String rHql = "select ru.TSRole.roleCode from TSRoleUser ru where ru.TSUser = ?";
		List<String> roleCodes = this.commonDao.findHql(rHql, tsUser);
		boolean isQueryAll = false;
		for (String newRoleCode : roleCodes) {
			if (Constants.HR.equals(newRoleCode) || Constants.HRDM.equals(newRoleCode)
					|| Constants.VICE.equals(newRoleCode) || Constants.CEO.equals(newRoleCode)
					|| Constants.ADMIN.equals(newRoleCode)) {
				isQueryAll = true;
			}
		}

		StringBuffer hql = new StringBuffer(" from TSDepart t where 1=1 ");
		if (isQueryAll) {
			hql.append(" and t.orgType = ? order by t.orgCode ");
			tSDeparts = this.commonDao.findHql(hql.toString(), "2");
		} else {

			TSDepart dePart = this.commonDao.getEntity(TSDepart.class, tsUser.getCurrentDepart().getId());
			hql.append(" and t.orgCode like '" + dePart.getOrgCode() + "%'  order by t.orgCode ");
			tSDeparts = this.commonDao.findHql(hql.toString());
			// tSDeparts.add(0, dePart);
		}

		return tSDeparts;
	}

	@Override
	public String getTSDepartAllStr() {
		List<TSDepart> listDepart = getAll();
		String replacedepart = "";
		for (int i = 0; i < listDepart.size(); i++) {
			if (listDepart.get(i).getDepartname() == null || "".equals(listDepart.get(i).getDepartname()))
				replacedepart += "无" + "_" + listDepart.get(i).getId() + ",";
			else {
				if (listDepart.get(i).getOrgCode().length() > 9) {
					replacedepart += listDepart.get(i).getTSPDepart().getDepartname() + "/"
							+ listDepart.get(i).getDepartname() + "_" + listDepart.get(i).getId() + ",";
				} else {
					replacedepart += listDepart.get(i).getDepartname() + "_" + listDepart.get(i).getId() + ",";

				}
			}
		}
		return replacedepart.substring(0, replacedepart.length() - 1);
	}

	public String getDeptAllStr() {
		StringBuffer hql = new StringBuffer(" from TSDepart t where 1=1 ");
		hql.append(" and t.orgType = ? order by t.orgCode ");
		List<TSDepart> listDepart = this.commonDao.findHql(hql.toString(), "2");

		String replacedepart = "";
		for (int i = 0; i < listDepart.size(); i++) {
			if (listDepart.get(i).getDepartname() == null || "".equals(listDepart.get(i).getDepartname()))
				replacedepart += "无" + "_" + listDepart.get(i).getId() + ",";
			else {
				if (listDepart.get(i).getOrgCode().length() > 9) {
					replacedepart += listDepart.get(i).getTSPDepart().getDepartname() + "/"
							+ listDepart.get(i).getDepartname() + "_" + listDepart.get(i).getId() + ",";
				} else {
					replacedepart += listDepart.get(i).getDepartname() + "_" + listDepart.get(i).getId() + ",";

				}
			}
		}
		return replacedepart.substring(0, replacedepart.length() - 1);
	}

}
