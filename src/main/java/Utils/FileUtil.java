package Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileUtil {
    public static File findPathFile(String testSiteLocation, String inputLine) throws IOException {

        char path[] = new char[100];
        int start_value=4;
        inputLine.getChars(start_value,inputLine.length()-9,path,0);
        String endpoint=String.valueOf(path);
        String p=testSiteLocation;
        endpoint=endpoint.trim();
        if(endpoint.length()==1){
            endpoint+="a.html";
        }
        p=p.concat(endpoint);
        p=p.trim();
        p=p.replaceAll("%20"," ");
        p=p.trim();

        File fi=new File(p);
        if(p.isEmpty()) System.out.println("emptyyyyy");
        System.out.println(endpoint);

        String finalEndpoint = endpoint.replace("/","").replaceAll("%20"," ");
        if(!fi.exists()){

            List<Path> mylist=Files.walk(Paths.get("src/main/resources/TestSite"))
                    .filter(Files::isRegularFile)
                    .filter(path1 -> path1.endsWith(finalEndpoint))
                    .collect(Collectors.toList());
            if(mylist.size()==0){

                return new File("src/main/resources/TestSite/error.html");

            }
            else {
               return new File(String.valueOf(mylist.get(0)));

            }

        }
        else {
            return fi;
        }
    }

}
