package chess;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myTeamColor = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn())).getTeamColor();

        // direction: {1,2}
        if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() + 2 <= 8) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2)) != null) {
                ChessGame.TeamColor otherPieceColor = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2)).getTeamColor();
                if (myTeamColor != otherPieceColor) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2), null));
                    System.out.println((myPosition.getRow() + 1) + " " + (myPosition.getColumn() + 2));
                }
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2), null));
                System.out.println((myPosition.getRow() + 1) + " " + (myPosition.getColumn() + 2));
            }
        }

        // direction: {-1,2}
        if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn() + 2 <= 8) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2)) != null) {
                ChessGame.TeamColor otherPieceColor = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2)).getTeamColor();
                if (myTeamColor != otherPieceColor) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2), null));
                    System.out.println((myPosition.getRow() - 1) + " " + (myPosition.getColumn() + 2));
                }
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2), null));
                System.out.println((myPosition.getRow() - 1) + " " + (myPosition.getColumn() + 2));
            }
        }

        // direction: {2,1}
        if (myPosition.getRow() + 2 <= 8 && myPosition.getColumn() + 1 <= 8) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1)) != null) {
                ChessGame.TeamColor otherPieceColor = board.getPiece(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1)).getTeamColor();
                if (myTeamColor != otherPieceColor) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1), null));
                    System.out.println((myPosition.getRow() + 2) + " " + (myPosition.getColumn() + 1));
                }
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1), null));
                System.out.println((myPosition.getRow() + 2) + " " + (myPosition.getColumn() + 1));
            }
        }

        // direction: {2,-1}
        if (myPosition.getRow() + 2 <= 8 && myPosition.getColumn() - 1 >= 1) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1)) != null) {
                ChessGame.TeamColor otherPieceColor = board.getPiece(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1)).getTeamColor();
                if (myTeamColor != otherPieceColor) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1), null));
                    System.out.println((myPosition.getRow() + 2) + " " + (myPosition.getColumn() - 1));
                }
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1), null));
                System.out.println((myPosition.getRow() + 2) + " " + (myPosition.getColumn() - 1));
            }
        }

        // direction: {1,-2}
        if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() - 2 >= 1) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2)) != null) {
                ChessGame.TeamColor otherPieceColor = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2)).getTeamColor();
                if (myTeamColor != otherPieceColor) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2), null));
                    System.out.println((myPosition.getRow() + 1) + " " + (myPosition.getColumn() - 2));
                }
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2), null));
                System.out.println((myPosition.getRow() + 1) + " " + (myPosition.getColumn() - 2));
            }
        }

        // direction: {-2,-1}
        if (myPosition.getRow() - 2 >= 1 && myPosition.getColumn() - 1 >= 1) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1)) != null) {
                ChessGame.TeamColor otherPieceColor = board.getPiece(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1)).getTeamColor();
                if (myTeamColor != otherPieceColor) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1), null));
                    System.out.println((myPosition.getRow() - 2) + " " + (myPosition.getColumn() - 1));
                }
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1), null));
                System.out.println((myPosition.getRow() - 2) + " " + (myPosition.getColumn() - 1));
            }
        }

        // direction: {-2,1}
        if (myPosition.getRow() - 2 >= 1 && myPosition.getColumn() + 1 <= 8) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1)) != null) {
                ChessGame.TeamColor otherPieceColor = board.getPiece(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1)).getTeamColor();
                if (myTeamColor != otherPieceColor) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1), null));
                    System.out.println((myPosition.getRow() - 2) + " " + (myPosition.getColumn() + 1));
                }
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1), null));
                System.out.println((myPosition.getRow() - 2) + " " + (myPosition.getColumn() + 1));
            }
        }

        // direction: {-1,-2}
        if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn() - 2 >= 1) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2)) != null) {
                ChessGame.TeamColor otherPieceColor = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2)).getTeamColor();
                if (myTeamColor != otherPieceColor) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2), null));
                    System.out.println((myPosition.getRow() - 1) + " " + (myPosition.getColumn() - 2));
                }
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2), null));
                System.out.println((myPosition.getRow() - 1) + " " + (myPosition.getColumn() - 2));
            }
        }
        return moves;
    }
}
