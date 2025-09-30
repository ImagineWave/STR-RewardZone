package ru.strid.strreward.entities;

import org.bukkit.Location;

public interface Region {
    boolean isInside(Location location);

    BlockPosition getPosition1();
    void setPosition1(BlockPosition position1);
    BlockPosition getPosition2();
    void setPosition2(BlockPosition position2);
    String toString();

}
