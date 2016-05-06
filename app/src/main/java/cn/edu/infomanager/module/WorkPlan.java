package cn.edu.infomanager.module;

public class WorkPlan {

	private int id;
	private String summary;
	private String remark;
	private String time;
	private String date;
	private int user_id;
	
	public WorkPlan(int id, String summary, String remark, String time,
			String date, int user_id) {
		super();
		this.id = id;
		this.summary = summary;
		this.remark = remark;
		this.time = time;
		this.date = date;
		this.user_id = user_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
}
