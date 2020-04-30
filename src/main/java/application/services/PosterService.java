package application.services;


import application.entities.Poster;
import application.repositories.PosterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;


// служба отвечающая за работу с файлом постера

@Service
public class PosterService{

    @Autowired private FileService fileService;

    @Autowired
    private PosterRepository posterRepository;

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


    // получение файла постера
    public Resource load(long id){
        try{
            Optional<Poster> p = posterRepository.findById(id);
            if (p.isPresent()){
                Poster poster = p.get();
                return fileService.loadFile(postersPath, poster.getName());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public Poster getPoster(long id){
        Optional<Poster> p = posterRepository.findById(id);
        if (p.isPresent()){
            Poster poster = p.get();
            return poster;
        }
        return null;
    }
}
