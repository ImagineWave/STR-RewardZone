package ru.strid.strreward.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BlockPosition implements Serializable {
    private final String worldName;
    private final long x;
    private final long y;
    private final long z;

    public BlockPosition(String worldName, int x, int y, int z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getWorldName() {
        return worldName;
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public long getZ() {
        return z;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("world", worldName);
        map.put("x", x);
        map.put("y", y);
        map.put("z", z);
        return map;
    }

    public static BlockPosition fromMap(Map<String, Object> map) {
        String world = (String) map.get("world");
        long x = ((Number) map.get("x")).longValue();
        long y = ((Number) map.get("y")).longValue();
        long z = ((Number) map.get("z")).longValue();
        return new BlockPosition(world, (int) x, (int) y, (int) z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockPosition blockPosition = (BlockPosition) o;
        return x == blockPosition.x && y == blockPosition.y && z == blockPosition.z && Objects.equals(worldName, blockPosition.worldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldName, x, y, z);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("World: ").append(worldName).append(", x: ").append(x).append(", y: ").append(y).append(", z: ").append(z);
        return sb.toString();
    }
}
