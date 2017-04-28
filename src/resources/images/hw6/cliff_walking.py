from mdp import MarkovDecisionProcess


class CliffWalkingMDP(MarkovDecisionProcess):
    def __init__(self, grid):
        self.grid = grid

        # parameters
        self.livingReward = -1.0
        self.noise = 0.2

        self.terminal_state = 'TERMINAL_STATE'

    def get_transition_states_and_probabilities(self, state, action):
        if action not in self.get_possible_actions(state):
            raise "Illegal action!"

        successors = []
        if action == 'north':
            successors.append(((state[0] - 1, state[1]), 1 - self.noise))

            mass_left = self.noise
            successors.append(((state[0], state[1] - 1), mass_left / 2.0))
            successors.append(((state[0], state[1] + 1), mass_left / 2.0))
        elif action == 'west':
            successors.append(((state[0], state[1] - 1), 1 - self.noise))

            mass_left = self.noise
            successors.append(((state[0] - 1, state[1]), mass_left / 2.0))
            successors.append(((state[0] + 1, state[1]), mass_left / 2.0))
        elif action == 'south':
            successors.append(((state[0] + 1, state[1]), 1 - self.noise))

            mass_left = self.noise
            successors.append(((state[0], state[1] - 1), mass_left / 2.0))
            successors.append(((state[0], state[1] + 1), mass_left / 2.0))
        elif action == 'east':
            successors.append(((state[0], state[1] + 1), 1 - self.noise))

            mass_left = self.noise
            successors.append(((state[0] - 1, state[1]), mass_left / 2.0))
            successors.append(((state[0] + 1, state[1]), mass_left / 2.0))
        elif action == 'exit':
            successors.append((self.terminal_state, 1.0))

        return successors

    def get_possible_actions(self, state):
        if self.is_terminal(state):
            return []

        x, y = state
        if type(self.grid[x][y]) in (int, float):
            return ['exit']
        return ['north', 'west', 'south', 'east']

    def get_start_state(self):
        for i in range(len(self.grid)):
            for j in range(len(self.grid[0])):
                if self.grid[i][j] == 'S':
                    return i, j

        raise "Grid has no start state!"

    # def get_reward(self, state, action, next_state):
    def get_reward(self, state):
        if self.is_terminal(state):
            return 0.0

        x, y = state
        cell = self.grid[x][y]
        if type(cell) in (int, float):
            return cell + self.livingReward
        return self.livingReward

    def is_terminal(self, state):
        return state == self.terminal_state

    def get_states(self):
        states = [self.terminal_state]
        for x in range(len(self.grid)):
            for y in range(len(self.grid[0])):
                states.append((x, y))
        return states


class Grid:
    """
    A 2-dimensional array of immutables backed by a list of lists.  Data is accessed
    via grid[x][y] where (x,y) are cartesian coordinates with x horizontal,
    y vertical and the origin (0,0) in the bottom left corner.
    """

    def __init__(self, width, height, initialValue=' '):
        self.width = width
        self.height = height
        self.data = [[initialValue for y in range(height)] for x in range(width)]
        self.terminalState = 'TERMINAL_STATE'

    def __getitem__(self, i):
        return self.data[i]

    def __setitem__(self, key, item):
        self.data[key] = item

    def __eq__(self, other):
        if other == None: return False
        return self.data == other.data

    def __hash__(self):
        return hash(self.data)

    def copy(self):
        g = Grid(self.width, self.height)
        g.data = [x[:] for x in self.data]
        return g

    def deepCopy(self):
        return self.copy()

    def shallowCopy(self):
        g = Grid(self.width, self.height)
        g.data = self.data
        return g

    def _getLegacyText(self):
        t = [[self.data[x][y] for x in range(self.width)] for y in range(self.height)]
        t.reverse()
        return t

    def __str__(self):
        return str(self._getLegacyText())
