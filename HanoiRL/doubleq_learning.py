from hanoi_mdp import *
from random import choice, random

N = 3

gamma = 0.9
learning_rate = 0.1

all_states = define_states(N)
# build auxiliary dictionary to assign an index to each state
state_idx = dict()
s = 0
for state in all_states:
    state_idx[dictify(state)] = s
    s += 1

# initialize Q with random values
# access Q as Q[state index][action index]
Q1 = [[random()-0.5 for action in actions] for state in all_states]
Q2 = [[random()-0.5 for action in actions] for state in all_states]
# the value of the final state is 0
Q1[state_idx[dictify(FINAL_STATE)]] = [0 for action in actions]
Q2[state_idx[dictify(FINAL_STATE)]] = [0 for action in actions]

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

def maximizeQs(Qa, Qb, s):
    """Choose the action that would maximize the joint Qa,Qb in the given state
        returns argmax_a (Qa(s,a)+Qb(s,a)), max_a (Qa(s,a)+Qb(s,a))"""
    idx = state_idx[dictify(s)]
    best_value = Qa[idx][0] + Qb[idx][0]
    best_action = actions[0]
    for i in range(1, len(actions)):
        v = Qa[idx][i] + Qb[idx][i]
        if v > best_value:
            best_value = v
            best_action = actions[i]
    return best_action, best_value

simulations = 1000
eps = 0.1
for n in range(simulations):
    state = choice(all_states[1:])
    # run the simulation until we stay in a final state two times in a row
    while state != FINAL_STATE:
        # with eps probability, ignore our belief of what the best action is
        # and instead act randomly
        if random() < eps:
            action = choice(actions)
        else:
            action, _ = maximizeQs(Q1, Q2, state)
        ai = actions.index(action)
        si = state_idx[dictify(state)]
        next_state, reward = transition(state, action)
        # update the Q value; randomly assign the Qi to be updated
        if random() < 0.5:
            Qa = Q1
            Qb = Q2
        else:
            Qa = Q2
            Qb = Q1
        if game_is_done(state):
            Qa[si][ai] = (1-learning_rate)*Qa[si][ai] + learning_rate*reward
        else:
            _, future_value = maximizeQ(Qb, next_state)
            Qa[si][ai] = (1-learning_rate)*Qa[si][ai] + \
                        learning_rate * (reward + gamma*future_value)
        state = next_state

for state in all_states:
    a, _ = maximizeQs(Q1, Q2, state)
    print("do {} in {}".format(a, state))

# solve the Tower of Hanoi
state = [[i for i in range(N, 0, -1)],[],[]]
stop_at = pow(2, N+1)   # optimal player takes ~ half this time
iter = 0
while state != FINAL_STATE and iter < stop_at:
    print("{}: {}".format(iter, state))
    # find the maximizing action
    action, _ = maximizeQs(Q1, Q2, state)
    state, _ = transition(state, action)
    iter += 1