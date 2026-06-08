package me.azuredev.smartreports.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public boolean hasCooldown(UUID uuid) {
        return cooldowns.containsKey(uuid)
                && cooldowns.get(uuid) > System.currentTimeMillis();
    }

    public long getRemaining(UUID uuid) {
        return (cooldowns.get(uuid) - System.currentTimeMillis()) / 1000;
    }

    public void setCooldown(UUID uuid, long seconds) {
        cooldowns.put(
                uuid,
                System.currentTimeMillis() + (seconds * 1000)
        );
    }
}