/** 
 * Class Monitor 
 * To synchronize dining philosophers. 
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca  
 
 */
import java.util.ArrayDeque;
import java.util.Queue;
public class Monitor   
{
	/* 
	 * Data members 
	 */
	
	//number of philosophers
	private final int iNumberOfPhilosophers;
	
	//states of each philosopher
	private enum State { THINKING, HUNGRY, EATING }
	//state of each philosopher index
	private final State[] aState;
	// True if some philosopher is currently talking, false otherwise.
	private boolean bIsTalking = false;
	
	// Queue for fair eating (stores 0-based philosopher indices)
	private final Queue<Integer> eatQueue = new ArrayDeque<>();

	// Queue for fair talking (stores 0-based philosopher indices)
	private final Queue<Integer> talkQueue = new ArrayDeque<>();
	


	/**
	 * Constructor
	 * @param piNumberOfPhilosophers number of philosophers 
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		iNumberOfPhilosophers = piNumberOfPhilosophers;

		// Initialize state array
		aState = new State[iNumberOfPhilosophers];
		//everybody starts THINKING
		for (int i = 0; i < iNumberOfPhilosophers; i++)
		{
			aState[i] = State.THINKING;
		}
	}
	
	//converting philosophers ID (1 base to 0 base)
	private int indexFromTID(final int piTID)
	{
		return piTID - 1;
	}
	
	//left neighbor
	private int left(int i)
	{
		return (i + iNumberOfPhilosophers - 1) % iNumberOfPhilosophers;
	}
	
	//right neighbor
	private int right(int i)
	{
		return (i + 1) % iNumberOfPhilosophers;
	}
	
	
	/*
	 *       TEST:
	 * if phi i is hungry 
	 * right is not eating 
	 * left is not eating
	 */
	private boolean canEat(int i)
	{
		return aState[i] == State.HUNGRY
			&& aState[left(i)] != State.EATING
			&& aState[right(i)] != State.EATING;
	}
	
	
	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	
	/**
	 * This mathod is synchronized so that checking state and changing it are done atomically 
	 * no deadlock can arise from partial actions.
	 * 
	 * 
	 * @param piTID TID of the calling philosopher (1-based).
	 */
	public synchronized void pickUp(final int piTID)
	{
		// This philosopher is hungry
		int i = indexFromTID(piTID);
		aState[i] = State.HUNGRY;
		
	    // Join the waiting queue if not already there
	    if (!eatQueue.contains(i))
	    {
	        eatQueue.add(i);
	    }

		try
		{
		     /*
	         * Wait while:
	         *  - this philosopher cannot safely eat, OR
	         *  - it is not this philosopher's turn in the queue.
	         * Using 'while' handles spurious wakeups.
	         */
	        while (!canEat(i) || eatQueue.peek() == null || eatQueue.peek() != i)
	        {
	            wait();
	        }

			// safe to eat
			aState[i] = State.EATING;
		}
		catch (InterruptedException e)
		{
			System.err.println("Monitor.pickUp():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
		
		
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 * 
	 * @param piTID TID of the calling philosopher (1-based).
	 */
	public synchronized void putDown(final int piTID)
	{
		int i = indexFromTID(piTID);
		
		// Philosopher goes back to thinking and releases both chopsticks
		aState[i] = State.THINKING;
		
		// Remove from the queue (we expect this philosopher to be at the head)
	    if (eatQueue.peek() != null && eatQueue.peek() == i)
	    {
	        eatQueue.remove();
	    }
	    else
	    {
	        // Fallback in case of any inconsistency
	        eatQueue.remove(i);
	    }
		/*
		 * Notify all waiting philosophers that the state has changed.
		 * Each waiting philosopher will test again if they can eat
		 * in their pickUp() while loop, which helps avoid starvation
		 * under fair scheduling.
		 */
		notifyAll();
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 * 
	 * block if another is talking
	 * if safe start talking
	 */
	public synchronized void requestTalk(final int piTID)
	{
		int i = indexFromTID(piTID);

	    // Join the talk queue if not already there
	    if (!talkQueue.contains(i))
	    {
	        talkQueue.add(i);
	    }
		try
		{
			/*
			 * While someone else is talking, wait.
			 *  - someone else is talking, OR
	         *  - it is not this philosopher's turn in the talk queue.
	         */
	        while (bIsTalking || talkQueue.peek() == null || talkQueue.peek() != i)
	        {
	            wait();
	        }

			// This philosopher is now talking
			bIsTalking = true;
		}
		catch (InterruptedException e)
		{
			System.err.println("Monitor.requestTalk():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk(final int piTID)
	{
		int i = indexFromTID(piTID);
			// Release the talking token
			bIsTalking = false;
			
			// Remove from the talk queue
		    if (talkQueue.peek() != null && talkQueue.peek() == i)
		    {
		        talkQueue.remove();
		    }
		    else
		    {
		        talkQueue.remove(i);
		    }

			// Wake up any philosophers waiting to talk (or eat),
			// so they can re test their conditions.
			notifyAll();
	}
}

// EOF
