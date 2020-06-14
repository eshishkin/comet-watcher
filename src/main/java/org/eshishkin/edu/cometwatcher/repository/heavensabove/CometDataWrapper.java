package org.eshishkin.edu.cometwatcher.repository.heavensabove;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Delegate;

@Getter
@AllArgsConstructor
final class CometDataWrapper {

    private final String name;

    @Delegate
    private final CometPositionWrapper position;

    @Delegate
    private final CometOrbitWrapper orbit;
}
