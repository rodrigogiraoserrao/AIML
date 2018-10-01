def define_states(n):
    """Recursively define all the possible states of an n-disk Hanoi tower"""
    if n == 1:
        return [
                [[1],[],[]],
                [[],[1],[]],
                [[],[],[1]]
                ]
    else:
        partial = define_states(n-1)
        final = []
        for state in partial:
            a, b, c = state
            final.append([[n]+a, b, c])
            final.append([a, [n]+b, c])
            final.append([a, b, [n] + c])
        return final

def is_final(state):
    """Return True if this is a final state"""
    return (len(state[0]) == len(state[1]) == 0 and sorted(state[2], reverse=True) == state[2]) or \
            (len(state[0]) == len(state[2]) == 0 and sorted(state[1], reverse=True) == state[1])

def transition(state, action):
    """Given a <state> and an <action>, return the new state and the reward we got.
    The actions are of the form L|C|R + L|C|R,
        where the first character says from where we remove (Left,Centre,Right)
        the second character says where we are inserting (Left,Centre,Right)"""
    R_illegal = -2  # reward if the action was an illegal move
    R_completed = 9 # reward if we finished the puzzle
    R_default = 0   # default reward for regular moves

    state_ = state[::]
    # disassemble the state and the action
    d = {'L': 0, 'C': 1, 'R': 2}
    i = d[action[0]]    # remove from this pile
    j = d[action[1]]    # insert into this pile
    moving = state_[i].pop()
    if (moving > state_[j][-1]):
        # illegal move found
        return state, R_illegal
    else:
        state_[j].append(moving)
        if is_final(state):
            return state_, R_completed
        else:
            return state_, R_default