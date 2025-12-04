package Model;

import java.io.Serializable;

public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;
    private String courseCode;
    private long materialTimestamp;
    private String commenterEmail;
    private String body;
    private long timestamp;

    public Comment(String courseCode, long materialTimestamp, String commenterEmail, String body, long timestamp) {
        this.courseCode = courseCode;
        this.materialTimestamp = materialTimestamp;
        this.commenterEmail = commenterEmail;
        this.body = body;
        this.timestamp = timestamp;
    }

    public String getCourseCode() { return courseCode; }
    public long getMaterialTimestamp() { return materialTimestamp; }
    public String getCommenterEmail() { return commenterEmail; }
    public String getBody() { return body; }
    public long getTimestamp() { return timestamp; }

    
}

