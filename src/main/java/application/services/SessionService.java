package application.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;


@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionService {

    private HashSet<Long> viewsList = new HashSet<>();

    public SessionService(){

    }

    public void addToViews(long videoId){
        this.viewsList.add(videoId);
    }

    public boolean isPresent(long videoId){
        if (viewsList.contains(videoId)) return true;

        return false;
    }


}
