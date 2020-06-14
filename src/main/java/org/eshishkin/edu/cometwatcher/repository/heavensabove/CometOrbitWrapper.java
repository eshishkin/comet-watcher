package org.eshishkin.edu.cometwatcher.repository.heavensabove;

import org.jsoup.select.Elements;

import lombok.AllArgsConstructor;

@AllArgsConstructor
final class CometOrbitWrapper {
    private final Elements row;

    @SuppressWarnings("MagicNumber")
    public String getDistanceFromSun() {
        return row.get(0).text();
    }

    @SuppressWarnings("MagicNumber")
    public String getPerihelion() {
        return row.get(1).text();
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
