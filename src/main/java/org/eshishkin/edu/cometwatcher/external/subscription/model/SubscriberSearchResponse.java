package org.eshishkin.edu.cometwatcher.external.subscription.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class SubscriberSearchResponse {
    private List<SubscriptionRecord> docs;
}
