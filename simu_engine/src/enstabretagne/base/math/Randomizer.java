/**
* Classe Randomizer.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.math;

import java.util.Random;

/**
 * @author Audoli
 *
 */
public class Randomizer {

    static final double ProbMin = 1.0E-12;
    static final double ProbMax = 1.0 - ProbMin;

    public Randomizer(Random r){
		
	}

    
	/**
	 * 
	 * @param replicaSeed
	 * @param mixer
	 * @return
	 */
	public static int GetSeed(int replicaSeed, String mixer) {
	      int seed = replicaSeed;
	      if (seed == 0)
	        seed = (int) System.currentTimeMillis();

	      if (mixer == null)
	        return seed;

	      boolean b = false;
	      for(int i=0;i<mixer.length(); i++) // Use a specific (but portable) HashCode 
	      {
	    	int n = mixer.codePointAt(i);
	        b = !b;
	        if (b)
	          seed += n;
	        else
	          seed *= n;
	      }
	      return seed;   
	}

	public String RandomGeneratorType() {
		// TODO Auto-generated method stub
		return "RandomADeterminer";
	}
}

