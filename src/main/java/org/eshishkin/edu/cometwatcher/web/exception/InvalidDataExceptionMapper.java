package org.eshishkin.edu.cometwatcher.web.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.eshishkin.edu.cometwatcher.exception.InvalidDataException;

@Provider
public class InvalidDataExceptionMapper implements ExceptionMapper<InvalidDataException> {

    @Override
    public Response toResponse(InvalidDataException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(ErrorResponse.of(exception.getMessage()))
                .build();
    }
}
