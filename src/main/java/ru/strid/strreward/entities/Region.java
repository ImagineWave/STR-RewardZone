package ru.strid.strreward.entities;

import org.bukkit.Location;

public interface Region {
    boolean isInside(Location location);

    BlockPosition getPosition1();
    void setPosition1(BlockPosition position1);
    BlockPosition getPosition2();
    void setPosition2(BlockPosition position2);
    String toString();

    default boolean isValid(){
        if(getPosition1() == null || getPosition2() == null){
            return false;
        }
        if(!getPosition1().getWorldName().equals(getPosition2().getWorldName())){
            return false;
        }
        //Добавить проверку на размер региона
        return true;
    }

}
