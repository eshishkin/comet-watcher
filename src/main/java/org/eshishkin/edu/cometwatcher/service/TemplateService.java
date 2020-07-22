package org.eshishkin.edu.cometwatcher.service;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eshishkin.edu.cometwatcher.model.Language;

import io.quarkus.qute.Engine;
import io.quarkus.qute.Template;

@ApplicationScoped
public class TemplateService {

    @Inject
    Engine templateEngine;

    public Template getTemplate(String name, Language language) {
        String key = String.format("%s_%s",
                name,
                Optional.ofNullable(language).orElse(Language.EN).name().toLowerCase()
        );

        return templateEngine.getTemplate(key);
    }
}
