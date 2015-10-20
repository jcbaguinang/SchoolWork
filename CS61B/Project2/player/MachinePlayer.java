/* MachinePlayer.java */

package player;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {
  private int player;
  private int otherPlayer;
  private int depth;
  private GameBoard gameBoard;
  public final static int ADD = 1;
  public final static int STEP = 2;

  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
    player = color;
    depth = 2;
    if (player == 0) {
      otherPlayer = 1;
    }
    else if (player == 1) {
      otherPlayer = 0;
    }
    gameBoard = new GameBoard();

  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
    player = color;
    if (player == 0) {
      otherPlayer = 1;
    }
    else if (player == 1) {
      otherPlayer = 0;
    }
    depth = searchDepth;
    gameBoard = new GameBoard();
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
    Best result;
    if (gameBoard.numPieces() < 20) {
      result = gameBoard.gameTreeSearch(depth, player, -10.0, 10.0, player, otherPlayer);
    }
    else {
      result = gameBoard.gameTreeSearch(2, player, -10.0, 10.0, player, otherPlayer);
    }
    if (result.move.moveKind == STEP) {
      gameBoard.movePiece(result.move, player);
    }
    else {
      gameBoard.addPiece(result.move, player);
    }
    return result.move;
  } 

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
    if (!(gameBoard.isValidMove(m, otherPlayer))) {
      return false;
    }
    if (m.moveKind == STEP) {
      gameBoard.movePiece(m, otherPlayer);
    }
    else {
      gameBoard.addPiece(m, otherPlayer);
    }
    return true;
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    if (!(gameBoard.isValidMove(m, player))) {
      return false;
    }
    if (m.moveKind == STEP) {
      gameBoard.movePiece(m, player);
    }
    else {
      gameBoard.addPiece(m, player);
    }
    return true;
  }
}
