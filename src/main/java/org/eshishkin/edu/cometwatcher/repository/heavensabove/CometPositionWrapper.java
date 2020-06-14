package org.eshishkin.edu.cometwatcher.repository.heavensabove;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.select.Elements;

import lombok.AllArgsConstructor;

@AllArgsConstructor
final class CometPositionWrapper {
    private static final Pattern PATTERN_AZIMUTH = Pattern.compile("(\\d{1,3}).*\\((\\S{1,3})\\)");
    private static final Pattern PATTERN_ALTITUDE = Pattern.compile("(-?\\d{1,2}\\.\\d).*");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MMM-dd", Locale.US);

    private final Elements row;

    @SuppressWarnings("MagicNumber")
    public String getRigthAccession() {
        return row.get(0).text();
    }

    @SuppressWarnings("MagicNumber")
    public String getDeclination() {
        return row.get(1).text();
    }

    @SuppressWarnings("MagicNumber")
    public String getConstellation() {
        return row.get(2).text();
    }

    @SuppressWarnings("MagicNumber")
    public String getDistanceFromEarth() {
        return row.get(3).text();
    }

    @SuppressWarnings("MagicNumber")
    public float getBrightness() {
        return Float.parseFloat(row.get(4).text());
    }

    @SuppressWarnings("MagicNumber")
    public LocalDate getObserved() {
        String date = row.get(5).text();
        return LocalDate.parse(date, FORMATTER);
    }

    @SuppressWarnings("MagicNumber")
    public Float getAltitude() {
        String azimuth = row.get(6).text();
        Matcher matcher = PATTERN_ALTITUDE.matcher(azimuth);

        return matcher.matches() ? Float.parseFloat(matcher.group(1)) : null;
    }

    @SuppressWarnings("MagicNumber")
    public Long getAzimuthDegrees() {
        String azimuth = row.get(7).text();
        Matcher matcher = PATTERN_AZIMUTH.matcher(azimuth);

        return matcher.matches() ? Long.valueOf(matcher.group(1)) : null;
    }

    @SuppressWarnings("MagicNumber")
    public String getAzimuthDirection() {
        String azimuth = row.get(7).text();
        Matcher matcher = PATTERN_AZIMUTH.matcher(azimuth);

        return matcher.matches() ? matcher.group(2) : null;
    }
}
