package org.eshishkin.edu.cometwatcher.web.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private String error;

    public static ErrorResponse of(String error) {
        ErrorResponse response = new ErrorResponse();
        response.setError(error);
        return response;
    }
}
