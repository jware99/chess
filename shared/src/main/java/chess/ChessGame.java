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
    private boolean resigned = false;

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

    public boolean getResigned() {
        return resigned;
    }

    public void setResigned() {
        this.resigned = true;
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
    public Collection<ChessMove> validMoves(ChessPosition startPosition) throws InvalidMoveException {
        if (chessBoard.getPiece(startPosition) == null) {
            throw new InvalidMoveException();
        }
        Collection<ChessMove> pieceMoves = chessBoard.getPiece(startPosition).pieceMoves(chessBoard, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        TeamColor pieceColor = chessBoard.getPiece(startPosition).getTeamColor();
        ChessPiece pieceToMove = chessBoard.getPiece(startPosition);
        ChessPiece pieceToCapture = null;
        boolean pieceCaptured = false;

        for(ChessMove move : pieceMoves) {
            if(chessBoard.getPiece(move.getEndPosition()) != null) {
                ChessPiece pieceAtEnd = chessBoard.getPiece(move.getEndPosition());
                pieceToCapture = new ChessPiece(pieceAtEnd.getTeamColor(), pieceAtEnd.getPieceType());
                pieceCaptured = true;
            }
            //make move
            chessBoard.addPiece(move.getEndPosition(), pieceToMove);
            chessBoard.addPiece(move.getStartPosition(), null);
            if (!isInCheck(pieceColor)) {
                validMoves.add(move);
            }
            //undo move
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
        if (pieceToMove == null || pieceToMove.getTeamColor() != teamColor) {
            throw new InvalidMoveException();
        }
        ChessPiece.PieceType pieceType = pieceToMove.getPieceType();
        if (validMoves(move.getStartPosition()) == null || !validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException();
        }
        if (pieceType == ChessPiece.PieceType.PAWN && pieceToMove.getTeamColor() == TeamColor.WHITE && move.getEndPosition().getRow() == 8) {
            chessBoard.addPiece(move.getEndPosition(), new ChessPiece(TeamColor.WHITE, move.getPromotionPiece()));
        } else if (pieceToMove.getPieceType() == ChessPiece.PieceType.PAWN
                && pieceToMove.getTeamColor() == TeamColor.BLACK
                && move.getEndPosition().getRow() == 1) {
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
        // Check if the king is in check - if not, it can't be checkmate
        if (!isInCheck(teamColor)) {
            return false;
        }

        ChessPosition kingPosition = null;

        // Find the king position
        for (int row = 1; row <= 8; row++) {
            for (int column = 1; column <= 8; column++) {
                ChessPosition position = new ChessPosition(row, column);
                ChessPiece piece = chessBoard.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor &&
                        piece.getPieceType() == ChessPiece.PieceType.KING) {
                    kingPosition = position;
                    break;
                }
            }
            if (kingPosition != null) break;
        }

        // Check if any legal move by the threatened team can get out of check
        for (int row = 1; row <= 8; row++) {
            for (int column = 1; column <= 8; column++) {
                ChessPosition position = new ChessPosition(row, column);
                ChessPiece piece = chessBoard.getPiece(position);

                // If this is a piece of the team in check
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> pieceMoves = piece.pieceMoves(chessBoard, position);

                    // Try each move to see if it gets out of check
                    for (ChessMove move : pieceMoves) {
                        // Make the move temporarily
                        ChessPiece pieceToMove = chessBoard.getPiece(move.getStartPosition());
                        ChessPiece capturedPiece = chessBoard.getPiece(move.getEndPosition());

                        // If it's a promotion move, we need to handle it differently
                        ChessPiece promotedPiece = null;
                        if (move.getPromotionPiece() != null) {
                            promotedPiece = new ChessPiece(teamColor, move.getPromotionPiece());
                            chessBoard.addPiece(move.getEndPosition(), promotedPiece);
                        } else {
                            chessBoard.addPiece(move.getEndPosition(), pieceToMove);
                        }
                        chessBoard.addPiece(move.getStartPosition(), null);

                        // Check if this move gets out of check
                        boolean stillInCheck = isInCheck(teamColor);

                        // Undo the move
                        chessBoard.addPiece(move.getStartPosition(), pieceToMove);
                        chessBoard.addPiece(move.getEndPosition(), capturedPiece);

                        // If any move gets out of check, it's not checkmate
                        if (!stillInCheck) {
                            return false;
                        }
                    }
                }
            }
        }

        // If no move can get the king out of check, it's checkmate
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
        //find moves of all pieces
        for (int row = 1; row <= 8; row++) {
            for (int column = 1; column <= 8; column++) {
                ChessPiece tempPiece = chessBoard.getPiece(new ChessPosition(row, column));
                if (tempPiece != null) {
                    if (tempPiece.getTeamColor() != teamColor) {
                        opponentMoves.addAll(tempPiece.pieceMoves(chessBoard, new ChessPosition(row, column)));
                    } else if (tempPiece.getTeamColor() == teamColor && tempPiece.getPieceType() == ChessPiece.PieceType.KING) {
                            kingMoves.addAll(tempPiece.pieceMoves(chessBoard, new ChessPosition(row, column)));
                    } else {
                        return false;
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
