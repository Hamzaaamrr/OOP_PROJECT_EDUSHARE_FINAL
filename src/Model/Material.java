package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Material implements Serializable {

	private static final long serialVersionUID = 1L;

	private String courseCode;
	private String uploaderEmail;
	private String title;
	private String body;
	private long timestamp;
	private boolean pinned;
	private int score;
	private ArrayList<String> filePaths;

	public Material(String courseCode, String uploaderEmail, String title, String body, long timestamp) {
		this.courseCode = courseCode;
		this.uploaderEmail = uploaderEmail;
		this.title = title;
		this.body = body;
		this.timestamp = timestamp;
		this.pinned = false;
		this.score = 0;
		this.filePaths = new ArrayList<>();
	}


	public ArrayList<String> getFilePaths() {
		return filePaths;
	}

	public void setFilePaths(ArrayList<String> filePaths) {
		this.filePaths = filePaths == null ? new ArrayList<>() : filePaths;
	}
	

	public String getCourseCode() {
		return courseCode;
	}

	public String getUploaderEmail() {
		return uploaderEmail;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public boolean isPinned() {
		return pinned;
	}

	public void setPinned(boolean pinned) {
		this.pinned = pinned;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	
}
