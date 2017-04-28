import random


class MarkovDecisionProcess:
    def get_states(self):
        """
        Return a list of all states in the MDP.
        Not generally possible for large MDPs.
        """
        raise NotImplementedError()

    def get_start_state(self):
        """
        Return the start state of the MDP.
        """
        raise NotImplementedError()

    def get_possible_actions(self, state):
        """
        Return a list of possible actions from 'state'.
        """
        raise NotImplementedError()

    def get_transition_states_and_probabilities(self, state, action):
        """
        Returns list of (nextState, prob) pairs
        representing the states reachable
        from 'state' by taking 'action' along
        with their transition probabilities.
        """
        raise NotImplementedError()

    # def get_reward(self, state, action, nextState):
    def get_reward(self, state):
        """
        Get the reward for the state, action, nextState transition.

        Not available in reinforcement learning.
        """
        raise NotImplementedError()

    def is_terminal(self, state):
        """
        Returns true if the current state is a terminal state.  By convention,
        a terminal state has zero future rewards.  Sometimes the terminal state(s)
        may have no possible actions.  It is also common to think of the terminal
        state as having a self-loop action 'pass' with zero reward; the formulations
        are equivalent.
        """
        raise NotImplementedError()


