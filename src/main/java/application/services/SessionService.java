package application.services;

import application.repositories.VideoDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashSet;


@Component
@SessionScope
public class SessionService {

    @Autowired
    private VideoDetailsService videoDetailsService;

    private HashSet<Long> viewsList = new HashSet<>();

    public SessionService(){

    }

    public void addToViews(long videoId){
        if (!isPresent(videoId)){
            this.viewsList.add(videoId);
            videoDetailsService.incrementViews(videoId);
        }

    }

    private boolean isPresent(long videoId){
        if (viewsList.contains(videoId)) {
            return true;
        } else {
            return false;
        }

    }


}
