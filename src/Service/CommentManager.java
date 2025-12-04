package Service;

import Model.Comment;
import java.util.ArrayList;

public class CommentManager {

    public CommentManager() {}

    public boolean saveComment(Comment c) {
        ArrayList<Comment> all = FileHandling.read("comments");
        if (all == null) all = new ArrayList<>();
        all.add(c);
        return FileHandling.write("comments", all);
    }

    public ArrayList<Comment> loadCommentsForMaterial(String courseCode, long materialTs) {
        ArrayList<Comment> all = FileHandling.read("comments");
        ArrayList<Comment> out = new ArrayList<>();
        if (all == null) return out;
        for (Comment c : all) {
            if (c.getCourseCode() != null && c.getCourseCode().equalsIgnoreCase(courseCode) && c.getMaterialTimestamp() == materialTs) out.add(c);
        }
        return out;
    }
}
