package org.eshishkin.edu.cometwatcher.external.subscription.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriberSelector implements SearchRequest.Selector {
    private String email;

    public SubscriberSelector email(String email) {
        this.email = email;
        return this;
    }
}
