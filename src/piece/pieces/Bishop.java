package piece.pieces;

import piece.Piece;
import piece.PieceColor;
import piece.PieceType;

public class Bishop extends Piece {

        public Bishop(PieceColor color, int column, int row) {
                super(color, column, row);
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