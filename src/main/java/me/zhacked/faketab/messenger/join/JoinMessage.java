package me.zhacked.faketab.messenger.join;

import java.util.UUID;

public record JoinMessage(UUID uuid, String prefix, String suffix, String name) {
}
