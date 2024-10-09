package piece.pieces;

import main.GamePanel;
import piece.*;
public class King extends Piece {

        public King(PieceColor pieceColor, int file, int rank) {
                super(pieceColor, file, rank);
                pieceType = PieceType.KING;
                bufferedImage = getImage(pieceType.getPieceName(), color.getPieceColorName());
        }

        @Override
        public boolean isMovable(int targetFile, int targetRank) {
                if (!isWithinBounds(targetFile, targetRank)) {
                        return false;
                }
                if (isSameSquare(targetFile, targetRank)) {
                        return false;
                }
                if (Math.abs(targetFile - nextFile) + Math.abs(targetRank - nextRank) == 1
                                || Math.abs(targetFile - nextFile) * Math.abs(targetRank - nextRank) == 1
                                                && isValidTile(targetFile, targetRank)) {
                        return true;
                }
                if (!hasMoved) {
                        // Right castling
                        if (targetFile != nextFile + 2 || targetRank != nextRank
                                        || isPieceInSightEast(targetFile, targetRank)) {
                                return false;
                        }
                        for (Piece piece : GamePanel.simPieces) {
                                if (piece.currentFile == nextFile + 3 && piece.currentRank == nextRank
                                                && !piece.hasMoved) {
                                        GamePanel.castlingPiece = piece;
                                        return true;
                                }
                        }
                        // Left castling
                        if (targetFile != nextFile - 2 || targetRank != nextRank
                                        || isPieceInSightWest(targetFile, targetRank)) {
                                return false;
                        }
                        for (Piece piece : GamePanel.simPieces) {
                                if (piece.currentFile == nextFile - 4 && piece.currentRank == nextRank
                                                && !piece.hasMoved) {
                                        GamePanel.castlingPiece = piece;
                                        return true;
                                }
                        }
                }
                return false;
        }
}