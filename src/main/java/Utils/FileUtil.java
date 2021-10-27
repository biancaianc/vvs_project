package Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FileUtil {
    public static ArrayList<File> listFileTree(File dir) {
        ArrayList<File> fileTree = new ArrayList<>();
        if(dir==null||dir.listFiles()==null){
            return fileTree;
        }
        for (File entry : dir.listFiles()) {
            if (entry.isFile()) fileTree.add(entry);
            else fileTree.addAll(listFileTree(entry));
        }
        return fileTree;
    }
}
