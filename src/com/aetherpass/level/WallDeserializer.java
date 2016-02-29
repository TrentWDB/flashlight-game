package com.aetherpass.level;

import com.google.gson.*;

import java.awt.*;
import java.lang.reflect.Type;

/**
 * Created by Trent on 2/27/2016.
 */
public class WallDeserializer implements JsonDeserializer<Wall> {
    private static final int SCALE = 2;
    @Override
    public Wall deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Wall returnWall = null;

        JsonObject jsonWallObject = jsonElement.getAsJsonObject();

        String wallType = jsonWallObject.get("type").getAsString();
        JsonArray jsonWallVertices = jsonWallObject.get("vertices").getAsJsonArray();

        Point[] wallVertices = new Point[jsonWallVertices.size()];
        for (int i = 0; i < jsonWallVertices.size(); i++) {
            JsonArray vertex = jsonWallVertices.get(i).getAsJsonArray();

            // TODO temporarily multiplying to scale the scene since I'm not yet scaling on export
            wallVertices[i] = new Point(vertex.get(0).getAsInt() * SCALE, vertex.get(1).getAsInt() * SCALE);
        }

        if (wallType.equals("rectangle")) {
            returnWall = new RectangleWall(wallType, wallVertices);
        } else if (wallType.equals("polygon")) {
            JsonArray trianglePartJsonArray = jsonWallObject.getAsJsonArray("triangles");
            TrianglePart[] trianglePartArray = new TrianglePart[trianglePartJsonArray.size()];

            for (int i = 0; i < trianglePartJsonArray.size(); i++) {
                JsonObject trianglePartObject = trianglePartJsonArray.get(i).getAsJsonObject();
                JsonArray trianglePartObjectVertices = trianglePartObject.getAsJsonArray("vertices");
                Point[] trianglePartVertices = new Point[trianglePartObjectVertices.size()];

                for (int a = 0; a < trianglePartObjectVertices.size(); a++) {
                    JsonArray trianglePartVertex = trianglePartObjectVertices.get(a).getAsJsonArray();
                    trianglePartVertices[a] = new Point(
                            trianglePartVertex.get(0).getAsInt() * SCALE,
                            trianglePartVertex.get(1).getAsInt() * SCALE
                    );
                }

                TrianglePart trianglePart = new TrianglePart(trianglePartVertices);
                trianglePartArray[i] = trianglePart;
            }

            returnWall = new PolygonWall(wallType, wallVertices, trianglePartArray);
        } else {
            System.err.println("Something is broken! The wall wasn't one of the expected types.");
        }

        return returnWall;
    }
}
