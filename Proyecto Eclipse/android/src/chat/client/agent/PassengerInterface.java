package chat.client.agent;

/**
 * This interface implements the logic of the chat client running on the user
 * terminal.
 * 
 * @author Michele Izzo - Telecomitalia
 */

public interface PassengerInterface {
	public void askForRide(final String origin, final String destiny,
            final String arrivalTime);
}