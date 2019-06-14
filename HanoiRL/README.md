**Solving the Hanoi Towers with Reinforcement Learning**

In this project I will use Reinforcement Learning and, in particular, Markov Decision Processes to solve the problem of the Hanoi Towers.
This is project is being explained in a blog series that you can find logged [here](http://mathspp.blogspot.com/2018/09/pledging-to-do-100-days-of-machine.html), of which [this](http://mathspp.blogspot.com/2018/09/markov-decision-processes-basics.html) is the first post.

Running any of the files named after one algorithm (may give some prints that I used for debugging) but in the end present a sequence of lists that represent the evolution of the game, starting from the position where all disks are on the first pole. For example, when `N = 2` (i.e. we have 2 disks) the user may be presented with
```
 0: [[2,1],[],[]]
 1: [[2],[],[1]]
 2: [[],[2],[1]]
 3: [[],[2,1],[]]
```
 which is a sequence the program may use to solve the Hanoi Tower with 2 disks

In case the names of the files aren't self-explanatory:
 - value_iteration.py solves the Hanoi Tower with the algorithm of value iteration;
 - policy_iteration.py solves the Hanoi Tower with the algorithm of policy iteration;
 - q_learning.py uses the algorithm of Q-learning (with the Q function represented in a table; no NN for this one)
 - doubleq_learning.py uses double Q-learning (again, no neural networks involved; only tables)


This was brought to you by the [#100DaysOfMLCode](http://mathspp.blogspot.com/2018/09/pledging-to-do-100-days-of-machine.html) initiative!
