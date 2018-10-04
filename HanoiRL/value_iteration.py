from hanoi_mdp import *
from random import choice

N = 5

gamma = 0.9

all_states = define_states(N)
actions = [a+b for a in ["L","C","R"] for b in ["L","C","R"]]

# initialize V
V = {tuplify(state): 0 for state in all_states}
max_change = 1  # error tolerance
i = 0
max_iters = pow(10, N)
while max_change > 0.001 and i < max_iters:
    max_change = 0
    for state in all_states:
        m = -10000
        for action in actions:
            go_to, reward = transition(state, action)
            temp = reward + gamma*V[tuplify(go_to)]
            if temp > m:
                m = temp
        max_change = max(max_change, abs(m - V[tuplify(state)]))
        V[tuplify(state)] = m
    i += 1
if i == max_iters:
    print("NO CONVERGENCE GUARANTEED")

def create_policy(V):
    """Given the final dict of state values, creates the policy"""
    known_values = dict()   # store already computed values
    def policy(state):
        # if we have computed this action, use it
        t = tuplify(state)
        if t in known_values.keys():
            return known_values[t]
        else:   # we still need to find what is the action that maximizes
            best_action = None
            best_value = -1000000
            for action in actions:
                go_to, _ = transition(state, action)
                value = V[tuplify(go_to)]
                if value > best_value:
                    best_value = value
                    best_action = action
            known_values[t] = best_action
            return best_action
    return policy

# solve the Tower of Hanoi
state = [[i for i in range(N, 0, -1)],[],[]]
old_state = None
pi = create_policy(V)
while old_state != state:
    old_state = state
    state, _ = transition(state, pi(state))
    print(old_state)
print(state)
