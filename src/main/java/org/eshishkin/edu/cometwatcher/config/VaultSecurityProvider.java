package org.eshishkin.edu.cometwatcher.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.http.auth.BasicUserPrincipal;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.quarkus.vault.VaultKVSecretEngine;
import io.quarkus.vault.runtime.client.VaultClientException;
import io.smallrye.mutiny.Uni;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@ApplicationScoped
public class VaultSecurityProvider implements IdentityProvider<UsernamePasswordAuthenticationRequest> {
    private static final String ROLE_PARAM = "role";
    private static final String PASSWORD_PARAM = "password";
    private static final String PASSWORD_NAME = "name";

    @Inject
    VaultKVSecretEngine secretEngine;

    @ConfigProperty(name = "application.vault.user-storage-path")
    String userStoragePath;

    @Override
    public Class<UsernamePasswordAuthenticationRequest> getRequestType() {
        return UsernamePasswordAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(UsernamePasswordAuthenticationRequest request,
                                              AuthenticationRequestContext context) {

        String username = request.getUsername();
        char[] password = request.getPassword().getPassword();

        Map<String, String> attributes = getUserAttributes(username);

        verifyPassword(password, attributes.getOrDefault(PASSWORD_PARAM, EMPTY).toCharArray());

        QuarkusSecurityIdentity identity = QuarkusSecurityIdentity.builder()
                .setPrincipal(new BasicUserPrincipal(attributes.get(PASSWORD_NAME)))
                .addRole(attributes.get(ROLE_PARAM))
                .addAttributes(Collections.emptyMap())
                .build();

        return Uni.createFrom().item(identity);
    }

    private Map<String, String> getUserAttributes(String username) {
        try {
            return secretEngine.readSecret(userStoragePath + "/" + username);
        } catch (VaultClientException ex) {
            throw new UnauthorizedException("User is not found", ex);
        }
    }

    private void verifyPassword(char[] expected, char[] passed) {
        if (!Arrays.equals(expected, passed)) {
            throw new ForbiddenException("Forbidden");
        }
    }
}
