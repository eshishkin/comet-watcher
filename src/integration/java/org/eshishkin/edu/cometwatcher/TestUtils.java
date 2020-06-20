package org.eshishkin.edu.cometwatcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import lombok.experimental.UtilityClass;

import static org.apache.commons.lang.StringUtils.EMPTY;


@UtilityClass
public final class TestUtils {

    public static String evaluate(String template) {
        return evaluate(template, Map.of());
    }

    public static String evaluate(String template, Map<String, Object> vars) {
        VelocityEngine engine = new VelocityEngine();

        engine.init();

        VelocityContext context = new VelocityContext();
        vars.forEach(context::put);

        StringWriter writer = new StringWriter();

        engine.evaluate(context, writer, EMPTY, template);

        return writer.toString();
    }

    public static String loadResource(String name, Class<?> c) {
        try (InputStream resource = c.getResourceAsStream(name)) {
            return new String(resource.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load resource " + name, e);
        }
    }
}
