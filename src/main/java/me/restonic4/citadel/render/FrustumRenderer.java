package me.restonic4.citadel.render;

import me.restonic4.ClientSide;
import me.restonic4.citadel.registries.built_in.managers.Shaders;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

@ClientSide
public class FrustumRenderer {
    public static void renderFrustum(Vector3f[] corners) {
        drawLine(corners[0], corners[1]);
        drawLine(corners[1], corners[2]);
        drawLine(corners[2], corners[3]);
        drawLine(corners[3], corners[0]);

        drawLine(corners[4], corners[5]);
        drawLine(corners[5], corners[6]);
        drawLine(corners[6], corners[7]);
        drawLine(corners[7], corners[4]);

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
