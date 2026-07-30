package betterwithelevators.net;

import betterwithelevators.ElevatorPreference;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

/**
 * Client to server: "elevators are on/off for me".
 * <p>
 * The server stores this on the sending player and skips elevators for them
 * when it is off, which is what makes the client option work in multiplayer.
 * In singleplayer halplibe loops this straight back to the local player.
 */
public class ElevatorPreferenceMessage implements NetworkMessage {

	private boolean enabled;

	//required: the network handler constructs instances reflectively when decoding
	public ElevatorPreferenceMessage() {
	}

	public ElevatorPreferenceMessage(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void encodeToUniversalPacket(UniversalPacket packet) {
		packet.writeBoolean(enabled);
	}

	@Override
	public void decodeFromUniversalPacket(UniversalPacket packet) {
		enabled = packet.readBoolean();
	}

	@Override
	public void handle(NetworkContext context) {
		if (context.player instanceof ElevatorPreference) {
			((ElevatorPreference) context.player).betterwithelevators$setElevatorsEnabled(enabled);
		}
	}
}
