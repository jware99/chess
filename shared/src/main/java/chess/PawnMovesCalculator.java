package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        var myTeamColor = board.getPiece(myPosition).getTeamColor();
        int direction = (myTeamColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (myTeamColor == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int promotionRow = (myTeamColor == ChessGame.TeamColor.WHITE) ? 8 : 1;

        // Normal move
        addMoveIfValid(board, myPosition, myPosition.getRow() + direction, myPosition.getColumn(), moves, promotionRow, false);

        // Double move from start position
        if (myPosition.getRow() == startRow && board.getPiece(new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn())) == null) {
            addMoveIfValid(board, myPosition, myPosition.getRow() + 2 * direction, myPosition.getColumn(), moves, promotionRow, false);
        }

        // Captures
        addMoveIfValid(board, myPosition, myPosition.getRow() + direction, myPosition.getColumn() + 1, moves, promotionRow, true);
        addMoveIfValid(board, myPosition, myPosition.getRow() + direction, myPosition.getColumn() - 1, moves, promotionRow, true);

        return moves;
    }

    private void addMoveIfValid(
            ChessBoard board,
            ChessPosition from,
            int toRow,
            int toCol,
            Collection<ChessMove> moves,
            int promotionRow,
            boolean isCapture
    ) {
        if (toRow < 1 || toRow > 8 || toCol < 1 || toCol > 8) { return; }
        ChessPiece targetPiece = board.getPiece(new ChessPosition(toRow, toCol));
        boolean isOccupied = (targetPiece != null);

        if ((isCapture && isOccupied && targetPiece.getTeamColor() != board.getPiece(from).getTeamColor()) || (!isCapture && !isOccupied)) {
            if (toRow == promotionRow) {
                for (ChessPiece.PieceType promotionType : ChessPiece.PieceType.values()) {
                    if (promotionType != ChessPiece.PieceType.PAWN && promotionType != ChessPiece.PieceType.KING) {
                        moves.add(new ChessMove(from, new ChessPosition(toRow, toCol), promotionType));
                    }
                }
            } else {
                moves.add(new ChessMove(from, new ChessPosition(toRow, toCol), null));
            }
        }
    }
}
