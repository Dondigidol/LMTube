package application.services;


import application.entities.Poster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


// служба отвечающая за работу с файлом постера

@Service
public class PosterService{

    @Autowired private FileService fileService;

    @Value("${storage.posters.path}")
    private String postersPath;

    // загрузка и сохранение файла постера
    public Poster upload(MultipartFile posterFile){
        try{
            String posterFileName = fileService.saveFile(postersPath, posterFile.getInputStream());

            Poster poster = new Poster();
            poster.setName(posterFileName);
            poster.setMimeType(posterFile.getContentType());
            poster.setContentLength(posterFile.getSize());

            return poster;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
