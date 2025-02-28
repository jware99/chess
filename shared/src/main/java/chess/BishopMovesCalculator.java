package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myTeamColor = board.getPiece(myPosition).getTeamColor();

        // Possible directions for a bishop: diagonal movements
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] dir : directions) {
            int rowOffset = dir[0], colOffset = dir[1];
            int row = myPosition.getRow() + rowOffset;
            int col = myPosition.getColumn() + colOffset;

            while (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
                ChessPosition newPosition = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(newPosition);

                if (piece != null) {
                    if (piece.getTeamColor() != myTeamColor) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                moves.add(new ChessMove(myPosition, newPosition, null));
                row += rowOffset;
                col += colOffset;
            }
        }
        return moves;
    }
}