class ValueEstimationAgent(object):
    def __init__(self, alpha=1.0, epsilon=0.05, gamma=0.8, numTraining=10):
        """
        Sets options
        alpha    - learning rate
        epsilon  - exploration rate
        gamma    - discount factor
        numTraining - number of training episodes, i.e. no learning after these many episodes
        """
        self.alpha = float(alpha)
        self.epsilon = float(epsilon)
        self.discount = float(gamma)
        self.numTraining = int(numTraining)

    def get_q_value(self, state, action):
        """
        Should return Q(state,action)
        """
        raise NotImplementedError()

    def get_value(self, state):
        """
        What is the value of this state under the best action? 
        Concretely, this is given by

        V(s) = max_{a in actions} Q(s,a)
        """
        raise NotImplementedError()

    def get_policy(self, state):
        """
        What is the best action to take in the state. Note that because
        we might want to explore, this might not coincide with get_action
        Concretely, this is given by

        policy(s) = arg_max_{a in actions} Q(s,a)

        If many actions achieve the maximal Q-value,
        it doesn't matter which is selected.
        """
        raise NotImplementedError()

    def get_action(self, state):
        """
        Choose an action and return it.
        """
        raise NotImplementedError()


class ReinforcementAgent(ValueEstimationAgent):
    """
    Abstract Reinforcemnt Agent: A ValueEstimationAgent
      which estimates Q-Values (as well as policies) from experience
      rather than a model

      What you need to know:
          - The environment will call
            observe_transition(state,action,nextState,deltaReward),
            which will call update(state, action, nextState, deltaReward)
            which you should override.
      - Use self.get_legal_actions(state) to know which actions
            are available in a state
    """

    ####################################
    #    Override These Functions      #
    ####################################

    def update(self, state, action, nextState, reward):
        """
        This class will call this function, which you write, after
        observing a transition and reward
        """
        raise NotImplementedError()

    ####################################
    #    Read These Functions          #
    ####################################

    def get_legal_actions(self, state):
        """
        Get the actions available for a given
        state. This is what you should use to
        obtain legal actions for a state
        """
        return self.actionFn(state)

    def observe_transition(self, state, action, nextState, deltaReward):
        """
        Called by environment to inform agent that a transition has
        been observed. This will result in a call to self.update
        on the same arguments

        NOTE: Do *not* override or call this function
        """
        self.episodeRewards += deltaReward
        self.update(state, action, nextState, deltaReward)

    def start_episode(self):
        """
        Called by environment when new episode is starting
        """
        self.lastState = None
        self.lastAction = None
        self.episodeRewards = 0.0

    def stop_episode(self):
        """
        Called by environment when episode is done
        """
        if self.episodesSoFar < self.numTraining:
            self.accumTrainRewards += self.episodeRewards
        else:
            self.accumTestRewards += self.episodeRewards
        self.episodesSoFar += 1
        if self.episodesSoFar >= self.numTraining:
            # Take off the training wheels
            self.epsilon = 0.0  # no exploration
            self.alpha = 0.0  # no learning

    def is_in_training(self):
        return self.episodesSoFar < self.numTraining

    def is_in_testing(self):
        return not self.isInTraining()

    def __init__(self, action_fn=None, numTraining=100, epsilon=0.5, alpha=0.5, gamma=1):
        """
        actionFn: Function which takes a state and returns the list of legal actions

        alpha    - learning rate
        epsilon  - exploration rate
        gamma    - discount factor
        numTraining - number of training episodes, i.e. no learning after these many episodes
        """
        super(ReinforcementAgent, self).__init__(alpha, epsilon, gamma, numTraining)
        if action_fn is None:
            action_fn = lambda state: state.getLegalActions()
        self.actionFn = action_fn
        self.episodesSoFar = 0
        self.accumTrainRewards = 0.0
        self.accumTestRewards = 0.0
        # self.numTraining = int(numTraining)
        # self.epsilon = float(epsilon)
        # self.alpha = float(alpha)
        # self.discount = float(gamma)
