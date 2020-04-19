package application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VideoIdException extends RuntimeException{

    public VideoIdException(String message) {
        super(message);
    }
}
