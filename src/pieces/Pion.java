package pieces;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import jeu.Piece;

public class Pion extends Piece {
	
	public Pion(int x, int y, boolean equipe) {
		super(x, y, equipe);
	}

	private boolean mvt; //indique si le pion a bougé au dernier coup
	
	public void setMvt(boolean mvt) {
		this.mvt=mvt;
	}
	
	protected void copie() {
		Pion p=new Pion(getX(),getY(),getEquipe());
		p.setMvt(mvt);
	}

	protected boolean deplacementPossible(int x, int y) {
		Piece p=Piece.getEchiquier().getPiece(x, y);
		if(getEquipe()) {
			if(p==null) {
				if(getY()!=y) {
					if(Math.abs(getY()-y)!=1 || x-getX()!=1) return false;
					Piece p2=Piece.getEchiquier().getPiece(x-1, y);
					return p2!=null && p2 instanceof Pion && ((Pion)p2).mvt;
				}
				if(x-getX()==1) return true;
				return getX()==1 && x-getX()==2 && Piece.getEchiquier().getPiece(x-1, y)==null;
			}
			return x-getX()==1 && Math.abs(getY()-y)==1; 
		}
		if(p==null) {
			if(getY()!=y) {
				if(Math.abs(getY()-y)!=1 || x-getX()!=-1) return false;
				Piece p2=Piece.getEchiquier().getPiece(x+1, y);
				return p2!=null && p2 instanceof Pion && ((Pion)p2).mvt;
			}
			if(x-getX()==-1) return true;
			return getX()==6 && x-getX()==-2 && Piece.getEchiquier().getPiece(x+1, y)==null;
		}
		return x-getX()==-1 && Math.abs(getY()-y)==1;
	}
	
	protected List<Point> deplacementsPossibles() {
		List<Point> res=new ArrayList<>();
		if(getEquipe()) {
			if(getEchiquier().getPiece(getX()+1, getY())==null) {
				res.add(new Point(getX()+1, getY()));
				if(getX()==1 && getEchiquier().getPiece(getX()+2, getY())==null)
					res.add(new Point(getX()+2, getY()));
			}
			if(getY()-1>=0 && getEchiquier().getPiece(getX()+1, getY()-1)!=null)
				res.add(new Point(getX()+1, getY()-1));
			if(getY()+1<8 && getEchiquier().getPiece(getX()+1, getY()+1)!=null)
				res.add(new Point(getX()+1, getY()+1));
			if(getX()==4) {
				if(getY()-1>=0) {
					Piece p=getEchiquier().getPiece(4, getY()-1);
					if(p!=null && (p.getEquipe()^getEquipe()) && p instanceof Pion && ((Pion)p).mvt)
						res.add(new Point(5,getY()-1));
				}
				if(getY()+1<8) {
					Piece p=getEchiquier().getPiece(4, getY()+1);
					if(p!=null && (p.getEquipe()^getEquipe()) && p instanceof Pion && ((Pion)p).mvt)
						res.add(new Point(5,getY()+1));
				}
			}
		} else {
			if(getEchiquier().getPiece(getX()-1, getY())==null) {
				res.add(new Point(getX()-1, getY()));
				if(getX()==6 && getEchiquier().getPiece(getX()-2, getY())==null)
					res.add(new Point(getX()-2, getY()));
			}
			if(getY()-1>=0 && getEchiquier().getPiece(getX()-1, getY()-1)!=null)
				res.add(new Point(getX()-1, getY()-1));
			if(getY()+1<8 && getEchiquier().getPiece(getX()-1, getY()+1)!=null)
				res.add(new Point(getX()-1, getY()+1));
			if(getX()==3) {
				if(getY()-1>=0) {
					Piece p=getEchiquier().getPiece(3, getY()-1);
					if(p!=null && p.getEquipe() ^ getEquipe() && p instanceof Pion && ((Pion)p).mvt)
						res.add(new Point(2,getY()-1));
				}
				if(getY()+1<8) {
					Piece p=getEchiquier().getPiece(3, getY()+1);
					if(p!=null && p.getEquipe() ^ getEquipe() && p instanceof Pion && ((Pion)p).mvt)
						res.add(new Point(2,getY()+1));
				}
			}
		}
		return res;
	}
	
	protected char symbole() {
		return 'P';
	}

	protected int coeff() {
		int inc;
		if(getEquipe()) inc=1;
		else inc=-1;
		for(int x=getX()+inc; x>=0 && x<8; x+=inc) {
			Piece p=getEchiquier().getPiece(x, getY());
			if(p!=null && p.getEquipe()==getEquipe() && p instanceof Pion) return -15;
		}
		return 0;
			
	}

}
