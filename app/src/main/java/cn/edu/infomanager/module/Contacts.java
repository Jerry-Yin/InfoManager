package cn.edu.infomanager.module;

public class Contacts {

	private int id;
	private int userid;
	private String name;
	private String sex;
	private String tel;
	private String email;
	private String birthday;
	private String nation;
	private String bloodtype;
	private String address;
	private String nickname;
	
	public Contacts(int id, int userid, String name, String sex, String tel,
			String email, String birthday, String nation, String bloodtype,
			String address, String nickname) {
		super();
		this.id = id;
		this.userid = userid;
		this.name = name;
		this.sex = sex;
		this.tel = tel;
		this.email = email;
		this.birthday = birthday;
		this.nation = nation;
		this.bloodtype = bloodtype;
		this.address = address;
		this.nickname = nickname;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getBloodtype() {
		return bloodtype;
	}
	public void setBloodtype(String bloodtype) {
		this.bloodtype = bloodtype;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
