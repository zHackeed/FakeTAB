package me.zhacked.faketab.player;

import java.util.UUID;

public record FakePlayer(UUID uuid, String prefix, String suffix, String name) {
}
