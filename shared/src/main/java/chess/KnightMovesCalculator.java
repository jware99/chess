package chess;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        var myTeamColor = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn())).getTeamColor();
        boolean occupied = false;
        boolean sameTeam = false;

        //direction: {2,1}
        if (myPosition.getRow() + 2 <= 8 && myPosition.getColumn() + 1 <= 8) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1)) != null) {
                var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1)).getTeamColor();
                occupied = true;
                if(myTeamColor == otherTeamColor) {
                    sameTeam = true;
                }
            }
            if (!occupied || !sameTeam) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1), null));
                System.out.println((myPosition.getRow() + 2) + " " + (myPosition.getColumn() + 1));
            }
        }

        //direction: {1,2}
        occupied = false;
        if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() + 2 <= 8) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2)) != null) {
                var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2)).getTeamColor();
                occupied = true;
                if(myTeamColor == otherTeamColor) {
                    sameTeam = true;
                }
            }
            if (!occupied || !sameTeam) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2), null));
                System.out.println((myPosition.getRow() + 1) + " " + (myPosition.getColumn() + 2));
            }
        }

        //direction: {-1,2}
        occupied = false;
        if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn() + 2 <= 8) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2)) != null) {
                var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2)).getTeamColor();
                occupied = true;
                if(myTeamColor == otherTeamColor) {
                    sameTeam = true;
                }
            }
            if (!occupied || !sameTeam) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2), null));
                System.out.println((myPosition.getRow() - 1) + " " + (myPosition.getColumn() + 2));
            }
        }

        //direction: {-2,1}
        occupied = false;
        if (myPosition.getRow() - 2 >= 1 && myPosition.getColumn() + 1 <= 8) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1)) != null) {
                var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1)).getTeamColor();
                occupied = true;
                if(myTeamColor == otherTeamColor) {
                    sameTeam = true;
                }
            }
            if (!occupied || !sameTeam) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1), null));
                System.out.println((myPosition.getRow() - 2) + " " + (myPosition.getColumn() + 1));
            }
        }

        //direction: {-2,-1}
        occupied = false;
        if (myPosition.getRow() - 2 >= 1 && myPosition.getColumn() - 1 >= 1) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1)) != null) {
                var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1)).getTeamColor();
                occupied = true;
                if(myTeamColor == otherTeamColor) {
                    sameTeam = true;
                }
            }
            if (!occupied || !sameTeam) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1), null));
                System.out.println((myPosition.getRow() - 2) + " " + (myPosition.getColumn() - 1));
            }
        }

        //direction: {-1,-2}
        occupied = false;
        if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn() - 2 >= 1) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2)) != null) {
                var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2)).getTeamColor();
                occupied = true;
                if(myTeamColor == otherTeamColor) {
                    sameTeam = true;
                }
            }
            if (!occupied || !sameTeam) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2), null));
                System.out.println((myPosition.getRow() - 1) + " " + (myPosition.getColumn() - 2));
            }
        }

        //direction: {1,-2}
        occupied = false;
        if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() - 2 >= 1) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2)) != null) {
                var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2)).getTeamColor();
                occupied = true;
                if(myTeamColor == otherTeamColor) {
                    sameTeam = true;
                }
            }
            if (!occupied || !sameTeam) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2), null));
                System.out.println((myPosition.getRow() + 1) + " " + (myPosition.getColumn() - 2));
            }
        }

        //direction: {2,-1}
        occupied = false;
        if (myPosition.getRow() + 2 <= 8 && myPosition.getColumn() - 1 >= 1) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1)) != null) {
                var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1)).getTeamColor();
                occupied = true;
                if(myTeamColor == otherTeamColor) {
                    sameTeam = true;
                }
            }
            if (!occupied || !sameTeam) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1), null));
                System.out.println((myPosition.getRow() + 2) + " " + (myPosition.getColumn() - 1));
            }
        }






        return moves;

    }
}
