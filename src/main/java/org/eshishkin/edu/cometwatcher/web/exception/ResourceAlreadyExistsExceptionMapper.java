package org.eshishkin.edu.cometwatcher.web.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.eshishkin.edu.cometwatcher.exception.ResourceAlreadyExistsException;

@Provider
public class ResourceAlreadyExistsExceptionMapper implements ExceptionMapper<ResourceAlreadyExistsException> {

    @Override
    public Response toResponse(ResourceAlreadyExistsException exception) {
        return Response
                .status(Response.Status.CONFLICT)
                .entity(ErrorResponse.of(exception.getMessage()))
                .build();
    }
}
