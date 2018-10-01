package com.excelsiormc.excelsiorsponge.utils.database;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.database.ServiceMongoDB;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.field.GridNormal;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.TerrainBuild;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.TerrainTypes;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class MongoUtils extends ServiceMongoDB {

    private static final String COLLECTION_ARENAS = "arenas";
    private static final String COLLECTION_TERRAIN_BUILDS = "builds";

    public MongoUtils(String username, String password, String ip, String databaseName) {
        super(username, password, ip, databaseName);
    }

    public void load(){
        readArenas();
        readCellTerrainBuilds();
    }

    /**
     * Arenas:
     * UUID id
     * String world
     * Gamemode (0 = duel)
     * Grid (vector start, int x, int z, int cellX, int cellZ)
     */

    public void readArenas(){
        if(isConnected()){
            MongoDatabase database = getDatabase();
            MongoCollection<Document> arenas = database.getCollection(COLLECTION_ARENAS);
            arenas.find().forEach(new Block<Document>() {
                @Override
                public void apply(Document document) {
                    UUID id = UUID.fromString(document.getString("id"));
                    String world = document.getString("world");

                    Document gridDoc = (Document) document.get("grid");
                    String[] temp = gridDoc.getString("startPos").split(",");

                    Vector3d v = new Vector3d(Double.valueOf(temp[0]), Double.valueOf(temp[1]), Double.valueOf(temp[2]));

                    int rowCount = gridDoc.getInteger("rowCount"), rowLength = gridDoc.getInteger("rowLength");
                    int cellDem = gridDoc.getInteger("cellDim");
                    ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager()
                            .add(new Arena(new GridNormal(v, world, rowCount, rowLength, cellDem, false), world, id));
                }
            });
        }
    }

    public void writeArenas(){
        if(isConnected()){
            List<Document> write = new ArrayList<>();

            MongoDatabase database = getDatabase();
            MongoCollection<Document> arenas = database.getCollection(COLLECTION_ARENAS);

            for(Arena arena: ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().getObjects()){
                if(arenas.find(eq("id", arena.getID().toString())).first() != null){
                    continue;
                }

                String startPos = "";
                Vector3d startPosV = arena.getGrid().getStartPos();

                startPos += startPosV.getFloorX();
                startPos += ",";
                startPos += startPosV.getFloorY();
                startPos += ",";
                startPos += startPosV.getFloorZ();

                write.add(new Document("id", arena.getID().toString())
                        .append("world", arena.getWorld())
                        .append("grid", new Document("startPos", startPos)
                                    .append("rowCount", arena.getGrid().getRowCount())
                                    .append("rowLength", arena.getGrid().getRowLength())
                                    .append("cellDim", arena.getGrid().getCellDimension())
                        )
                );
            }

            if(write.size() > 0){
                arenas.insertMany(write);
            }
        }
    }

    public void writeCards(){
        if(isConnected()){
            MongoClient client = getClient();
        }
    }

    public void readCards(){
        if(isConnected()){
            MongoClient client = getClient();
        }
    }

    public void writeCellTerrainBuilds(){
        if(isConnected()){
            List<Document> write = new ArrayList<>();

            MongoDatabase database = getDatabase();
            MongoCollection<Document> builds = database.getCollection(COLLECTION_TERRAIN_BUILDS);

            boolean exists = false;
            for(TerrainTypes type: TerrainTypes.values()){
                if(type.getCellType().getBuild().size() == 0){
                    continue;
                }

                if(builds.find(eq("type", type.toString().toUpperCase())).first() != null){
                    exists = true;
                }

                Document document = new Document("type", type.toString().toUpperCase())
                        .append("world", ((World)type.getCellType().getBuild().get(0).getStart().getExtent()).getName());

                int count = 1;
                for(TerrainBuild build: type.getCellType().getBuild()){
                    Vector3i start = build.getStart().getPosition().toInt(), end = build.getEnd().getPosition().toInt();
                    String s = "" + start.getX() + "," + start.getY() + "," + start.getZ();
                    String e = "" + end.getX() + "," + end.getY() + "," + end.getZ();
                    document.append(type.toString() + String.valueOf(count),
                            new Document("start", s).append("end", e));
                    count++;
                }

                if(!exists) {
                    write.add(document);
                } else {
                    builds.replaceOne(eq("type", type.toString().toUpperCase()), document);
                }
            }

            if(write.size() > 0){
                builds.insertMany(write);
            }
        }
    }

    public void readCellTerrainBuilds(){
        if(isConnected()){
            MongoDatabase database = getDatabase();
            MongoCollection<Document> arenas = database.getCollection(COLLECTION_TERRAIN_BUILDS);
            arenas.find().forEach(new Block<Document>() {
                @Override
                public void apply(Document document) {
                    TerrainTypes type = TerrainTypes.valueOf(document.getString("type").toUpperCase());
                    World world = Sponge.getServer().getWorld(document.getString("world")).get();

                    Set<Map.Entry<String, Object>> entries = document.entrySet();

                    int count = 0;
                    for(Map.Entry<String, Object> entry: entries){
                        if(count < 3){
                            count++;
                            continue;
                        }

                        //Now we've passed the "type" and "world"

                        Document doc = (Document) document.get(entry.getKey());
                        String[] s = doc.getString("start").split(","), e = doc.getString("end").split(",");
                        Location start = new Location(world, new Vector3d(Double.valueOf(s[0]), Double.valueOf(s[1]), Double.valueOf(s[2])));
                        Location end = new Location(world, new Vector3d(Double.valueOf(e[0]), Double.valueOf(e[1]), Double.valueOf(e[2])));

                        type.getCellType().addBuild(new TerrainBuild(start, end));
                        count++;
                    }
                }
            });
        }
    }

    @Override
    public void close() {
        //write all data to database
        writeArenas();
        writeCellTerrainBuilds();

        super.close();
    }
}
