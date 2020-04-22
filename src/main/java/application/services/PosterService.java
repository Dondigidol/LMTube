package application.services;


import application.entities.Poster;
import application.repositories.PosterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Optional;


// служба отвечающая за работу с файлом постера

@Service
public class PosterService{

    @Autowired private FileService fileService;

    @Autowired private PosterRepository posterRepository;

    @Value("${storage.posters.path}")
    private String postersPath;

    // загрузка и сохранение файла постера
    public String upload(MultipartFile posterFile){
        try{
            String posterFileName = fileService.saveFile(postersPath, posterFile.getInputStream());

            Poster poster = new Poster();
            poster.setId(posterFileName);
            poster.setMimeType(posterFile.getContentType());
            poster.setContentLength(posterFile.getSize());

            posterRepository.save(poster);

            return posterFileName;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    // получение потока файла постера
    public HashMap<String, Object> load(String posterName){
        try{
            Optional<Poster> p = posterRepository.findById(posterName);
            if (p.isPresent()){
                HashMap<String, Object> result = new HashMap<>();
                Poster poster = p.get();
                Resource resource = fileService.loadFile(postersPath, poster.getId());
                result.put("resource", resource);
                result.put("mimeType", poster.getMimeType());
                result.put("contentLength", String.valueOf(poster.getContentLength()));
                return result;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    // удаление файла постера
    public boolean delete(String posterName){
        try {
            Optional<Poster> p = posterRepository.findById(posterName);
            if (p.isPresent()){
                fileService.deleteFile(postersPath, posterName);

                Poster poster = p.get();
                posterRepository.delete(poster);
                return true;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }


}
