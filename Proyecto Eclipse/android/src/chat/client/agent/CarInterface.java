package chat.client.agent;

import chat.client.gui.CarResponse;

public interface CarInterface {
		public void addRide(final String origin, final String destiny, final String departureTime ,
                final String arrivalTime, final int freeSeats, final int price);
		public void setDelegate(CarResponse newDelegate);
}
