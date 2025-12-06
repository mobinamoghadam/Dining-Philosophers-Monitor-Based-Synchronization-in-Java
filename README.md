# Dining Philosophers – Monitor-Based Synchronization in Java

A clean and correct Java implementation of the Dining Philosophers problem using a monitor-based synchronization design. The solution guarantees **no deadlock** and **no starvation**, while also supporting an additional talking feature where only one philosopher may talk at a time.

---

## Features
- Deadlock-free chopstick acquisition  
- Starvation-free behavior through fair signaling  
- Monitor-based synchronization using `wait()`, `notify()`, and `notifyAll()`  
- Exclusive talking control (only one philosopher can talk at a time)  
- Supports custom number of philosophers via command-line argument  
- Input validation for incorrect arguments  

---

## How It Works
### Philosopher  
Each philosopher runs as a thread and repeatedly:  
- Thinks  
- Tries to pick up chopsticks  
- Eats  
- Puts down chopsticks  
- Optionally requests to talk  
- Talks if no one else is talking  

### Monitor  
The monitor coordinates all access:  
- Ensures atomic pick-up of chopsticks  
- Guarantees fairness to prevent starvation  
- Allows exactly one philosopher to talk  
- Notifies waiting philosophers when resources become available  

---

## Usage
Run with default number of philosophers:
```bash
java DiningPhilosophers


invalid input:
"abc" is not a positive decimal integer
Usage: java DiningPhilosophers [NUMBER_OF_PHILOSOPHERS]

project structure:
src/
└── diningphilosophers/
    ├── DiningPhilosophers.java   # Program entry point
    ├── Philosopher.java          # Thread behavior
    ├── Monitor.java              # Synchronization logic
