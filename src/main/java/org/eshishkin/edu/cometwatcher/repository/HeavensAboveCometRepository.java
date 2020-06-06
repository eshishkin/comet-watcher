package org.eshishkin.edu.cometwatcher.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang3.StringUtils;
import org.eshishkin.edu.cometwatcher.external.HeavensAboveExternalService;
import org.eshishkin.edu.cometwatcher.model.Comet;
import org.eshishkin.edu.cometwatcher.model.GeoRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.toList;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class HeavensAboveCometRepository implements CometExternalRepository {

    private final HeavensAboveExternalService heavensAboveExternalService;

    @Override
    public List<Comet> getComets() {
        return getComets(GeoRequest.asNullIsland());
    }

    @Override
    public List<Comet> getComets(GeoRequest observer) {
        return getListOfComets(observer)
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

    private List<CometRow> getListOfComets(GeoRequest observer) {
        String html = heavensAboveExternalService.getComets(
                observer.getLatitude(), observer.getLongitude(),
                observer.getAltitude(), "GMT"
        );

        Elements rows = Jsoup.parse(html).select("table.standardTable > tbody > tr");
        return rows.stream().map(CometRow::new).collect(toList());
    }


    @AllArgsConstructor
    private static final class CometRow {
        private static final Pattern PATTERN_AZIMUTH = Pattern.compile("(\\d{1,3}).*\\((\\S{1,3})\\)");
        private static final Pattern PATTERN_ALTITUDE = Pattern.compile("(-?\\d{1,2}\\.\\d).*");
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MMM-dd", Locale.US);
        private final Element row;

        public String getName() {
            return row.child(0).text();
        }

        public float getBrightness() {
            return Float.parseFloat(row.child(1).text());
        }

        @SuppressWarnings("MagicNumber")
        public LocalDate getObserved() {
            String date = row.child(2).text();
            return LocalDate.parse(date, FORMATTER);
        }

        @SuppressWarnings("MagicNumber")
        public Float getAltitude() {
            String azimuth = row.child(4).text();
            Matcher matcher = PATTERN_ALTITUDE.matcher(azimuth);

            return matcher.matches() ? Float.parseFloat(matcher.group(1)) : null;
        }

        @SuppressWarnings("MagicNumber")
        public Long getAzimuthDegrees() {
            String azimuth = row.child(5).text();
            Matcher matcher = PATTERN_AZIMUTH.matcher(azimuth);

            return matcher.matches() ? Long.valueOf(matcher.group(1)) : null;
        }

        @SuppressWarnings("MagicNumber")
        public String getAzimuthDirection() {
            String azimuth = row.child(5).text();
            Matcher matcher = PATTERN_AZIMUTH.matcher(azimuth);

            return matcher.matches() ? matcher.group(2) : null;
        }

        @SuppressWarnings("MagicNumber")
        public String getConstellation() {
            return row.child(6).text();
        }
    }
}
