package org.eshishkin.edu.cometwatcher.web.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class UnhandledExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException exception) {
        log.error("Unhandled exception occurred", exception);

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ErrorResponse.of(exception.getMessage()))
                .build();
    }
}
