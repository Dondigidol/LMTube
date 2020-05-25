package application.validators;

import application.payload.VideoUploadRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return VideoUploadRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        VideoUploadRequest video = (VideoUploadRequest) object;
        String title = video.getTitle();
        String description = video.getDescription();
        MultipartFile videoFile = video.getVideoFile();
        MultipartFile posterFile = video.getPosterFile();

        if (title.isEmpty() || title == null){
            errors.rejectValue("title", "title.empty", "Заполните заголовок");
        }
        if (description.isEmpty() || description == null){
            errors.rejectValue("description", "description.empty", "Заполните описание");
        }
        if (videoFile == null || videoFile.isEmpty()){
            errors.rejectValue("videoFile", "videoFile.empty", "Выбирете видеофайл");
        }
        if (posterFile == null || posterFile.isEmpty()){
            errors.rejectValue("posterFile", "posterFile.empty", "Выбирете постер");
        }


    }
}
