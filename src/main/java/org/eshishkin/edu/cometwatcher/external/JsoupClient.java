package org.eshishkin.edu.cometwatcher.external;

import org.jsoup.nodes.Document;

public interface JsoupClient {

    Document get(String url);
}
