package org.eshishkin.edu.cometwatcher.service;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.AllArgsConstructor;
import org.eshishkin.edu.cometwatcher.model.Comet;
import org.eshishkin.edu.cometwatcher.repository.CometExternalRepository;

@ApplicationScoped
@AllArgsConstructor
public class CometService {

    private final CometExternalRepository cometExternalRepository;

    public List<Comet> getComets() {
        return cometExternalRepository.getComets();
    }
}
