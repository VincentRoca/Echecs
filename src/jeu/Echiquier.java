package jeu;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import affichage.Plateau;
import pieces.Cavalier;
import pieces.Dame;
import pieces.Fou;
import pieces.Pion;
import pieces.Roi;
import pieces.Tour;
import utile.Coup;

public class Echiquier {
	
	private Piece[][] pieces=new Piece[8][8];
	
	private final List<Piece> piecesBlanches=new ArrayList<>(), piecesNoires=new ArrayList<>();
	
	private static int precision; // permet de connaitre la précision demandée au départ dans l'utilisation de l'IA
	
	private static final Plateau plateau=new Plateau(new String[]{"images/BLANC.gif","images/GRIS.gif","images/PB.gif","images/PN.gif","images/TB.gif","images/TN.gif",
			"images/CB.gif","images/CN.gif","images/FB.gif","images/FN.gif","images/DB.gif","images/DN.gif","images/RB.gif","images/RN.gif"},8);
	
	public static boolean renverse;
	
	private Echiquier() {}
	
	public Echiquier(boolean init) {
		if(init) init();
	}
	
	public Piece getPiece(int x, int y) {
		return pieces[x][y];
	}
	
	List<Piece> getPieces(boolean equipe) {
		if(equipe) return piecesBlanches;
		return piecesNoires;
	}
	
	/**
	 * ajoute la piece p dans la liste des piece
	 */
	void ajouterPiece(Piece p) {
		getPieces(p.getEquipe()).add(p);
	}
	
	void setPiece(Piece piece, int x, int y) {
		pieces[x][y]=piece;
	}
	
	private Point positionRoi(boolean equipe) {
		List<Piece> pieces=getPieces(equipe);
		for(Piece p : pieces)
			if(p instanceof Roi)
				return new Point(p.getX(),p.getY());
		return null;
	}
	
	/**
	 * initialisation début de partie
	 */
	private void init() {
		Piece.setEchiquier(this);
		new Tour(0,0,true); new Tour(0,7,true); new Tour(7,0,false); new Tour(7,7,false);
		new Cavalier(0,1,true); new Cavalier(0,6,true); new Cavalier(7,1,false); new Cavalier(7,6,false);
		new Fou(0,2,true); new Fou(0,5,true); new Fou(7,2,false); new Fou(7,5,false);
		new Dame(0,3,true); new Roi(0,4,true); new Dame(7,3,false); new Roi(7,4,false);
		for(int i=0; i<8; i++)
			new Pion(1,i,true);
		for(int i=0; i<8; i++)
			new Pion(6,i,false);
	}
	
	public boolean echec(boolean equipe) {
		Point roi=positionRoi(equipe);
		List<Piece> pieces=getPieces(!equipe);
		for(Piece p : pieces)
			if(p.deplacementPossible(roi.x, roi.y)) return true;
		return false;
	}
	
	public boolean mat(boolean equipe) {
		List<Piece> pieces=getPieces(equipe);
		for(Piece p : pieces)
			if(!p.deplacementsReelsPossibles().isEmpty()) return false;
		return true;
	}
	
	public Echiquier copie() {
		Echiquier ech=new Echiquier();
		//Echiquier tmp=Piece.getEchiquier();
		Piece.setEchiquier(ech);
		for(Piece p : piecesBlanches)
			p.copie();
		for(Piece p : piecesNoires)
			p.copie();
		Piece.setEchiquier(this);
		return ech;
	}
	
	public Coup getCoup(String coup, boolean joueur) {
		List<Piece> pieces=getPieces(joueur);
		for(Piece p : pieces) {
			Point dest=p.coups().get(coup);
			if(dest!=null) return new Coup(p.getX(),p.getY(),dest);
		}
		return null;
	}
	
	
	static int getPrecision() {
		return precision;
	}
	
	public static void setPrecision(int precision) {
		Echiquier.precision=precision;
	}
	
