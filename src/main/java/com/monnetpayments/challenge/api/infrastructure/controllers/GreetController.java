/* MONNET PAYMENTS: (C)2023 */
package com.monnetpayments.challenge.api.infrastructure.controllers;

import com.monnetpayments.challenge.api.application.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "")
public class GreetController {

    @Autowired
    private Service service;

    @GetMapping(path = "greet")

    public ResponseEntity<String> doAGreet() {
        return ResponseEntity.ok(service.doAGreet());
    }

}
