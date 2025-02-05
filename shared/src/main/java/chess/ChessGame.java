package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard chessBoard;
    private TeamColor teamColor;

    public ChessGame() {
        chessBoard = new ChessBoard();
        chessBoard.resetBoard();
        teamColor = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        /**
        Collection<ChessMove> pieceMoves = chessBoard.getPiece(startPosition).pieceMoves(chessBoard, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        TeamColor pieceColor = chessBoard.getPiece(startPosition).getTeamColor();

        for(ChessMove move : validMoves) {
            ChessPiece potentialMove = chessBoard.
            if (!isInCheck(pieceColor)) {

            }
        }
         */
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> allMoves = new ArrayList<>();
        ChessPosition kingPosition = null;
        for (int row = 1; row <= 8; row++) {
            for (int column = 1; column <= 8; column++) {
                ChessPiece tempPiece = chessBoard.getPiece(new ChessPosition(row, column));
                if (tempPiece != null) {
                    allMoves.addAll(tempPiece.pieceMoves(chessBoard, new ChessPosition(row, column)));
                    if (tempPiece.getTeamColor() == teamColor && tempPiece.getPieceType() == ChessPiece.PieceType.KING) {
                        kingPosition = new ChessPosition(row, column);
                    }
                }
            }
        }
        for (ChessMove move : allMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        Collection<ChessMove> allMoves = new ArrayList<>();
        ChessPosition kingPosition = null;
        Collection<ChessMove> kingMoves = new ArrayList<>();
        for (int row = 1; row <= 8; row++) {
            for (int column = 1; column <= 8; column++) {
                ChessPiece tempPiece = chessBoard.getPiece(new ChessPosition(row, column));
                if (tempPiece != null) {
                    allMoves.addAll(tempPiece.pieceMoves(chessBoard, new ChessPosition(row, column)));
                    if (tempPiece.getTeamColor() == teamColor && tempPiece.getPieceType() == ChessPiece.PieceType.KING) {
                        kingPosition = new ChessPosition(row, column);
                        kingMoves.addAll(tempPiece.pieceMoves(chessBoard, new ChessPosition(row, column)));
                    }
                }
            }
        }
        for (ChessMove move : kingMoves) {
            if (!allMoves.contains(move)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(chessBoard, chessGame.chessBoard) && teamColor == chessGame.teamColor;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "chessBoard=" + chessBoard +
                ", teamColor=" + teamColor +
                '}';
    }
}
