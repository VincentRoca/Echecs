package affichage;


import java.util.Stack;

import javax.swing.JOptionPane;

import jeu.Echiquier;
import jeu.Piece;
import utile.Coup;

class Partie {

	private static Echiquier ech=new Echiquier(true);

	private static final Stack<Echiquier> pile=new Stack<>();

	public static void main(String[] args) {
		ech.affichage();
		if(JOptionPane.showConfirmDialog(null, "Voulez-vous retourner l'échiquier ?","",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
			Echiquier.renverse=true;
			ech.affichage();
		}
		boolean joueur=true;
		while(!ech.mat(joueur)) {
			ech.affichage();
			String annonceTour;
			if(joueur) annonceTour="C'est aux blancs de jouer,";
			else annonceTour="C'est aux noirs de jouer,";
			if(!pile.isEmpty() && JOptionPane.showConfirmDialog(null, annonceTour+" revenir position précédente ?","",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
				ech=pile.pop();
				Piece.setEchiquier(ech);
			} else {
				pile.push(ech);
				ech=ech.copie();
				Piece.setEchiquier(ech);
				Coup coup;
				if(JOptionPane.showConfirmDialog(null, "Voulez-vous utilisez l'IA ?", "Choix prochain coup",JOptionPane.YES_NO_OPTION)
						==JOptionPane.YES_OPTION) {
					Echiquier.setPrecision(Integer.valueOf(JOptionPane.showInputDialog("Entrez la précision de l'IA")));
					coup=ech.meilleurCoup(joueur);
				} else {
					coup=choixJoueur(joueur);
				}
				ech.getPiece(coup.dx, coup.dy).deplacement(coup.ax, coup.ay);
			}
			joueur=!joueur;
		}
		ech.affichage();
		if(ech.echec(joueur)) {
			if(joueur) JOptionPane.showMessageDialog(null, "Les noirs ont gagné !");
			else JOptionPane.showMessageDialog(null, "Les blancs ont gagné !");
		} else 
			JOptionPane.showMessageDialog(null, "Partie nulle");
	}

	private static Coup choixJoueur(boolean joueur) {
		Coup res=null;
		do {
			res=ech.getCoup(JOptionPane.showInputDialog("Entrez votre coup"),joueur);
		} while(res==null);
		return res;
	}
}
