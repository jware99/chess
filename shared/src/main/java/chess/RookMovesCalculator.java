package chess;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myTeamColor = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn())).getTeamColor();

        // direction: {1,0}
        int i = 1;
        while(true) {
            if (myPosition.getRow() + i <= 8) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn())) != null) {
                    ChessGame.TeamColor otherPieceColor = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn())).getTeamColor();
                    if (myTeamColor != otherPieceColor) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn()), null));
                        System.out.println((myPosition.getRow() + i) + " " + (myPosition.getColumn()));
                    }
                    break;
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn()), null));
                    System.out.println((myPosition.getRow() + i) + " " + (myPosition.getColumn()));
                    i++;
                }
            } else {
                break;
            }
        }

        // direction: {-1,0}
        i = 1;
        while(true) {
            if (myPosition.getRow() - i >= 1) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn())) != null) {
                    ChessGame.TeamColor otherPieceColor = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn())).getTeamColor();
                    if (myTeamColor != otherPieceColor) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn()), null));
                        System.out.println((myPosition.getRow() - i) + " " + (myPosition.getColumn()));
                    }
                    break;
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn()), null));
                    System.out.println((myPosition.getRow() - i) + " " + (myPosition.getColumn()));
                    i++;
                }
            } else {
                break;
            }
        }

        // direction: {0,1}
        i = 1;
        while(true) {
            if (myPosition.getColumn() + i <= 8) {
                if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i)) != null) {
                    ChessGame.TeamColor otherPieceColor = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i)).getTeamColor();
                    if (myTeamColor != otherPieceColor) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i), null));
                        System.out.println((myPosition.getRow()) + " " + (myPosition.getColumn() + i));
                    }
                    break;
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i), null));
                    System.out.println((myPosition.getRow()) + " " + (myPosition.getColumn() + i));
                    i++;
                }
            } else {
                break;
            }
        }

        // direction: {0,-1}
        i = 1;
        while(true) {
            if (myPosition.getColumn() - i >= 1) {
                if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i)) != null) {
                    ChessGame.TeamColor otherPieceColor = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i)).getTeamColor();
                    if (myTeamColor != otherPieceColor) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i), null));
                        System.out.println((myPosition.getRow()) + " " + (myPosition.getColumn() - i));
                    }
                    break;
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i), null));
                    System.out.println((myPosition.getRow()) + " " + (myPosition.getColumn() - i));
                    i++;
                }
            } else {
                break;
            }
        }
        return moves;
    }
}
