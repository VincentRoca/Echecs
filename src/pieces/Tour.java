package pieces;

import jeu.Piece;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Tour extends Piece {
	
	private boolean mvt; // indique si la tour a déjà bougé durant la partie

	public Tour(int x, int y, boolean equipe) {
		super(x, y, equipe);
	}
	
	public void mvt() {
		mvt=true;
	}
	
	boolean getMvt() {
		return mvt;
	}
	
	protected void copie() {
		Tour t=new Tour(getX(),getY(),getEquipe());
		if(mvt) t.mvt();
	}

	protected boolean deplacementPossible(int x, int y) {
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
		return res;
	}
	
	protected char symbole() {
		return 'T';
	}
	
	protected int coeff() {
		boolean ouvert=true;
		int inc;
		if(getEquipe()) inc=1;
		else inc=-1;
		for(int x=getX()+inc; x>=0 && x<8 && ouvert; x+=inc) {
			Piece p=getEchiquier().getPiece(x, getY());
			if(p!=null && p.getEquipe()==getEquipe() && p instanceof Pion) ouvert=false;
		}
		int res;
		if(ouvert) res=40;
		else res=-40;
		boolean lies=true;
		for(int y=getY()-1; y>=0 && lies; y--) {
			Piece p=getEchiquier().getPiece(getX(), y);
			if(p!=null)
				if(p instanceof Tour && p.getEquipe()==getEquipe()) return res+20;
				else lies=false;
		}
		lies=true;
		for(int y=getY()+1; y<8 && lies; y++) {
			Piece p=getEchiquier().getPiece(getX(), y);
			if(p!=null)
				if(p instanceof Tour && p.getEquipe()==getEquipe()) return res+20;
				else lies=false;
		}
		
		for(int x=getX()-1; x>=0 && lies; x--) {
			Piece p=getEchiquier().getPiece(x, getY());
			if(p!=null)
				if(p instanceof Tour && p.getEquipe()==getEquipe()) return res+20;
				else lies=false;
		}
		for(int x=getX()+1; x<8 && lies; x++) {
			Piece p=getEchiquier().getPiece(x, getY());
			if(p!=null)
				if(p instanceof Tour && p.getEquipe()==getEquipe()) return res+20;
				else lies=false;
		}
		return res;
	}

}
