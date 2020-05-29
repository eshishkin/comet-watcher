package org.eshishkin.edu.cometwatcher.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.eshishkin.edu.cometwatcher.model.Comet;
import org.eshishkin.edu.cometwatcher.external.JsoupClient;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static java.util.stream.Collectors.toList;

public class HeavensAboveCometRepository implements CometExternalRepository {
    private static final String ENDPOINT_COMETS = "/Comets.aspx";

    private final String baseUrl;
    private final JsoupClient jsoup;

    public HeavensAboveCometRepository(String baseUrl, JsoupClient jsoup) {
        this.baseUrl = baseUrl;
        this.jsoup = jsoup;
    }

    public List<Comet> getComets() {
        return getListOfComets()
                .stream()
                .filter(row -> StringUtils.isNotBlank(row.getName()))
                .map(row -> {
                    Comet comet = new Comet();
                    comet.setName(row.getName());
                    comet.setBrightness(row.getBrightness());
                    comet.setObserved(row.getObserved());
                    comet.setAltitude(row.getAltitude());
                    comet.setAzimuth(row.getAzimuthDegrees());
                    comet.setDirection(row.getAzimuthDirection());
                    comet.setConstellation(row.getConstellation());
                    return comet;
                })
                .collect(toList());

    }

    private List<CometRow> getListOfComets() {
        String url = baseUrl + ENDPOINT_COMETS;
        Elements rows = jsoup.get(url).select("table.standardTable > tbody > tr");
        return rows.stream().map(CometRow::new).collect(toList());
    }


    @AllArgsConstructor
    private static final class CometRow {
        private static final Pattern PATTERN_AZIMUTH = Pattern.compile("(\\d{1,3}).*\\((\\S{1,3})\\)");
        private static final Pattern PATTERN_ALTITUDE = Pattern.compile("(-?\\d{1,2}\\.\\d).*");
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
        private final Element row;

        public String getName() {
            return row.child(0).text();
        }

        public float getBrightness() {
            return Float.parseFloat(row.child(1).text());
        }

        public LocalDate getObserved() {
            String date = row.child(2).text();
            return LocalDate.parse(date, FORMATTER);
        }

        public Float getAltitude() {
            String azimuth = row.child(4).text();
            Matcher matcher = PATTERN_ALTITUDE.matcher(azimuth);

            return matcher.matches() ? Float.parseFloat(matcher.group(1)) : null;
        }

        public Long getAzimuthDegrees() {
            String azimuth = row.child(5).text();
            Matcher matcher = PATTERN_AZIMUTH.matcher(azimuth);

            return matcher.matches() ? Long.valueOf(matcher.group(1)) : null;
        }

        public String getAzimuthDirection() {
            String azimuth = row.child(5).text();
            Matcher matcher = PATTERN_AZIMUTH.matcher(azimuth);

            return matcher.matches() ? matcher.group(2) : null;
        }

        public String getConstellation() {
            return row.child(6).text();
        }
    }
}
