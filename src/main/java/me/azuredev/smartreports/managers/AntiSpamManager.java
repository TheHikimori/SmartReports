package me.azuredev.smartreports.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AntiSpamManager {

    private final Map<UUID, Long> cooldowns =
            new HashMap<>();

    public boolean isOnCooldown(
            UUID player,
            long cooldownMillis
    ) {

        Long last =
                cooldowns.get(player);

        if (last == null)
            return false;

        return System.currentTimeMillis()
                - last
                < cooldownMillis;
    }

    public long getRemaining(
            UUID player,
            long cooldownMillis
    ) {

        Long last =
                cooldowns.get(player);

        if (last == null)
            return 0;

        long passed =
                System.currentTimeMillis()
                        - last;

        return Math.max(
                0,
                cooldownMillis - passed
        );
    }

    public void update(
            UUID player
    ) {
        cooldowns.put(
                player,
                System.currentTimeMillis()
        );
    }
}