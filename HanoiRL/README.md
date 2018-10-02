**Solving the Hanoi Towers with Reinforcement Learning**

In this project I will use Reinforcement Learning and, in particular, Markov Decision Processes to solve the problem of the Hanoi Towers.
This is project is being explained in a blog series that you can find logged [here](http://mathspp.blogspot.com/2018/09/pledging-to-do-100-days-of-machine.html), of which [this](http://mathspp.blogspot.com/2018/09/markov-decision-processes-basics.html) is the first post.

 - value_iteration.py uses the helper functions of hanoi_mdp.py to solve the Hanoi Towers with the algorithm of value iteration; running value_iteration.py will present the user with a sequence of lists that represent the evolution of the game, starting with all disks on the first pole.

As an example, when `N = 2` (i.e. we have 2 disks) the user is presented with
 ```
 [[2,1],[],[]]
 [[2],[],[1]]
 [[],[2],[1]]
 [[],[2,1],[]]
 ```
 which is the sequence the program uses to solve the Hanoi Tower with 2 disks

This was brought to you by the [#100DaysOfMLCode](http://mathspp.blogspot.com/2018/09/pledging-to-do-100-days-of-machine.html) initiative!
