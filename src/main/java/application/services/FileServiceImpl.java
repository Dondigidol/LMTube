package application.services;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Component
public class FileServiceImpl implements FileService {

    @Override
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

    @Override
    public InputStream loadFile(String path, String name) throws IOException{
        String remotePath = path + "\\" + name;
        InputStream stream = new FileInputStream(remotePath);
        return stream;
    }

    @Override
    public boolean deleteFile(String path, String name) {
        File file = new File(path + name);
        return file.delete();
    }
}
