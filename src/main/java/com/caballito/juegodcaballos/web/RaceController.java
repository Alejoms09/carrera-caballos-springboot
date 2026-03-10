package com.caballito.juegodcaballos.web;

import com.caballito.juegodcaballos.model.Suit;
import com.caballito.juegodcaballos.persistence.entity.UserAccount;
import com.caballito.juegodcaballos.service.BettingService;
import com.caballito.juegodcaballos.service.BusinessException;
import com.caballito.juegodcaballos.service.PointStoreService;
import com.caballito.juegodcaballos.service.UserAccountService;
import com.caballito.juegodcaballos.service.dto.BetOutcome;
import com.caballito.juegodcaballos.service.dto.PointPurchaseOutcome;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class RaceController {

    private static final String SESSION_USER_ID = "userId";
    private static final String SESSION_LAST_BET = "lastBetOutcome";

    private final UserAccountService userAccountService;
    private final BettingService bettingService;
    private final PointStoreService pointStoreService;

    public RaceController(
            UserAccountService userAccountService,
            BettingService bettingService,
            PointStoreService pointStoreService
    ) {
        this.userAccountService = userAccountService;
        this.bettingService = bettingService;
        this.pointStoreService = pointStoreService;
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        session.removeAttribute(SESSION_LAST_BET);
        model.addAttribute("suits", Suit.values());
        model.addAttribute("groups", userAccountService.getGroupStatuses());
        model.addAttribute("packageSize", pointStoreService.getPackageSize());
        model.addAttribute("packagePriceCop", pointStoreService.getPackagePriceCop());

        Long userId = currentUserId(session);
        if (userId == null) {
            return "index";
        }

        Optional<UserAccount> maybeUser = userAccountService.findById(userId);
        if (maybeUser.isEmpty()) {
            session.invalidate();
            model.addAttribute("error", "Tu sesion expiro. Inicia sesion de nuevo.");
            return "index";
        }

        UserAccount user = maybeUser.get();
        model.addAttribute("user", user);
        model.addAttribute("bets", bettingService.recentBets(user.getId(), 10));
        model.addAttribute("purchases", pointStoreService.recentPurchases(user.getId(), 10));
        return "index";
    }

    @GetMapping("/replay")
    public String replay(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long userId = currentUserId(session);
        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "Inicia sesion para ver la simulacion.");
            return "redirect:/";
        }

        Optional<UserAccount> maybeUser = userAccountService.findById(userId);
        if (maybeUser.isEmpty()) {
            session.invalidate();
            redirectAttributes.addFlashAttribute("error", "Tu sesion expiro. Inicia sesion de nuevo.");
            return "redirect:/";
        }

        Object value = session.getAttribute(SESSION_LAST_BET);
        if (!(value instanceof BetOutcome betOutcome)) {
            redirectAttributes.addFlashAttribute("error", "No hay simulacion activa para mostrar.");
            return "redirect:/";
        }

        model.addAttribute("user", maybeUser.get());
        model.addAttribute("suits", Suit.values());
        model.addAttribute("betOutcome", betOutcome);
        return "replay";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        try {
            UserAccount user = userAccountService.register(username, password);
            session.setAttribute(SESSION_USER_ID, user.getId());
            redirectAttributes.addFlashAttribute("success", "Registro exitoso. Ya tienes 1000 puntos para jugar.");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        try {
            UserAccount user = userAccountService.login(username, password);
            session.setAttribute(SESSION_USER_ID, user.getId());
            redirectAttributes.addFlashAttribute("success", "Bienvenido, " + user.getUsername() + ".");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/play")
    public String play(
            @RequestParam Suit chosenSuit,
            @RequestParam long betPoints,
            @RequestParam(defaultValue = "7") int trackLength,
            @RequestParam(defaultValue = "true") boolean falteringEnabled,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = currentUserId(session);
        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "Inicia sesion para apostar.");
            return "redirect:/";
        }

        try {
            BetOutcome outcome = bettingService.play(userId, chosenSuit, betPoints, trackLength, falteringEnabled);
            session.setAttribute(SESSION_LAST_BET, outcome);
            return "redirect:/replay";
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/buy-points")
    public String buyPoints(
            @RequestParam(defaultValue = "1") int packagesCount,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = currentUserId(session);
        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "Inicia sesion para comprar puntos.");
            return "redirect:/";
        }

        try {
            PointPurchaseOutcome outcome = pointStoreService.buyPackages(userId, packagesCount);
            redirectAttributes.addFlashAttribute("purchaseOutcome", outcome);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Compra exitosa: +" + outcome.pointsAdded() + " puntos por $" + outcome.amountCop() + " COP."
            );
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/";
    }

    private Long currentUserId(HttpSession session) {
        Object value = session.getAttribute(SESSION_USER_ID);
        if (value instanceof Long userId) {
            return userId;
        }
        return null;
    }
}
