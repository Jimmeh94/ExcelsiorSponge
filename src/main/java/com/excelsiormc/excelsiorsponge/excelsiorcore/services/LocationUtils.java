package com.excelsiormc.excelsiorsponge.excelsiorcore.services;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class LocationUtils {

    private static final Random random = new Random();

    public static Location getMiddleLocation(Location firstCorner, Location secondCorner){
        Vector3d temp = getMiddleLocation(firstCorner.getPosition(), secondCorner.getPosition());
        return new Location(firstCorner.getExtent(), temp.getX(), temp.getY(), temp.getZ());
    }

    public static Vector3d getMiddleLocation(Vector3d firstCorner, Vector3d secondCorner){
        double x = Math.max(firstCorner.getX(), secondCorner.getX()) - Math.min(firstCorner.getX(), secondCorner.getX());
        double y = Math.max(firstCorner.getY(), secondCorner.getY()) - Math.min(firstCorner.getY(), secondCorner.getY());
        double z = Math.max(firstCorner.getZ(), secondCorner.getZ()) - Math.min(firstCorner.getZ(), secondCorner.getZ());
        x /= 2;
        y /= 2;
        z /= 2;
        return new Vector3d(firstCorner.getX() + x + 0.5, firstCorner.getY() + y + 0.5, firstCorner.getZ() + z + 0.5);
    }

    public static boolean areCoordsEqual(Vector3d first, Vector3d second, boolean xNeedToMatch, boolean yNeedToMatch, boolean zNeedToMatch){
        boolean give = false;
        Vector3i one = first.toInt(), two = second.toInt();
        if(xNeedToMatch){
            if(one.getX() == two.getX()) {
                give = true;
            } else give = false;
        }

        if(yNeedToMatch){
            if(one.getY() == two.getY()){
                give = true;
            } else give = false;
        }

        if(zNeedToMatch){
            if(one.getZ() == two.getZ()){
                give = true;
            } else give = false;
        }

        return give;
    }

    public static boolean isBetween(Vector3d first, Vector3d second, Vector3d check, boolean ignoreY){
        double fx = first.getX(), fy = first.getY(), fz = first.getZ();
        double sx = second.getX(), sy = second.getY(), sz = second.getZ();

        if(ignoreY){
            return Math.min(fx, sx) <= check.getX() && check.getX() <= Math.max(fx, sx) &&
                    Math.min(fz, sz) <= check.getZ() && check.getZ() <= Math.max(fz, sz);
        } else {
            return Math.min(fx, sx) <= check.getX() && check.getX() <= Math.max(fx, sx) &&
                    Math.min(fy, sy) <= check.getY() && check.getY() <= Math.max(fy, sy) &&
                    Math.min(fz, sz) <= check.getZ() && check.getZ() <= Math.max(fz, sz);
        }
    }

    public static String locationToString(Location location){
        String give = location.getExtent().getUniqueId().toString();
        give += "," + String.valueOf(location.getX());
        give += "," + String.valueOf(location.getY());
        give += "," + String.valueOf(location.getZ());
        return give;
    }

    public static Location locationFromString(String loc){
        String[] temp = loc.split(",");
        return new Location(Sponge.getServer().getWorld(UUID.fromString(temp[0])).get(),
                Double.valueOf(temp[1]), Double.valueOf(temp[2]), Double.valueOf(temp[3]));
    }

    public static Vector3d getOffsetBetween(Location start, Location target){
        if(start == null || target == null)
            return new Vector3d(0.0, 0.0, 0.0);

        double deltaX, deltaY, deltaZ;
        deltaX = Math.max(start.getX(), target.getX()) - Math.min(start.getX(), target.getX());
        if(deltaX > 0)
            deltaX = Math.max(start.getX(), target.getX()) == start.getX() ? deltaX *-1 : deltaX * 1;

        deltaY = Math.max(start.getY(), target.getY()) - Math.min(start.getY(), target.getY());
        if(deltaY > 0)
            deltaY = Math.max(start.getY(), target.getY()) == start.getY() ? deltaY *-1 : deltaY * 1;

        deltaZ = Math.max(start.getZ(), target.getZ()) - Math.min(start.getZ(), target.getZ());
        if(deltaZ > 0)
            deltaZ = Math.max(start.getZ(), target.getZ()) == start.getZ() ? deltaZ *-1 : deltaZ * 1;

        return new Vector3d(deltaX, deltaY, deltaZ);
    }

    public static List<Location> getConnectingLine(Location start, Location target){
        List<Location> give = new ArrayList<>();
        give.add(start);

        double deltaX = Math.max(start.getX(), target.getX()) - Math.min(start.getX(), target.getX());
        double deltaY = Math.max(start.getY(), target.getY()) - Math.min(start.getY(), target.getY());
        double deltaZ = Math.max(start.getZ(), target.getZ()) - Math.min(start.getZ(), target.getZ());

        int xCoefficient = start.getX() > target.getX() ? -1 : 1;
        if(start.getX() == target.getX())
            xCoefficient = 0;

        int zCoefficient = start.getZ() > target.getZ() ? -1 : 1;
        if(start.getZ() == target.getZ())
            zCoefficient = 0;

        int yCoefficient = start.getY() > target.getY() ? -1 : 1;
        if(start.getY() == target.getY())
            yCoefficient = 0;

        Location temp = start.copy();
        do{
            if(deltaX == 0)
                xCoefficient = 0;
            if(deltaY == 0)
                yCoefficient = 0;
            if(deltaZ == 0)
                zCoefficient = 0;

            temp = temp.add(xCoefficient, yCoefficient, zCoefficient);
            give.add(temp);

            deltaX--;
            deltaY--;
            deltaZ--;
        } while(deltaX > 0 && deltaY > 0&& deltaZ> 0);

        give.add(target);

        return give;
    }

    public static List<Location> getAllLocationsBetween(Location start, Location finish){
        Vector3d difference = getOffsetBetween(start, finish);
        List<Location> give = new ArrayList<>();
        EditableVector temp = new EditableVector(start.getPosition());

        int xCoefficient = start.getBlockX() > finish.getBlockX() ? -1 : 1;
        int zCoefficient = start.getBlockZ() > finish.getBlockZ() ? -1 : 1;
        int yCoefficient = start.getBlockY() > finish.getBlockY() ? -1 : 1;

        int yLimit = difference.getFloorY() == 0 ? 1 : difference.getFloorY();

        for(int y = 0; y < yLimit; y++) {
            for (int x = 0; x < difference.getFloorX(); x++) {
                for (int z = 0; z < difference.getFloorZ(); z++) {
                    give.add(new Location(start.getExtent(), temp.toVector3d()));
                    temp.add(0, 0, zCoefficient);
                }
                temp.setZ(start.getBlockZ());
                temp.add(xCoefficient, 0, 0);
            }
            temp.setX(start.getBlockX());
            temp.add(0, yCoefficient, 0);
        }
        return give;
    }

    public static Vector3i getRandomDirection2D(){
        Random random = new Random();
        int x = 0, z = 0, r;

        r = random.nextInt(3);
        switch (r){
            case 0: x = -1;
                break;
            case 1: x = 0;
                break;
            case 2: x = 1;
        }

        r = random.nextInt(3);
        switch (r){
            case 0: z = -1;
                break;
            case 1: z = 0;
                break;
            case 2: z = 1;
        }

        if(x == 0 && z == 0){
            return getRandomDirection2D();
        }

        return new Vector3i(x, 0, z);
    }

    public static Vector3i rotateDirectionBy90(Vector3i direction){
        Direction d = Direction.getClosest(direction.toDouble());
        switch(d){
            case NORTH: return Direction.EAST.asBlockOffset();
            case NORTHEAST: return Direction.SOUTHEAST.asBlockOffset();
            case EAST: return Direction.SOUTH.asBlockOffset();
            case SOUTHEAST: return Direction.SOUTHWEST.asBlockOffset();
            case SOUTH: return Direction.WEST.asBlockOffset();
            case SOUTHWEST: return Direction.NORTHWEST.asBlockOffset();
            case WEST: return Direction.NORTH.asBlockOffset();
            case NORTHWEST: return Direction.NORTHEAST.asBlockOffset();
        }
        return direction;
    }

    public static Vector3i rotateDirectionBy180(Vector3i direction){
        return rotateDirectionBy90(rotateDirectionBy90(direction));
    }

    public static Vector3i rotateDirectionBy270(Vector3i direction){
        return rotateDirectionBy90(rotateDirectionBy90(rotateDirectionBy90(direction)));
    }

    public static Vector3i getDirection(Vector3d current, Vector3d target) {
        
        int x = current.getFloorX() > target.getFloorX() ? -1 : 1;
        if(current.getFloorX() == target.getFloorX())
            x = 0;

        int z = current.getFloorZ() > target.getFloorZ() ? -1 : 1;
        if(current.getFloorZ() == target.getFloorZ())
            z = 0;

        int y = current.getFloorY() > target.getFloorY() ? -1 : 1;
        if(current.getFloorY() == target.getFloorY())
            y = 0;
        
        return new Vector3i(x, y, z);
    }

    public static boolean isWithinDistance(Vector3d position, Vector3d target, double radius) {
        return position.distance(target) <= radius;
    }

    public static double getRandomNegOrPos() {
        return random.nextInt(2) == 0 ? -1 : 1;
    }
}
