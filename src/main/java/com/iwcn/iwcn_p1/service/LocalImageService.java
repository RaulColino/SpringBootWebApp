package com.iwcn.iwcn_p1.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


//https://www.youtube.com/watch?v=vY7c7k8xmKE


@Service
@Profile("dev")
public class LocalImageService implements ImageService{

    private static final Path IMAGES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");


	private Path getFilePath(long imageId, Path folder) {
		return folder.resolve("image-" + imageId + ".jpg");
	}


    public void saveImage(String folderName, Long imageId, MultipartFile image) throws IOException {

        //create images folder
		Path folder = IMAGES_FOLDER.resolve(folderName);
		Files.createDirectories(folder);
		
        //save image as "image-id.jpg"
		Path newFile = getFilePath(imageId, folder);
        image.transferTo(newFile);
	}


    public ResponseEntity<Object> createResponseFromImage(String eventImagesFolder, long imageId) throws MalformedURLException {
        
		Path folder = IMAGES_FOLDER.resolve(eventImagesFolder);
		Path imagePath = getFilePath(imageId, folder);
		Resource imageFile = new UrlResource(imagePath.toUri());

		if(!Files.exists(imagePath)) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(imageFile);
		}	
    }
}
