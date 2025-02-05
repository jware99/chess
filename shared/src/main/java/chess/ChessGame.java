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
        Collection<ChessMove> pieceMoves = chessBoard.getPiece(startPosition).pieceMoves(chessBoard, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        TeamColor pieceColor = chessBoard.getPiece(startPosition).getTeamColor();
        ChessPiece pieceToMove = chessBoard.getPiece(startPosition);
        ChessPiece pieceToCapture = null;
        boolean pieceCaptured = false;

        for(ChessMove move : pieceMoves) {
            if(chessBoard.getPiece(move.getEndPosition()) != null) {
                pieceToCapture = new ChessPiece(chessBoard.getPiece(move.getEndPosition()).getTeamColor(), chessBoard.getPiece(move.getEndPosition()).getPieceType());
                pieceCaptured = true;
            }
            chessBoard.addPiece(move.getEndPosition(), pieceToMove);
            chessBoard.addPiece(move.getStartPosition(), null);
            if (!isInCheck(pieceColor)) {
                validMoves.add(move);
            }
            chessBoard.addPiece(move.getStartPosition(), pieceToMove);
            if (pieceCaptured) {
                chessBoard.addPiece(move.getEndPosition(), pieceToCapture);
                pieceCaptured = false;
            } else {
                chessBoard.addPiece(move.getEndPosition(), null);
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece pieceToMove = chessBoard.getPiece(move.getStartPosition());
        if (pieceToMove == null || pieceToMove.getTeamColor() != teamColor || !validMoves(move.getStartPosition()).contains(move) || validMoves(move.getStartPosition()) == null) {
            throw new InvalidMoveException();
        }
        if (pieceToMove.getPieceType() == ChessPiece.PieceType.PAWN && pieceToMove.getTeamColor() == TeamColor.WHITE && move.getEndPosition().getRow() == 8) {
            chessBoard.addPiece(move.getEndPosition(), new ChessPiece(TeamColor.WHITE, move.getPromotionPiece()));
        } else if (pieceToMove.getPieceType() == ChessPiece.PieceType.PAWN && pieceToMove.getTeamColor() == TeamColor.BLACK && move.getEndPosition().getRow() == 1) {
            chessBoard.addPiece(move.getEndPosition(), new ChessPiece(TeamColor.BLACK, move.getPromotionPiece()));
        } else {
            chessBoard.addPiece(move.getEndPosition(), pieceToMove);
        }
        chessBoard.addPiece(move.getStartPosition(), null);

        if (teamColor == TeamColor.WHITE) {
            teamColor = TeamColor.BLACK;
        } else {
            teamColor = TeamColor.WHITE;
        }
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
        //find moves of all pieces
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
        //check to king check
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
        //check for king in check
        if (!isInCheck(teamColor)) {
            return false;
        }
        Collection<ChessMove> allMoves = new ArrayList<>();
        Collection<ChessMove> kingMoves = new ArrayList<>();
        ChessPosition threateningMove = null;
        ChessPosition kingPosition = null;
        //find moves of all pieces
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
        //check to see if king can move
        for (ChessMove move : kingMoves) {
            if (!allMoves.contains(move)) {
                return false;
            }
        }
        //find piece in check
        for (ChessMove move : allMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                threateningMove = move.getStartPosition();
            }
        }
        //check to see if piece can be attacked
        for (ChessMove move : allMoves) {
            if (move.getEndPosition().equals(threateningMove)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        Collection<ChessMove> opponentMoves = new ArrayList<>();
        Collection<ChessMove> kingMoves = new ArrayList<>();
        ChessPosition threateningMove = null;
        ChessPosition kingPosition = null;
        //find moves of all pieces
        for (int row = 1; row <= 8; row++) {
            for (int column = 1; column <= 8; column++) {
                ChessPiece tempPiece = chessBoard.getPiece(new ChessPosition(row, column));
                if (tempPiece != null) {
                    if (tempPiece.getTeamColor() != teamColor) {
                        opponentMoves.addAll(tempPiece.pieceMoves(chessBoard, new ChessPosition(row, column)));
                    } else if (tempPiece.getTeamColor() == teamColor && tempPiece.getPieceType() == ChessPiece.PieceType.KING) {
                            kingPosition = new ChessPosition(row, column);
                            kingMoves.addAll(tempPiece.pieceMoves(chessBoard, new ChessPosition(row, column)));
                    }
                }
            }
        }
        //check possible king moves
        kingMoves.removeIf(move -> opponentMoves.stream()
                .anyMatch(oppMove -> oppMove.getEndPosition().equals(move.getEndPosition())));

        return kingMoves.isEmpty();
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
