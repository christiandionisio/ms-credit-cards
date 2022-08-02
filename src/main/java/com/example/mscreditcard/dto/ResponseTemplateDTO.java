package com.example.mscreditcard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseTemplateDTO {

    private Object response;
    private String errorMessage;

}
