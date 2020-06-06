package org.eshishkin.edu.cometwatcher.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.eshishkin.edu.cometwatcher.model.Comet;
import org.eshishkin.edu.cometwatcher.model.GeoRequest;
import org.eshishkin.edu.cometwatcher.repository.CometExternalRepository;

import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class CometService {

    private final CometExternalRepository cometExternalRepository;

    public List<Comet> getComets() {
        return cometExternalRepository.getComets();
    }

    public List<Comet> getComets(GeoRequest request) {
        return cometExternalRepository.getComets(request);
    }
}
