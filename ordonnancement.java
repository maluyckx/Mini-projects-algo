/*
INFO-F203-0-202021
Luyckx Marco-496283
Projet INFO-F203 partie ordonnancement

But du programme :
Répondre à la question : Est-il possible de répartir un groupe de tâches sur un nombre k de machine donné
en paramètre tout en sachant que ces tâches doivent être effectuées à un moment précis (temps de début et de fin) ?

Entrées : deux paramètres sont necéssaires au programme :
- fichier contenant le graphe où la première ligne est le nombre de tâches et toutes les autres lignes
reprennent les indices des tâches ainsi que leur temps de début et de fin.
- un nombre de machine à tester

Sorties :
- si toutes les tâches peuvent effectivement être placées avec le nombre de machine donné, alors on imprimera
une liste avec l'indice de la tâche et le numéro de la machine où la tâche est placée.
- sinon rien ne sera imprimer
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class ordonnancement {

    private int nbrTaches;
    private final int nbrMachine;
    private GrapheO listeTaches;
    private float[] listeMachine; // contient le temps de la dernière tâche effectuée sur la machine i

    public ordonnancement(String file, int nbrMachine) {
        /* constructeur*/
        this.nbrMachine = nbrMachine;
        if ( nbrMachine <= 0) return;
        try {
            setText(file);
        } catch (IOException e) {
            System.out.println("Erreur dans le fichier.");
        }
        listeTaches.sorting();
    }

    public void setText(String file) throws IOException {
        /* Lecture du fichier + remplissage des attributs*/
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        this.nbrTaches = Integer.parseInt(br.readLine());
        this.listeTaches = new GrapheO(nbrTaches); 
        this.listeMachine = new float[nbrMachine];
        Arrays.fill(listeMachine, (float) 0); // remplit l'array de 0
        while ((line = br.readLine()) != null) {
            String[] spliter = line.split(" "); // j'aurais pu l'imbriquer dans l'instruction suivante, mais je préfère la clarté
            setListeTaches(spliter);
        }
        br.close();
    }
    public void setListeTaches(String[] spliter){
        if (Float.parseFloat(spliter[2]) >= Float.parseFloat(spliter[1]) && Float.parseFloat(spliter[1]) >= 0 && Float.parseFloat(spliter[2]) >= 0 ) {
            listeTaches.addIntoGraph(Integer.parseInt(spliter[0]) - 1,Float.parseFloat(spliter[1]),Float.parseFloat(spliter[2]));
        }
        else{
            System.out.println("Erreur de date ou de numéro de tâches.");
            System.exit(0);
        }
    }

    public void findOrdo(){
        /* Fonction principale du programme
         * On vérifie si le temps de début de tâche est supérieur au temps de la dernière tâche effectuée sur la machine.
         * Si il l'est, on insère la tâche sur la machine et on met à jour le dernier temps de la machine.
         * Si il ne l'est pas, on passe à la machine suivante et on refait la méthode.
         * Si aucune machine n'est libre pour une tâche donnée, le nombre de machine n'est pas assez elevé.
         * Si toutes les tâches sont insérées sans accro, on imprime la solution.
         * */
        for(int i = 0; i < nbrTaches; i++) {
            boolean inserted = false;
            int j = 0;
            while( j < nbrMachine && !inserted){
                if (listeMachine[j] <= listeTaches.getSommet(i,0)){ // si le temps de début de tâche est supérieur/égal au dernier temps enregistré sur la machine
                    inserted = true;
                    listeMachine[j] = listeTaches.getSommet(i,1); // mise a jour du dernier temps de la machine avec le temps de fin de la tâche
                    listeTaches.setMachine(i,j+1); 
                }
                j++;
            }
            if (!inserted) System.exit(0);// trop peu de machines disponibles
        }

        for( int i = 0; i < nbrTaches ; i++){ // impression de la solution trouvée
            for (int j = 0; j < nbrTaches ; j++){
                if (((int)listeTaches.getSommet(j,3)) == i)
                    System.out.println((int)listeTaches.getSommet(j,3)+1 + " " + (int)listeTaches.getSommet(j,2));
            }
        }
    }


    public static void main(String[] args) {
        try {
            ordonnancement ordo = new ordonnancement(args[0], Integer.parseInt(args[1]));
            ordo.findOrdo();
        }
        catch (Exception e){
            System.out.println("Erreur de paramètres ou lors de l'exécution du programme.");
        }
    }
}



class GrapheO {
    /* Structure de données pour représenter un graphe */

    private final float[][] listeTaches;

    public GrapheO(int nbrTaches){
        this.listeTaches = new float[nbrTaches][4];
    }
    public void addIntoGraph(int indice, float debut, float fin){
        listeTaches[indice][0] = debut;
        listeTaches[indice][1] = fin;
        listeTaches[indice][2] = 0.f; // aucune machine par défaut
        listeTaches[indice][3] = (float) indice; // pour l'affichage, pour retrouver après le sort
    }
    public void setMachine(int sommet,float machine){
        listeTaches[sommet][2] = machine;
    }
    public float getSommet(int sommet, int caracteristique){
        return listeTaches[sommet][caracteristique];
    }
    public void sorting(){
        Arrays.sort(listeTaches, java.util.Comparator.comparingDouble(a -> a[0]));
    }
}

