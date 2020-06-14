package org.eshishkin.edu.cometwatcher.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.eshishkin.edu.cometwatcher.model.Comet;
import org.eshishkin.edu.cometwatcher.model.CometStub;
import org.eshishkin.edu.cometwatcher.model.GeoRequest;
import org.eshishkin.edu.cometwatcher.repository.CometExternalRepository;

import io.quarkus.cache.CacheResult;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class CometService {

    private final CometExternalRepository cometExternalRepository;

    @CacheResult(cacheName = "comet_list")
    public List<CometStub> getComets(GeoRequest observer) {
        return cometExternalRepository.getComets(observer);
    }

    @CacheResult(cacheName = "comet_by_id")
    public Comet getComet(String cometId, GeoRequest observer) {
        return cometExternalRepository.getComets(cometId, observer);
    }
}
