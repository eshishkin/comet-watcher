package org.eshishkin.edu.cometwatcher.repository;

import java.util.List;

import org.eshishkin.edu.cometwatcher.model.Comet;
import org.eshishkin.edu.cometwatcher.model.GeoRequest;

public interface CometExternalRepository {

    List<Comet> getComets();

    List<Comet> getComets(GeoRequest observer);
}
