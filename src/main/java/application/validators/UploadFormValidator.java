package application.validators;

import application.payload.VideoUploadRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UploadFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return VideoUploadRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        VideoUploadRequest videoUploadRequest = (VideoUploadRequest) object;

        if (videoUploadRequest.getTitle() == null || videoUploadRequest.getTitle().isEmpty()){
            errors.rejectValue("title", "title.isEmpty", "Заполните заголовок");
        }
        if (videoUploadRequest.getDescription() == null || videoUploadRequest.getDescription().isEmpty()){
            errors.rejectValue("description", "description.isEmpty", "Заполните описание");
        }
        if (videoUploadRequest.getPosterFile() != null && videoUploadRequest.getPosterFile().isEmpty()){
            errors.rejectValue("posterFile", "posterFile.isEmpty", "Выбирете постер");
        }
        if (videoUploadRequest.getVideoFile() != null && videoUploadRequest.getVideoFile().isEmpty()){
            errors.rejectValue("videoFile", "videoFile.isEmpty", "Выбирете видеоролик");
        }
    }
}
