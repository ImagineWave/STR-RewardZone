package ru.strid.strreward.enums;

import net.md_5.bungee.api.ChatColor;

public enum MessageType {
    INFO(ChatColor.AQUA),
    GOOD(ChatColor.GREEN),
    BAD(ChatColor.RED);

    private ChatColor color;

    MessageType (ChatColor color) {
        this.color = color;
    }

    public ChatColor getColor() {
        return color;
    }
}
