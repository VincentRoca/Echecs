package jeu;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pieces.Cavalier;
import pieces.Dame;
import pieces.Fou;
import pieces.Pion;
import pieces.Roi;
import pieces.Tour;

public abstract class Piece {

	private static Echiquier echiquier;
	
	private int x,y;
	
	private final boolean equipe; // true blanc , false noir
	
	public static void setEchiquier(Echiquier ech) {
		echiquier=ech;
	}
	
	public static Echiquier getEchiquier() {
		return echiquier;
	}
	
	protected Piece(int x,int y,boolean equipe) {
		this.x=x;
		this.y=y;
		this.equipe=equipe;
		echiquier.setPiece(this, x, y);
		echiquier.ajouterPiece(this);
	}
	
	public boolean getEquipe() {
		return equipe;
	}
	
	protected int getX() {
		return x;
	}
	
	protected int getY() {
		return y;
	}
	
	protected abstract void copie();
	
	/**
	 * pas de vérif des echecs ni de présence de pieces alliées
	 * pour l'instant, pas nécessaire de vérifier que ca rentre dans les bornes 0-8
	 */
	protected abstract boolean deplacementPossible(int x,int y);
	
	protected abstract char symbole();
	
	public String toString() {
		if(equipe) return symbole()+"B";
		return symbole()+"N";
	}
	
	/*List<Point> deplacementsReelsPossibles() {
		List<Point> res=new ArrayList<>();
		for(int i=0; i<8; i++)
			for(int j=0; j<8; j++) 
				if(deplacementReelPossibleUser(i,j)) res.add(new Point(i,j));
		return res;
	}*/
	
	List<Point> deplacementsReelsPossibles() {
		List<Point> ref=deplacementsPossibles();
		List<Point> res=new ArrayList<>();
		for(Point p :ref) 
			if(deplacementReelPossible(p.x, p.y)) res.add(p);
		return res;
	}
	
	protected abstract List<Point> deplacementsPossibles();
	
	/**
	 * renvoie le nombre de déplacements possibles d'apres la méthode booléenne deplacementPossible()
	 */
	/*int nbDeplacementsPossibles() {
		int res=0;
		for(int i=0; i<8; i++)
			for(int j=0; j<8; j++)
				if(deplacementPossible(i, j)) res++;
		return res;
	}*/
	
	public Map<String, Point> coups() {
		List<Point> deplacements=deplacementsReelsPossibles();
		Map<String,Point> res=new HashMap<>();
		for(Point p : deplacements)
			res.put(notationDeplacement(p.x, p.y), p);
		return res;
	}
	
	public String notationDeplacement(int x, int y) {
		if(this instanceof Roi && Math.abs(this.y-y)==2) {
			if(y<this.y) return "O-O-O";
			return "O-O";
		}
		String res=""+(char)('a'+y)+(1+x); // ici on a case destination
		if(this instanceof Pion) {
			if(x%7==0) res+='D';
			if(y!=this.y) res=(char)('a'+this.y)+"x"+res;
		} else if(echiquier.getPiece(x, y)!=null)res='x'+res;
		if(this instanceof Cavalier || this instanceof Tour || this instanceof Dame) {
			List<Piece> pieces=echiquier.getPieces(equipe);
			for(Piece p : pieces)
				if(p!=this && ((this instanceof Cavalier && p instanceof Cavalier && p.deplacementReelPossibleUser(x, y)) || 
						(this instanceof Tour && p instanceof Tour && p.deplacementReelPossibleUser(x, y)) || 
						this instanceof Dame && p instanceof Dame && p.deplacementReelPossibleUser(x, y))) {
					if(this.y!=p.y) res=(char)('a'+this.y)+res;
					else res=(1+this.x)+res;
				}
		}
		if(!(this instanceof Pion)) 
			res=symbole()+res;
		Echiquier tmp=echiquier;
		echiquier=echiquier.copie();
		echiquier.getPiece(this.x, this.y).deplacement(x, y);
		if(echiquier.echec(!equipe)) res+='+';
		if(echiquier.mat(!equipe)) res+='#';
		echiquier=tmp;
		return res;
	}
	
	boolean deplacementReelPossibleUser(int x,int y) {
		if(!deplacementPossible(x, y)) return false;
		return deplacementReelPossible(x, y);
	}
	
	private boolean deplacementReelPossible(int x, int y) {
		if(echiquier.getPiece(x,y)!=null && echiquier.getPiece(x,y).equipe==equipe) return false;
		if(this instanceof Roi && Math.abs(y-this.y)==2) {
			if(echiquier.echec(equipe)) return false;
			Echiquier tmp=echiquier;
			echiquier=echiquier.copie();
			if(y>this.y) {
				echiquier.getPiece(this.x, this.y).deplacement(x, y-1);
				if(echiquier.echec(equipe)) {
					echiquier=tmp;
					return false;
				}
			} else {
				echiquier.getPiece(this.x, this.y).deplacement(x, y+1);
				if(echiquier.echec(equipe)) {
					echiquier=tmp;
					return false;
				}
			}
			echiquier=tmp;
		}
		Echiquier tmp=echiquier;
		echiquier=echiquier.copie();
		echiquier.getPiece(this.x, this.y).deplacement(x, y);
		boolean echec=echiquier.echec(equipe);
		echiquier=tmp;
		return !echec;
	}
	
