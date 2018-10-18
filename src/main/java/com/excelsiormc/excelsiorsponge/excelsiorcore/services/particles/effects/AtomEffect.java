package com.excelsiormc.excelsiorsponge.excelsiorcore.services.particles.effects;


import com.excelsiormc.excelsiorsponge.excelsiorcore.services.particles.ParticleData;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.particles.PreloadedParticle;

import java.util.Arrays;

public class AtomEffect extends AbstractEffect {

	private double[][][] coreCoordinates;
	private double[][] ringCoordinates;

	public AtomEffect(double ringRadius, double coreRadius, double yawOffset){
		super(null);

		init(ringRadius, coreRadius, yawOffset);
	}

	public AtomEffect(ParticleData effectData, PreloadedParticle loaded){
		super(effectData);

		AtomEffect atomEffect = (AtomEffect)loaded.getEffect();
		coreCoordinates = Arrays.copyOf(atomEffect.coreCoordinates, atomEffect.coreCoordinates.length);
		ringCoordinates = Arrays.copyOf(atomEffect.ringCoordinates, atomEffect.ringCoordinates.length);
	}

	private void init(double rr, double cr, double yawOffset) {
		double x, y, z;
		coreCoordinates = new double[24][][];
		for (int i = 0; i < coreCoordinates.length; i++) {
			coreCoordinates[i] = new double[16][];
			double r = Math.sin((Math.PI / 12) * i);
			for (int i2 = 0; i2 < 16; i2++) {
				double theta = i2 * (Math.PI / 8);
				x = cr * Math.cos(theta) * r;
				z = cr * Math.sin(theta) * r;
				y = cr * Math.cos((Math.PI / 12) * i);
				coreCoordinates[i][i2] = new double[] { x, y, z };
			}
		}
		int iterationCounter = 0;
		ringCoordinates = new double[120][];
		for (int ringCounter = -1; ringCounter <= 1; ringCounter++) {
			for (int i2 = 0; i2 < 40; i2++) {
				double a = i2 * (Math.PI / 20);
				y = ringCounter == 0 ? 0 : Math.sin(a + Math.toRadians(yawOffset)) * rr * ringCounter;
				x = Math.cos(a) * rr;
				z = Math.sin(a) * rr;
				ringCoordinates[i2 + 40 * iterationCounter] = new double[] { x, y, z };
			}
			iterationCounter++;
		}
	}

	@Override
	protected void play() {
		for (double[][] firstArray : coreCoordinates)
			for (double[] secondArray : firstArray) {
				effectData.setDisplayAt(effectData.getCenter().add(secondArray[0], secondArray[1], secondArray[2]));
				playParticle();
				effectData.getCenter().sub(secondArray[0], secondArray[1], secondArray[2]);
			}
		for (double[] array : ringCoordinates) {
			effectData.setDisplayAt(effectData.getCenter().add(array[0], array[1], array[2]));
			playParticle();
			effectData.getCenter().sub(array[0], array[1], array[2]);
		}
	}

}