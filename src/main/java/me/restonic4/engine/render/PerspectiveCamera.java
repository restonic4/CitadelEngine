package me.restonic4.engine.render;

import me.restonic4.engine.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class PerspectiveCamera extends Camera {
    protected float fov = (float) Math.toRadians(70.0f);
    private Vector3f orientation = new Vector3f(0, 1, 0);

    public PerspectiveCamera(Vector3f position, Vector3f rotation) {
        super(position, rotation);
    }

    public void adjustProjection() {
        this.projectionMatrix.identity();

        //projectionMatrix.perspective(fov, Window.getInstance().getAspectRatio(), nearPlane, farPlane);
        //projectionMatrix.setPerspective(fov, Window.getInstance().getAspectRatio(), nearPlane, farPlane);
        //projectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, nearPlane, farPlane);
    }

    public Matrix4f getViewMatrix() {
        this.viewMatrix.identity();

        Vector3f lookPoint = new Vector3f(0.0f, 0.0f, -1.0f);

        lookPoint.rotateX((float) Math.toRadians(getPitch()), lookPoint);
        lookPoint.rotateY((float) Math.toRadians(getYaw()), lookPoint);

        lookPoint.x += position.x;
        lookPoint.y += position.y;
        lookPoint.z += position.z;

        this.viewMatrix.lookAt(position, lookPoint, orientation, this.viewMatrix);

        return this.viewMatrix;

        /*this.viewMatrix.identity();

        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);

        Vector3f front = new Vector3f(position.x + cameraFront.x, position.y + cameraFront.y, position.z + cameraFront.z);

        this.viewMatrix.lookAt(position, front, cameraUp);

        return this.viewMatrix;*/

        /*this.viewMatrix.identity();
        Vector3f cameraPos = getPosition();
        viewMatrix.translate(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        return viewMatrix;*/

        /*Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, position.z),
                cameraFront.add(position.x, position.y, position.z),
                cameraUp);

        return this.viewMatrix;*/
        //this.viewMatrix.identity();  // Resetea la matriz a la identidad

        // Aplica la rotación en el eje X (pitch)
        //viewMatrix.rotateX((float) Math.toRadians(getPitch()));
        // Aplica la rotación en el eje Y (yaw)
        //viewMatrix.rotateY((float) Math.toRadians(getYaw()));

        // Obtiene la posición de la cámara
        //Vector3f cameraPos = getPosition();

        // Traslada la matriz con la posición negativa de la cámara
        //viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        //return this.viewMatrix;

        // Resetear la matriz de vista
        /*this.viewMatrix.identity();

        // Rotar la cámara según la rotación actual
        viewMatrix.rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z));

        // Trasladar la cámara a su posición
        viewMatrix.translate(-position.x, -position.y, -position.z);

        return this.viewMatrix;*/

        /*Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, position.z),
                cameraFront.add(position.x, position.y, position.z),
                cameraUp);

        return this.viewMatrix;*/

        /*viewMatrix.identity();

        // Apply rotation
        viewMatrix.rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z));

        // Apply translation
        viewMatrix.translate(-position.x, -position.y, -position.z);

        return this.viewMatrix;*/

        /*Vector3f cameraFront = new Vector3f(1, 0, 0);
        Vector3f cameraUp = new Vector3f(0, 1, 0);
        Vector3f cameraRight = new Vector3f(0, 0, 1);

        Vector3f center = new Vector3f(position.x, position.y, position.z);
        center.add(cameraFront);

        this.viewMatrix.identity();

        this.viewMatrix = viewMatrix.lookAt(position, center, cameraUp);

        return this.viewMatrix;*/
    }
}
