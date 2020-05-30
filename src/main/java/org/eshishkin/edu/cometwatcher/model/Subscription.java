package org.eshishkin.edu.cometwatcher.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Subscription {

    @Email
    @NotEmpty
    private String email;
}
