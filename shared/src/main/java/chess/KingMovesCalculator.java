package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        var myTeamColor = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn())).getTeamColor();
        boolean occupied = false;
        boolean sameTeam = false;

        //direction: {0,1}
        if (myPosition.getColumn() + 1 <= 8) {
            if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1)) != null) {
                var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1)).getTeamColor();
                occupied = true;
                if(myTeamColor == otherTeamColor) {
                    sameTeam = true;
                }
            }
            if (!occupied) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1), null));
                System.out.println((myPosition.getRow()) + " " + (myPosition.getColumn() + 1));
            } else if(!sameTeam) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1), null));
                System.out.println((myPosition.getRow()) + " " + (myPosition.getColumn() + 1));
            }
        }

        //direction: {0,-1}
        occupied = false;
        sameTeam = false;
        if (myPosition.getColumn() - 1 >= 1) {
            if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1)) != null) {
                var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1)).getTeamColor();
                occupied = true;
                if(myTeamColor == otherTeamColor) {
                    sameTeam = true;
                }
            }
            if (!occupied) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1), null));
                System.out.println((myPosition.getRow()) + " " + (myPosition.getColumn() - 1));
            } else if(!sameTeam) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1), null));
                System.out.println((myPosition.getRow()) + " " + (myPosition.getColumn() - 1));
            }
        }

        //direction: {-1,0}
        occupied = false;
        sameTeam = false;
        if (myPosition.getRow() - 1 >= 1) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn())) != null) {
                var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn())).getTeamColor();
                occupied = true;
                if(myTeamColor == otherTeamColor) {
                    sameTeam = true;
                }
            }
            if (!occupied) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));
                System.out.println((myPosition.getRow() - 1) + " " + (myPosition.getColumn()));
            } else if(!sameTeam) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));
                System.out.println((myPosition.getRow() - 1) + " " + (myPosition.getColumn()));
            }
        }

        //direction: {1,0}
        occupied = false;
        sameTeam = false;
        if (myPosition.getRow() + 1 <= 8) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn())) != null) {
                var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn())).getTeamColor();
                occupied = true;
                if(myTeamColor == otherTeamColor) {
                    sameTeam = true;
                }
            }
            if (!occupied) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null));
                System.out.println((myPosition.getRow() + 1) + " " + (myPosition.getColumn()));
            } else if(!sameTeam) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null));
                System.out.println((myPosition.getRow() + 1) + " " + (myPosition.getColumn()));
            }
        }

        //direction: {1,1}
        occupied = false;
        sameTeam = false;
        if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() + 1 <= 8) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)) != null) {
                var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)).getTeamColor();
                occupied = true;
                if(myTeamColor == otherTeamColor) {
                    sameTeam = true;
                }
            }
            if (!occupied) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), null));
                System.out.println((myPosition.getRow() + 1) + " " + (myPosition.getColumn() + 1));
            } else if(!sameTeam) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), null));
                System.out.println((myPosition.getRow() + 1) + " " + (myPosition.getColumn() + 1));
            }
        }

        //direction: {1,-1}
        occupied = false;
        sameTeam = false;
        if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() - 1 >= 1) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1)) != null) {
                var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1)).getTeamColor();
                occupied = true;
                if (myTeamColor == otherTeamColor) {
                    sameTeam = true;
                }
            }
            if (!occupied) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), null));
                System.out.println((myPosition.getRow() + 1) + " " + (myPosition.getColumn() - 1));
            } else if (!sameTeam) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), null));
                System.out.println((myPosition.getRow() + 1) + " " + (myPosition.getColumn() - 1));
            }
        }

        //direction: {-1,1}
        occupied = false;
        sameTeam = false;
        if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn() + 1 <= 8) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1)) != null) {
                var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1)).getTeamColor();
                occupied = true;
                if(myTeamColor == otherTeamColor) {
                    sameTeam = true;
                }
            }
            if (!occupied) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), null));
                System.out.println((myPosition.getRow() - 1) + " " + (myPosition.getColumn() + 1));
            } else if(!sameTeam) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), null));
                System.out.println((myPosition.getRow() - 1) + " " + (myPosition.getColumn() + 1));
            }
        }

        //direction: {-1,-1}
        occupied = false;
        sameTeam = false;
        if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn() - 1 >= 1) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1)) != null) {
                var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1)).getTeamColor();
                occupied = true;
                if(myTeamColor == otherTeamColor) {
                    sameTeam = true;
                }
            }
            if (!occupied) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));
                System.out.println((myPosition.getRow() - 1) + " " + (myPosition.getColumn() - 1));
            } else if(!sameTeam) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));
                System.out.println((myPosition.getRow() - 1) + " " + (myPosition.getColumn() - 1));
            }
        }




        return moves;

    }
}
