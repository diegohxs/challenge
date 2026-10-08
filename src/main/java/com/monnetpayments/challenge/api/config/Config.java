package com.monnetpayments.challenge.api.config;

import com.monnetpayments.challenge.api.domain.EnglishQ;
import com.monnetpayments.challenge.api.domain.Q;
import com.monnetpayments.challenge.api.domain.SpainQ;
import com.monnetpayments.challenge.api.domain.UppercaseQ;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public Q spainQ() {
        return new SpainQ();
    }

    @Bean
    public Q englishQ() {
        return new EnglishQ();
    }

    @Bean
    public Q uppercaseQ(EnglishQ englishQ) {
        return new UppercaseQ(englishQ);
    }
}