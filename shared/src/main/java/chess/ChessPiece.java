package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int i = 1;
        while (myPosition.getRow() + i <= 8 && myPosition.getColumn() + i <= 8) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i), null));
            System.out.println((myPosition.getRow() + i) + " " + (myPosition.getColumn() + i));
            i++;
        }
        i = 1;
        int j = 1;
        while (myPosition.getRow() + i <= 8 && myPosition.getColumn() - j >= 1) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - j), null));
            System.out.println((myPosition.getRow() + i) + " " + (myPosition.getColumn() - j));
            i++;
            j++;
        }
        i = 1;
        j = 1;
        while (myPosition.getRow() - i >= 1 && myPosition.getColumn() + j  <= 8) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + j), null));
            System.out.println((myPosition.getRow() - i) + " " + (myPosition.getColumn() + j));
            i++;
            j++;
        }
        i = 1;
        j = 1;
        while (myPosition.getRow() - i >= 1 && myPosition.getColumn() - j  >= 1) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - j), null));
            System.out.println((myPosition.getRow() - i) + " " + (myPosition.getColumn() - j));
            i++;
            j++;
        }

        return moves;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessPiece) {
            return this.type == ((ChessPiece) obj).type;
        }
        return false;
    }

}

