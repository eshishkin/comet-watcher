package org.eshishkin.edu.cometwatcher.external;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupClientImpl implements JsoupClient {

    @Override
    public Document get(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException ex) {
            throw new JsoupException("Unable to retrieve data from URL: " + url, ex);
        }
    }
}
