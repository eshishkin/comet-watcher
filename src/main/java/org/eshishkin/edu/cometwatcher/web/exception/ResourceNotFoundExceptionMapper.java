package org.eshishkin.edu.cometwatcher.web.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.eshishkin.edu.cometwatcher.exception.ResourceNotFoundException;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {

    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(ErrorResponse.of(exception.getMessage()))
                .build();
    }
}
