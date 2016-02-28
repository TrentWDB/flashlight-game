package com.aetherpass.level;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by Trent on 2/27/2016.
 */
public class WallDeserializer implements JsonDeserializer<Wall> {
    @Override
    public Wall deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Wall returnWall = null;

        JsonObject jsonWallObject = jsonElement.getAsJsonObject();

        String wallType = jsonWallObject.get("type").getAsString();
        JsonArray jsonWallVertices = jsonWallObject.get("vertices").getAsJsonArray();

        int[][] wallVertices = new int[jsonWallVertices.size()][2];
        for (int i = 0; i < jsonWallVertices.size(); i++) {
            JsonArray vertex = jsonWallVertices.get(i).getAsJsonArray();

            // TODO temporarily multiplying to scale the scene since I'm not yet scaling on export
            wallVertices[i][0] = vertex.get(0).getAsInt() * 2;
            wallVertices[i][1] = vertex.get(1).getAsInt() * 2;
        }

        if (wallType.equals("rectangle")) {
            returnWall = new RectangleWall(wallType, wallVertices);
        } else if (wallType.equals("polygon")) {
            returnWall = new PolygonWall(wallType, wallVertices);
        } else {
            System.err.println("Something is broken! The wall wasn't one of the expected types.");
        }

        return returnWall;
    }
}
