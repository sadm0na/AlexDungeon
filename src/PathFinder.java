package src;

import java.io.File;

/**
 * Finds files in different directory structures for flexible project setup.
 * 
 * @author Monika Khachatryan
 * @ID 2276380
 * @author Caroline Savchenko
 * @ID 2338793
 * 
 */
public class PathFinder {
    
    /**
     * Locates file by checking direct path and parent directory.
     */
    public static String findFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return path; 
        }

        file = new File("../" + path);
        if (file.exists())  {
            return "../" + path;
        }

        return "File wasn't found!"; 
    }
}