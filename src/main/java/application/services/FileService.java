package application.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    public String saveFile(String path, InputStream stream) throws IOException {
        String fileName = createUniqueFileName();
        String remotePath = path + fileName;

        InputStream is = new BufferedInputStream(stream);
        FileOutputStream fileOutputStream = new FileOutputStream(remotePath);

        byte[] buffer = new byte[1024];
        int lengthRead;
        while ((lengthRead = is.read(buffer)) >0){
            fileOutputStream.write(buffer, 0, lengthRead);
            fileOutputStream.flush();
        }
        return fileName;
    }

    public Resource loadFile(String path, String name) throws IOException{
        Path remotePath = Paths.get(path + "\\" + name);
        Resource resource = new UrlResource(remotePath.toUri());
        return resource;
    }

    public void deleteFile(String path, String filename) throws IOException{
        Path filePath = Paths.get(path + filename);
        if (Files.exists(filePath)) Files.delete(filePath);
    }

    private String createUniqueFileName(){
        return UUID.randomUUID().toString();
    }
}
