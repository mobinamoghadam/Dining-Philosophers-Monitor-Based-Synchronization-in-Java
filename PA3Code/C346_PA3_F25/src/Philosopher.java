import common.BaseThread;  

/**
 * Class Philosopher.
 * Outlines main subrutines of our virtual philosopher.  
 * 
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca  
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)   
	 */
	public static final long TIME_TO_WASTE = 1000; 

	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating. 
	 * - Then sleep() for a random interval.
	 * - The print that they are done eating.
	 */
	public void eat()
	{
		try
		{
			//Announcing that the philosopher has started eating
			System.out.println("Philosopher "+ getTID()+"starts eating.");
			sleep((long)(Math.random() * TIME_TO_WASTE));
			//Philosopher has done eating
			System.out.println("Philosopher "+ getTID()+"has finished eating.");
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - Then sleep() for a random interval.
	 * - The print that they are done thinking.
	 */
	public void think()
	{
		try {
		//Philosopher has started thinking
		System.out.println("Philosopher "+ getTID()+"starts thinking.");
		sleep((long)(Math.random() * TIME_TO_WASTE));
		//phil has finished thinking
		System.out.println("Philosopher "+ getTID()+"has finished thinking.");
		}
		
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.think():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
		
	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - Say something brilliant at random
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		try
	    {
	        // Philosopher has started talking
	        System.out.println("Philosopher " + getTID() + " starts talking.");

	        saySomething();
	        sleep((long)(Math.random() * TIME_TO_WASTE));

	        // Philosopher has finished talking
	        System.out.println("Philosopher " + getTID() + " has finished talking.");
	    }
	    catch(InterruptedException e)
	    {
	        System.err.println("Philosopher.talk():");
	        DiningPhilosophers.reportException(e);
	        System.exit(1);
	    }
	}

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run()
	{
		//each philosopher will go through a fixed number of dining steps
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
		{
			/*
			 * test:
			 * 1. philosopher tries to pickup the chop stick
			 * 2. call will block in monitor if its unavailable 
			 * 3. until its safe for the phil to eat
			 */
			DiningPhilosophers.soMonitor.pickUp(getTID());
			
			/*
			 * once its safe
			 * eat
			 */
			eat();
			
			/*
			 * after he's done
			 * put down the chop stick so others can use
			 */
			DiningPhilosophers.soMonitor.putDown(getTID());
			
			/*
			 * then he can start thinking */
			think();

			/*
			 * TODO:
			 * A decision is made at random whether this particular
			 * philosopher is about to say something terribly useful.
			 */
			
			/*
			 * If he is deciding to say something useful 
			 * A decision is made at random 
			 * math.random() is in [0.0,1.0]
			 * we choose 50%
			 */
			if(Math.random() < 0.5) // A random decison 
			{
				/*
				 * Request permission to talk via monitor 
				 * only one phil can talk at a time
				 * philosophers can't talk while eating
				 */
				DiningPhilosophers.soMonitor.requestTalk(getTID());
				
				//If the conditions applies then start talking
				talk();
				
				// End talking
				DiningPhilosophers.soMonitor.endTalk(getTID());
			}


		}
	} // run()

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething()
	{
		String[] astrPhrases =
		{
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + ""
		};

		System.out.println
		(
			"Philosopher " + getTID() + " says: " +
			astrPhrases[(int)(Math.random() * astrPhrases.length)]
		);
	}
}

// EOF
