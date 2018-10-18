package com.excelsiormc.excelsiorsponge.excelsiorcore.services.particles.effects;

import com.excelsiormc.excelsiorsponge.excelsiorcore.services.particles.ParticleData;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.particles.ParticlePlayer;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.particles.effects.options.EffectOption;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractEffect {

	protected ParticleData effectData;
	protected List<EffectOption> effectOptions = new ArrayList<>();
	protected Task task;
	protected long delay, interval, cancel = 0; //cancel should be the amount of intervals to run

	public AbstractEffect(long interval, ParticleData effectData, EffectOption... options){
		this(interval, 0, 0, effectData, options);
	}

	public AbstractEffect(long interval, long delay, long cancel, ParticleData effectData, EffectOption... options) {
		this.delay = delay;
		this.interval = interval;
		this.cancel = cancel;
		this.effectData = effectData;
		if(options != null){
			effectOptions = Arrays.asList(options);
		}
	}

	protected <T> Optional<T> getOption(EffectOption.EffectOptionTypes types){
		for(EffectOption option: effectOptions){
			if(option.getType() == types){
				return Optional.of((T) option);
			}
		}
		return Optional.empty();
	}

	/**
	 * An abstract method used to display the animation.
	 */
	protected abstract void play();

	/**
	 * Starts the runnable, which makes the effect display itself every interval.
	 * This would be used outside of the effect being in an ability
	 * 
	 * @return The current instance of the effect to allow chaining of methods.
	 */
	public AbstractEffect start(Object plugin) {

		Task.Builder taskBuilder = Sponge.getScheduler().createTaskBuilder();
		task = taskBuilder.delayTicks(delay).intervalTicks(interval).execute(
				new Runnable() {
					int c = 0;

					@Override
					public void run() {
						prePlay();
						c++;
						if (c >= cancel && cancel != 0)
							stop();
					}
				}
		).submit(plugin);

		return this;
	}

	public void prePlay(){
		play();
	}

	/**
	 * Stops the effect from automaticly displaying.
	 * 
	 * @return The current instance of the effect, to allow 'chaining' of
	 *         methods.
	 */
	public AbstractEffect stop() {
		if (task == null)
			return this;
		try {
			task.cancel();
		} catch (IllegalStateException exc) {
		}
		return this;
	}

	/**
	 * Spawns a particle using the set particle effect.
	 */
	protected void playParticle(){
		ParticlePlayer.display(effectData);
	}

	public ParticleData getEffectData() {
		return effectData;
	}

	protected Vector3d rotateAroundAxisX(Vector3d v, double cos, double sin) {
		double y = v.getY() * cos - v.getZ() * sin;
		double z = v.getY() * sin + v.getZ() * cos;
		return new Vector3d(v.getX(), y, z);
	}

	protected Vector3d rotateAroundAxisY(Vector3d v, double cos, double sin) {
		double x = v.getX() * cos + v.getZ() * sin;
		double z = v.getX() * -sin + v.getZ() * cos;
		return new Vector3d(x, v.getY(), z);
	}

	protected Vector3d rotateAroundAxisZ(Vector3d v, double cos, double sin) {
		double x = v.getX() * cos - v.getY() * sin;
		double y = v.getX() * sin + v.getY() * cos;
		return new Vector3d(x, y, v.getZ());
	}

	/*public float[] vectorToYawPitch(Vector3d v) {
		Location loc = new Location(null, 0, 0, 0);
		loc.setDirection(v);
		//return new float[] { loc.getYaw(), loc.getPitch() };
		return new float[] { v.getYaw(), loc.getPitch() };
	}*/

	public Vector3d yawPitchToVector(float yaw, float pitch) {
		yaw += 90;
		return new Vector3d(Math.cos(Math.toRadians(yaw)), Math.sin(Math.toRadians(pitch)),
				Math.sin(Math.toRadians(yaw)));
	}
}