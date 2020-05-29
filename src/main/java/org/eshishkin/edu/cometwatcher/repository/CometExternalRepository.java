package org.eshishkin.edu.cometwatcher.repository;

import java.util.List;
import org.eshishkin.edu.cometwatcher.model.Comet;

public interface CometExternalRepository {

    List<Comet> getComets();
}
