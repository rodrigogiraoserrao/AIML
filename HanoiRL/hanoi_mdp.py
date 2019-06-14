from copy import deepcopy

COMPLETED = "COMPLETED" # special action when the MDP thinks we are done
FINAL_STATE = "DONE"    # special state for when the Hanoi Tower was solved
actions = [COMPLETED, "LC", "LR", "CL", "CR", "RL", "RC"]

def dictify(state):
    """Turns a state into a valid dict key"""
    if state == FINAL_STATE:
        return state
    else:
        return tuple([tuple(item) for item in state])

def define_states(n):
    """Recursively define all the possible states of an n-disk Hanoi tower"""
    def aux(n):
        if n == 1:
            return [
                    [[1],[],[]],
                    [[],[1],[]],
                    [[],[],[1]]
                    ]
        else:
            partial = aux(n-1)
            final = []
            for state in partial:
                a, b, c = state
                final.append([[n]+a, b, c])
                final.append([a, [n]+b, c])
                final.append([a, b, [n]+c])
            return final

    return [FINAL_STATE] + aux(n)

def game_is_done(state):
    """Return if the Hanoi Tower was solved;
        Assume we always start with all disks on the left"""
    return (len(state[0]) == len(state[1]) == 0 and \
                sorted(state[2], reverse=True) == state[2]) or \
            (len(state[0]) == len(state[2]) == 0 and \
                sorted(state[1], reverse=True) == state[1])

def transition(state, action):
    """Given a <state> and an <action>, return the new state
        and the reward we got.
    The actions are of the form L|C|R + L|C|R,
        where the first character says from where we remove (Left,Centre,Right)
        the second character says where we are inserting (Left,Centre,Right)
        OR
        COMPLETED to transition into the final state when the game is done"""
    R_illegal = -3      # reward if the action was an illegal move
    R_final = 5         # reward if we end the game
    R_default = 0       # default reward

    # deal with the action COMPLETED separately
    if action == COMPLETED:
        if game_is_done(state):
            return FINAL_STATE, R_final
        else:
            return state, R_illegal

    state_ = deepcopy(state)
    # disassemble the state and the action
    d = {'L': 0, 'C': 1, 'R': 2}
    i = d[action[0]]    # remove from this pile
    j = d[action[1]]    # insert into this pile
    if not state_[i]:   # can we remove?
        return state, R_illegal
    moving = state_[i].pop()
    if (state_[j] and moving > state_[j][-1]):
        # illegal move found
        return state, R_illegal
    else:
        state_[j].append(moving)
        return state_, R_default