package com.sportevents.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Controller {


    @GetMapping
    public ResponseEntity<String> exampleGet() {
        return ResponseEntity.ok().body("OK");
    }


}
