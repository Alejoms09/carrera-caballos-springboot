package com.caballito.juegodcaballos.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
public class OpsController {

    @Value("${RAILWAY_GIT_COMMIT_SHA:local}")
    private String railwayCommitSha;

    @Value("${spring.application.name:juegodcaballos}")
    private String appName;

    @GetMapping("/version")
    public String version() {
        return appName + " | commit=" + railwayCommitSha + " | time=" + OffsetDateTime.now();
    }
}
