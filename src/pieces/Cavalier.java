package pieces;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import jeu.Piece;

public class Cavalier extends Piece {

	public Cavalier(int x, int y, boolean equipe) {
		super(x, y, equipe);
	}
	
	protected void copie() {
		new Cavalier(getX(),getY(),getEquipe());
	}

	protected boolean deplacementPossible(int x, int y) {
		return (Math.abs(x-getX())==1 && Math.abs(y-getY())==2) || (Math.abs(x-getX())==2 && Math.abs(y-getY())==1);
	}
	
	protected char symbole() {
		return 'C';
	}

	protected int coeff() {
		if(getX()%7==0 || getY()%7==0) return -20;
		return 20;
	}
	
	protected List<Point> deplacementsPossibles() {
		List<Point> res=new ArrayList<>();
		if(getX()-1>=0) {
			if(getY()-2>=0) res.add(new Point(getX()-1, getY()-2));
			if(getY()+2<8) res.add(new Point(getX()-1, getY()+2));
			if(getX()-2>=0) {
				if(getY()-1>=0) res.add(new Point(getX()-2, getY()-1));
				if(getY()+1<8) res.add(new Point(getX()-2, getY()+1));
			}
		}
		if(getX()+1<8) {
			if(getY()-2>=0) res.add(new Point(getX()+1, getY()-2));
			if(getY()+2<8) res.add(new Point(getX()+1, getY()+2));
			if(getX()+2<8) {
				if(getY()-1>=0) res.add(new Point(getX()+2, getY()-1));
				if(getY()+1<8) res.add(new Point(getX()+2, getY()+1));
			}
		}
		return res;
	}

}
