package com.xy.oa.activiti.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "act_hi_taskinst", schema = "")
@SuppressWarnings("serial")
public class HisTaskInstanceEntity implements java.io.Serializable{



		/**主键*/
		private java.lang.String id;
		private java.lang.String procInstId;
		private java.lang.String name;
		private java.lang.String assignee;
		private java.util.Date endTime;
		private java.lang.String deleteReason;
		
		@Id
		@Column(name ="ID_",nullable=false,length=64)
		public java.lang.String getId() {
			return id;
		}
		public void setId(java.lang.String id) {
			this.id = id;
		}
		@Column(name ="PROC_INST_ID_",nullable=true,length=64)
		public java.lang.String getProcInstId() {
			return procInstId;
		}
		public void setProcInstId(java.lang.String procInstId) {
			this.procInstId = procInstId;
		}
		@Column(name ="NAME_",nullable=true,length=255)
		public java.lang.String getName() {
			return name;
		}
		public void setName(java.lang.String name) {
			this.name = name;
		}
		
		@Column(name ="ASSIGNEE_",nullable=true,length=255)
		public java.lang.String getAssignee() {
			return assignee;
		}
		public void setAssignee(java.lang.String assignee) {
			this.assignee = assignee;
		}
		
		@Column(name = "END_TIME_", nullable = true)
		public java.util.Date getEndTime() {
			return endTime;
		}
		public void setEndTime(java.util.Date endTime) {
			this.endTime = endTime;
		}
		
		@Column(name = "DELETE_REASON_", nullable = true, length = 4000)
		public java.lang.String getDeleteReason() {
			return deleteReason;
		}
		public void setDeleteReason(java.lang.String deleteReason) {
			this.deleteReason = deleteReason;
		}
		
		
}

