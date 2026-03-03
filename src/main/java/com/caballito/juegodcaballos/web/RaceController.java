package com.caballito.juegodcaballos.web;

import com.caballito.juegodcaballos.model.RaceConfig;
import com.caballito.juegodcaballos.model.RaceState;
import com.caballito.juegodcaballos.service.RaceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RaceController {

    private final RaceService raceService;

    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        RaceState race = (RaceState) session.getAttribute("race");
        if (race == null) {
            race = raceService.newRace(new RaceConfig(7, true));
            session.setAttribute("race", race);
            session.setAttribute("log", new ArrayList<String>());
        }

        model.addAttribute("race", race);
        model.addAttribute("log", session.getAttribute("log"));
        return "index";
    }

    @PostMapping("/new")
    public String newRace(@RequestParam(defaultValue = "7") int trackLength,
                          @RequestParam(defaultValue = "true") boolean falteringEnabled,
                          HttpSession session) {
        RaceState race = raceService.newRace(new RaceConfig(trackLength, falteringEnabled));
        session.setAttribute("race", race);
        session.setAttribute("log", new ArrayList<String>());
        return "redirect:/";
    }

    @PostMapping("/step")
    public String step(HttpSession session) {
        RaceState race = (RaceState) session.getAttribute("race");
        if (race == null) {
            return "redirect:/";
        }

        @SuppressWarnings("unchecked")
        List<String> log = (List<String>) session.getAttribute("log");
        if (log == null) {
            log = new ArrayList<>();
        }

        log.add(0, raceService.step(race));
        session.setAttribute("log", log);
        return "redirect:/";
    }

    @PostMapping("/auto")
    public String auto(HttpSession session) {
        RaceState race = (RaceState) session.getAttribute("race");
        if (race == null) {
            return "redirect:/";
        }

        @SuppressWarnings("unchecked")
        List<String> log = (List<String>) session.getAttribute("log");
        if (log == null) {
            log = new ArrayList<>();
        }

        int guard = 500;
        while (race.getWinner() == null && guard-- > 0) {
            log.add(0, raceService.step(race));
        }

        session.setAttribute("log", log);
        return "redirect:/";
    }
}
