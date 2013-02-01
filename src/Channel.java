import javax.swing.JButton;

public class Channel {
	int nr;
	Frame frame;
	boolean OI, IO;
	public boolean working;
	JButton bChannels;

	Channel(int nr, JButton bChannels) {
		this.nr = nr;
		this.bChannels = bChannels;
		frame = null;
		this.working = true;
	}
}
