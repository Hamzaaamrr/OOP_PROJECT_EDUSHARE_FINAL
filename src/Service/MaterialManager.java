package Service;

import Model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MaterialManager {

    public MaterialManager() {
    }

    public ArrayList<Material> loadAllMaterials() {
        return FileHandling.read("materials");
    }

    public ArrayList<Material> loadMaterialsForCourse(String courseCode) {
        ArrayList<Material> result = new ArrayList<>();
        for (Material m : loadAllMaterials()) {
            if (m.getCourseCode() != null && m.getCourseCode().equalsIgnoreCase(courseCode)) {
                result.add(m);
            }
        }
        return result;
    }

    public boolean saveMaterial(Material material) {
        ArrayList<Material> mats = FileHandling.read("materials");
        if (mats == null) mats = new ArrayList<>();
        mats.add(material);
        return FileHandling.write("materials", mats);
    }

    public void sortByTimestamp(ArrayList<Material> list, boolean ascending) {
        if (list == null) return;
        Comparator<Material> cmp = new Comparator<Material>() { //override compare method to include pinned logic and sort by timestamp
            @Override
            public int compare(Material a, Material b) { //-1 if a<b, 1 if a>b, 0 if equal 
                // pinned items come first
                if (a.isPinned() && !b.isPinned()) return -1;
                if (!a.isPinned() && b.isPinned()) return 1;
                // both pinned or both unpinned: compare timestamps
                int cmpTs = Long.compare(a.getTimestamp(), b.getTimestamp());
                return ascending ? cmpTs : -cmpTs;
            }
        };
        Collections.sort(list, cmp); //sorts list based on comparator logic
    }

    public void sortByScore(ArrayList<Material> list, boolean descending) { 
        if (list == null) return;
        Comparator<Material> cmp = new Comparator<Material>() {
            @Override
            public int compare(Material a, Material b) {
                // pinned items come first
                if (a.isPinned() && !b.isPinned()) return -1;
                if (!a.isPinned() && b.isPinned()) return 1;
                int cmpScore = Integer.compare(a.getScore(), b.getScore());
                return descending ? -cmpScore : cmpScore; 
            }
        };
        Collections.sort(list, cmp); 
    }

    public boolean deleteMaterial(Material toDelete) {
        ArrayList<Material> all = FileHandling.read("materials");
        boolean removed = false;
        for (int i = 0; i < all.size(); i++) {
            Material m = all.get(i);
            if (matchesMaterial(m, toDelete)) {
                all.remove(i);
                removed = true;
                break;
            }
        }
        if (removed) {
            return FileHandling.write("materials", all);
        }
        return false;
    }

    public boolean pinMaterial(Material toPin) {
        ArrayList<Material> all = FileHandling.read("materials");
        boolean changed = false;
        for (Material m : all) {
            if (matchesMaterial(m, toPin)) {
                // toggle pinned state
                m.setPinned(!m.isPinned());
                changed = true;
                break;
            }
        }
        if (changed) {
            return FileHandling.write("materials", all);
        }
        return false;
    }

    public boolean voteMaterial(Material toVote, int voteValue, String voterEmail) {
        if (toVote == null || voterEmail == null || voterEmail.isEmpty()) return false;
        ArrayList<Vote> votes = FileHandling.read("votes");
        if (votes == null) votes = new ArrayList<>();

        boolean found = false; //if the voter has already voted on this material
        int oldValue = 0; //previous vote value by this voter on this material
        int foundIndex = -1; //index of existing vote in list
        for (int i = 0; i < votes.size(); i++) {
            Vote v = votes.get(i);
            if (v.getCourseCode() != null && v.getCourseCode().equalsIgnoreCase(toVote.getCourseCode())
                    && v.getMaterialTimestamp() == toVote.getTimestamp()
                    && v.getVoterEmail() != null && v.getVoterEmail().equalsIgnoreCase(voterEmail)) { //ensures only one vote per user per material
                found = true;
                oldValue = v.getValue();
                foundIndex = i;
                break;
            }
        }

        boolean ok = false; 
        int delta = 0; //how much to adjust material score by
        if (found) {
            if (oldValue == voteValue) {
                // toggle off (unvote): remove this vote
                votes.remove(foundIndex);
                delta = -oldValue; // remove previous contribution, vote change by oldValue (+1 or -1)
            } else {
                // change vote from upvote to downvote or vice versa
                votes.get(foundIndex).setValue(voteValue);
                delta = voteValue - oldValue; // adjust by difference (+2 or -2)
            }
            ok = FileHandling.write("votes", votes);
        } else {
            // new vote
            Vote nv = new Vote(toVote.getCourseCode(), toVote.getTimestamp(), voterEmail, voteValue);
            votes.add(nv);
            ok = FileHandling.write("votes", votes);
            if (ok) delta = voteValue;
        }

        if (!ok) return false;

        // adjust material score based on delta
        if (delta == 0) return true;
        ArrayList<Material> all = FileHandling.read("materials");
        boolean changed = false;
        for (Material m : all) {
            if (matchesMaterial(m, toVote)) {
                m.setScore(m.getScore() + delta);
                changed = true;
                break;
            }
        }
        if (changed) return FileHandling.write("materials", all);
        return true;
    }

    private boolean matchesMaterial(Material a, Material b) { // checks if two materials are the same
        if (a == null || b == null) return false;
        // match by timestamp if present, otherwise by uploader+title+body
        if (a.getTimestamp() != 0 && b.getTimestamp() != 0) {
            return a.getTimestamp() == b.getTimestamp() && safeEquals(a.getUploaderEmail(), b.getUploaderEmail());
        }
        return safeEquals(a.getUploaderEmail(), b.getUploaderEmail())
                && safeEquals(a.getTitle(), b.getTitle())
                && safeEquals(a.getBody(), b.getBody())
                && safeEquals(a.getCourseCode(), b.getCourseCode());
    }

    private boolean safeEquals(String s1, String s2) { //null-safe string equality check, FIXES SOME ERRORS THAT HAPPENED
        if (s1 == null && s2 == null) return true;
        if (s1 == null || s2 == null) return false;
        return s1.equals(s2);
    }
}
