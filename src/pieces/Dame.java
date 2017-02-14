package pieces;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import jeu.Piece;

public class Dame extends Piece{

	public Dame(int x, int y, boolean equipe) {
		super(x, y, equipe);
	}
	
	protected void copie() {
		new Dame(getX(),getY(),getEquipe());
	}

	protected boolean deplacementPossible(int x, int y) {
		if(x!=getX() && y!=getY()) {
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
		if(!(x==getX() ^ y==getY())) return false;
		if(x!=getX()) {
			int inc;
			if(x<getX()) inc=-1;
			else inc=1;
			for(int i=getX()+inc; i!=x; i+=inc)
				if(getEchiquier().getPiece(i, y)!=null) return false;
		} else {
			int inc;
			if(y<getY()) inc=-1;
			else inc=1;
			for(int i=getY()+inc; i!=y; i+=inc)
				if(getEchiquier().getPiece(x, i)!=null) return false;
		}
		return true;
	}
	
	protected List<Point> deplacementsPossibles() {
		List<Point> res=new ArrayList<>();
		boolean ouvert=true;
		for(int y=getY()-1; y>=0 && ouvert; y--) {
			res.add(new Point(getX(), y));
			if(getEchiquier().getPiece(getX(), y)!=null) ouvert=false;
		}
		ouvert=true;
		for(int y=getY()+1; y<8 && ouvert; y++) {
			res.add(new Point(getX(), y));
			if(getEchiquier().getPiece(getX(), y)!=null) ouvert=false;
		}
		ouvert=true;
		for(int x=getX()-1; x>=0 && ouvert; x--) {
			res.add(new Point(x,getY()));
			if(getEchiquier().getPiece(x, getY())!=null) ouvert=false;
		}
		ouvert=true;
		for(int x=getX()+1; x<8 && ouvert; x++) {
			res.add(new Point(x,getY()));
			if(getEchiquier().getPiece(x, getY())!=null) ouvert=false;
		}
		ouvert=true;
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
		return 'D';
	}

	protected int coeff() {
		return 0;
	}

}
