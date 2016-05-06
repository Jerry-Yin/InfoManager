package cn.edu.infomanager.module;

public class Blog {

	private int id;
	private String summary;
	private String world;
	private String date;
	private String audio;
	private String video;
	private int user_id;
	private String poto;
	
	public Blog(int id, String world,String summary, String audio, String video, int user_id,
			String poto, String date) {
		super();
		this.id = id;
		this.summary = summary;
		this.world = world;
		this.audio = audio;
		this.video = video;
		this.user_id = user_id;
		this.poto = poto;
		this.date = date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWorld() {
		return world;
	}
	public void setWorld(String world) {
		this.world = world;
	}
	public String getAudio() {
		return audio;
	}
	public void setAudio(String audio) {
		this.audio = audio;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getPoto() {
		return poto;
	}
	public void setPoto(String poto) {
		this.poto = poto;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
