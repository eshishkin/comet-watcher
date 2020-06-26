package org.eshishkin.edu.cometwatcher.repository.heavensabove;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.select.Elements;

import lombok.AllArgsConstructor;

@AllArgsConstructor
final class CometOrbitWrapper {
    private static final Pattern PERIHELION_PATTERN = Pattern.compile("(.*) AU \\((.*)\\)");

    private final Elements row;

    @SuppressWarnings("MagicNumber")
    public String getDistanceFromSun() {
        return row.get(0).text();
    }

    @SuppressWarnings("MagicNumber")
    public String getPerihelionDistance() {
        String value = row.get(1).text();
        Matcher matcher = PERIHELION_PATTERN.matcher(value);
        return matcher.matches() ? matcher.group(1) : null;
    }

    @SuppressWarnings("MagicNumber")
    public String getPerihelionDate() {
        String value = row.get(1).text();
        Matcher matcher = PERIHELION_PATTERN.matcher(value);
        return matcher.matches() ? matcher.group(2) : null;
    }

    @SuppressWarnings("MagicNumber")
    public String getAphelion() {
        return row.get(2).text();
    }

    @SuppressWarnings("MagicNumber")
    public String getPeriod() {
        return row.get(3).text();
    }

    @SuppressWarnings("MagicNumber")
    public String getEccentricity()  {
        return row.get(4).text();
    }
}
