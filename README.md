Dining Philosophers – Deadlock-Free, Starvation-Free Monitor Implementation (Java)

This repository contains a Java implementation of the Dining Philosophers synchronization problem using a monitor-based design. The solution ensures deadlock-free and starvation-free behavior while supporting additional functionality such as controlled talking, where only one philosopher may talk at a time while not eating.

The project demonstrates practical use of Java’s synchronization primitives and thread coordination techniques to solve a complex concurrency problem.

Features

Deadlock-free chopstick acquisition using atomic resource control

Starvation prevention through fair, bounded waiting

Exclusive talking mechanism ensuring only one philosopher speaks at a time

Monitor-based synchronization using wait(), notify(), and related constructs

Configurable number of philosophers via command-line arguments

Input validation and clear usage instructions

How It Works

The system is structured around two primary components:

Philosopher

Each philosopher runs as an independent thread, cycling through:

Thinking

Attempting to pick up chopsticks

Eating

Putting down chopsticks

Optionally requesting to talk

Talking (only if nobody else is talking)

Monitor

The monitor enforces:

Safe, atomic chopstick acquisition

Fair turn-taking to eliminate starvation

Mutual exclusion for talking

Coordinated signaling through wait() / notifyAll() or Lock/Condition

Usage
Running the Program
java DiningPhilosophers [NUMBER_OF_PHILOSOPHERS]

Examples

Use default number of philosophers:

java DiningPhilosophers


Specify a custom number:

java DiningPhilosophers 6


Invalid argument example:

"abc" is not a positive decimal integer
Usage: java DiningPhilosophers [NUMBER_OF_PHILOSOPHERS]

Project Structure
src/
└── diningphilosophers/
    ├── DiningPhilosophers.java   # Main program entry point
    ├── Philosopher.java          # Thread behavior for each philosopher
    ├── Monitor.java              # Synchronization logic and resource control
    └── (supporting classes)

Starvation Prevention

Starvation is eliminated through bounded waiting. Philosophers waiting to eat or talk are signaled in a fair manner, ensuring that no philosopher can be indefinitely delayed. The monitor enforces orderly resource allocation and ensures progress for all participants.

Purpose

This project demonstrates advanced concurrency control techniques and is an example of building a custom monitor structure in Java to enforce correctness properties such as:

Mutual exclusion

Progress and fairness

Deadlock freedom

Starvation freedom

It is suitable for understanding monitor design, thread synchronization, and structured concurrent programming patterns.
