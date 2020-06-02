package org.eshishkin.edu.cometwatcher.repository;

import org.eshishkin.edu.cometwatcher.model.Comet;
import org.eshishkin.edu.cometwatcher.model.GeoRequest;

import java.util.List;

public interface CometExternalRepository {

    List<Comet> getComets();

    List<Comet> getComets(GeoRequest observer);
}
