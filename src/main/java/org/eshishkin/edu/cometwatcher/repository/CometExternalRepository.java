package org.eshishkin.edu.cometwatcher.repository;

import java.util.List;

import org.eshishkin.edu.cometwatcher.model.Comet;
import org.eshishkin.edu.cometwatcher.model.CometStub;
import org.eshishkin.edu.cometwatcher.model.GeoRequest;

public interface CometExternalRepository {

    List<CometStub> getComets(GeoRequest observer);

    Comet getComet(String cometId, GeoRequest observer);
}
