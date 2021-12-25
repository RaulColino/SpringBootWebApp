package com.iwcn.iwcn_p1.service;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component
@Profile("prod")
public class S3ImageService implements ImageService {

    @Override
    public ResponseEntity<Object> createResponseFromImage(String eventImagesFolder, long id) {
        // TODO Auto-generated method stub
        return null;
    }

}
