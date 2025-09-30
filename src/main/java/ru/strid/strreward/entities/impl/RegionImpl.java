package ru.strid.strreward.entities.impl;

import org.bukkit.Location;
import ru.strid.strreward.entities.BlockPosition;
import ru.strid.strreward.entities.Region;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RegionImpl implements Region, Serializable {
    private BlockPosition position1;
    private BlockPosition position2;

    public RegionImpl() {}

    public RegionImpl(BlockPosition position1, BlockPosition position2) {
        this.position1 = position1;
        this.position2 = position2;
    }

    @Override
    public BlockPosition getPosition1() {
        return position1;
    }

    @Override
    public void setPosition1(BlockPosition position1) {
        this.position1 = position1;
    }

    @Override
    public BlockPosition getPosition2() {
        return position2;
    }

    @Override
    public void setPosition2(BlockPosition position2) {
        this.position2 = position2;
    }

    @Override
    public boolean isInside(Location location) {
        if (location == null || location.getWorld() == null) return false;

        if (!location.getWorld().getName().equals(position1.getWorldName())) return false;

        long minX = Math.min(position1.getX(), position2.getX());
        long maxX = Math.max(position1.getX(), position2.getX());
        long minY = Math.min(position1.getY(), position2.getY());
        long maxY = Math.max(position1.getY(), position2.getY());
        long minZ = Math.min(position1.getZ(), position2.getZ());
        long maxZ = Math.max(position1.getZ(), position2.getZ());

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return x >= minX && x <= maxX &&
                y >= minY && y <= maxY &&
                z >= minZ && z <= maxZ;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("position1", position1.toMap());
        map.put("position2", position2.toMap());
        return map;
    }

    // Десериализация RegionImpl из Map
    public static RegionImpl fromMap(Map<String, Object> map) {
        Map<String, Object> pos1Map = (Map<String, Object>) map.get("position1");
        Map<String, Object> pos2Map = (Map<String, Object>) map.get("position2");

        BlockPosition pos1 = BlockPosition.fromMap(pos1Map);
        BlockPosition pos2 = BlockPosition.fromMap(pos2Map);

        return new RegionImpl(pos1, pos2);
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("§bPosition1: ");
        sb.append("\n ");
        if(position1 != null){
            sb.append(position1.toString());
        } else {
            sb.append("NULL");
        }
        sb.append("\n ");
        sb.append("\n ");
        sb.append("\n ");
        sb.append("§6Position2: ");
        sb.append("\n ");
        if(position2 != null){
            sb.append(position2.toString());
        } else {
            sb.append("NULL");
        }
       return sb.toString();
    }
}
