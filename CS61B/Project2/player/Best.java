package player;
public class Best {
	public double score;
	public Move move;
	//Constructor for best object.
	//@param Move m: the Best move.
	//@param double score: the score associated with the move.
	public Best(Move m, double score) {
		this.move = m;
		this.score = score;
	}
	//Another constructor for best (default score -10, move null).
	public Best() {
		this.score = -10;
		move = null;
	}
}