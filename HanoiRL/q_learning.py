from hanoi_mdp import *
from random import choice, random

N = 4

gamma = 0.9
learning_rate = 0.1

all_states = define_states(N)
# build auxiliary dictionary to assign an index to each state
state_idx = dict()
s = 0
for state in all_states:
    state_idx[dictify(state)] = s
    s += 1

# initialize Q with random small values
# access Q as Q[state index][action index]
Q = [[random()-0.5 for action in actions] for state in all_states]
# the value of the final state is 0
Q[state_idx[dictify(FINAL_STATE)]] = [0 for action in actions]

def maximizeQ(Q, s):
    """Choose the action that would maximize Q in the given state;
        returns argmax_a Q(s, a), max_a Q(s, a)"""
    idx = state_idx[dictify(s)]
    best_value = Q[idx][0]
    best_action = actions[0]
    for i in range(1, len(actions)):
        v = Q[idx][i]
        if v > best_value:
            best_value = v
            best_action = actions[i]
    return best_action, best_value

simulations = 1000
eps = 1
for n in range(simulations):
    eps = 1 - n/simulations # go from 100% to 0% exploration rate
    state = choice(all_states[1:])
    # a list of all the states and actions we go through
    # run the simulation until we get to a final state
    while state != FINAL_STATE:
        # with eps probability, ignore our belief of what the best action is
        # and instead act randomly
        if random() < eps:
            action = choice(actions)
        else:
            action, _ = maximizeQ(Q, state)
        ai = actions.index(action)
        si = state_idx[dictify(state)]
        state, reward = transition(state, action)
        # update the Q value
        if state == FINAL_STATE:
            Q[si][ai] = (1-learning_rate)*Q[si][ai] + learning_rate*reward
        else:
            _, future_value = maximizeQ(Q, state)
            Q[si][ai] = (1-learning_rate)*Q[si][ai] + \
                        learning_rate * (reward + gamma * future_value)

for state in all_states:
    a, _ = maximizeQ(Q, state)
    print("do {} in {}".format(a, state))


# solve the Tower of Hanoi
state = [[i for i in range(N, 0, -1)],[],[]]
stop_at = pow(2, N+1)   # optimal player takes ~ half this time
iter = 0
while state != FINAL_STATE and iter < stop_at:
    print("{}: {}".format(iter, state))
    # find the maximizing action
    action, _ = maximizeQ(Q, state)
    state, _ = transition(state, action)
    iter += 1
