package org.eshishkin.edu.cometwatcher.repository.heavensabove;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;

import lombok.AllArgsConstructor;

import static java.net.URLDecoder.decode;

@AllArgsConstructor
final class CometStubWrapper {
    private static final Pattern PATTERN_ID = Pattern.compile("^.*cid=([0-9A-Z%]*).*$");
    private final Element row;

    @SuppressWarnings("MagicNumber")
    public String getId() {
        String id = row.child(0).selectFirst("a").attr("href");
        Matcher matcher = PATTERN_ID.matcher(id);

        return matcher.matches()
                ? decode(matcher.group(1), StandardCharsets.UTF_8)
                : null;
    }

    public String getName() {
        return row.child(0).text();
    }

    public float getBrightness() {
        return Float.parseFloat(row.child(1).text());
    }
}
