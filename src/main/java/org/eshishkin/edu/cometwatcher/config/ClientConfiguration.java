package org.eshishkin.edu.cometwatcher.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eshishkin.edu.cometwatcher.external.HeavensAboveExternalService;
import org.eshishkin.edu.cometwatcher.utils.LoggingInterceptor;
import org.slf4j.LoggerFactory;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Dependent
public class ClientConfiguration {

    private static final int DEFAULT_CONNECTION_TIMEOUT = 30_000;
    private static final int DEFAULT_READ_TIMEOUT = 180_000;

    @Produces
    @ApplicationScoped
    public HeavensAboveExternalService heavensAboveExternalService(
            @ConfigProperty(name = "application.external.heavens-above.url") String url) {

        return getDefaultBuilder()
                .baseUrl(toURL(url))
                .register(new LoggingInterceptor(LoggerFactory.getLogger(HeavensAboveExternalService.class)))
                .build(HeavensAboveExternalService.class);
    }

    @Produces
    @ApplicationScoped
    public MongoClient mongoClient(
            @ConfigProperty(name = "application.datasource.url") String url,
            @ConfigProperty(name = "application.datasource.port") int port,
            @ConfigProperty(name = "application.datasource.database") String database,
            @ConfigProperty(name = "application.datasource.user") String user,
            @ConfigProperty(name = "application.datasource.password") String password) {

        CodecRegistry registry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
        return MongoClients.create(MongoClientSettings
                .builder()
                .applyConnectionString(new ConnectionString(String.format("mongodb://%s:%s", url, port)))
                .applyToSocketSettings(socket -> socket
                        .connectTimeout(DEFAULT_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                        .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                )
                .credential(MongoCredential.createCredential(user, database, password.toCharArray()))
                .codecRegistry(registry)
                .retryWrites(false)
                .build());
    }

    private URL toURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Unable to create URL", ex);
        }
    }

    private RestClientBuilder getDefaultBuilder() {
        return RestClientBuilder.newBuilder()
                .connectTimeout(DEFAULT_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS);
    }
}
