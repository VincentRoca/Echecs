package utile;

import java.awt.Point;

import jeu.Piece;

public class Coup {

	public int dx, dy, ax, ay;

	private Coup(int dx, int dy, int ax, int ay) {
		this.dx = dx;
		this.dy = dy;
		this.ax = ax;
		this.ay = ay;
	}
	
	public Coup(int dx, int dy, Point p) {
		this(dx,dy,p.x,p.y);
	}
	
	public String toString() {
		return Piece.getEchiquier().getPiece(dx, dy).notationDeplacement(ax, ay);
	}
}
