package Service;

import Model.Material;

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
        Comparator<Material> cmp = new Comparator<Material>() {
            @Override
            public int compare(Material a, Material b) {
                // pinned items come first
                if (a.isPinned() && !b.isPinned()) return -1;
                if (!a.isPinned() && b.isPinned()) return 1;
                // both pinned or both unpinned: compare timestamps
                int cmpTs = Long.compare(a.getTimestamp(), b.getTimestamp());
                return ascending ? cmpTs : -cmpTs;
            }
        };
        Collections.sort(list, cmp);
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
        ArrayList<Model.Vote> votes = FileHandling.read("votes");
        if (votes == null) votes = new ArrayList<>();
        boolean found = false;
        int oldValue = 0;
        int foundIndex = -1;
        for (int i = 0; i < votes.size(); i++) {
            Model.Vote v = votes.get(i);
            if (v.getCourseCode() != null && v.getCourseCode().equalsIgnoreCase(toVote.getCourseCode())
                    && v.getMaterialTimestamp() == toVote.getTimestamp()
                    && v.getVoterEmail() != null && v.getVoterEmail().equalsIgnoreCase(voterEmail)) {
                found = true;
                oldValue = v.getValue();
                foundIndex = i;
                break;
            }
        }

        boolean ok = false;
        int delta = 0;
        if (found) {
            if (oldValue == voteValue) {
                // toggle off (unvote): remove this vote
                votes.remove(foundIndex);
                delta = -oldValue; // remove previous contribution
            } else {
                // change vote
                votes.get(foundIndex).setValue(voteValue);
                delta = voteValue - oldValue;
            }
            ok = FileHandling.write("votes", votes);
        } else {
            // new vote
            Model.Vote nv = new Model.Vote(toVote.getCourseCode(), toVote.getTimestamp(), voterEmail, voteValue);
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

    private boolean matchesMaterial(Material a, Material b) {
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

    private boolean safeEquals(String s1, String s2) {
        if (s1 == null && s2 == null) return true;
        if (s1 == null || s2 == null) return false;
        return s1.equals(s2);
    }
}
