package src;

import java.io.File;

public class PathFinder {
    public static String findFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return path; 
        }

        file = new File("../" + path);
        if (file.exists())  {
            return "../" + path;
        }

        return "File wasn't found!"; // вот это конечно опасный трюк 
    }
}