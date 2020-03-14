package application.config;

import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.content.fs.io.FileSystemResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@EnableFilesystemStores
public class StorageConfig {
    File filesSystemRoot(){
        File root = new File("./videos/");
        return root;
    }

    @Bean
    public FileSystemResourceLoader fsResourceLoader() throws Exception{
        return new FileSystemResourceLoader(filesSystemRoot().getAbsolutePath());
    }

}
