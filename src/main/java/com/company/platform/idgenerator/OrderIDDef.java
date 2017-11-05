package com.company.platform.idgenerator;

import java.io.Serializable;

public class OrderIDDef implements Serializable,Cloneable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8322680777193563541L;

	
	
	private String defId;
	
	/**
	 * 分配一个数据库ID，默认长度为
	 */
	private String dbId;
	
	private int dbIdLength;
	/**
	 * 分区的标识在订单ID中占据的长度
	 */
	private int partitionIdLength= 3;
	
	
	/**
	 * 分区起始ID，最小为100
	 */
	private int partitionStartId= 100;
	
	/**
	 * 分区的个数
	 */
	private int partitionNum = 100;
	
	
	public String getDefId() {
		return defId;
	}


	public void setDefId(String defId) {
		this.defId = defId;
	}


	public String getDbId() {
		return dbId;
	}


	public void setDbId(String dbId) {
		this.dbId = dbId;
	}


	public int getPartitionIdLength() {
		return partitionIdLength;
	}


	public void setPartitionIdLength(int partitionIdLength) {
		this.partitionIdLength = partitionIdLength;
	}


	public int getPartitionStartId() {
		return partitionStartId;
	}


	public void setPartitionStartId(int partitionStartId) {
		this.partitionStartId = partitionStartId;
	}


	public int getPartitionNum() {
		return partitionNum;
	}


	public void setPartitionNum(int partitionNum) {
		this.partitionNum = partitionNum;
	}


	public int getDbIdLength() {
		return dbIdLength;
	}


	public void setDbIdLength(int dbIdLength) {
		this.dbIdLength = dbIdLength;
	}


	public OrderIDDef clone() {
		// TODO Auto-generated method stub
		try {
			return (OrderIDDef)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
}
