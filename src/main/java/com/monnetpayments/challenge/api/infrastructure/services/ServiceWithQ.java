package com.monnetpayments.challenge.api.infrastructure.services;

import com.monnetpayments.challenge.api.application.services.Service;
import com.monnetpayments.challenge.api.domain.Q;
import org.springframework.beans.factory.annotation.Qualifier;

@org.springframework.stereotype.Service
public class ServiceWithQ implements Service {

    private final Q q;

    public ServiceWithQ(@Qualifier("spainQ") Q q) {
        this.q = q;
    }

    @Override
    public String doAGreet() {
        return q.greet();
    }
}