	/*public Coup meilleurCoup(boolean equipe) {
		Coup best=null;
		List<Piece> pieces=getPieces(equipe);
		int seuil;
		if(equipe) seuil=Integer.MIN_VALUE;
		else seuil=Integer.MAX_VALUE;
		for(Piece p : pieces) {
			List<Point> points=p.deplacementsReelsPossibles();
			for(Point point : points) {
				int val=p.valeurCoup(point, precision, seuil);
				if((equipe && val>seuil) || (!equipe && val<seuil)) {
					seuil=val;
					best=new Coup(p.getX(),p.getY(),point);
				}
			}
		}
		return best;
	}*/
	
	public String toString() {
		String ligne="+";
		for(int i=0; i<8; i++)
			ligne+="----+";
		ligne+='\n';
		String res=ligne;
		for(int i=0; i<8; i++) {
			res+='|';
			for(int j=0; j<8; j++)
				if(pieces[7-i][j]!=null) res+=" "+pieces[7-i][j]+" |";
				else res+="    |";
			res+='\n'+ligne;
		}
		return res;
	}
	
	public void affichage() {
		int[][] tab=new int[8][8];
		for(int i=0; i<8; i++)
			for(int j=0; j<8; j++) {
				int l, c;
				if(renverse) { l=i; c=7-j; }
				else { l=7-i; c=j; }
				Piece p=pieces[l][c];
				if(p==null)
					tab[j][i]=0;
				else {
					if(p instanceof Pion) {
						if(p.getEquipe()) tab[j][i]=3;
						else tab[j][i]=4;
					} else if(p instanceof Tour) {
						if(p.getEquipe()) tab[j][i]=5;
						else tab[j][i]=6;
					} else if(p instanceof Cavalier) {
						if(p.getEquipe()) tab[j][i]=7;
						else tab[j][i]=8;
					} else if(p instanceof Fou) {
						if(p.getEquipe()) tab[j][i]=9;
						else tab[j][i]=10;
					} else if(p instanceof Dame) {
						if(p.getEquipe()) tab[j][i]=11;
						else tab[j][i]=12;
					} else {
						if(p.getEquipe()) tab[j][i]=13;
						else tab[j][i]=14;
					}
				}
			}
		plateau.setJeu(tab);
		plateau.affichage();
	}
	
	private List<Coup> ensembleCoups(boolean equipe) {
		List<Coup> res=new ArrayList<>();
		List<Piece> pieces=getPieces(equipe);
		for(Piece p : pieces) {
			List<Point> points=p.deplacementsReelsPossibles();
			for(Point point : points) 
				res.add(new Coup(p.getX(), p.getY(), point));
		}
		return res;
	}
	
	private Coup[] triCoups(int precision, boolean equipe) {
		List<Coup> ref=ensembleCoups(equipe);
		int[] values=new int[ref.size()];
		for(int i=0; i<values.length; i++) {
			Coup c=ref.get(i);
			values[i]=pieces[c.dx][c.dy].valeurCoup(c.ax, c.ay, precision);
		}
		int[] indices=new int[values.length];
		for(int i=0; i<values.length; i++) indices[i]=i;
		int last=values.length-1;
		while(last!=0) {
			int last2=0;
			for(int i=0; i<last; i++)
				if((equipe && values[i]<values[i+1]) || (!equipe && values[i]>values[i+1])) {
					last2=i;
					int tmp=values[i];
					values[i]=values[i+1];
					values[i+1]=tmp;
					tmp=indices[i];
					indices[i]=indices[i+1];
					indices[i+1]=tmp;
				}
			last=last2;
		}
		Coup[] res=new Coup[values.length];
		for(int i=0; i<res.length; i++)
			res[i]=ref.get(indices[i]);
		return res;
	}
	
	public Coup meilleurCoup(boolean equipe) {
		Coup[] triCoups=triCoups(precision/2, equipe);
		int seuil;
		if(equipe) seuil=Integer.MIN_VALUE;
		else seuil=Integer.MAX_VALUE;
		Coup best=null;
		for(int i=0; i<triCoups.length; i++) {
			Coup c=triCoups[i];
			System.out.print(i+"/"+triCoups.length+"  "+c+" : ");
			int val=pieces[c.dx][c.dy].valeurCoup(c.ax, c.ay, precision, seuil);
			System.out.println(val);
			if((equipe && val>seuil) || (!equipe && val<seuil)) {
				seuil=val;
				best=c;
			}
		}
		return best;
	}
	
