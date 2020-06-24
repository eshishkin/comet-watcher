package org.eshishkin.edu.cometwatcher;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
@SuppressWarnings("HideUtilityClassConstructor")
public class Application {

    public static void main(String... args) {
        Quarkus.run(args);
    }
}
