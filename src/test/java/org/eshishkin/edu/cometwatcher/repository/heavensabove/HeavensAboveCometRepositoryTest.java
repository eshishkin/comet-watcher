package org.eshishkin.edu.cometwatcher.repository.heavensabove;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;

import org.eshishkin.edu.cometwatcher.config.FeatureConfig;
import org.eshishkin.edu.cometwatcher.external.HeavensAboveExternalService;
import org.eshishkin.edu.cometwatcher.model.Comet;
import org.eshishkin.edu.cometwatcher.model.CometStub;
import org.eshishkin.edu.cometwatcher.model.GeoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class HeavensAboveCometRepositoryTest {

    private static final double DELTA = 0.1;

    @Mock
    private HeavensAboveExternalService heavensAboveExternalService;

    @Mock
    private Clock clock;

    @Mock
    private FeatureConfig features;

    @InjectMocks
    private HeavensAboveCometRepository repository;

    @Test
    public void testGetComets_Success() {
        doReturn(load("HeavensAboveCometRepositoryTest_CometList.html"))
                .when(heavensAboveExternalService).getComets("0", "0", 0, "GMT");

        List<CometStub> comets = repository.getComets(GeoRequest.asNullIsland());

        assertEquals(3, comets.size());

        assertEquals("C/2019 U6", comets.get(0).getName());
        assertEquals(6.7, comets.get(0).getBrightness(), DELTA);

        assertEquals("C/2020 F3 NEOWISE", comets.get(1).getName());
        assertEquals(7.7, comets.get(1).getBrightness(), DELTA);

        assertEquals("C/2020 F8 SWAN", comets.get(2).getName());
        assertEquals(7.9, comets.get(2).getBrightness(), DELTA);
    }

    @Test
    public void testGetComet() {
        doReturn(false).when(features).isUseMidnightTime();
        doReturn(Instant.parse("2020-01-01T00:00:00Z")).when(clock).instant();
        doReturn(ZoneId.of("UTC")).when(clock).getZone();

        doReturn(load("HeavensAboveCometRepositoryTest_CometPage.html"))
                .when(heavensAboveExternalService).getComet("C/2019 U6", "0", "0", 0, "GMT");

        Comet comet = repository.getComet("C/2019 U6", GeoRequest.asNullIsland());

        assertEquals("C/2019 U6", comet.getName());
        assertEquals(6.8, comet.getBrightness(), DELTA);
        assertEquals(LocalDate.of(2020, Month.JUNE, 9), comet.getObserved());
        assertEquals(56.4, comet.getAltitude(), DELTA);
        assertEquals(239, comet.getAzimuth());
        assertEquals("WSW", comet.getDirection());
        assertEquals("Puppis", comet.getConstellation());

        assertEquals("7h 54.6m", comet.getRightAccession());
        assertEquals("-16° 37'", comet.getDeclination());
        assertEquals("0.958 AU", comet.getDistanceFromEarth());
        assertEquals("860.766 AU", comet.getAphelion());
        assertEquals("0.914", comet.getPerihelionDistance());
        assertEquals("2020-Jun-18", comet.getPerihelionDate());
        assertEquals("0.929 AU", comet.getDistanceFromSun());
        assertEquals("0.997878", comet.getEccentricity());
        assertEquals("8,943 years", comet.getPeriod());
    }

    @Test
    public void testGetComets_EmptyTableWithComets() {
        doReturn(load("HeavensAboveCometRepositoryTest_EmptyTableWithComets.html"))
                .when(heavensAboveExternalService).getComets("0", "0", 0, "GMT");

        assertEquals(0, repository.getComets(GeoRequest.asNullIsland()).size());
    }

    @Test
    public void testGetComets_EmptyCometName() {
        doReturn(load("HeavensAboveCometRepositoryTest_EmptyCometName.html"))
                .when(heavensAboveExternalService).getComets("0", "0", 0, "GMT");

        assertEquals(0, repository.getComets(GeoRequest.asNullIsland()).size());
    }


    private String load(String name) {

        try (InputStream resource = HeavensAboveCometRepositoryTest.class.getResourceAsStream(name)) {
            return new String(resource.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load or parse document " + name, e);
        }
    }
}