	public void deplacement(int x, int y) {
		echiquier.setPiece(null, this.x,this.y);
		if(this instanceof Tour) ((Tour)this).mvt();
		else if(this instanceof Roi) {
			Roi r=(Roi)this;
			r.mvt();
			int dy=this.y-y;
			if(Math.abs(dy)==2) {
				r.rock();
				if(dy>0) echiquier.getPiece(x, y-2).deplacement(x, y+1);
				else echiquier.getPiece(x, y+1).deplacement(x, y-1);
			}
		}
		Piece p=echiquier.getPiece(x, y);
		if(p!=null) echiquier.getPieces(!equipe).remove(p);
		if(this instanceof Pion && x%7==0) {
			echiquier.getPieces(equipe).remove(this);
			new Dame(x, y, equipe);
		}
		else  { 
			echiquier.setPiece(this, x, y);
			if(this instanceof Pion && p==null && y!=this.y) {
				echiquier.getPieces(!equipe).remove(echiquier.getPiece(this.x, y));
				echiquier.setPiece(null, this.x, y);
			}
		}
		List<Piece> pieces=echiquier.getPieces(equipe);
		for(Piece piece : pieces)
			if(piece instanceof Pion)
				((Pion)piece).setMvt(false);
		if(this instanceof Pion && Math.abs(x-this.x)==2)
			((Pion)this).setMvt(true);
		this.x=x; this.y=y;
	}
	
	int valeurCoup(int l, int c, int precision) {
		Echiquier tmp=echiquier;
		echiquier=echiquier.copie();
		echiquier.getPiece(x, y).deplacement(l, c);
		if(precision==0 || echiquier.mat(!equipe)) {
			int value=echiquier.evalPosition(!equipe);
			echiquier=tmp;
			return value;
		}
		List<Piece> pieces=echiquier.getPieces(!equipe);
		int seuilSuivant;
		if(equipe) seuilSuivant=Integer.MAX_VALUE;
		else seuilSuivant=Integer.MIN_VALUE;
		for(Piece p : pieces) {
			List<Point> points=p.deplacementsReelsPossibles();
			for(Point p2 : points) {
				int value=p.valeurCoup(p2.x, p2.y, precision-1,seuilSuivant);
				if((equipe && value<seuilSuivant) || (!equipe && value>seuilSuivant))
					seuilSuivant=value;
			}
		}
		echiquier=tmp;
		return seuilSuivant;
	}

	/*int valeurCoup(int l, int c,int precision, int seuil) {
		//if(cpt==421) System.out.println(this+"\txd = "+x+"\tyd = "+y+"\txa = "+point.x+"\tya = "+point.y+"\tprecision = "+precision+"\t equipe = "+equipe+"\t cpt = "+cpt);
		Echiquier tmp=echiquier;
		echiquier=echiquier.copie();
		echiquier.getPiece(x, y).deplacement(l, c);
		int value=echiquier.evalPosition(!equipe);
		if(precision==0 || echiquier.mat(!equipe)) {
			if(Math.abs(value)==10000) value/=(Echiquier.getPrecision()-precision+1);
			echiquier=tmp;
			return value;
		}
		List<Piece> pieces=echiquier.getPieces(!equipe);
		int seuilSuivant;
		if(equipe) seuilSuivant=Integer.MAX_VALUE;
		else seuilSuivant=Integer.MIN_VALUE;
		for(Piece p : pieces) {
			List<Point> points=p.deplacementsReelsPossibles();
			for(Point p2 : points) {
				int val=p.valeurCoup(p2.x,p2.y, precision-1,seuilSuivant)+value;
				if((equipe && val<seuilSuivant) || (!equipe && val>seuilSuivant))
					seuilSuivant=val;
				if((equipe && val<=seuil) || (!equipe && val>=seuil)) {
					echiquier=tmp;
					if(equipe) return Integer.MIN_VALUE;
					return Integer.MAX_VALUE;
				}
				
			}
		}
		echiquier=tmp;
		return seuilSuivant;
	}*/
	
	//private static long cpt;
	int valeurCoup(int l, int c, int precision, int seuil) {
		//cpt++;
		//if(cpt==46792)System.out.println(notationDeplacement(l, c));
		//36766 45881 46792 46831 46848
		// g3 d6 - Tc6
		Echiquier tmp=echiquier;
		echiquier=echiquier.copie();
		echiquier.getPiece(x, y).deplacement(l, c);
		if(echiquier.mat(!equipe)) {
			if(echiquier.echec(!equipe)) {
				echiquier=tmp;
				if(equipe) return Integer.MAX_VALUE/(Echiquier.getPrecision()-precision+1);
				return Integer.MIN_VALUE/(Echiquier.getPrecision()-precision+1);
			}
			echiquier=tmp;
			return 0;
		}
		if(precision==0) {
			int value=echiquier.evalPosition(!equipe);
			echiquier=tmp;
			return value;
		}
		List<Piece> pieces=echiquier.getPieces(!equipe);
		int seuilSuivant;
		if(equipe) seuilSuivant=Integer.MAX_VALUE;
		else seuilSuivant=Integer.MIN_VALUE;
		for(Piece p : pieces) {
			List<Point> points=p.deplacementsReelsPossibles();
			for(Point p2 : points) {
				int value=p.valeurCoup(p2.x, p2.y, precision-1,seuilSuivant);
				if((equipe && value<=seuil) || (!equipe && value>=seuil)) {
					echiquier=tmp;
					if(equipe) return Integer.MIN_VALUE;
					return Integer.MAX_VALUE;
				}
				if((equipe && value<seuilSuivant) || (!equipe && value>seuilSuivant))
					seuilSuivant=value;
			}
		}
		echiquier=tmp;
		return seuilSuivant;
	}

	int valeurMateriel() {
		int valeur;
		if(this instanceof Pion) valeur=100;
		else if(this instanceof Cavalier) valeur=320;
		else if(this instanceof Fou) valeur=333;
		else if(this instanceof Tour) valeur=510;
		else if(this instanceof Dame) valeur=880;
		else valeur=0;
		if(equipe) return valeur;
		else return -valeur;
	}

	protected abstract int coeff();
	
}
