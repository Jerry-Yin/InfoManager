package cn.edu.infomanager.module;

import android.os.Environment;

import java.io.File;
import java.io.Serializable;

public class User implements Serializable {
	
	public static final String ID = "id";
	public static final String NAME = "username";
	public static final String PASSWORD = "password";
	public static final String EMAIL = "email";
	public static final String AGE = "age";
	public static final String SEX = "sex";
	public static final String POTO = "poto";
	public static final File PHOTO_DIR = 	//用户头像的默认存储路径
			new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
	
	private int id;				//ID
	private String username;	//用户名
	private String password;	//密码
	private String email;		//邮箱
	private int age;			//年龄
	private String sex;			//性别
	private byte[] poto;		//头像(保存本地的路径/非数据库)
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public byte[] getPoto() {
		return poto;
	}
	public void setPoto(byte[] poto) {
		this.poto = poto;
	}
	public User(String username, String password, String email, int age, String sex, byte[] poto) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.age = age;
		this.sex = sex;
		this.poto = poto;
	}
	public User(int id, String username, String password, String email, int age, String sex, byte[] poto) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.age = age;
		this.sex = sex;
		this.poto = poto;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		if(email==null)
			email = "";
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		if(username==null)
			username="";
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		if(password==null)
			password="";
		this.password = password;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		if(sex==null)
			sex="女";
		this.sex = sex;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password="
				+ password + ", email="+email+", age=" + age + 
				", sex=" + sex +", poto=" + poto + "]";
	}
	
}
