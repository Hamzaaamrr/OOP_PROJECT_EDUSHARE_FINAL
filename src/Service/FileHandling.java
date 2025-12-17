package Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class FileHandling {

    private static final String DATA_DIR = "Data";

    // Map simple keys to .ser filenames
    private static final java.util.Map<String, String> KEY_MAP = new HashMap<>();
    static {
        KEY_MAP.put("users", DATA_DIR + File.separator + "users.ser");
        KEY_MAP.put("courses", DATA_DIR + File.separator + "courses.ser");
        KEY_MAP.put("materials", DATA_DIR + File.separator + "materials.ser");
        KEY_MAP.put("comments", DATA_DIR + File.separator + "comments.ser");
        KEY_MAP.put("votes", DATA_DIR + File.separator + "votes.ser");
        
    }

    //single generic read that returns the deserialized object (or null if missing)
    @SuppressWarnings("unchecked")
    public static <T> T read(String key) {
        String path = KEY_MAP.get(key);
        if (path == null) return null;
        File f = new File(path);
        if (!f.exists()) {
            // Return empty collections for known list-like keys to avoid NullPointerExceptions 
            if ("users".equals(key) || "courses".equals(key) || "materials".equals(key)
                    || "comments".equals(key) || "votes".equals(key) || "enrollments".equals(key)) {
                return (T) new java.util.ArrayList<>();
            }
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) { //open file input stream and wrap it in object input stream to read objects
            Object obj = ois.readObject();
            return (T) obj;
        } catch (Exception e) {
            // Failed to read/deserialize
            e.printStackTrace();
            return null;
        }
    }

    //single write that serializes the provided object to disk (overwrites existing)
    public static boolean write(String key, Object obj) {
        String path = KEY_MAP.get(key);
        if (path == null) return false;
        try {
            File dataDir = new File(DATA_DIR);
            if (!dataDir.exists()) dataDir.mkdirs();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) { //open file output stream and wrap it in object output stream to write objects
                oos.writeObject(obj);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}