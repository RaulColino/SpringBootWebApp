package com.iwcn.iwcn_p1;

import com.iwcn.iwcn_p1.service.ImageService;
import com.iwcn.iwcn_p1.service.LocalImageService;
import com.iwcn.iwcn_p1.service.S3ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	@Autowired
	public ImageService imageService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

/* 	@Value("${spring.profiles.active}")
	private String activeProfile;

    @Bean
	public ImageService getImageService(){
		if (activeProfile=="dev"){
			return new LocalImageService();
		} else {
			return new S3ImageService();
		}
	}
 */
}
