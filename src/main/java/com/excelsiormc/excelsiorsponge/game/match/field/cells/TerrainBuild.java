package com.excelsiormc.excelsiorsponge.game.match.field.cells;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.LocationUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.Location;

import java.util.List;

public class TerrainBuild {

    private List<Location> locations;
    private Location start, end;

    public TerrainBuild(Location start, Location end) {
        this.start = start;
        this.end = end;
        loadBuild();
    }

    private void loadBuild(){
        locations = LocationUtils.getAllLocationsBetween(start, end);
    }

    public void draw(Vector3d start){
        Vector3d distance = locations.get(0).getPosition().sub(start);

        for(Location location: locations){
            Location loc = location.getExtent().getLocation(location.getPosition().sub(distance));
            loc.setBlockType(location.getBlockType());
            loc.setBlock(location.getBlock());
        }
    }

    public void destroy(Vector3d start){
        Vector3d distance = locations.get(0).getPosition().sub(start);

        for(Location location: locations){
            Location loc = location.getExtent().getLocation(location.getPosition().sub(distance));
            loc.setBlockType(BlockTypes.AIR);
        }
    }
}
