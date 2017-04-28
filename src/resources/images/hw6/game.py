import MalmoPython
import time
import sys
import json
import numpy as np


class CliffWalkingGame(object):
    def __init__(self, agent, noise=0.2):
        self.agent = agent
        self.noise = noise

        self.agent_host = None
        self.mission_file = './cliff_walking.xml'

        self.commands = {"north": "movenorth 1",
                        "south": "movesouth 1",
                        "east": "moveeast 1",
                        "west": "movewest 1"}

    def start_mission(self):
        self.agent_host = MalmoPython.AgentHost()
        self.agent_host.setObservationsPolicy(MalmoPython.ObservationsPolicy.LATEST_OBSERVATION_ONLY)

        my_mission = None
        with open(self.mission_file, 'r') as f:
            print "Loading mission from {0}".format(self.mission_file)
            mission_xml = f.read()
            my_mission = MalmoPython.MissionSpec(mission_xml, True)

        # Attempt to start a mission:
        max_retries = 3
        for retry in range(max_retries):
            try:
                self.agent_host.startMission(my_mission, MalmoPython.MissionRecordSpec())
                break
            except RuntimeError as e:
                if retry == max_retries - 1:
                    print "Unable to start mission:", e
                    return False
                else:
                    time.sleep(2)

        # Loop until mission starts:
        print "Waiting for the mission to start ",
        world_state = self.agent_host.peekWorldState()
        while not world_state.is_mission_running:
            sys.stdout.write(".")
            time.sleep(0.1)
            world_state = self.agent_host.peekWorldState()
            for error in world_state.errors:
                print "Error: ", error.text

        print "Mission running"
        return True

    def run_mission(self):
        started = self.start_mission()
        if not started:
            print "Mission failed to start"
            return False

        cumulative_reward = 0.0

        world_state = self.agent_host.getWorldState()
        while world_state.number_of_observations_since_last_state < 1:
            time.sleep(0.1)
            world_state = self.agent_host.getWorldState()

        while world_state.is_mission_running:
            obs_text = world_state.observations[-1].text
            obs = json.loads(obs_text)
            if u'XPos' not in obs or u'ZPos' not in obs:
                print "Incomplete observation received: {0}".format(obs_text)
                return False

            previous_reward = sum(r.getValue() for r in world_state.rewards)
            cumulative_reward += previous_reward

            # grid accesses Z first (rows), then X (columns)
            current_state = (int(obs[u'ZPos']), int(obs[u'XPos']))
            action = self.agent.get_action(current_state)

            self.take_action(action)

            time.sleep(0.5)
            world_state = self.agent_host.getWorldState()

        previous_reward = sum(r.getValue() for r in world_state.rewards)
        cumulative_reward += previous_reward

        return cumulative_reward

    def take_action(self, action):
        primary_prob = 1.0 - self.noise
        secondary_prob = self.noise / 2.0

        noisy_action = action
        if action == 'north':
            noisy_action = np.random.choice(['north', 'west', 'east'], p=[primary_prob, secondary_prob, secondary_prob])
        elif action == 'west':
            noisy_action = np.random.choice(['west', 'north', 'south'], p=[primary_prob, secondary_prob, secondary_prob])
        elif action == 'south':
            noisy_action = np.random.choice(['south', 'west', 'east'], p=[primary_prob, secondary_prob, secondary_prob])
        elif action == 'east':
            noisy_action = np.random.choice(['east', 'north', 'south'], p=[primary_prob, secondary_prob, secondary_prob])

        command = self.commands[noisy_action]
        self.agent_host.sendCommand(command)

    def run_game(self, num_missions):
        print "Starting game. Running {0} missions.".format(num_missions)

        for i in range(num_missions):
            print "Running mission {0}.".format(i)
            mission_reward = self.run_mission()
            print "Completed mission {0}. Total reward: {1}".format(i, mission_reward)
            time.sleep(2.0)

        print "Completed {0} missions.".format(num_missions)

    def run_game_q(self, num_missions):
        print "Starting game. Running {0} missions.".format(num_missions)

        for i in range(num_missions):
            print "Running mission {0}.".format(i)
            mission_reward = self.run_mission_q()
            print "Completed mission {0}. Total reward: {1}".format(i, mission_reward)
            time.sleep(2.0)

        print "Completed {0} missions.".format(num_missions)

    def run_mission_q(self):
        started = self.start_mission()
        if not started:
            print "Mission failed to start"
            return False

        world_state = self.agent_host.getWorldState()
        while world_state.number_of_observations_since_last_state < 1:
            time.sleep(0.1)
            world_state = self.agent_host.getWorldState()

        self.agent.start_episode()

        cumulative_reward = 0.0
        previous_state = None
        action = None
        while world_state.is_mission_running:
            obs_text = world_state.observations[-1].text
            obs = json.loads(obs_text)
            if u'XPos' not in obs or u'ZPos' not in obs:
                print "Incomplete observation received: {0}".format(obs_text)
                return False

            reward = sum(r.getValue() for r in world_state.rewards)
            # grid accesses Z first (rows), then X (columns)
            current_state = (int(obs[u'ZPos']), int(obs[u'XPos']))
            if action is not None and previous_state is not None:
                self.agent.observe_transition(previous_state, action, current_state, reward)

            cumulative_reward += reward
            previous_state = current_state
            action = self.agent.get_action(current_state)

            self.take_action(action)

            time.sleep(0.5)
            world_state = self.agent_host.getWorldState()

        reward = sum(r.getValue() for r in world_state.rewards)
        self.agent.observe_transition(previous_state, action, 'TERMINAL_STATE', reward)

        cumulative_reward += reward

        self.agent.stop_episode()

        return cumulative_reward
