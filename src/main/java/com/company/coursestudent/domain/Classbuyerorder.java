package com.company.coursestudent.domain;
import java.io.Serializable;

/**
 * Model class of ClassBuyerOrder.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class Classbuyerorder implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** 课程编号. */
	private String courseid;

	/** 课时ID，标识一个视频的唯一编号. */
	private String classid;

	/** 课时标题. */
	private String classtitle;

	/** 实际成交价格. */
	private float realprice;

	/** 发布的原价. */
	private float originalprice;

	/** 视频播放id. */
	private String vodeoid;

	/** 视频播放地址. */
	private String voidurl;

	/** 教师名称. */
	private String teachername;

	/**
	 * Constructor.
	 */
	public Classbuyerorder() {
	}

	/**
	 * Set the 课程编号.
	 * 
	 * @param courseid
	 *            课程编号
	 */
	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}

	/**
	 * Get the 课程编号.
	 * 
	 * @return 课程编号
	 */
	public String getCourseid() {
		return this.courseid;
	}

	/**
	 * Set the 课时ID，标识一个视频的唯一编号.
	 * 
	 * @param classid
	 *            课时ID，标识一个视频的唯一编号
	 */
	public void setClassid(String classid) {
		this.classid = classid;
	}

	/**
	 * Get the 课时ID，标识一个视频的唯一编号.
	 * 
	 * @return 课时ID，标识一个视频的唯一编号
	 */
	public String getClassid() {
		return this.classid;
	}

	/**
	 * Set the 课时标题.
	 * 
	 * @param classtitle
	 *            课时标题
	 */
	public void setClasstitle(String classtitle) {
		this.classtitle = classtitle;
	}

	/**
	 * Get the 课时标题.
	 * 
	 * @return 课时标题
	 */
	public String getClasstitle() {
		return this.classtitle;
	}

	/**
	 * Set the 实际成交价格.
	 * 
	 * @param realprice
	 *            实际成交价格
	 */
	public void setRealprice(float realprice) {
		this.realprice = realprice;
	}

	/**
	 * Get the 实际成交价格.
	 * 
	 * @return 实际成交价格
	 */
	public float getRealprice() {
		return this.realprice;
	}

	/**
	 * Set the 发布的原价.
	 * 
	 * @param originalprice
	 *            发布的原价
	 */
	public void setOriginalprice(float originalprice) {
		this.originalprice = originalprice;
	}

	/**
	 * Get the 发布的原价.
	 * 
	 * @return 发布的原价
	 */
	public float getOriginalprice() {
		return this.originalprice;
	}

	/**
	 * Set the 视频播放id.
	 * 
	 * @param vodeoid
	 *            视频播放id
	 */
	public void setVodeoid(String vodeoid) {
		this.vodeoid = vodeoid;
	}

	/**
	 * Get the 视频播放id.
	 * 
	 * @return 视频播放id
	 */
	public String getVodeoid() {
		return this.vodeoid;
	}

	/**
	 * Set the 视频播放地址.
	 * 
	 * @param voidurl
	 *            视频播放地址
	 */
	public void setVoidurl(String voidurl) {
		this.voidurl = voidurl;
	}

	/**
	 * Get the 视频播放地址.
	 * 
	 * @return 视频播放地址
	 */
	public String getVoidurl() {
		return this.voidurl;
	}

	/**
	 * Set the 教师名称.
	 * 
	 * @param teachername
	 *            教师名称
	 */
	public void setTeachername(String teachername) {
		this.teachername = teachername;
	}

	/**
	 * Get the 教师名称.
	 * 
	 * @return 教师名称
	 */
	public String getTeachername() {
		return this.teachername;
	}


}
