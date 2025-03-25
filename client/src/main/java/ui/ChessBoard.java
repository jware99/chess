package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoard {

    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 3;
    private static final String EMPTY = "   ";

    private static final String[][] WHITE_JOIN_PIECES = {
            {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK},
            {BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN},
            {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK}
    };

    private static final String[][] BLACK_JOIN_PIECES = {
            {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK},
            {WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN},
            {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK}
    };

    public static void createBoard(ChessGame.TeamColor teamColor) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        drawColumnHeaders(out, teamColor);
        drawChessBoard(out, teamColor);
        drawColumnHeaders(out, teamColor);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawColumnHeaders(PrintStream out, ChessGame.TeamColor teamColor) {
        setBlack(out);
        System.out.print("   ");
        String[] headers;
        if (teamColor == ChessGame.TeamColor.WHITE || teamColor == null) {
            headers = new String[]{" A ", " B ", " C ", " D ", " E ", " F ", " G ", " H "};
        } else {
            headers = new String[]{" H ", " G ", " F ", " E ", " D ", " C ", " B ", " A "};
        }
        for (String header : headers) {
            drawHeader(out, header);
        }
        System.out.println();
    }

    private static void drawHeader(PrintStream out, String header) {
        int prefixLength = SQUARE_SIZE / 2;
        int suffixLength = SQUARE_SIZE - prefixLength - 1;
        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, header);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String text) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(text);
        setBlack(out);
    }

    private static void drawChessBoard(PrintStream out, ChessGame.TeamColor teamColor) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            drawRow(out, row, teamColor);
        }
    }

    private static void drawRow(PrintStream out, int row, ChessGame.TeamColor teamColor) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE; squareRow++) {
            printTopBottomHeaders(row, teamColor, squareRow);

            for (int col = 0; col < BOARD_SIZE; col++) {
                boolean isWhiteSquare = (row + col) % 2 == 0;
                setSquareColor(out, isWhiteSquare);

                if (teamColor == ChessGame.TeamColor.WHITE || teamColor == null) {
                    if (squareRow == SQUARE_SIZE / 2 && WHITE_JOIN_PIECES[row][col] != null) {
                        printPiece(out, WHITE_JOIN_PIECES[row][col]);
                    } else {
                        out.print(EMPTY.repeat(SQUARE_SIZE));
                    }
                } else {
                    if (squareRow == SQUARE_SIZE / 2 && BLACK_JOIN_PIECES[row][col] != null) {
                        printPiece(out, BLACK_JOIN_PIECES[row][col]);
                    } else {
                        out.print(EMPTY.repeat(SQUARE_SIZE));
                    }
                }
            }

            setBlack(out);

            printTopBottomHeaders(row, teamColor, squareRow);

            System.out.println();
        }
    }

    private static void printTopBottomHeaders(int row, ChessGame.TeamColor teamColor, int squareRow) {
        if (squareRow == SQUARE_SIZE / 2) {
            if (teamColor == ChessGame.TeamColor.WHITE || teamColor == null) {
                System.out.print(" " + (8 - row) + " ");
            } else {
                System.out.print(" " + (row + 1) + " ");
            }
        } else {
            System.out.print("   ");
        }
    }


    private static void printPiece(PrintStream out, String piece) {
        out.print(EMPTY.repeat(SQUARE_SIZE / 2));
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(piece);
        out.print(EMPTY.repeat(SQUARE_SIZE / 2));
    }

    private static void setSquareColor(PrintStream out, boolean isWhite) {
        if (isWhite) {
            out.print(SET_BG_COLOR_WHITE);
        } else {
            out.print(SET_BG_COLOR_LIGHT_GREY);
        }
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }
}
