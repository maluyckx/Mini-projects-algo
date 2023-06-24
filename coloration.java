/*
INFO-F203-0-202021
Luyckx Marco-496283
Projet INFO-F203 partie coloration

But du programme :
Répondre à la question : Est-ce que le nombre chromatique d'un graphe est inférieur, égale ou supérieur
à un nombre k donné en paramètre ?

Entrées : deux paramètres sont necéssaires au programme :
- fichier contenant le graphe où la première ligne est le nombre de sommets et toutes les autres lignes
correspondent aux paires de sommets adjacents
- un entier positif à tester

Sorties :
- si le nombre chromatique est inférieur ou égal, alors on imprimera la liste de sommets avec leurs couleurs
correspondantes
- sinon rien ne sera imprimer
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

public class coloration{

    private int nbrSommets;
    private int nbrChroma; // valeur qu'on veut vérifier
    private GrapheC listeSommets;
    int[] couleurSommet;

    public coloration(String file, int nbrChroma){
        /* Constructeur*/
        if ( nbrChroma <= 0) return;
        try {
            setText(file);
        }
        catch (IOException e){
            System.out.println("Erreur dans le fichier.");
        }
        this.nbrChroma = nbrChroma;
        this.couleurSommet = new int[nbrSommets];
        for (int i = 0; i < nbrSommets; i++) { // on commence par assigner une couleur 0 à tous les sommets
            couleurSommet[i] = 0;
        }
    }
    public void setText(String file) throws IOException {
        /* Lecture du fichier + remplissage des attributs*/
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        this.nbrSommets = Integer.parseInt(br.readLine());
        this.listeSommets = new GrapheC(nbrSommets);
        while ((line = br.readLine()) != null) {
            String[] spliter = line.split(" ");
            listeSommets.addIntoGraph(Integer.parseInt(spliter[0]) - 1,Integer.parseInt(spliter[1]) - 1);
        }
        br.close();
    }
    public void findChroma(){
        if (backtrack( 0)) {
            for (int i = 0; i < nbrSommets; i++) {
                System.out.println((i+1) +" "+ couleurSommet[i] );
            }
        }
    }

    boolean backtrack(int indiceSommet) {
        /* Fonction principale du programme.
        * itére sur tous les sommets en essayant de leur assigner une couleur.
        * Si impossible, on revient sur le sommet précédent et on essaye une autre couleur.
        * Si possible, on colore le sommet et on passe au sommet suivant.
        * Si on parvient à colorer tous les sommets, il y a bien une coloration pour le nombre chromatique donné.
        * */
        boolean isColored = false;
        if (nbrSommets == indiceSommet){ // quand tous les sommets sont colorés
            isColored = true;
        }
        else {
            int indiceCouleur = 1; // on commence avec la première couleur
            while( indiceCouleur <= nbrChroma && !isColored){
                if (isColorable(indiceSommet, indiceCouleur)) { // vérification que l'on peut colorer le sommet avec la couleur 
                    couleurSommet[indiceSommet] = indiceCouleur;
                    if (backtrack(indiceSommet + 1)){ // l'appel récursif sur le prochain sommet
                        isColored = true;
                    }
                    else {
                        couleurSommet[indiceSommet] = 0; // on retire la couleur
                    }
                }
                indiceCouleur++;
            }
        }
        return isColored;
    }
    boolean isColorable(int indiceSommet, int couleur) {
        /* Vérifie si le sommet est colorable avec la couleur donnée.*/
        boolean isColorable = true;
        int i = 0;
        while ( i < nbrSommets && isColorable){
            if (listeSommets.getSommet(indiceSommet,i) == 1 && couleur == couleurSommet[i]){
                isColorable = false;
            }
            i++;
        }
        return isColorable;
    }

    public static void main(String[] args) {
        try {
            coloration colo = new coloration(args[0], Integer.parseInt(args[1]));
            colo.findChroma();
        }
        catch(Exception e){
            System.out.println("Erreur de paramètres ou lors de l'exécution du programme.");
        }
    }
}

class GrapheC {
    /* Structure de données pour représenter un graphe */

    private final int[][] listeSommets;

    public GrapheC(int nbrSommets){ // matrice d'ajdacence
        this.listeSommets = new int[nbrSommets][nbrSommets];
    }
    public void addIntoGraph(int indiceSommet, int indiceAutreSommet){
        listeSommets[indiceSommet][indiceAutreSommet] = 1;
        // il faut bien sûr compléter le sommet complémentaire
        listeSommets[indiceAutreSommet][indiceSommet] = 1;
    }
    public int getSommet(int indiceSommet, int indiceAutreSommet){
        return listeSommets[indiceSommet][indiceAutreSommet];
    }
}
