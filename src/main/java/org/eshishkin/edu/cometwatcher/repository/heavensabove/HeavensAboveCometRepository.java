package org.eshishkin.edu.cometwatcher.repository.heavensabove;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eshishkin.edu.cometwatcher.external.HeavensAboveExternalService;
import org.eshishkin.edu.cometwatcher.model.Comet;
import org.eshishkin.edu.cometwatcher.model.Comet.ImageLink;
import org.eshishkin.edu.cometwatcher.model.CometStub;
import org.eshishkin.edu.cometwatcher.model.GeoRequest;
import org.eshishkin.edu.cometwatcher.repository.CometExternalRepository;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import static java.util.stream.Collectors.toList;

@ApplicationScoped
public class HeavensAboveCometRepository implements CometExternalRepository {

    private static final String GMT = "GMT";
    private static final String SELECTOR_TABLE_SECOND_COLUMN = "tr > td:nth-child(2)";

    @ConfigProperty(name = "application.external.heavens-above.url")
    String url;

    @Inject
    HeavensAboveExternalService heavensAboveExternalService;

    @Override
    public List<CometStub> getComets(GeoRequest observer) {
        return getListOfComets(observer)
                .stream()
                .filter(row -> StringUtils.isNotBlank(row.getName()))
                .map(row -> {
                    CometStub stub = new CometStub();
                    stub.setId(row.getId());
                    stub.setName(row.getName());
                    stub.setBrightness(row.getBrightness());
                    return stub;
                })
                .collect(toList());
    }

    @Override
    public Comet getComets(String cometId, GeoRequest observer) {
        return convert(getCometData(cometId, observer));
    }

    private Comet convert(CometDataWrapper data) {
        Comet comet = new Comet();
        comet.setName(data.getName());
        comet.setBrightness(data.getBrightness());
        comet.setObserved(data.getObserved());
        comet.setAltitude(data.getAltitude());
        comet.setAzimuth(data.getAzimuthDegrees());
        comet.setDirection(data.getAzimuthDirection());
        comet.setConstellation(data.getConstellation());

        comet.setRightAccession(data.getRigthAccession());
        comet.setDeclination(data.getDeclination());
        comet.setDistanceFromEarth(data.getDistanceFromEarth());

        comet.setDistanceFromSun(data.getDistanceFromSun());
        comet.setAphelion(data.getAphelion());
        comet.setPerihelion(data.getPerihelion());
        comet.setPeriod(data.getPeriod());
        comet.setEccentricity(data.getEccentricity());


        comet.setLinks(Arrays.asList(
                ImageLink.of("Skychart", generateSkyChartLink(data)),
                ImageLink.of("90° above ecliptic", generateOrbitLink(data))
        ));
        return comet;
    }

    private List<CometStubWrapper> getListOfComets(GeoRequest observer) {
        String html = heavensAboveExternalService.getComets(
                observer.getLatitude(), observer.getLongitude(),
                observer.getAltitude(), GMT
        );

        Elements rows = Jsoup.parse(html).select("table.standardTable > tbody > tr");
        return rows.stream().map(CometStubWrapper::new).collect(toList());
    }

    private CometDataWrapper getCometData(String id, GeoRequest observer) {
        String html = heavensAboveExternalService.getComet(
                id,
                observer.getLatitude(), observer.getLongitude(),
                observer.getAltitude(), GMT
        );

        Elements tables = Jsoup.parse(html).select("table.standardTable > tbody");

        Elements position = tables.get(0).select(SELECTOR_TABLE_SECOND_COLUMN);
        Elements orbit = tables.get(1).select(SELECTOR_TABLE_SECOND_COLUMN);

        return new CometDataWrapper(
                id,
                new CometPositionWrapper(position),
                new CometOrbitWrapper(orbit)
        );
    }

    private String generateSkyChartLink(CometDataWrapper data) {
        String dec = StringUtils.substringBefore(data.getDeclination(), "°").trim();
        String ra = StringUtils.substringBefore(data.getRigthAccession(), "h").trim();

        return String.format(
                "%s/skychart.ashx?cometID=%s&RA=%s&DEC=%s&size=400&FOV=70&innerFOV=15&MaxMag=5&cn=1&cl=1",
                url, data.getName(), ra, dec
        );
    }

    private String generateOrbitLink(CometDataWrapper data) {
        return String.format(
                "%s/CometOrbitPic.ashx?cid=%s&eclLat=90&eclLong=-90&sz=400",
                url, data.getName()
        );
    }
}
