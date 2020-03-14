package application.services;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NameGeneratorService {

    public UUID getUniqueName(){
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    public String getExtension(String fileName){
        String[] strList = fileName.split("\\.");
        if (strList.length <= 1) return null;
        else return strList[strList.length-1];
    }

}
