package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        var myTeamColor = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn())).getTeamColor();
        boolean occupied = false;
        boolean sameTeam = false;

        //direction: {0,1}
        int i = 1;
        while (true) {
            if (myPosition.getColumn() + i <= 8) {
                if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i)) != null) {
                    var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i)).getTeamColor();
                    occupied = true;
                    if(myTeamColor == otherTeamColor) {
                        sameTeam = true;
                    }
                }
                if (!occupied) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i), null));
                    System.out.println((myPosition.getRow()) + " " + (myPosition.getColumn() + i));
                    i++;
                } else if(!sameTeam) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i), null));
                    System.out.println((myPosition.getRow()) + " " + (myPosition.getColumn() + i));
                    break;
                }
                else {
                    break;
                }
            } else {
                break;
            }
        }

        //direction: {0,-1}
        i = 1;
        while (true) {
            if (myPosition.getColumn() - i >= 1) {
                if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i)) != null) {
                    var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i)).getTeamColor();
                    occupied = true;
                    if(myTeamColor == otherTeamColor) {
                        sameTeam = true;
                    }
                }
                if (!occupied) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i), null));
                    System.out.println((myPosition.getRow()) + " " + (myPosition.getColumn() - i));
                    i++;
                } else if(!sameTeam) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i), null));
                    System.out.println((myPosition.getRow()) + " " + (myPosition.getColumn() - i));
                    break;
                }
                else {
                    break;
                }
            } else {
                break;
            }
        }

        //direction: {-1,0}
        i = 1;
        while (true) {
            if (myPosition.getRow() - i >= 1) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn())) != null) {
                    var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn())).getTeamColor();
                    occupied = true;
                    if(myTeamColor == otherTeamColor) {
                        sameTeam = true;
                    }
                }
                if (!occupied) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn()), null));
                    System.out.println((myPosition.getRow() - i) + " " + (myPosition.getColumn()));
                    i++;
                } else if(!sameTeam) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn()), null));
                    System.out.println((myPosition.getRow() - i) + " " + (myPosition.getColumn()));
                    break;
                }
                else {
                    break;
                }
            } else {
                break;
            }
        }

        //direction: {1,0}
        i = 1;
        while (true) {
            if (myPosition.getRow() + i <= 8) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn())) != null) {
                    var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn())).getTeamColor();
                    occupied = true;
                    if(myTeamColor == otherTeamColor) {
                        sameTeam = true;
                    }
                }
                if (!occupied) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn()), null));
                    System.out.println((myPosition.getRow() + i) + " " + (myPosition.getColumn()));
                    i++;
                } else if(!sameTeam) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn()), null));
                    System.out.println((myPosition.getRow() + i) + " " + (myPosition.getColumn()));
                    break;
                }
                else {
                    break;
                }
            } else {
                break;
            }
        }

        //direction: {1,1}
        i = 1;
        int j = 1;
        while (true) {
            if (myPosition.getRow() + i <= 8 && myPosition.getColumn() + j <= 8) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)) != null) {
                    var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)).getTeamColor();
                    occupied = true;
                    if(myTeamColor == otherTeamColor) {
                        sameTeam = true;
                    }
                }
                if (!occupied) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j), null));
                    System.out.println((myPosition.getRow() + i) + " " + (myPosition.getColumn() + j));
                    i++;
                    j++;
                } else if(!sameTeam) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j), null));
                    System.out.println((myPosition.getRow() + i) + " " + (myPosition.getColumn() + j));
                    break;
                }
                else {
                    break;
                }
            } else {
                break;
            }
        }

        //direction: {1,-1}
        i = 1;
        j = 1;
        while (true) {
            if (myPosition.getRow() + i <= 8 && myPosition.getColumn() - j >= 1) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - j)) != null) {
                    var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - j)).getTeamColor();
                    occupied = true;
                    if(myTeamColor == otherTeamColor) {
                        sameTeam = true;
                    }
                }
                if (!occupied) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - j), null));
                    System.out.println((myPosition.getRow() + i) + " " + (myPosition.getColumn() - j));
                    i++;
                    j++;
                } else if(!sameTeam) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - j), null));
                    System.out.println((myPosition.getRow() + i) + " " + (myPosition.getColumn() - j));
                    break;
                }
                else {
                    break;
                }
            } else {
                break;
            }
        }

        //direction: {-1,1}
        i = 1;
        j = 1;
        while (true) {
            if (myPosition.getRow() - i >= 1 && myPosition.getColumn() + j <= 8) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + j)) != null) {
                    var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + j)).getTeamColor();
                    occupied = true;
                    if(myTeamColor == otherTeamColor) {
                        sameTeam = true;
                    }
                }
                if (!occupied) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + j), null));
                    System.out.println((myPosition.getRow() - i) + " " + (myPosition.getColumn() + j));
                    i++;
                    j++;
                } else if(!sameTeam) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + j), null));
                    System.out.println((myPosition.getRow() - i) + " " + (myPosition.getColumn() + j));
                    break;
                }
                else {
                    break;
                }
            } else {
                break;
            }
        }

        //direction: {-1,-1}
        i = 1;
        j = 1;
        while (true) {
            if (myPosition.getRow() - i >= 1 && myPosition.getColumn() - j >= 1) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - j)) != null) {
                    var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - j)).getTeamColor();
                    occupied = true;
                    if(myTeamColor == otherTeamColor) {
                        sameTeam = true;
                    }
                }
                if (!occupied) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - j), null));
                    System.out.println((myPosition.getRow() - i) + " " + (myPosition.getColumn() - j));
                    i++;
                    j++;
                } else if(!sameTeam) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - j), null));
                    System.out.println((myPosition.getRow() - i) + " " + (myPosition.getColumn() - j));
                    break;
                }
                else {
                    break;
                }
            } else {
                break;
            }
        }




        return moves;

    }
}
