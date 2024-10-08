package piece.pieces;

import piece.Piece;
import piece.PieceColor;
import piece.PieceType;

public class Rook extends Piece {

        public Rook(PieceColor color, int column, int row) {
                super(color, column, row);
                pieceType = PieceType.ROOK;
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

                if (targetFile == nextFile || targetRank == nextRank) {
                        return isValidTile(targetFile, targetRank)
                                        && !isPieceInVerticalAndHorizontalSight(targetFile, targetRank);
                }
                return false;
        }
}