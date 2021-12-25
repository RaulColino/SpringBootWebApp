package com.iwcn.iwcn_p1.service;

import java.net.MalformedURLException;

import org.springframework.http.ResponseEntity;


public interface ImageService {

    ResponseEntity<Object> createResponseFromImage(String eventImagesFolder, long id) throws MalformedURLException;
    
}
