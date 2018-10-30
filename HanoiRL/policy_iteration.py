from hanoi_mdp import *
from random import choice

N = 4

gamma = 0.9

all_states = define_states(N)

def extract_policy(V):
    """Given the values of all states, build the policy that
        maximizes the expected return"""
    pi = dict()
    # no need to know how to "act" when in the FINAL_STATE
    for state in all_states[1:]:
        best_action = actions[0]
        go_to, reward = transition(state, actions[0])
        best_value = reward + gamma*V[dictify(go_to)]
        for action in actions[1:]:
            go_to, reward = transition(state, action)
            t = reward + gamma*V[dictify(go_to)]
            if t > best_value:
                best_value = t
                best_action = action
        pi[dictify(state)] = best_action
    return pi

# initialize V
V = {dictify(state): 0 for state in all_states[1:]}
V[FINAL_STATE] = 99999 # the final state has infinite value
# initialize pi
pi = extract_policy(V)
policy_has_changed = True
iters = 0
while policy_has_changed:
    iters += 1
    # update all the values until convergence
    max_change = 1  # biggest change found over all states,
                    # used as stopping criterion
    i = 0
    max_iters = pow(10, N)
    while max_change > 0.001 and i < max_iters:
        max_change = 0
        # skip the final state, no need to touch that
        for state in all_states[1:]:
            action = pi[dictify(state)]
            go_to, reward = transition(state, action)
            m = reward + gamma*V[dictify(go_to)]
            max_change = max(max_change, abs(m - V[dictify(state)]))
            V[dictify(state)] = m
        i += 1
    if i == max_iters:
        print("NO CONVERGENCE GUARANTEED")
    # supposedly the values converged, now extract the policy
    pi_ = extract_policy(V)
    policy_has_changed = False
    for key in pi_.keys():
        if pi_[key] != pi[key]:
            policy_has_changed = True
            pi[key] = pi_[key]
print("Took {} iterations".format(iters))

# solve the Tower of Hanoi
state = [[i for i in range(N, 0, -1)],[],[]]
stop_at = pow(2, N+1)   # ~ twice as much moves needed by an optimal player
iter = 0
while state != FINAL_STATE and iter < stop_at:
    print("{}: {}".format(iter, state))
    state, _ = transition(state, pi[dictify(state)])
    iter += 1