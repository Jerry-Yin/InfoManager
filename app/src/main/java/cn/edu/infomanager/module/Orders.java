package cn.edu.infomanager.module;

public class Orders {

	private int id;
	private String date;
	private String money;	//金额
	private String in_out;	//收入or支出
	private String use;		//
	private String remark;
	private int user_id;
	
	public Orders(int id, String date, String money, String in_out, String use,
			String remark, int user_id) {
		super();
		this.id = id;
		this.date = date;
		this.money = money;
		this.in_out = in_out;
		this.use = use;
		this.remark = remark;
		this.user_id = user_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getIn_out() {
		return in_out;
	}
	public void setIn_out(String in_out) {
		this.in_out = in_out;
	}
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
}
