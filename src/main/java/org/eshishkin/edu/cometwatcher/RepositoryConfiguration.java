package org.eshishkin.edu.cometwatcher;

import io.quarkus.arc.DefaultBean;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import org.eshishkin.edu.cometwatcher.external.JsoupClient;
import org.eshishkin.edu.cometwatcher.external.JsoupClientImpl;
import org.eshishkin.edu.cometwatcher.repository.CometExternalRepository;
import org.eshishkin.edu.cometwatcher.repository.HeavensAboveCometRepository;

@Dependent
public class RepositoryConfiguration {

    @Produces
    @DefaultBean
    public CometExternalRepository cometExternalRepository(JsoupClient jsoupClient) {
        return new HeavensAboveCometRepository("https://www.heavens-above.com", jsoupClient);
    }

    @Produces
    public JsoupClient jsoupClient() {
        return new JsoupClientImpl();
    }
}
