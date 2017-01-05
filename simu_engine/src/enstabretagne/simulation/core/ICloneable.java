/**
* Classe ICloneable.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;
// Résumé :
//     Prend en charge le clonage, qui crée une nouvelle instance d'une classe avec
//     la même valeur qu'une instance existante.
public interface ICloneable extends Cloneable
{
    // Résumé :
    //     Crée un nouvel objet qui est une copie de l'instance en cours.
    //
    // Retourne :
    //     Nouvel objet qui est une copie de cette instance.
    Object clone();
}

