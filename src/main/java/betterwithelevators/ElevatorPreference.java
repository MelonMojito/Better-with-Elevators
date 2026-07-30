package betterwithelevators;

/**
 * Per-player elevator opt-out, implemented on Player by the elevator mixin.
 * Defaults to enabled, so a player without the mod (or without the option
 * touched) still gets elevators when the server has them on.
 */
public interface ElevatorPreference {

	boolean betterwithelevators$elevatorsEnabled();

	void betterwithelevators$setElevatorsEnabled(boolean enabled);
}
