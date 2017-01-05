/**
* Classe IRecordable.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.utility;

public interface IRecordable {
	/*
	 * Renvoie les entêtes correspondants aux données enregistrées => permet de donner un nom aux variables du records
	 */
	String[] getTitles();
	
	/*
	 * renvoie les données sous forme de chaines des données
	 */
	String[] getRecords();
	
	/*
	 * permet de catégoriser l'enregistrement. Sera utilisé pour classer les données enregistrées
	 */
	String getClassement();
}