	/*private int valeurCoup(Coup c, int precision, boolean equipe, int seuil) {
		Echiquier e=copie();
		Piece.setEchiquier(e);
		e.pieces[c.dx][c.dy].deplacement(c.ax, c.ay);
		if(precision==0 || e.mat(!equipe)) {
			int value=e.evalPosition(!equipe);
			Piece.setEchiquier(this);
			return value;
		}
		Coup[] coups=e.triCoups(precision/2, !equipe);
		int seuilSuivant;
		if(equipe) seuilSuivant=Integer.MAX_VALUE;
		else seuilSuivant=Integer.MIN_VALUE;
		for(Coup c2 : coups) {
			int value=e.valeurCoup(c2, precision-1, !equipe, seuilSuivant);
			if((equipe && value<=seuil) || (!equipe && value>=seuil)) {
				Piece.setEchiquier(this);
				if(equipe) return Integer.MAX_VALUE;
				return Integer.MIN_VALUE;
			}
			if((equipe && value<seuilSuivant) || (!equipe && value>seuilSuivant))
				seuilSuivant=value;
		}
		Piece.setEchiquier(this);
		return seuilSuivant;
	}
	
	private int valeurCoup(Coup c, boolean equipe, int precision) {
		Echiquier e=copie();
		Piece.setEchiquier(e);
		e.pieces[c.dx][c.dy].deplacement(c.ax, c.ay);
		if(precision==0 || e.mat(!equipe)) {
			int value=e.evalPosition(!equipe);
			Piece.setEchiquier(this);
			return value;
		}
		Coup[] coups=e.triCoups(precision/2, !equipe);
		int seuilSuivant;
		if(equipe) seuilSuivant=Integer.MAX_VALUE;
		else seuilSuivant=Integer.MIN_VALUE;
		for(Coup c2 : coups) {
			int value=e.valeurCoup(c2, precision-1, !equipe, seuilSuivant);
			if((equipe && value<seuilSuivant) || (!equipe && value>seuilSuivant))
				seuilSuivant=value;
		}
		Piece.setEchiquier(this);
		return seuilSuivant;
	}*/

	/**
	 * @param joueur joueur qui doit jouer au prochain tour
	 * @return évaluation de la position, plus c'est grand, plus c'est favorable aux blancs, si echec et mat renvoie 1000 ou -1000
	 */
	int evalPosition(boolean joueur) {
		/*if(mat(joueur)) {
			if(echec(joueur)) {
				if(joueur) return -10000;
				return 10000;
			}
			return null;
		}*/
		return evalMateriel()+evalDeveloppement()+evalCtrlCentre()+coeffPieces();
	}
	
	private int coeffPieces() {
		int res=0;
		for(Piece p : piecesBlanches) 
			res+=p.coeff();
		for(Piece p : piecesNoires)
			res-=p.coeff();
		return res;
	}
	private int evalCtrlCentre() {
		int res=0;
		for(Piece p : piecesBlanches) {
			if(p.deplacementPossible(4, 3)) res++;
			if(p.deplacementPossible(4, 4)) res++;
		}
		for(Piece p : piecesNoires) {
			if(p.deplacementPossible(3, 3)) res--;
			if(p.deplacementPossible(3, 4)) res--;
		}
		int nbPieces=piecesBlanches.size()+piecesNoires.size();
		if(nbPieces>25) return res*12;
		if(nbPieces>17) return res*8;
		if(nbPieces>9) return res*4;
		return res;
	}

	private int evalMateriel() {
		int res=0;
		for(Piece p : piecesBlanches)
			res+=p.valeurMateriel();
		for(Piece p : piecesNoires)
			res+=p.valeurMateriel();
		return res;
	}
	
	private int evalDeveloppement() {
		int res=0;
		for(Piece p : piecesBlanches)
			res+=p.deplacementsPossibles().size();
		for(Piece p : piecesNoires)
			res-=p.deplacementsPossibles().size();
		return res;
	}
}
