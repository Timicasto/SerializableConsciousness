package cc.sukazyo.sericons.util;

import java.util.Random;

public class PerlinNoiseGenerator {
    public static final float AMPLITUDE = 40;
    public static final int OCTAVES = 3;
    public static final float ROUGHNESS = 0.3F;
    public Random random = new Random();
    private int seed;

    public PerlinNoiseGenerator() {
        this.seed = random.nextInt(1000000000);
    }

    public float generateHeight(int x, int z) {
        float total = 0;
        float d = (float) Math.pow(2, OCTAVES - 1);
        for (int i = 0; i < OCTAVES; i++) {
            float freq = (float) (Math.pow(2, i) / d);
            float amp = (float) (Math.pow(ROUGHNESS, i) * AMPLITUDE);
            total += getInterpolatedNoise(x * freq, z * freq) * amp;
        }
        return total;
    }

    private float getSmoothNoise(int x, int z) {
        float corners = (getNoise(x - 1, z - 1) + getNoise(x + 1, z - 1) + getNoise(x - 1, z + 1) + getNoise(x + 1, z + 1)) / 16F;
        float sides = (getNoise(x - 1, z) + getNoise(x, z - 1) + getNoise(x, z + 1)) / 8;
        float center = getNoise(x, z) / 4;
        return corners + sides + center;
    }

    private float getInterpolatedNoise(float x, float z) {
        int intX = (int) x;
        int intZ = (int) z;
        float fracX = x - intX;
        float fracZ = z - intZ;
        float v1 = getSmoothNoise(intX, intZ);
        float v2 = getSmoothNoise(intX + 1, intZ);
        float v3 = getSmoothNoise(intX, intZ + 1);
        float v4 = getSmoothNoise(intX + 1, intZ + 1);
        float i1 = interpolate(v1, v2, fracX);
        float i2 = interpolate(v3, v4, fracZ);
        return interpolate(i1, i2, fracZ);
    }

    private float interpolate(float a, float b, float blend) {
        double theta = blend * Math.PI;
        float f = (float) ((1 - Math.cos(theta)) * 0.5);
        return a * (1 - f) + b * f;
    }

    public float getNoise(int x, int z) {
        random.setSeed(x * 49632 + z * 325176 + seed);
        return random.nextFloat() * 2 - 1;
    }
}