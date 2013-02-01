public class Monitor {
	// Valid Frame Timer, czas, po jakim zostaje wystawiony nowy token (ramka z
	// tokenem nie odwiedzi³a monitora)
	int VFT = 0;
	boolean vftError = false;

	// wyslanie po jakims czasie inforamacji o poprawnym dzialaniu
	int AMT = 0;
	boolean amtSend = true;

	public Monitor() {

	}

	public Frame newToken() {
		return new Frame(true);
	}

	public boolean isVisited(Frame frame) {
		boolean visited = false;

		return visited;
	}

	public boolean vftControll(int value) {
		boolean isOk = true;

		VFT += 1;
		if (VFT >= value) {
			VFT = 0;
			isOk = false;
		}

		return isOk;
	}
}
