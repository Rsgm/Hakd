package hakd.game;

import com.badlogic.gdx.Gdx;
import libnoiseforjava.NoiseGen;
import libnoiseforjava.exception.ExceptionInvalidParam;
import libnoiseforjava.module.*;

public final class Noise {
    public static final ModuleBase TERRAIN; // mostly for aesthetics
    public static final ModuleBase DENSITY; // cities/towns/rural, how close networks will be or the chance of generating a network on that spot.
    public static final ModuleBase POLITICS; // political espionage? dependant on countries, maybe
    public static final ModuleBase ETHICS; // hacker ethics, somewhat dependant on crime
    public static final ModuleBase COUNTRY; // not sure yet, maybe voroni noise, or maybe code
    public static final ModuleBase INCOME; // somewhat dependant on density and crime
    public static final ModuleBase CRIME; // dependant on density

    private static ModuleBase land;

    static {
        TERRAIN = TERRAIN();
        DENSITY = density();
        POLITICS = politics();
        ETHICS = ethics();
        COUNTRY = country();
        INCOME = income();
        CRIME = crime();
    }

    private static ModuleBase TERRAIN() { // modified version of http://libnoise.sourceforge.net/tutorials/tutorial5.html
        try {
            Perlin land1;
            Billow land2;
            RidgedMulti mountainTerrain;
            Billow baseFlatTerrain;
            ScaleBias flatTerrain;
            Perlin terrainType;
            ScaleBias terrainTypeScaleBias;
            Select finalTerrain;

            land1 = new Perlin();
            land1.setNoiseQuality(NoiseGen.NoiseQuality.QUALITY_BEST);
            land1.setSeed((int) (Math.random() * Integer.MAX_VALUE));
            land1.setFrequency(.0001);
            land1.setPersistence(.45);
            land1.setLacunarity(2.5);
            land1.setOctaveCount(6);

            land2 = new Billow();
            land2.setNoiseQuality(NoiseGen.NoiseQuality.QUALITY_BEST);
            land2.setSeed((int) (Math.random() * Integer.MAX_VALUE));
            land2.setFrequency(.0001);
            land2.setPersistence(.45);
            land2.setLacunarity(2.5);
            land2.setOctaveCount(6);

            ScaleBias scale = new ScaleBias(new Add(land1, land2));
            scale.setScale(.5);
            scale.setBias(0);

            land = new Clamp(scale);
            ((Clamp) land).setBounds(-1, 1);


            mountainTerrain = new RidgedMulti();
            mountainTerrain.setNoiseQuality(NoiseGen.NoiseQuality.QUALITY_BEST);
            mountainTerrain.setSeed((int) (Math.random() * Integer.MAX_VALUE));
            mountainTerrain.setFrequency(.0005);

            ScaleBias mountainScale = new ScaleBias(mountainTerrain);
            mountainScale.setScale(.25);
            mountainScale.setBias(.65);


            baseFlatTerrain = new Billow();
            baseFlatTerrain.setNoiseQuality(NoiseGen.NoiseQuality.QUALITY_BEST);
            baseFlatTerrain.setSeed((int) (Math.random() * Integer.MAX_VALUE));
            baseFlatTerrain.setFrequency(.0003);

            flatTerrain = new ScaleBias(baseFlatTerrain);
            flatTerrain.setScale(0.125);
            flatTerrain.setBias(.5);

            terrainType = new Perlin();
            terrainType.setNoiseQuality(NoiseGen.NoiseQuality.QUALITY_BEST);
            terrainType.setSeed((int) (Math.random() * Integer.MAX_VALUE));
            terrainType.setFrequency(.00008);
            terrainType.setPersistence(0.25);

            terrainTypeScaleBias = new ScaleBias(terrainType);
            terrainTypeScaleBias.setScale(.3);
            terrainTypeScaleBias.setBias(.6);

            finalTerrain = new Select(flatTerrain, mountainScale, terrainTypeScaleBias);
            finalTerrain.setBounds(.7, 10);
            finalTerrain.setEdgeFalloff(.05);


            Select landSelector = new Select(new Const(0), finalTerrain, land);
            landSelector.setBounds(0, 10); // -0.3, 1
            landSelector.setEdgeFalloff(.2);

            Clamp clamp = new Clamp(landSelector);
            clamp.setBounds(0, 1);
            return clamp;
        } catch (ExceptionInvalidParam exceptionInvalidParam) {
            exceptionInvalidParam.printStackTrace();
        }
        return null;
    }

