import Main.ControlDesk;
import Views.ControlDeskView;

public class drive {

	public static void main(String[] args) {

		final int NUM_LANES = 3;
		final int MAX_PATRONS_PER_ALLEY = 5;

		// Create a control desk and run the GUI
		ControlDesk controlDesk = new ControlDesk(NUM_LANES);
		ControlDeskView cdv = new ControlDeskView( controlDesk, MAX_PATRONS_PER_ALLEY);
		controlDesk.addObserver(cdv);
	}
}