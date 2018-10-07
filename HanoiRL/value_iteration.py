from hanoi_mdp import *
from random import choice

N = 5

gamma = 0.9

all_states = define_states(N)

# initialize V
V = {dictify(state): 0 for state in all_states[1:]}
V[FINAL_STATE] = 99999  # infinite value
max_change = 1  # error tolerance
i = 0
max_iters = pow(10, N)
while max_change > 0.001 and i < max_iters:
    max_change = 0
    # skip the terminal state, that one has infinite value
    for state in all_states[1:]:
        m = -10000
        for action in actions:
            go_to, reward = transition(state, action)
            temp = reward + gamma*V[dictify(go_to)]
            if temp > m:
                m = temp
        max_change = max(max_change, abs(m - V[dictify(state)]))
        V[dictify(state)] = m
    i += 1
if i == max_iters:
    print("NO CONVERGENCE GUARANTEED")

def create_policy(V):
    """Given the final dict of state values, creates the policy"""
    known_values = dict()   # store already computed values
    def policy(state):
        # if we have computed this action, use it
        t = dictify(state)
        if t in known_values.keys():
            return known_values[t]
        else:   # we still need to find what is the action that maximizes
            best_action = None
            best_value = -1000000
            for action in actions:
                go_to, _ = transition(state, action)
                value = V[dictify(go_to)]
                if value > best_value:
                    best_value = value
                    best_action = action
            known_values[t] = best_action
            return best_action
    return policy

# solve the Tower of Hanoi
state = [[i for i in range(N, 0, -1)],[],[]]
pi = create_policy(V)
stop_at = pow(2, N+1)
iter = 0
while state != FINAL_STATE and iter < stop_at:
    print("{}: {}".format(iter, state))
    state, _ = transition(state, pi(state))
    iter += 1