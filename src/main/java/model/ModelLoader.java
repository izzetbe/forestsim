package model;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.io.*;
import java.util.*;

/*
 Basic custom OBJ loader for JavaFX 3D.
 Only supports vertices (v) and faces (f).
 Quads get split into triangles.
 No texture coordinates (vt) or normals (vn) are supported.
 */
public class ModelLoader {

    //Store loaded meshes so we don't reload the same file twice
    private static final Map<String, TriangleMesh> meshCache = new HashMap<>();

    //Reads an OBJ file and returns a TriangleMesh
    public static TriangleMesh loadMesh(String resourcePath) throws IOException {
        if (meshCache.containsKey(resourcePath))
            return meshCache.get(resourcePath);

        InputStream is = ModelLoader.class.getResourceAsStream(resourcePath);
        if (is == null)
            throw new FileNotFoundException("Missing OBJ: " + resourcePath);

        List<float[]> vertices = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        //Read the file line by line
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    //vertex line: v x y z
                    String[] parts = line.trim().split("\\s+");
                    vertices.add(new float[]{
                            Float.parseFloat(parts[1]),
                            Float.parseFloat(parts[2]),
                            Float.parseFloat(parts[3])
                    });
                } else if (line.startsWith("f ")) {
                    //face line: f 1 2 3 or f 1 2 3 4
                    addFace(line.trim().split("\\s+"), faces);
                }
            }
        }

        if (vertices.isEmpty() || faces.isEmpty())
            throw new IOException("OBJ has no geometry: " + resourcePath);

        TriangleMesh mesh = new TriangleMesh();

        //Add vertex data
        for (float[] v : vertices)
            mesh.getPoints().addAll(v[0], v[1], v[2]);

        //JavaFX needs at least one texture coordinate
        mesh.getTexCoords().addAll(0, 0);

        //Add faces (each face = 3 vertex indices)
        for (int[] f : faces)
            mesh.getFaces().addAll(f[0], 0, f[1], 0, f[2], 0);

        meshCache.put(resourcePath, mesh);
        return mesh;
    }

    //Wraps a mesh in a MeshView with color and scale
    public static MeshView createInstance(String resourcePath, Color color, double scale) throws IOException {
        TriangleMesh mesh = loadMesh(resourcePath);

        MeshView view = new MeshView(mesh);
        view.setMaterial(new PhongMaterial(color));
        view.setCullFace(CullFace.NONE); //render both sides
        view.setScaleX(scale);
        view.setScaleY(scale);
        view.setScaleZ(scale);
        return view;
    }

    //Parse face data, handle both triangles and quads
    private static void addFace(String[] parts, List<int[]> faces) {
        int[] idx = Arrays.stream(parts)
                .skip(1) //skip "f"
                .mapToInt(p -> Integer.parseInt(p.split("/")[0]) - 1) // OBJ is 1-indexed
                .toArray();

        if (idx.length == 3) {
            //normal triangle
            faces.add(new int[]{idx[0], idx[1], idx[2]});
        } else if (idx.length == 4) {
            //split quad into two triangles
            faces.add(new int[]{idx[0], idx[1], idx[2]});
            faces.add(new int[]{idx[0], idx[2], idx[3]});
        }
    }
}