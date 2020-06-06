package org.eshishkin.edu.cometwatcher.utils;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import lombok.AllArgsConstructor;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@AllArgsConstructor
public class LoggingInterceptor implements ClientRequestFilter, ClientResponseFilter {

    private static final String HEADER_PATTERN = "%s: %s";
    private static final String NEW_LINE = "\n";

    private final Logger log;

    @Override
    public void filter(ClientRequestContext context) throws IOException {
        if (!log.isDebugEnabled()) {
            return;
        }

        String method = context.getMethod();
        URI uri = context.getUri();

        String headers = context.getStringHeaders().entrySet().stream()
                .filter(e -> !HttpHeaders.AUTHORIZATION.equals(e.getKey()))
                .map(e -> format(HEADER_PATTERN, e.getKey(), e.getValue()))
                .collect(joining(NEW_LINE));

        log.debug(
                "Performing request\n{} {}\n{}\n",
                method, uri, headers
        );
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        if (!log.isDebugEnabled()) {
            return;
        }
        Response.StatusType status = responseContext.getStatusInfo();

        String headers = responseContext.getHeaders().entrySet().stream()
                .map(e -> format(HEADER_PATTERN, e.getKey(), e.getValue()))
                .collect(joining(NEW_LINE));

        log.debug(
                "Receiveing response\n{} - {}\n{}\n",
                status.getStatusCode(), status.getReasonPhrase(), headers
        );
    }
}
