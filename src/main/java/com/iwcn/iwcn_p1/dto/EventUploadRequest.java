package com.iwcn.iwcn_p1.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.lang.NonNullFields;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data // bundles @ToString, @EqualsAndHashCode, @Getter/@Setter and @RequiredArgsConstructor together
@AllArgsConstructor
//@Builder // lets you do something like: Person.builder().name("Adam").city("San Francisco").build();
public class EventUploadRequest {
    
    @NotNull
    private String name;

    @NotNull
    private String description;
    
    @NotNull
    private String datetime;
    
    @NotNull
    private Double price;

    @NotNull
    @Min(1)
    private Integer maxSeats;
    
    private Integer reservedSeats; // if null the Event entity uses the default value
    
    @NotNull
    private MultipartFile image;
}
