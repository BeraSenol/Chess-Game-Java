package piece.pieces;

import piece.*;
public class Bishop extends Piece {
        public Bishop(PieceColor pieceColor, int file, int rank) {
                super(pieceColor, file, rank);
                pieceType = PieceType.BISHOP;
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
                if (Math.abs(targetFile - nextFile) == Math.abs(targetRank - nextRank)) {
                        return isValidTile(targetFile, targetRank) && !isPieceInDiagonalSight(targetFile, targetRank);
                }
                return false;
        }
}