package pieces;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import jeu.Piece;

public class Fou extends Piece {

	public Fou(int x, int y, boolean equipe) {
		super(x, y, equipe);
	}
	
	protected void copie() {
		new Fou(getX(),getY(),getEquipe());
	}

	protected boolean deplacementPossible(int x, int y) {
		if(x==getX() || Math.abs(x-getX())!=Math.abs(y-getY())) return false;
		int incX, incY;
		if(x>getX()) incX=1;
		else incX=-1;
		if(y>getY()) incY=1;
		else incY=-1;
		int l=getX()+incX, c=getY()+incY;
		while(l!=x) {
			if(getEchiquier().getPiece(l, c)!=null) return false;
			l+=incX; c+=incY;
		}
		return true;
	}
	
	protected List<Point> deplacementsPossibles() {
		List<Point> res=new ArrayList<>();
		boolean ouvert=true;
		int x=getX()-1, y=getY()-1;
		while(ouvert && x>=0 && y>=0) {
			res.add(new Point(x,y));
			if(getEchiquier().getPiece(x, y)!=null) ouvert=false;
			x--; y--;
		}
		ouvert=true;
		x=getX()-1; y=getY()+1;
		while(ouvert && x>=0 && y<8) {
			res.add(new Point(x,y));
			if(getEchiquier().getPiece(x, y)!=null) ouvert=false;
			x--; y++;
		}
		ouvert=true;
		x=getX()+1; y=getY()-1;
		while(ouvert && x<8 && y>=0) {
			res.add(new Point(x,y));
			if(getEchiquier().getPiece(x, y)!=null) ouvert=false;
			x++; y--;
		}
		ouvert=true;
		x=getX()+1; y=getY()+1;
		while(ouvert && x<8 && y<8) {
			res.add(new Point(x,y));
			if(getEchiquier().getPiece(x, y)!=null) ouvert=false;
			x++; y++;
		}
		return res;
	}
	
	protected char symbole() {
		return 'F';
	}

	protected int coeff() {
		return 0;
	}

}
