public class Frame {
	enum State {
		E_BUSY, E_FREE, E_BUSY_RETURN
	}

	State state;
	boolean token, monitor, busy;
	int data[];
	int from, to;
	int direction;
	public boolean badNode = false;
	int whereBadNode=-1;
	boolean visited=false;

	Frame(boolean token) {
		this.state = State.E_FREE;
		this.token = token;
		this.busy = false;
		this.from = -1;
		this.to = -1;
		this.direction = 1;
	}

	Frame(boolean token, int from, int to) {
		this.token = token;
		this.from = from;
		this.to = to;
	}

}