    private static ModuleBase density() {
        try {
            Perlin perlin1 = new Perlin();
            perlin1.setNoiseQuality(NoiseGen.NoiseQuality.QUALITY_BEST);
            perlin1.setSeed((int) (Math.random() * Integer.MAX_VALUE));
            perlin1.setFrequency(.00015);
            perlin1.setLacunarity(2.5);
            perlin1.setOctaveCount(4);

            ScaleBias scale1 = new ScaleBias(perlin1);
            scale1.setScale(3);
            scale1.setBias(-1.5);


            Perlin perlin2 = new Perlin();
            perlin2.setNoiseQuality(NoiseGen.NoiseQuality.QUALITY_BEST);
            perlin2.setSeed((int) (Math.random() * Integer.MAX_VALUE));
            perlin2.setFrequency(.0003);
            perlin2.setOctaveCount(6);

            ScaleBias scale2 = new ScaleBias(perlin2);
            scale2.setScale(.8);
            scale2.setBias(-.1);


            Select select = new Select(new Const(-1), new Max(scale1, scale2), land);
            select.setBounds(0, 1);

            Clamp clamp = new Clamp(select);
            clamp.setBounds(-1, 1);
            return clamp;
        } catch (ExceptionInvalidParam exceptionInvalidParam) {
            exceptionInvalidParam.printStackTrace();
        }
        return null;
    }

    private static ModuleBase politics() {
        try {
            Clamp clamp = new Clamp(new Const());
            clamp.setBounds(0, 1);
            return clamp;
        } catch (ExceptionInvalidParam exceptionInvalidParam) {
            exceptionInvalidParam.printStackTrace();
        }
        return null;
    }

    private static ModuleBase ethics() {
        try {
            Clamp clamp = new Clamp(new Const());
            clamp.setBounds(0, 1);
            return clamp;
        } catch (ExceptionInvalidParam exceptionInvalidParam) {
            exceptionInvalidParam.printStackTrace();
        }
        return null;
    }

    private static ModuleBase country() {
        try {
            Clamp clamp = new Clamp(new Const());
            clamp.setBounds(0, 1);
            return clamp;
        } catch (ExceptionInvalidParam exceptionInvalidParam) {
            exceptionInvalidParam.printStackTrace();
        }
        return null;
    }

    private static ModuleBase income() {
        try {
            Clamp clamp = new Clamp(new Const());
            clamp.setBounds(0, 1);
            return clamp;
        } catch (ExceptionInvalidParam exceptionInvalidParam) {
            exceptionInvalidParam.printStackTrace();
        }
        return null;
    }

    private static ModuleBase crime() {
        try {
            Clamp clamp = new Clamp(new Const());
            clamp.setBounds(0, 1);
            return clamp;
        } catch (ExceptionInvalidParam exceptionInvalidParam) {
            exceptionInvalidParam.printStackTrace();
        }
        return null;
    }

    public static double getValue(NoiseType type, double x, double y) {
        switch (type) {
            case TERRAIN:
                return TERRAIN.getValue(x, 0, y);
            case DENSITY:
                return DENSITY.getValue(x, 0, y);
            case COUNTRY:
                return COUNTRY.getValue(x, 0, y);
            case POLITICS:
                return POLITICS.getValue(x, 0, y);
            case ETHICS:
                return ETHICS.getValue(x, 0, y);
            case INCOME:
                return INCOME.getValue(x, 0, y);
            case CRIME:
                return CRIME.getValue(x, 0, y);
        }
        Gdx.app.error("Noise.getValue", "Unknown noise type");
        return 0; // this should never happen
    }

    public enum NoiseType {
        TERRAIN, DENSITY, POLITICS, ETHICS, COUNTRY, INCOME, CRIME
    }

    public static ModuleBase getLand() {
        return land;
    }
}
