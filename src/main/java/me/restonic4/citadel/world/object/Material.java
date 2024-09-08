package me.restonic4.citadel.world.object;

public class Material {
    private float reflectance;
    private float shineDamper;

    public Material() {
        this.reflectance = 0;
        this.shineDamper = 1;
    }

    public Material(float reflectance) {
        this.reflectance = reflectance;
        this.shineDamper = 1;
    }

    public Material(float reflectance, float shineDamper) {
        this.reflectance = reflectance;
        this.shineDamper = shineDamper;
    }

    public float getReflectance() {
        return reflectance;
    }

    public float getShineDamper() {
        return shineDamper;
    }
}
