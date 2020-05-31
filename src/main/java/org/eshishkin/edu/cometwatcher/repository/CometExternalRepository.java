package org.eshishkin.edu.cometwatcher.repository;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import org.eshishkin.edu.cometwatcher.model.Comet;

public interface CometExternalRepository {

    List<Comet> getComets();

    List<Comet> getComets(ObserverData observer);

    @Data
    class ObserverData {
        private int altitude;
        // todo make it big decimal and non nullable
        private String latitude;
        private String longitude;
    }
}
