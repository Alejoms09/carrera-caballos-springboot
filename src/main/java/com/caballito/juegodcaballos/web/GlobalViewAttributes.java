package com.caballito.juegodcaballos.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalViewAttributes {

    private final String assetVersion;

    public GlobalViewAttributes(@Value("${RAILWAY_GIT_COMMIT_SHA:local}") String railwayCommitSha) {
        this.assetVersion = railwayCommitSha;
    }

    @ModelAttribute("assetVersion")
    public String assetVersion() {
        return assetVersion;
    }
}
