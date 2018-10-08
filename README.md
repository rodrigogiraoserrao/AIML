# AIML
Repo for everything related to AI/ML I produce!

What I have for now is:
  - Language Recognizer, using Naive Bayes to find the language of sentences;
  
  - Vacuum Cleaner, using genetic algorithms to breed a robot that cleans the floor of a house:
    - src/resources/resources.pde is a Processing script that reads an ArrayList of Robots and plays the several robots in a random room, so we can compare them;
    - src/simulators/BasicSimulator.java is a Java script that performs a basic evolution simulation with a given generation size, number of rooms to test the robots in and fixed number of generations. The best robot in each generation is stored in an ArrayList that can later be read by the script mentioned above;
    - src/simulators/WBasicSimulator.java (which has been exported to an executable .jar file in WBasicSimulator.jar) is a simple application that encapsulates the capabilities of BasicSimulator.java in a GUI;
  - HanoiRL, using Reinforcement Learning to solve the puzzle of the Tower of Hanoi. I already implemented value and policy iteration, as well as Q-learning and double Q-learning. Those already solve the puzzle correctly and with the [optimal number of moves](https://mathspp.blogspot.com/2018/10/twitter-proof-tower-of-hanoi.html).
    - hanoiRL.ipynb is a Python notebook where all code will be explained and the main ideas and concepts behind the algorithms are explained as well. The notebook assumes you already [know what an mdp is](https://mathspp.blogspot.com/2018/09/markov-decision-processes-basics.html)
