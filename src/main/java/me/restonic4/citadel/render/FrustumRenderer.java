package me.restonic4.citadel.render;

import me.restonic4.ClientSide;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

@ClientSide
public class FrustumRenderer {
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

    protected static void drawLine(Vector3f start, Vector3f end) {
        glBegin(GL_LINES);
        glVertex3f(start.x, start.y, start.z);
        glVertex3f(end.x, end.y, end.z);
        glEnd();
    }
}
