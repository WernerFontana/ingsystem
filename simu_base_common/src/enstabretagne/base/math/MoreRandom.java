/**
* Classe MoreRandom.java
*@author Olivier VERRON
*@version 1.0.
*/
/**
 * Fournit les classes nécessaires pour effectuer
 * des simulations à événements discrets basiques.
 * @author 	Pascal CANTOT (cantot@wanadoo.fr]
 * @version	1.0
 */
package enstabretagne.base.math;

import java.util.Random;
import java.lang.System;


/** Etend les fonctions de génération de nombre aléatoires.
 *  Fournit notamment des générateurs suivant différentes lois.
 *  @see java.util.Random
 *  @author cantot@wanadoo.fr
 */
public class MoreRandom extends Random {

	
		public static final long serialVersionUID = 1L;

		public static long globalSeed=0;
		
		private static long initialSeed = 0L;
		
		/**
		 * Constructeur par défaut.
		 * Le générateur aléatoire sera initialisé avec un germe "aléaloire" (horloge système)
		 */
		public MoreRandom()
		{
			
			super();
			initialSeed = globalSeed;
		}

		/**
		 * Constructeur initialisé avec un germe aléatoire explicité.
		 * @param seed germe.
		 */
		public MoreRandom(long seed)
		{
			super(seed);
			initialSeed = seed;
		}

		/**
		 * Réinitialise le germe à la valeur indiquée.
		 * @param seed germe.
		 */
		public void setSeed(long seed)
		{
			super.setSeed(seed);
			initialSeed = seed;
		}

		/**
		 * Retourne le germe initial.
		 * @return germe.
		 */
		public long getSeed()
		{
			return initialSeed;
		}

		/**
		 * Initialise le germe à une valeur aléatoire,
		 * construite à partir de l'heure système.
		 * Fournit une valeur de germe différente du germe précédent à chaque exécution,
		 * même lors de deux appels se succédant à moins d'une milliseconde d'écart.
		 * @return la valeur du germe.
		 */
		public long randomize()
		{
			long newSeed;
			
			// Boucle pour éviter d'avoir un germe identique si appels trop rapprochés.
			// Inconvénient: prend jusqu'à 1ms maxi à s'exécuter (rarement)
			// Avantage: très rapide dans la plupart des cas
			// (donc peut-être mieux qu'un thread.sleep(1);)
			do
			{
				newSeed = System.currentTimeMillis();
			} while (newSeed == initialSeed);
			setSeed(newSeed);
			return newSeed;		
		}

		/**
		 * Générateur pseudo-aléatoire suivant la loi uniforme sur [0,1].
		 * @return un nombre pseudo-aléatoire entre 0.0 et 1.0, suivant la loi uniforme
		 */
		public double nextUniform()
		{
			return super.nextDouble();
		}
		
		/**
		 * Générateur pseudo-aléatoire suivant la loi uniforme sur [a,b].
		 * @return	un nombre pseudo-aléatoire entre <code>a</code> et <code>b</code>,
		 * 			suivant la loi uniforme
		 */
		public double nextUniform(double a, double b)
		{
			return a + (b-a)*nextDouble();
		}

		/**
		 * Générateur pseudo-aléatoire suivant la loi triangulaire.
		 * @param	a	borne inférieure;
		 * @param	b	abscisse de la densité de probabilité maximale;
		 * @param	c	borne supérieure.
		 * @return	un nombre pseudo-aléatoir entre <code>a</code> et <code>c</code>
		 * 			suivant une loi triangulaire(a,b,c).
		 */
		public double nextTriangle(double a, double b, double c)
		{
			double beta;
			double t;
			double u;

			// Méthode de la transformée inverse
			u = nextUniform();
			beta = (b-a)/(c-a);
			if (u < beta)
				t = Math.sqrt(beta*u);
			else
				t = 1.0 - Math.sqrt((1-beta)*(1-u));

			return a + (c-a)*t;
		}

		/**
		 * Générateur pseudo-aléatoire suivant la loi exponentielle.
		 * @param	lambda	paramètre de la loi
		 * @return	un nombre pseudo-aléatoir suivant la loi exponentielle.
		 */
		public double nextExp(double lambda)
		{
			return (-1.0/lambda)*Math.log(1-nextUniform());
		}
		
		private static void doTest()
		{
			int i;
			MoreRandom alea = new MoreRandom();
			
			System.out.println("*** TEST CLASSE moreRandom ***");
			
			// Teste les générateurs
			System.out.print("Loi uniforme [0;1]: ");
			System.out.println();
			for (i=0; i<10; i++)
				System.out.print(" " + alea.nextUniform());
			System.out.println();
			
			System.out.print("Loi uniforme [-10;10]: ");
			System.out.println();
			for (i=0; i<10; i++)
				System.out.print(" " + alea.nextUniform(-10.0,10.0));
			System.out.println();
			
			System.out.print("Après changement de germe: ");
			alea.setSeed(1234L);
			System.out.println("(germe:" + alea.getSeed()+ ")");
			for (i=0; i<10; i++)
				System.out.print(" " + alea.nextUniform());
			System.out.println();

			System.out.print("Après randomize: ");
			alea.randomize();
			System.out.println("(germe:" + alea.getSeed()+ ")");
			for (i=0; i<10; i++)
				System.out.print(" " + alea.nextUniform());
			System.out.println();

			System.out.print("Après randomize (bis): ");
			alea.randomize();
			System.out.println("(germe:" + alea.getSeed()+ ")");
			for (i=0; i<10; i++)
				System.out.print(" " + alea.nextUniform());
			System.out.println();

			System.out.print("Après reprise du germe: ");
			alea.setSeed(1234L);
			System.out.println("(germe:" + alea.getSeed()+ ")");
			for (i=0; i<10; i++)
				System.out.print(" " + alea.nextUniform());
			System.out.println();

			System.out.print("Loi triangulaire(0,5,10): ");
			System.out.println();
			for (i=0; i<10; i++)
				System.out.print(" " + alea.nextTriangle(0,5,10));
			System.out.println();
			
			System.out.print("Loi exponentielle(1.0/10.0): ");
			System.out.println();
			for (i=0; i<10; i++)
				System.out.print(" " + alea.nextExp(1.0/10.0));
			System.out.println();
			
		}

		public static void main(String[] args)
		{
			doTest();
		}

		
}  // Class MoreRandom




