package affichage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * La classe Plateau permet d'afficher un plateau de Jeu carré
 * sur lequel sont disposés des images représentant les éléments du jeu
 * Les images sont toutes de même taille et carrées
 * @author M2103-Team
 */
public class Plateau extends JPanel{
	private static final long serialVersionUID = 1L;
	private int nbImages;
	private ImageIcon[] images;
	private int dimImage;
	private int[][] jeu;
	
	/**
	 * Constructeur de la classe Plateau qui crée un plateau de jeu 
	 * vide de dimension taille x taille cellules vides
	 * @param gif tableau 1D des chemins des fichiers des différentes images affichées
	 * @param taille dimension d'un côté du plateau
	 */
	public Plateau(String[] gif,int taille){
		jeu=new int[taille][taille];
		JFrame plateau=new JFrame();
		if (gif!=null){
		nbImages=gif.length;
		images=new ImageIcon[nbImages];	
		for (int i=0;i<nbImages;i++) images[i]=new ImageIcon(gif[i]);
		dimImage=images[0].getIconHeight()+2;
		plateau.setTitle("Plateau de jeu ("+taille+"X"+taille+")");
		plateau.setSize(taille*dimImage+50,taille*dimImage+50);
		//plateau.setLocationRelativeTo(null);
		plateau.setLocation(150, 200);
		plateau.setLayout(new FlowLayout());
		plateau.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setPreferredSize(new java.awt.Dimension(taille*(dimImage),taille*(dimImage)));
		this.setBackground(Color.BLACK);
		plateau.getContentPane().add(this);
		plateau.setVisible(true);
		}
	}
	/**
	 * Méthode d'affichage du composant
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Dimension taille=this.getSize();
		int x=2,y=1,lg=0,col=0;
		g.setColor( Color.white );
		while (y<taille.height){
			while (x<taille.width){
				if(col%2==0 ^ lg%2==0) {
					g.drawImage(images[1].getImage(),x,y,null);
				} else {
					g.drawImage(images[0].getImage(),x,y,null);
				}
				if (jeu[col][lg]!=0) {
					g.drawImage(images[jeu[col][lg]-1].getImage(),x,y,null);
				}/* else {
					g.setColor(Color.red);
					g.drawImage(images[jeu])
					g.drawImage(images[2].getImage(),x,y,null);
				}*/
				x+=dimImage;
				col++;
			}
			lg++;
			col=0;
			x=2;
			y+=dimImage;
		}
	}
	/**
	 * Méthode permettant de placer les éléments sur le plateau
	 * @param jeu tableau 2D représentant le plateau  
	 * la valeur numérique d'une cellule désigne l'image correspondante
	 * dans le tableau des chemins (décalé de un, 0 désigne ne case vide)
	 */
	public void setJeu(int[][] jeu){
		this.jeu=jeu;	
	}
	/**
	 * Retourne le tableau d'entiers représentant le plateau
	 * @return le tableau d'entiers
	 */
	public int[][] getJeu(){return jeu;}
			
	/**
	 * Méthode d'affichage
	 */
	public void affichage(){ repaint(); }
}
