(function () {
    "use strict";

    const SUITS = ["OROS", "COPAS", "BASTOS", "ESPADAS"];
    const numberFormatter = new Intl.NumberFormat("es-CO");
    const currencyFormatter = new Intl.NumberFormat("es-CO", {
        style: "currency",
        currency: "COP",
        maximumFractionDigits: 0
    });

    setupReveal();
    setupFlashAutoHide();
    setupRippleButtons();
    setupTiltPanels();
    setupBetCalculator();
    setupPurchaseCalculator();
    setupRacePlayback();

    function setupReveal() {
        const revealItems = document.querySelectorAll("[data-reveal]");
        revealItems.forEach((item) => {
            const delayStep = Number(item.getAttribute("data-reveal") || 0);
            item.style.setProperty("--delay", (delayStep * 90) + "ms");
        });
        requestAnimationFrame(() => document.body.classList.add("ready"));
    }

    function setupFlashAutoHide() {
        document.querySelectorAll(".flash").forEach((flash, index) => {
            window.setTimeout(() => flash.classList.add("hide"), 3600 + (index * 300));
        });
    }

    function setupRippleButtons() {
        document.querySelectorAll(".btn").forEach((button) => {
            button.addEventListener("click", (event) => {
                const ripple = document.createElement("span");
                ripple.className = "ripple";
                ripple.style.left = event.offsetX + "px";
                ripple.style.top = event.offsetY + "px";
                button.appendChild(ripple);
                window.setTimeout(() => ripple.remove(), 650);
            });
        });
    }

    function setupTiltPanels() {
        const panels = document.querySelectorAll(".tilt");
        panels.forEach((panel) => {
            panel.addEventListener("mousemove", (event) => {
                const rect = panel.getBoundingClientRect();
                const offsetX = (event.clientX - rect.left) / rect.width;
                const offsetY = (event.clientY - rect.top) / rect.height;
                const rotateY = (offsetX - 0.5) * 5;
                const rotateX = (0.5 - offsetY) * 5;
                panel.style.setProperty("--rx", rotateX.toFixed(2) + "deg");
                panel.style.setProperty("--ry", rotateY.toFixed(2) + "deg");
            });

            panel.addEventListener("mouseleave", () => {
                panel.style.setProperty("--rx", "0deg");
                panel.style.setProperty("--ry", "0deg");
            });
        });
    }

    function setupBetCalculator() {
        const betInput = document.querySelector("input[name='betPoints']");
        const payoutTarget = document.getElementById("potentialPayout");
        if (!betInput || !payoutTarget) {
            return;
        }

        const updatePayout = () => {
            const bet = toPositiveNumber(betInput.value);
            const payout = bet * 5;
            payoutTarget.textContent = numberFormatter.format(payout) + " puntos";
        };

        betInput.addEventListener("input", updatePayout);
        updatePayout();
    }

    function setupPurchaseCalculator() {
        const packageInput = document.querySelector("input[name='packagesCount']");
        const totalTarget = document.getElementById("purchaseTotal");
        if (!packageInput || !totalTarget) {
            return;
        }

        const packagePrice = toPositiveNumber(document.body.dataset.packagePrice || "10000");
        const updateTotal = () => {
            const packageCount = Math.max(1, toPositiveNumber(packageInput.value));
            const total = packageCount * packagePrice;
            totalTarget.textContent = currencyFormatter.format(total);
        };

        packageInput.addEventListener("input", updateTotal);
        updateTotal();
    }

    function setupRacePlayback() {
        const playback = document.getElementById("racePlayback");
        if (!playback) {
            return;
        }

        const logLines = Array.from(playback.querySelectorAll("[data-log-line]"))
            .map((line) => (line.textContent || "").trim())
            .filter((line) => line.length > 0);

        if (logLines.length === 0) {
            return;
        }

        const trackLength = Math.max(3, toPositiveNumber(playback.dataset.trackLength || "7"));
        const winnerSuit = (playback.dataset.winner || "").trim();
        const payoutPoints = toPositiveNumber(playback.dataset.payout || "0");
        const won = (playback.dataset.won || "false") === "true";

        const status = document.getElementById("playbackStatus");
        const progressiveLog = document.getElementById("progressiveLog");
        const finalOutcome = document.getElementById("finalOutcome");
        const finalOutcomeText = document.getElementById("finalOutcomeText");
        const playbackToggle = document.getElementById("playbackToggle");

        const state = {
            positions: {
                OROS: 0,
                COPAS: 0,
                BASTOS: 0,
                ESPADAS: 0
            },
            currentIndex: 0,
            paused: false
        };

        renderBoard(playback, state.positions, trackLength);
        updateStatus(status, 0, logLines.length);

        const intervalMs = 780;
        const timer = window.setInterval(() => {
            if (state.paused) {
                return;
            }

            if (state.currentIndex >= logLines.length) {
                finalizePlayback();
                return;
            }

            const line = logLines[state.currentIndex];
            state.currentIndex += 1;
            applyLineToPositions(line, state.positions);
            renderBoard(playback, state.positions, trackLength);
            appendLog(progressiveLog, line);
            updateStatus(status, state.currentIndex, logLines.length);

            if (state.currentIndex >= logLines.length) {
                finalizePlayback();
            }
        }, intervalMs);

        if (playbackToggle) {
            playbackToggle.addEventListener("click", () => {
                state.paused = !state.paused;
                playbackToggle.textContent = state.paused ? "Continuar" : "Pausar";
                if (status) {
                    status.textContent = state.paused
                            ? "Simulacion en pausa"
                            : "Simulacion reanudada";
                }
            });
        }

        function finalizePlayback() {
            window.clearInterval(timer);
            if (playbackToggle) {
                playbackToggle.classList.add("hidden");
            }
            if (status) {
                status.textContent = "Carrera finalizada";
            }
            if (finalOutcome && finalOutcomeText) {
                finalOutcome.classList.remove("hidden");
                if (won) {
                    finalOutcome.classList.add("win");
                    finalOutcomeText.textContent =
                            "Ganaste. Caballo vencedor: " + winnerSuit + ". Premio: " +
                            numberFormatter.format(payoutPoints) + " puntos.";
                } else {
                    finalOutcome.classList.add("lose");
                    finalOutcomeText.textContent =
                            "Perdiste esta ronda. Caballo vencedor: " + winnerSuit + ".";
                }
            }
        }
    }

    function applyLineToPositions(line, positions) {
        const advanceMatch = line.match(/avanza\s+([A-Z]+)/i);
        if (advanceMatch) {
            const suit = normalizeSuit(advanceMatch[1]);
            if (positions[suit] !== undefined) {
                positions[suit] += 1;
            }
        }

        const retreatMatches = line.matchAll(/retrocede\s+([A-Z]+)/gi);
        for (const match of retreatMatches) {
            const suit = normalizeSuit(match[1]);
            if (positions[suit] !== undefined) {
                positions[suit] = Math.max(0, positions[suit] - 1);
            }
        }
    }

    function renderBoard(playback, positions, trackLength) {
        SUITS.forEach((suit) => {
            const lane = playback.querySelector(".race-lane[data-suit='" + suit + "']");
            if (!lane) {
                return;
            }

            const currentPosition = positions[suit];
            const cells = lane.querySelectorAll(".lane-cell");
            cells.forEach((cell, index) => {
                const step = index + 1;
                cell.classList.toggle("filled", currentPosition >= step);
                cell.classList.toggle("runner", currentPosition === step);
            });

            const positionBadge = lane.querySelector(".lane-position");
            if (positionBadge) {
                positionBadge.textContent = String(currentPosition);
            }

            const progressBar = lane.querySelector(".lane-progress span");
            if (progressBar) {
                const progress = Math.min(100, (currentPosition / trackLength) * 100);
                progressBar.style.width = progress + "%";
            }
        });
    }

    function appendLog(target, line) {
        if (!target) {
            return;
        }
        const item = document.createElement("li");
        item.textContent = line;
        target.prepend(item);
    }

    function updateStatus(status, current, total) {
        if (!status) {
            return;
        }
        if (current === 0) {
            status.textContent = "Simulacion iniciada";
            return;
        }
        status.textContent = "Paso " + current + " de " + total;
    }

    function normalizeSuit(raw) {
        if (!raw) {
            return "";
        }
        return raw.toUpperCase().trim();
    }

    function toPositiveNumber(value) {
        const parsed = Number(value);
        if (!Number.isFinite(parsed) || parsed <= 0) {
            return 0;
        }
        return Math.floor(parsed);
    }
})();
