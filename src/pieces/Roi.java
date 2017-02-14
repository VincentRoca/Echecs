package pieces;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import jeu.Piece;

public class Roi extends Piece {
	
	private boolean mvt; // indique si le roi a déjà bougé durant la partie

	private boolean rock;
	
	public Roi(int x,int y, boolean equipe) {
		super(x,y,equipe);
	}
	
	public void mvt() {
		mvt=true;
	}
	
	public void rock() {
		rock=true;
	}
	
	protected void copie() {
		Roi r=new Roi(getX(),getY(),getEquipe());
		if(mvt) r.mvt();
		if(rock) r.rock();
	}

	protected boolean deplacementPossible(int x, int y) {
		if(Math.abs(x-getX())==1 && Math.abs(y-getY())==1) return true;
		if(Math.abs(x-getX())==1 && Math.abs(y-getY())==0) return true;
		if(Math.abs(x-getX())==0 && Math.abs(y-getY())==1) return true;
		if(x==getX() && getY()-y==2 && !mvt && getEchiquier().getPiece(x, y-2) instanceof Tour && 
				!((Tour)getEchiquier().getPiece(x, y-2)).getMvt() && getEchiquier().getPiece(x, y+1)==null &&
				getEchiquier().getPiece(x, y)==null && getEchiquier().getPiece(x, y-1)==null) return true;
		if(x==getX() && getY()-y==-2 && !mvt && getEchiquier().getPiece(x, y+1) instanceof Tour && 
				!((Tour)getEchiquier().getPiece(x, y+1)).getMvt() && getEchiquier().getPiece(x, y)==null &&
				getEchiquier().getPiece(x, y-1)==null) return true;
		return false;
	}
	
	protected char symbole() {
		return 'R';
	}
	
	protected List<Point> deplacementsPossibles() {
		List<Point> res=new ArrayList<>();
		if(getX()-1>=0) {
			res.add(new Point(getX()-1, getY()));
			if(getY()-1>=0) res.add(new Point(getX()-1, getY()-1));
			if(getY()+1<8) res.add(new Point(getX()-1, getY()+1));
		}
		if(getX()+1<8) {
			res.add(new Point(getX()+1, getY()));
			if(getY()-1>=0) res.add(new Point(getX()+1, getY()-1));
			if(getY()+1<8) res.add(new Point(getX()+1, getY()+1));
		}
		if(getY()-1>=0) res.add(new Point(getX(), getY()-1));
		if(getY()+1<8) res.add(new Point(getX(), getY()+1));
		if(!mvt) {
			Piece p=getEchiquier().getPiece(getX(), 0);
			if(p instanceof Tour && !((Tour)p).getMvt() && getEchiquier().getPiece(getX(), 1)==null && 
					getEchiquier().getPiece(getX(), 2)==null &&	getEchiquier().getPiece(getX(), 3)==null) 
				res.add(new Point(getX(),2));
			p=getEchiquier().getPiece(getX(), 7);
			if(p instanceof Tour && !((Tour)p).getMvt() && getEchiquier().getPiece(getX(), 5)==null &&
					getEchiquier().getPiece(getX(), 6)==null)
				res.add(new Point(getX(),6));
		}
		return res;
	}

	protected int coeff() {
		if(rock) return 20;
		if(mvt) return -30;
		Piece p1=getEchiquier().getPiece(getX(), 0), p2=getEchiquier().getPiece(getX(), 7);
		if((!(p1 instanceof Tour) || ((Tour)p1).getMvt()) && (!(p2 instanceof Tour) || ((Tour)p2).getMvt())) return -30;
		return 0;
	}
	
}
