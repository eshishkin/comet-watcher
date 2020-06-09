package org.eshishkin.edu.cometwatcher.repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.eshishkin.edu.cometwatcher.external.HeavensAboveExternalService;
import org.eshishkin.edu.cometwatcher.model.Comet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class HeavensAboveCometRepositoryTest {

    private static final double DELTA = 0.1;

    @Mock
    private HeavensAboveExternalService heavensAboveExternalService;

    private CometExternalRepository repository;

    @BeforeEach
    public void before() {
        repository = new HeavensAboveCometRepository(heavensAboveExternalService);
    }

    @Test
    public void testGetComets_Success() {
        doReturn(load("HeavensAboveCometRepositoryTest_CometList.html"))
                .when(heavensAboveExternalService).getComets("0", "0", 0, "GMT");

        doReturn(load("HeavensAboveCometRepositoryTest_CometPage.html"))
                .when(heavensAboveExternalService).getComet("C/2019 U6", "0", "0", 0, "GMT");

        doReturn(load("HeavensAboveCometRepositoryTest_CometPage.html"))
                .when(heavensAboveExternalService).getComet("C/2020 F3", "0", "0", 0, "GMT");

        doReturn(load("HeavensAboveCometRepositoryTest_CometPage.html"))
                .when(heavensAboveExternalService).getComet("C/2020 F8", "0", "0", 0, "GMT");

        List<Comet> comets = repository.getComets();

        assertEquals(3, comets.size());

        assertEquals("C/2019 U6", comets.get(0).getName());
        assertEquals(6.7, comets.get(0).getBrightness(), DELTA);
        assertEquals(LocalDate.of(2020, Month.MAY, 28), comets.get(0).getObserved());
        assertEquals(52, comets.get(0).getAltitude(), DELTA);
        assertEquals(124, comets.get(0).getAzimuth());
        assertEquals("ESE", comets.get(0).getDirection());
        assertEquals("Canis Major", comets.get(0).getConstellation());

        assertEquals("7h 54.6m", comets.get(0).getRigthAccession());
        assertEquals("-16° 37'", comets.get(0).getDeclination());
        assertEquals("0.958 AU", comets.get(0).getDistanceFromEarth());
        assertEquals("860.766 AU", comets.get(0).getAphelion());
        assertEquals("0.914 AU (2020-Jun-18)", comets.get(0).getPerihelion());
        assertEquals("0.929 AU", comets.get(0).getDistanceFromSun());
        assertEquals("0.997878", comets.get(0).getEccentricity());
        assertEquals("8,943 years", comets.get(0).getPeriod());


        assertEquals("C/2020 F3 NEOWISE", comets.get(1).getName());
        assertEquals(7.7, comets.get(1).getBrightness(), DELTA);
        assertEquals(LocalDate.of(2020, Month.MAY, 28), comets.get(1).getObserved());
        assertEquals(-68.7, comets.get(1).getAltitude(), DELTA);
        assertEquals(100, comets.get(1).getAzimuth());
        assertEquals("E", comets.get(1).getDirection());
        assertEquals("Orion", comets.get(1).getConstellation());

        assertEquals("C/2020 F8 SWAN", comets.get(2).getName());
        assertEquals(7.9, comets.get(2).getBrightness(), DELTA);
        assertEquals(LocalDate.of(2020, Month.MAY, 26), comets.get(2).getObserved());
        assertEquals(43.7, comets.get(2).getAltitude(), DELTA);
        assertEquals(2, comets.get(2).getAzimuth());
        assertEquals("N", comets.get(2).getDirection());
        assertEquals("Perseus", comets.get(2).getConstellation());
    }

    @Test
    public void testGetComets_EmptyTableWithComets() {
        doReturn(load("HeavensAboveCometRepositoryTest_EmptyTableWithComets.html"))
                .when(heavensAboveExternalService).getComets("0", "0", 0, "GMT");

        assertEquals(0, repository.getComets().size());
    }

    @Test
    public void testGetComets_EmptyCometName() {
        doReturn(load("HeavensAboveCometRepositoryTest_EmptyCometName.html"))
                .when(heavensAboveExternalService).getComets("0", "0", 0, "GMT");

        assertEquals(0, repository.getComets().size());
    }


    private String load(String name) {
        try (InputStream resource = HeavensAboveCometRepositoryTest.class.getResourceAsStream(name)) {
            return new String(resource.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load or parse document " + name, e);
        }
    }
}
