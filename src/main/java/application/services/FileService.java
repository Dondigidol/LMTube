package application.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

public interface FileService {

    default String createUniqueFileName(){
        return UUID.randomUUID().toString();
    }

    String saveFile(String path, InputStream is) throws IOException;

    InputStream loadFile(String path, String name) throws IOException;

    boolean deleteFile(String path, String name);
}
