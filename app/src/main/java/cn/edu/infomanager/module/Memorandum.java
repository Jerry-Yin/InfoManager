/**
 * 
 */
package cn.edu.infomanager.module;

/**
 * @author 
 * @date 2012-5-2
 * @time 下午01:57:47
 */
public class Memorandum{
	/*"id integer primary key autoincrement, " +
	"category integer not null, " +
	"summary text not null, " +
	"description text not null, " +
	"_date date not null"+
	"constraint user_id_FK foreign key(user_id) references user(id)" +*/

	
	
	private int id;		//
	private int real_id;
	private int category;
	private String summary;
	private String description;
	private String date;
	private int userid;
	private int isRemind;	//是否提醒  默认提醒 0/1


	public Memorandum(int category, String summary, String description,
					  String date, int userId) {
		super();
		this.category = category;
		this.summary = summary;
		this.description = description;
		this.date = date;
		this.userid = userId;
		this.isRemind = 1;	//默认提醒
	}

	public Memorandum(int id, int category, String summary, String description,
			String date, int userId) {
		super();
		this.id = id;
		this.category = category;
		this.summary = summary;
		this.description = description;
		this.date = date;
		this.userid = userId;
		this.isRemind = 1;	//默认提醒
	}

	public Memorandum(int id, int category, String summary, String description,
					  String date, int userId, int remind) {
		super();
		this.id = id;
		this.category = category;
		this.summary = summary;
		this.description = description;
		this.date = date;
		this.userid = userId;
		this.isRemind = remind;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int isRemind() {
		return isRemind;
	}

	public void setIsRemind(int isRemind) {
		this.isRemind = isRemind;
	}
}
