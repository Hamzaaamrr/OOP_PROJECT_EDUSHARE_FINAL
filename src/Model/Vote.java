package Model;

import java.io.Serializable;

public class Vote implements Serializable {

    private static final long serialVersionUID = 1L;
    private String courseCode;
    private long materialTimestamp;
    private String voterEmail;
    private int value; // 1 or -1

    public Vote(String courseCode, long materialTimestamp, String voterEmail, int value) {
        this.courseCode = courseCode;
        this.materialTimestamp = materialTimestamp;
        this.voterEmail = voterEmail;
        this.value = value;
    }

    public String getCourseCode() { return courseCode; }
    public long getMaterialTimestamp() { return materialTimestamp; }
    public String getVoterEmail() { return voterEmail; }
    public int getValue() { return value; }
    public void setValue(int v) { this.value = v; }

    
}
