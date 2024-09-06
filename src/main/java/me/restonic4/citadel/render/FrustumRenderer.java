package me.restonic4.citadel.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class FrustumRenderer {
    public static Vector3f[] extractFrustumCorners(Matrix4f projMatrix, Matrix4f viewMatrix) {
        Matrix4f invProjView = new Matrix4f();
        projMatrix.mul(viewMatrix, invProjView).invert();

        // Coordenadas de las esquinas del frustum en el espacio de recorte
        Vector3f[] frustumCorners = new Vector3f[8];
        frustumCorners[0] = new Vector3f(-1, -1, -1); // Near-bottom-left
        frustumCorners[1] = new Vector3f(1, -1, -1);  // Near-bottom-right
        frustumCorners[2] = new Vector3f(1, 1, -1);   // Near-top-right
        frustumCorners[3] = new Vector3f(-1, 1, -1);  // Near-top-left

        frustumCorners[4] = new Vector3f(-1, -1, 1);  // Far-bottom-left
        frustumCorners[5] = new Vector3f(1, -1, 1);   // Far-bottom-right
        frustumCorners[6] = new Vector3f(1, 1, 1);    // Far-top-right
        frustumCorners[7] = new Vector3f(-1, 1, 1);   // Far-top-left

        // Transformar las esquinas al espacio del mundo
        for (int i = 0; i < 8; i++) {
            frustumCorners[i].mulProject(invProjView);
        }

        return frustumCorners;
    }

    public static void renderFrustum(Vector3f[] corners) {
        // Dibujar near plane en amarillo
        glColor3f(1.0f, 1.0f, 0.0f); // Amarillo
        drawLine(corners[0], corners[1]);
        drawLine(corners[1], corners[2]);
        drawLine(corners[2], corners[3]);
        drawLine(corners[3], corners[0]);

        // Dibujar far plane en rojo
        glColor3f(1.0f, 0.0f, 0.0f); // Rojo
        drawLine(corners[4], corners[5]);
        drawLine(corners[5], corners[6]);
        drawLine(corners[6], corners[7]);
        drawLine(corners[7], corners[4]);

        // Dibujar las líneas que conectan near plane con far plane en blanco
        glColor3f(1.0f, 1.0f, 1.0f); // Blanco
        drawLine(corners[0], corners[4]);
        drawLine(corners[1], corners[5]);
        drawLine(corners[2], corners[6]);
        drawLine(corners[3], corners[7]);
    }

    private static void drawLine(Vector3f start, Vector3f end) {
        glBegin(GL_LINES);
        glVertex3f(start.x, start.y, start.z);
        glVertex3f(end.x, end.y, end.z);
        glEnd();
    }
}
