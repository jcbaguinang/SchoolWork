package player;
public class GameBoard {

    private int[][] gameBoard;
    private Coord[][] coordList = new Coord[8][8];
	private int numPiecesP1 = 0;
	private int numPiecesP2 = 0;
	private int numPieces;
	public final static int PLAYER1 = 0;
	public final static int PLAYER2 = 1;
	public final static int ADD = 1;
  	public final static int STEP = 2;
  	public final static int EMPTY = -1;
  	final static int UP = 1;
	final static int DOWN = -1;
	final static int LEFT = -2;
	final static int RIGHT = 2;
	final static int UPLEFT = -3;
	final static int DOWNRIGHT = 3;
	final static int UPRIGHT = 4;
	final static int DOWNLEFT = -4;
	
	// This is the constructor for a Gameboard object that stores the pieces in an 8x8 gameboard.
	public GameBoard() {
		gameBoard = new int[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				gameBoard[i][j] = EMPTY;
			}
		}
		for (int x = 0; x < 8; x++){
			for (int y = 0; y < 8; y++){
				coordList[x][y] = new Coord(x, y);
			}
		}
	}
	//Adds a piece of the player's into the gameboard.
	//@param Move m: the move that the player wants to execute.
	//@param int player: the player executing the move.
	public void addPiece(Move m, int player) {
		gameBoard[m.x1][m.y1] = player;
		if (player == PLAYER1) {
			numPiecesP1++;
		}
		else {
			numPiecesP2++;
		}
		numPieces++;

	}
	//Moves a piece of the player's from one location to another.
	//@param Move m: the move that the player wants to execute.
	//@param int player: the player executing the move.
	public void movePiece(Move m, int player) {
		gameBoard[m.x1][m.y1] = player;
		gameBoard[m.x2][m.y2] = EMPTY;

	}
	//returns of the number of pieces of the board.
	//@return: the number of pieces.
	public int numPieces(){
		return this.numPieces;
	}
	//toString method for gameboard.
	//@return String: string representation of board.
	public String toString() {
		String s = "|";
		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				if (gameBoard[i][j] == EMPTY) {
					s += "   |";
				}
				else if (gameBoard[i][j] == PLAYER1) {
					s += " B |";
				}
				else if (gameBoard[i][j] == PLAYER2) {
					s += " W |";
				}
			}
			s+= "\n|";
		}
		return s;
	}
	//checks if a given move is valid for the player. Specifics of whether a move is valid is on the readme.
	//@param Move m: the Move isValidMove is checking.
	//@param int player: the player that wishes to perform this move.
	//@return boolean: returns true if the move is valid for the player, false otherwise.
	public boolean isValidMove(Move m, int player) {
		if (numPieces < 20 && m.moveKind == STEP) {
			return false;
		}
		else if (m.moveKind == STEP && m.x1 == m.x2 && m.y1 == m.y2) {
			return false;
		}
		else if (numPieces >= 20 && m.moveKind == ADD) {
			return false;
		}
		else if (gameBoard[m.x1][m.y1] != EMPTY) {
			return false;
		}
		else if ((m.x1 == 0 && m.y1 == 0) || (m.x1 == 0 && m.y1 == 7) || (m.x1 == 7 && m.y1 == 7) || (m.x1 == 7 && m.y1 == 0)) {
			return false;
		}
		else if (player == PLAYER1 && (m.x1 == 0 || m.x1 == 7)) {
			return false;
		}
		else if (player == PLAYER2 && (m.y1 == 0 || m.y1 == 7)) {
			return false;
		}
		gameBoard[m.x2][m.y2] = -1;
		if (Cluster(m.x1, m.y1, player)) {
			if (m.moveKind == STEP) {
				gameBoard[m.x2][m.y2] = player;
			}
			return false;
		}
		else {
			if (m.moveKind == STEP) {
				gameBoard[m.x2][m.y2] = player;
			}
		}
		return true;
	}
	//Helper method for isvalidmove. The cluster method checks if there is a cluster of three or more of the same player's pieces next to each other (directly next or diagonally).
	//@param int x: x coordinate it is checking for clusters at.
	//@param int y: y coordinate it is checking for clusters at.
	//@param int player: the player that Cluster is looking for pieces of.
	//@return boolean: returns true if there is a cluster of 3 or more at that location, false otherwise.
	private boolean Cluster(int x, int y, int player) {
		int neighbors = countNeighbor(x, y, player);
		if (neighbors >= 2) {
			return true;
		}
		else if (neighbors == 0) {
			return false;
		}
		else {
			Coord neighbor = getNeighbor(x, y, player);
			if (countNeighbor(neighbor.x(), neighbor.y(), player) >= 1) {
				return true;
			}
		}
		return false;
	}
	//counts the number of neighboring pieces for the given player.
	//@param int x: x coordinate it is checking for neighbors at.
	//@param int y: y coordinate it is checking for neighbors at.
	//@param int player: the player that countNeighbor is looking for pieces at.
	//@return int: the number of total neighbors (excluding itself).
	private int countNeighbor(int x, int y, int player) {
		int count = 0;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i < 0 || j < 0 || i > 7 || j > 7 || (i == 0 && j == 0) || (i == 0 && j == 7) || (i == 7 && j == 7) || (i == 7 && j == 0)) {
					continue;
				}
				else if (i == x && j == y) {
					continue;
				}
				else if(gameBoard[i][j] == player) {
					count++;
				}
			}
		}
		return count;
	}
	//returns a neighboring piece for the given player (null if no neighboring piece).
	//@param int x: x coordinate it is checking for neighbors at.
	//@param int y: y coordinate it is checking for neighbors at.
	//@param int player: the player that countNeighbor is looking for pieces at.
	//@return Coord: returns a coordinate with the locations of the neighbor.
	private Coord getNeighbor(int x, int y, int player) {
		int count = 0;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i < 0 || j < 0 || i > 7 || j > 7 || (i == 0 && j == 0) || (i == 0 && j == 7) || (i == 7 && j == 7) || (i == 7 && j == 0)) {
					continue;
				}
				else if (i == x && j == y) {
					continue;
				}
				else if(gameBoard[i][j] == player) {
					return new Coord(i, j);
				}
			}
		}
		return null;
	}
	// the gametreesearch function returns what gametreeSearch concludes is the best move and score for a player's turn.
	//@param int depth: the depth at which gametreeSearch should search to.
	//@param int player: the player it is searching for a move for.
	//@param double alpha: the minimum score for the gametreeSearch. Used in alpha-beta pruning.
	//@param double beta: the maximum score for the gametreeSearch. Used in alpha-beta pruning.
	//@param int machinePlayer: the color (number) of the machine player.
	//@param int opponent: the color (number) of the opponent.
	//@return Best: returns a Best object that contains a Move and a score gametreeSearch has decided to be optimal.
	public Best gameTreeSearch(int depth, int player, double alpha, double beta, int machinePlayer, int opponent) {
		Best myBest = new Best();
		Best reply;
		if (numPieces < 2 && isValidMove(new Move(4, 4), player)) {
			return new Best(new Move(4, 4), 6);
		}
		if (player == opponent) {
			if (hasNetwork(machinePlayer)) {
				return new Best(null, evalWin(player));
			}
			else if (depth == 0) {
				return new Best(null, evalWin(machinePlayer));
			}
		}
		else if (player == machinePlayer) {
			if (hasNetwork(opponent)) {
				return new Best(null, -10.0);
			}
			else if (depth == 0) {
				return new Best(null, evalWin(machinePlayer));
			}
		}
		if (depth == 0) {
			return new Best(null, this.evalWin(machinePlayer));
		}
		else if (player == machinePlayer) {
			myBest.score = alpha;
		}
		else if (player == opponent) {
			myBest.score = beta;
		}
		if (this.numPieces < 20) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					Move moveOption = new Move(i, j);
					if (isValidMove(moveOption, player)) {
						addPiece(moveOption, player);
						if (player == opponent) {
							reply = gameTreeSearch(depth - 1, machinePlayer, alpha, beta, machinePlayer, opponent);
						}
						else {
							reply = gameTreeSearch(depth - 1, opponent, alpha, beta, machinePlayer, opponent);
						}
						reply.score = .99 * reply.score;
						undo(moveOption, player);
						if (player == machinePlayer && reply.score > myBest.score) {
							myBest.move = moveOption;
							myBest.score = reply.score;
							alpha = reply.score;
						}
						else if (player == opponent && reply.score < myBest.score) {
							myBest.move = moveOption;
							myBest.score = reply.score;
							beta = reply.score;
						}
					}
					if (alpha >= beta) {
						return myBest;
					}
				}
			}
			return myBest;
		}
		Coord[] pieceLocation = pieceLocations(player);
		for (int i = 0; i < pieceLocation.length; i++) {
			for (int l = 0; l < 8; l++) {
				for (int m = 0; m < 8; m++) {
					Move moveOption = new Move(l, m, pieceLocation[i].x(), pieceLocation[i].y());
					if (isValidMove(moveOption, player)) {
						movePiece(moveOption, player);
						if (player == opponent) {
							reply = gameTreeSearch(depth - 1, machinePlayer, alpha, beta, machinePlayer, opponent);
						}
						else {
							reply = gameTreeSearch(depth - 1, opponent, alpha, beta, machinePlayer, opponent);
						}
						reply.score = .99 * reply.score;
						undo(moveOption, player);
						if (player == machinePlayer && reply.score > myBest.score) {
							myBest.move = moveOption;
							myBest.score = reply.score;
							alpha = reply.score;
						}
						else if (player == opponent && reply.score < myBest.score) {
							myBest.move = moveOption;
							myBest.score = reply.score;
							beta = reply.score;
						}
					}
					if (alpha >= beta) {
						return myBest;
					}
				}
			}
		}
		return myBest;
	}
	//undos a move that was previously done to the gameboard.
	//@param Move m: the move that should be undone.
	//@param int player: the player the move will be undone for.
	private void undo(Move m, int player) {
		if (m.moveKind == ADD) {
			gameBoard[m.x1][m.y1] = EMPTY;
			if (player == PLAYER1) {
				numPiecesP1--;
			}
			else {
				numPiecesP2--;
			}
			numPieces--;
		}
		else if (m.moveKind == STEP) {
			gameBoard[m.x1][m.y1] = EMPTY;
			gameBoard[m.x2][m.y2] = player;
		}
	}
	// returns a list of coordinate objects that the player has a piece on. Does not work for player = -1).
	//@param int player: returns a list of the player's pieces.
	//@return Coord[]: returns a list of coordinate objects, each one being a location for a player's piece.
	private Coord[] pieceLocations(int player) {
		Coord[] coords;
		if (player == PLAYER1) {
			coords = new Coord[numPiecesP1];
		}
		else {
			coords = new Coord[numPiecesP2];
		}
		int counter = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (gameBoard[i][j] == player) {
					coords[counter] = new Coord(i, j);
					counter++;
				}
			}
		}
		return coords;
	}

	//returns a score for the current board from the perspective of player
	//@param player: an int representing the player
	//@return: a double from -10 to 10 representing a score for the current board
	public double evalWin(int player){
		double score = 0;
		double connections1 = 0;
		double connections2 = 0;
		int goal1 = findGoal1(player);
		int goal2 = findGoal2(player);
		int goals = goal1 + goal2;
		if (hasNetwork(player)) {
			return 10.0;
		}
		for (int x = 0; x < 8; x++){
			for (int y = 0; y < 8; y++){
				if ((x == 0 && y == 0) || (x == 0 && y == 7) || (x == 7 && y == 0) && (x == 7 && y == 7)){
					continue;
				}
				else if (gameBoard[x][y] == player){
					connections1 += numConnect(new Coord(x, y), player);
				}
				else if (gameBoard[x][y] != EMPTY){
					int other = -1;
					if (player == PLAYER1){
						other = PLAYER2;
					}
					else{
						other = PLAYER1;
					}
					connections2 += numConnect(new Coord(x, y), other);
				}
			}
		}
		score =  6 * (connections1 - connections2)/(connections1 + connections2 + 1);
		if (goal1 < 1){
			score -= 2;
		}
		else if (goal1 > 2){
			score -= 2;
		}
		if (goal2 < 1){
			score -= 2;
		}
		else if (goal2 > 2){
			score -= 2;
		}
		return score;
		
	}
	
	//returns the number of connections for the player from a given coordinate
	//@param coord: a Coord object from which connections are found
	//@param player: an int that represents the current player
	//@return: a double representing the number of connections that can be made from the given Coord
	private double numConnect(Coord coord, int player){
		double sum = 0;
		if (findUp(coord.x(), coord.y(), player).x() != 8){
			sum++;
		}
		if (findUpRight(coord.y(), coord.x(), player).x() != 8){
			sum++;
		}
		if (findRight(coord.y(), coord.x(), player).x() != 8){
			sum++;
		}
		if (findDownRight(coord.y(), coord.x(), player).x() != 8){
			sum++;
		}
		if (findDown(coord.x(), coord.y(), player).x() != 8){
			sum++;
		}
		if (findDownLeft(coord.y(), coord.x(), player).x() != 8){
			sum++;
		}
		if (findLeft(coord.y(), coord.x(), player).x() != 8){
			sum++;
		}
		if (findUpLeft(coord.y(), coord.x(), player).x() != 8){
			sum++;
		}
		return sum;
	}
	
	//returns the number of pieces on the top or left goal areas depending on the player
	//@param player: an int representing the current player
	//@return: an int representing the number of pieces on the top or left goal areas
	private int findGoal1(int player){
		int goal1 = 0;
		if (player == PLAYER1){
			for (int x = 0; x < 7; x++){
				if (gameBoard[x][0] == player){
					goal1++;
				}
			}
		}
		else{
			for (int y = 0; y < 7; y++){
				if (gameBoard[0][y] == player){
					goal1++;
				}
			}
		}
		return goal1;
	}
	
	//returns the number of pieces in the bottom or right goal areas depending on the player
	//@param player: an int representing the current player
	//@return: an int representing the number of pieces in the goal area
	private int findGoal2(int player){
		int goal2 = 0;
		if (player == PLAYER1){
			for (int x = 0; x < 7; x++){
				if (gameBoard[x][7] == player){
					goal2++;
				}
			}
		}
		else{
			for (int y = 0; y < 7; y++){
				if (gameBoard[7][y] == player){
					goal2++;
				}
			}
		}
		return goal2;
	}
	
	//returns whether a network has been established
	//@param player: an int representing the player which will be evaluated for a network
	//@return: a boolean representing whether the player has established a successful network
	public boolean hasNetwork(int player){
		if (findFirst(0, player).x() == 8){
			return false;
		}
		Coord currentCoord = findFirst(0, player);
		if (player == PLAYER1){
			while(currentCoord.x() < 8){
				if (makeConnections(currentCoord, player, RIGHT, 1)){
					return true;
				}
				else{
					currentCoord = findFirst(currentCoord.x() + 1, player);
				}
			}
		}
		else{
			while(currentCoord.y() < 8){
				if (makeConnections(currentCoord, player, DOWN, 1)){
					return true;
				}
				else{
					currentCoord = findFirst(currentCoord.y() + 1, player);
				}
			}
		}
        return false;
	}
	
	//returns whether a network can be formed from the current coord
	//@param coord: a Coord object representing the starting point of a potential network
	//@param player: an int representing the current player
	//@param direction: an int representing the direction an incoming connection is coming from
	//@param length: an int representing how long the current "network" is
	//@return: whether a network can be formed from the current coord
	private boolean makeConnections(Coord coord, int player, int direction, int length){
		Coord[] connections = connections(coord, player);
		coordList[coord.x()][coord.y()].visit();
		if ((coord.x() == 7 || coord.y() == 7) && length >= 6){
			return true;
		}	
		for (int i = 0; i < 8; i++){
			if (connections[i] == null){
				continue;
			}
			else{
				if ((connections[i].x() == 7 || connections[i].y() == 7) && length >= 5 && i != direction){
					return true;
				}	
				if (i == 0 && direction != UP && direction != DOWN){
					boolean connected = makeConnections(connections[i], player, UP, length + 1);
					if (connected){
						return connected;
					}
				}
				if( i == 1 && direction != UPRIGHT && direction != DOWNLEFT){
					boolean connected = makeConnections(connections[i], player, UPRIGHT, length + 1);	
					if (connected){
						return connected;
					}
				}
				if (i == 2 && direction != RIGHT && direction != LEFT){
					boolean connected = makeConnections(connections[i], player, RIGHT, length + 1);
					if (connected){
						return connected;
					}
				}
				if (i == 3 && direction != DOWNRIGHT && direction != UPLEFT){
					boolean connected = makeConnections(connections[i], player, DOWNRIGHT, length + 1);	
					if (connected){
						return connected;
					}
				}
				if ( i == 4 && direction != DOWN && direction != UP){
					boolean connected = makeConnections(connections[i], player, DOWN, length + 1);	
					if (connected){
						return connected;
					}
				}
				if (i == 5 && direction != DOWNLEFT && direction != UPRIGHT){
					boolean connected = makeConnections(connections[i], player, DOWNLEFT, length + 1);	
					if (connected){
						return connected;
					}
				}
				if (i == 6 && direction != LEFT && direction != RIGHT){
					boolean connected = makeConnections(connections[i], player, LEFT, length + 1);	
					if (connected){
						return connected;
					}
				}
				if (i == 7 && direction != UPLEFT && direction != DOWNRIGHT){
					boolean connected = makeConnections(connections[i], player, UPLEFT, length + 1);	
					if (connected){
						return connected;
					}
				}
			}
		}
		coordList[coord.x()][coord.y()].unvisit();
		return false;
	}
	
	//returns the first piece in the goal area after the first piece
	//@param first: the x-coordinate or y-coordinate of the goal area to search after depending on the player
	//@param player: the current player
	//@return: a Coord representing the first piece in the goal area found after the given x or y coordinate or the nonsense Coord (8,8) if none exists
	private Coord findFirst(int first, int player){
		if (player == PLAYER1){
			for (int x = first + 1; x < 7; x++){
				if (gameBoard[x][0] == player){
					return coordList[x][0];
				}
			}
		}
		else{
			for (int y = first + 1; y < 7; y++){
				if (gameBoard[0][y] == player){
					return coordList[0][y];
				}
			}
		}
		
		return new Coord(8, 8);
	}
	
	//finds the first player piece to the left of the current piece
	//@param row: the current row (y-value) of the Coord to look left from
	//@param initial: the x-value of the Coord to look left from
	//@param player: an int representing the current player
	//@return: a Coord representing the first player piece to the left of a coordinate or the nonsense Coord (8,8) if non exists
	private Coord findLeft(int row, int initial, int player){
		if (initial == 0){
			return new Coord(8, 8);
		}
		for (int x = initial - 1; x >= 0; x--){
			if (gameBoard[x][row] == player){
				return coordList[x][row];
			}
			else if (gameBoard[x][row] != player && gameBoard[x][row] != EMPTY){
				return new Coord(8, 8);
			}
		}
		return new Coord(8, 8);
	}
	
	//finds the first player piece to the right of the current piece
	//@param row: the current row (y-value) of the Coord to look right from
	//@param initial: the x-value of the Coord to look right from
	//@param player: an int representing the current player
	//@return: a Coord representing the first player piece to the right of a coordinate or the nonsense Coord (8,8) if non exists
	private Coord findRight(int row, int initial, int player){
		if (initial == 7){
			return new Coord(8, 8);
		}
		for (int x = initial + 1; x < 8; x++){
			if (gameBoard[x][row] == player){
				return coordList[x][row];
			}
			else if (gameBoard[x][row] != player && gameBoard[x][row] != EMPTY){
				return new Coord(8, 8);
			}
		}
		return new Coord(8, 8);
	}
	
	//finds the first player piece below the current piece
	//@param column: the current column (x-value) of the Coord to look down from
	//@param initial: the y-value of the Coord to look down from
	//@param player: an int representing the current player
	//@return: a Coord representing the first player piece to the bottom of a coordinate or the nonsense Coord (8,8) if non exists
	private Coord findDown(int column, int initial, int player){
		if (initial == 8){
			return new Coord(8, 8);
		}
		for (int y = initial + 1; y < 8; y++){
			if (gameBoard[column][y] == player){
				return coordList[column][y];
			}
			else if (gameBoard[column][y] != player && gameBoard[column][y] != EMPTY){
				return new Coord(8, 8);
			}
		}
		return new Coord(8, 8);
	}
	
	//finds the first player piece above the current piece
	//@param column: the current column (x-value) of the Coord to look up from
	//@param initial: the y-value of the Coord to look up from
	//@param player: an int representing the current player
	//@return: a Coord representing the first player piece to the top of a coordinate or the nonsense Coord (8,8) if non exists
	private Coord findUp(int column, int initial, int player){
		if (initial == 0){
			return new Coord(8, 8);
		}
		for (int y = initial - 1; y >= 0; y--){
			if (gameBoard[column][y] == player){
				return coordList[column][y];
			}
			else if (gameBoard[column][y] != player && gameBoard[column][y] != EMPTY){
				return new Coord(8, 8);
			}
		}
		return new Coord(8, 8);
	}
	
	//finds the first player piece above and left of the current piece
	//@param y1: the current y-value of the Coord to look up and left from
	//@param x1: the x-value of the Coord to look up and left from
	//@param player: an int representing the current player
	//@return: a Coord representing the first player piece to the top-left of a coordinate or the nonsense Coord (8,8) if non exists
	private Coord findUpLeft(int y1, int x1, int player){
		if (x1 == 0 || y1 == 0){
			return new Coord(8, 8);
		}
		int x = x1 - 1;
		int y = y1 - 1;
		while (x >= 0 && y >=0){
			if (gameBoard[x][y] == player){
				return coordList[x][y];
			}
			else if (gameBoard[x][y] != player && gameBoard[x][y] != EMPTY){
				return new Coord(8, 8);
			}
			x--;
			y--;
		}
		return new Coord(8, 8);
	}
	
	//finds the first player piece above and right of the current piece
	//@param y1: the current y-value of the Coord to look up and right from
	//@param x1: the x-value of the Coord to look up and rightfrom
	//@param player: an int representing the current player
	//@return: a Coord representing the first player piece to the top-right of a coordinate or the nonsense Coord (8,8) if non exists
	private Coord findUpRight(int y1, int x1, int player){
		if (x1 == 7 || y1 == 0){
			return new Coord(8, 8);
		}
		int x = x1 + 1;
		int y = y1 - 1;
		while (x < 8 && y >=0){
			if (gameBoard[x][y] == player){
				return coordList[x][y];
			}
			else if (gameBoard[x][y] != player && gameBoard[x][y] != EMPTY){
				return new Coord(8, 8);
			}
			x++;
			y--;
		}
		return new Coord(8, 8);
	}
	
	//finds the first player piece down and left of the current piece
	//@param y1: the current y-value of the Coord to look down and left from
	//@param x1: the x-value of the Coord to look down and left from
	//@param player: an int representing the current player
	//@return: a Coord representing the first player piece to the bottom-left of a coordinate or the nonsense Coord (8,8) if non exists
	private Coord findDownLeft(int y1, int x1, int player){
		if (x1 == 0 || y1 == 7){
			return new Coord(8, 8);
		}
		int x = x1 - 1;
		int y = y1 + 1;
		while (x >= 0 && y < 8){
			if (gameBoard[x][y] == player){
				return coordList[x][y];
			}
			else if (gameBoard[x][y] != player && gameBoard[x][y] != EMPTY){
				return new Coord(8, 8);
			}
			x--;
			y++;
		}
		return new Coord(8, 8);
	}
	
	//finds the first player piece down and right of the current piece
	//@param y1: the current y-value of the Coord to look down and right from
	//@param x1: the x-value of the Coord to look down and right from
	//@param player: an int representing the current player
	//@return: a Coord representing the first player piece to the bottom-right of a coordinate or the nonsense Coord (8,8) if non exists
	private Coord findDownRight(int y1, int x1, int player){
		if (x1 == 7 || y1 == 7){
			return new Coord(8, 8);
		}
		int x = x1 + 1;
		int y = y1 + 1;
		while (x < 8 && y < 8){
			if (gameBoard[x][y] == player){
				return coordList[x][y];
			}
			else if (gameBoard[x][y] != player && gameBoard[x][y] != EMPTY){
				return new Coord(8, 8);
			}
			x++;
			y++;
		}
		return new Coord(8, 8);
	}
	
	//returns an array of the connections that can be made from the current coordinate
	//@param coord: a Coord representing the current coordinates
	//@param player: an int representing the current player
	//@return: an array of Coords of size 8 with the connections that can be formed from the current Coord, or null if they do not exist
	private Coord[] connections(Coord coord, int player){
		Coord[] coords = new Coord[8];
		if (findUp(coord.x(), coord.y(), player).x() != 8 && findUp(coord.x(),coord.y(), player).visited() == false){
			Coord next = findUp(coord.x(), coord.y(), player);
			coords[0] = next;
		}
		else{
			coords[0] = null;
		}
		if (findUpRight(coord.y(), coord.x(), player).x() != 8 && findUpRight(coord.y(),coord.x(), player).visited() == false){
			Coord next = findUpRight(coord.y(), coord.x(), player);
			coords[1] = next;
		}
		else{
			coords[1] = null;
		}		
		if (findRight(coord.y(), coord.x(), player).x() != 8 && findRight(coord.y(),coord.x(), player).visited() == false){
			Coord next = findRight(coord.y(), coord.x(), player);
			coords[2] = next;
		}
		else{
			coords[2] = null;
		}
		if (findDownRight(coord.y(), coord.x(), player).x() != 8 && findDownRight(coord.y(),coord.x(), player).visited() == false){
			Coord next = findDownRight(coord.y(), coord.x(), player);
			coords[3] = next;
		}
		else{
			coords[3] = null;
		}
		if (findDown(coord.x(), coord.y(), player).x() != 8 && findDown(coord.x(),coord.y(), player).visited() == false){
			Coord next = findDown(coord.x(), coord.y(), player);
			coords[4] = next;
		}
		else{
			coords[4] = null;
		}
		if (findDownLeft(coord.y(), coord.x(), player).x() != 8 && findDownLeft(coord.y(),coord.x(), player).visited() == false){
			Coord next = findDownLeft(coord.y(), coord.x(), player);
			coords[5] = next;
		}
		else{
			coords[5] = null;
		}
		if (findLeft(coord.y(), coord.x(), player).x() != 8 && findLeft(coord.y(), coord.x(), player).visited() == false){
			Coord next = findLeft(coord.y(), coord.x(), player);
			coords[6] = next;
		}
		else{
			coords[6] = null;
		}
		if (findUpLeft(coord.y(), coord.x(), player).x() != 8 && findUpLeft(coord.y(),coord.x(), player).visited() == false){
			Coord next = findUpLeft(coord.y(), coord.x(), player);
			coords[7] = next;
		}
		else{
			coords[7] = null;
		}
		return coords;
	}
		public static void main(String[] args) {
		GameBoard gb5 = new GameBoard();
		gb5.addPiece(new Move(0, 1), 1);
		gb5.addPiece(new Move(1, 0), 0);
		gb5.addPiece(new Move(2, 2), 1);
		gb5.addPiece(new Move(2, 3), 1);
		gb5.addPiece(new Move(3, 4), 0);
		gb5.addPiece(new Move(6, 4), 0);
		gb5.addPiece(new Move(7, 4), 1);
		gb5.addPiece(new Move(6, 1), 1);
		gb5.addPiece(new Move(6, 7), 0);
		gb5.addPiece(new Move(1, 6), 0);
		Best b1 = gb5.gameTreeSearch(3, 1, -10, 10, 1, 0);
		System.out.println(gb5);
		System.out.println(b1.move.x1 + " " + b1.move.y1);
		gb5.addPiece(new Move(4, 1), 0);
		System.out.println(gb5.evalWin(0));
		gb5.undo(new Move(4, 1), 0);
		gb5.addPiece(new Move(1, 5), 0);
		System.out.println(gb5.evalWin(0));

	}
}