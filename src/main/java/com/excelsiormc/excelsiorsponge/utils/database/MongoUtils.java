package com.excelsiormc.excelsiorsponge.utils.database;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.database.ServiceMongoDB;
import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.field.GridNormal;
import com.flowpowered.math.vector.Vector3d;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class MongoUtils extends ServiceMongoDB {

    private static final String COLLECTION_ARENAS = "arenas";

    public MongoUtils(String username, String password, String ip, String databaseName) {
        super(username, password, ip, databaseName);
    }

    public void load(){
        readArenas();
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
                    Vector3d v = new Vector3d(Double.valueOf(temp[0]) + 1, Double.valueOf(temp[1]) + 1, Double.valueOf(temp[2]) + 1);
                    int gx = gridDoc.getInteger("gridX"), gz = gridDoc.getInteger("gridZ");
                    int cx = gridDoc.getInteger("cellX"), cz = gridDoc.getInteger("cellZ");
                    ExcelsiorSponge.INSTANCE.getArenaManager().add(new Arena(new GridNormal(v, world, gx, gz, cx, cz, false), world, id));
                }
            });
        }
    }

    public void writeArenas(){
        if(isConnected()){
            List<Document> write = new ArrayList<>();

            MongoDatabase database = getDatabase();
            MongoCollection<Document> arenas = database.getCollection(COLLECTION_ARENAS);

            for(Arena arena: ExcelsiorSponge.INSTANCE.getArenaManager().getObjects()){
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
                                    .append("gridX", arena.getGrid().getGridX())
                                    .append("gridZ", arena.getGrid().getGridZ())
                                    .append("cellX", arena.getGrid().getCellX())
                                    .append("cellZ", arena.getGrid().getCellZ())
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

    @Override
    public void close() {
        //write all data to database
        writeArenas();

        super.close();
    }
}
