package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        RookMovesCalculator rookMoves = new RookMovesCalculator();
        Collection<ChessMove> moves = new ArrayList<>(rookMoves.pieceMoves(board, myPosition));

        BishopMovesCalculator bishopMoves = new BishopMovesCalculator();
        moves.addAll(bishopMoves.pieceMoves(board, myPosition));

        return moves;
    }
}
