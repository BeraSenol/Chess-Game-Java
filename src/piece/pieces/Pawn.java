package piece.pieces;

import main.GamePanel;
import piece.Piece;
import piece.PieceColor;
import piece.PieceType;

public class Pawn extends Piece {

        public Pawn(PieceColor color, int column, int row) {
                super(color, column, row);
                pieceType = PieceType.PAWN;
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
                int moveValue;
                if (color == PieceColor.WHITE) {
                        moveValue = -1;
                } else {
                        moveValue = 1;
                }
                collidingPiece = getCollidingPiece(targetFile, targetRank);
                // 1 tile movement
                if (targetFile == nextFile && targetRank == nextRank + moveValue && collidingPiece == null) {
                        return true;
                }
                // 2 tile movement
                if (targetFile == nextFile && targetRank == nextRank + moveValue * 2 && collidingPiece == null
                                && !hasMoved && !isPieceInSightNorth(targetFile, targetRank)) {
                        return true;
                }
                if (Math.abs(targetFile - nextFile) == 1 && targetRank == nextRank + moveValue
                                && collidingPiece != null
                                && collidingPiece.color != color) {
                        return true;
                }
                // En Passant
                if (Math.abs(targetFile - nextFile) != 1 || targetRank != nextRank + moveValue) {
                        return false;
                }
                for (Piece piece : GamePanel.simPieces) {
                        if (piece.currentFile == targetFile && piece.currentRank == nextRank
                                        && piece.hasTwoStepped == true) {
                                collidingPiece = piece;
                                return true;
                        }
                }

                return false;
        }

}
