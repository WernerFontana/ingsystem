/**
* Classe AbstractRungeKuttaNG.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

public class AbstractRungeKuttaNG {
	
    /// <summary>
    /// current state could be read inside integration step and could be written outside integration step
    /// </summary>
    public  IContinuousState curState; 

    /// <summary>
    /// current rate must be written inside integration step and could be read as smoothed rate (RO) outside integration step
    /// </summary>
    public IContinuousRate  curRate; 

    /// <summary>
    /// State as saved at step beginning
    /// </summary>
    private IContinuousState memState;

    /// <summary>
    /// Rate Sum as stored during integration step
    /// </summary>
    private IContinuousRate memRate;

    public AbstractRungeKuttaNG(IContinuousState state, IContinuousRate rate) {
        curState = state;
        memState = (IContinuousState) state.clone();

        // Set the current rate and rate sum to zero.
        // SUBTIL : Since we do not know how the quantity is represented, we create a rate with the same structure
        // as the state (by Cloneage), and multiply ask it to multiply itself by 0.
        curRate  = rate;
        CurRate().Mult(0);
        memRate  = (IContinuousRate) curRate.clone() ;

	}

	/// <summary>
    /// Computes the integration variable increment to apply for runge kutta at order
    /// <c>order</c> and for pass n°<c>pass</c> if we need to get to global increment <c>dt</c>
    /// </summary>
    /// <param name="order">Order of the integration (betwenne </param>
    /// <param name="pass">Pass number (between 1 and 3 included), depending on the order of the integration</param>
    /// <param name="dt">Integration variable increment to apply</param>
    /// <returns></returns>
    public static double TimeIncrement (int order, int pass, double dt)
    {
      switch (order)
      {
        case 1:
        switch (pass)
        {
          case 1: return dt;
          default: return 0;
        }
        case 2: 
        switch (pass)
        {
          case 1: return dt;
          default: return 0;
        }
        case 3 :
        switch (pass)
        {
          case 1: return 0.5*dt;
          case 2: return 0.5*dt;
          default: return 0;
        }
        case 4:
        switch (pass)
        {
          case 1: return 0.5*dt;
          case 3: return 0.5*dt;
          default: return 0;
        }
        default: return 0;
      }
    }

	public IContinuousState CurState() {
		return curState;
	}

	public IContinuousRate CurRate() {
		return curRate;
	}

    /// <summary>
    /// Realized a pass of the Runge Kutta algorithm
    /// Update state for specified integation order and specified pass for an integration
    /// variable increment of dt.
    /// </summary>
    /// <remarks>This is the main part of the algorithm.</remarks>
    /// <param name="order">Order of the integration (betwenne </param>
    /// <param name="pass">Pass number (between 1 and 3 included), depending on the order of the integration</param>
    /// <param name="dt">Integration variable increment to apply</param>
    public void UpdateState(int order, int pass, double dt)
    {
      switch (order)
      {
        case 1:
        switch (pass)
        {
          case 1: 
            CurState().Update(CurRate(),dt);
            break;
          default: break;
        }
          break;
        case 2: 
        switch (pass)
        {
          case 1: 
            MemState().CopyFrom(CurState());
            CurState().Update(CurRate(),dt);
            MemRate().CopyFrom(CurRate());
            break;
          case 2: 
            CurRate().Affine(MemRate(), 1.0);
            CurRate().Mult(0.5);
            CurState().CopyFrom(MemState());
            CurState().Update(CurRate(), dt);
            break;
        }
          break;
        case 3 :
        switch (pass)
        {
          case 1: 
            MemState().CopyFrom(CurState());
            CurState().Update(CurRate(), 0.5*dt);
            MemRate().CopyFrom(CurRate());
            break;
          case 2: 
            CurState().CopyFrom(MemState());
            CurState().Update(CurRate(), 2.0*dt);
            CurState().Update(MemRate(), -dt);
            MemRate().Affine(CurRate(), 4.0);
            break;
          case 3: 
            CurRate().Affine(MemRate(), 1.0);
            CurRate().Mult(1.0/6.0);
            CurState().CopyFrom(MemState());
            CurState().Update(CurRate(), dt);
            break;
          default: break;
        }
          break;
        case 4:
        switch (pass)
        {
          case 1: 
            MemState().CopyFrom(CurState());
            CurState().Update(CurRate(), 0.5*dt);
            MemRate().CopyFrom(CurRate());
            break;
          case 2:
            CurState().CopyFrom(MemState());
            CurState().Update(CurRate(), 0.5*dt);
            MemRate().Affine(CurRate(), 2.0);
            break;
          case 3:
            CurState().CopyFrom(MemState());
            CurState().Update(CurRate(), dt);
            MemRate().Affine(CurRate(), 2.0);
            break;
          case 4:
            CurRate().Affine(MemRate(), 1.0);
            CurRate().Mult(1.0/6.0);
            CurState().CopyFrom(MemState());
            CurState().Update(CurRate(), dt);
            break;
          default: break;
        }
          break;
        default: break;
      }
    }


	private IContinuousRate MemRate() {
		return memRate;
	}

	private IContinuousState MemState() {
		return memState;
	}
}
