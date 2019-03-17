import random
import gym
import numpy as np
import matplotlib.pyplot as plt
from collections import deque
from keras.models import Sequential
from keras.layers import Dense
from keras.optimizers import Adam

# parameters
EPISODES = 300
MAX_SCORE = 250
ENV_NAME = 'CartPole-v0'
MEMORY_SIZE = 2000
GAMMA = 0.95
ALPHA = 0.8
EPSILON_MIN = 0.01
EPSILON_DECAY = 0.995
LEARNING_RATE = 0.001
STABLE_RUNTIME = 100
STABLE_THRESHOLD = 195


# when finish, build the graph of score and loss
class Score_Logger:
    def __init__(self):
        self.score = []
        self.run = []
        self.mean_score = []
        self.loss = []
        self.epoch = []
        self.mean_loss = []
        self.stable_run = []
        self.angle = []
        self.mean_angle = []
        self.step = []
        self.mean_run = []

    def add_score(self, score, run):
        self.score.append(score)
        self.run.append(run)

    def add_loss(self, loss, epoch, mean_loss):
        self.loss = loss
        self.epoch = epoch
        self.mean_loss = mean_loss

    def add_angle(self, angle, step):
        self.angle.append(angle)
        self.step.append(step)
        self.average_angle()

    def average_angle(self):
        nsum = 0
        for i in range(len(self.angle)):
            nsum += self.angle[i]
        self.mean_angle.append(nsum / len(self.angle))

    def getAngel(self):
        return self.angle

    def getStep(self):
        return self.step

    def getAverage_score(self):
        return self.mean_score[len(self.mean_score) - 1]

    def getLoss(self):
        return self.loss

    def getStable(self):
        return self.stable_run

    def average_score(self):
        score = []
        for i in range(1, STABLE_RUNTIME + 1):
            score.append(self.score[len(self.score) - i])
        nsum = 0
        for i in range(len(score)):
            nsum += score[i]
        mean = (nsum / len(score))
        self.mean_score.append(mean)
        self.mean_run.append(len(self.score))

    def isStable(self):
        if len(self.stable_run) > 0:
            return
        score = []
        for i in range(1, STABLE_RUNTIME + 1):
            score.append(self.score[len(self.score) - i])

        nsum = 0
        for i in range(len(score)):
            nsum += score[i]
        mean = (nsum / len(score))
        if mean >= STABLE_THRESHOLD:
            self.stable_run.append(len(self.score))

    def calTime(self):
        time = 0.0
        if len(self.stable_run) == 1:
            index = 0
            index = self.stable_run[0]
            for i in range(0, index):
                time += self.score[i]
            time = time * 0.02
            return str(time) + 's' + '\nStable run: ' + str(index)
        else:
            return 'Not stable'

    def plot(self):
        # create the subpolts
        aim = np.full((EPISODES,), STABLE_THRESHOLD)
        stable = np.full((len(self.stable_run),), 0)
        plt.figure(1)
        pic_score = plt.subplot(3, 1, 1)
        pic_loss = plt.subplot(3, 1, 2)
        pic_angle = plt.subplot(3, 1, 3)

        # create the score plot
        plt.sca(pic_score)
        plt.plot(self.run, self.score, 'b-', linewidth=1, label='score')
        plt.plot(self.mean_run, self.mean_score, 'r-', linewidth=2, label='average_score')
        plt.plot(self.run, aim, 'm-.', linewidth=1, label='aim_score')
        plt.plot(self.stable_run, stable, 'r.', linewidth=3, label='stable_point')
        plt.legend(loc='upper left')
        plt.xlabel('run')
        plt.ylabel('score')

        # create the loss plot
        plt.sca(pic_loss)
        plt.plot(self.epoch, self.mean_loss, 'c-', linewidth=2, label='average_loss')
        plt.legend(loc='upper right')
        plt.xlabel('epoch')
        plt.ylabel('loss')

        # create the angle plot
        plt.sca(pic_angle)
        plt.plot(self.step, self.angle, 'b-', linewidth=1, label='radian')
        plt.legend(loc='upper left')
        plt.xlabel('epoch')
        plt.ylabel('angle/radian')
        x = Sequential()
        plt.show()


# principal class for DQN algorithm
class DQNAgent:
    def __init__(self, state_size, action_size):
        self.state_size = state_size  # the size for input is the size of state space which is 4
        self.action_size = action_size  # the size for output is the size of action space which is 2
        self.memory = deque(maxlen=MEMORY_SIZE)  # create the container of the memory
        self.epsilon = 1.0  # exploration rate
        self.model = self._build_model()
        self.epoch = 0
        self.epoch_list = []  # record the number of epoch
        self.loss_list = []  # record the loss for each epoch
        self.mean_loss = []

    def _build_model(self):  # build of neural network for Deep-Q learning Model
        model = Sequential()  # using a sequntial model
        model.add(Dense(24, input_dim=self.state_size, activation='relu'))  # input layer
        model.add(Dense(24, activation='relu'))  # hidden layer
        model.add(Dense(self.action_size, activation='linear'))  # output layer
        model.compile(loss='mse', optimizer=Adam(lr=LEARNING_RATE))  # after build,compile the DNN
        return model

    def remember(self, state, action, reward, next_state, done):  # store the data for memory
        self.memory.append((state, action, reward, next_state, done))

    def act(self, state):  # using the epsilon-greedy strategy to choose the action
        if np.random.rand() <= self.epsilon:
            return random.randrange(self.action_size)
        act_values = self.model.predict(state)
        return np.argmax(act_values[0])  # returns action

    def replay(self, batch_size):
        minibatch = random.sample(self.memory, batch_size)
        states, targets_f = [], []
        for state, action, reward, next_state, done in minibatch:
            target = reward
            if not done:
                # principal code for DQN,we update the Q-value by using the q-value formula
                target = (1 - ALPHA) * (self.model.predict(state)[0][action]) + ALPHA * (reward + GAMMA *
                                                                                         np.amax(self.model.predict(
                                                                                             next_state)[0]))
            target_f = self.model.predict(state)
            target_f[0][action] = target
            # Filtering out states and targets for training
            states.append(state[0])
            targets_f.append(target_f[0])
        history = self.model.fit(np.array(states), np.array(targets_f), epochs=1,
                                 verbose=0)  # using the state and the new q-value to train the DNN for once
        # Keeping track of loss
        loss = history.history['loss'][0]
        self.loss_list.append(loss)
        self.average_loss()
        self.epoch_list.append(self.epoch)
        self.epoch += 1
        if self.epsilon > EPSILON_MIN:
            self.epsilon *= EPSILON_DECAY

    def load(self, name):
        self.model.load_weights(name)

    def save(self, name):
        self.model.save_weights(name)

    def average_loss(self):
        nsum = 0
        for i in range(len(self.loss_list)):
            nsum += self.loss_list[i]
        self.mean_loss.append(nsum / len(self.loss_list))

    def getLoss(self):
        return self.loss_list

    def getEpoch(self):
        return self.epoch_list

    def getMeanloss(self):
        return self.mean_loss


# main loop
if __name__ == "__main__":
    ### gym environment set up start
    env = gym.make(ENV_NAME)
    env._max_episode_steps = MAX_SCORE
    state_size = env.observation_space.shape[0]
    action_size = env.action_space.n
    agent = DQNAgent(state_size, action_size)
    score_logger = Score_Logger()
    done = False
    batch_size = 32
    # agent.load("./save/cartpole-dqn.h5")
    # gym environment set up end
    run = 0
    epoch = 0  # to match the epoch of the angle
    for e in range(EPISODES):
        if run >= STABLE_RUNTIME:
            score_logger.isStable()
            score_logger.average_score()
        run += 1
        state = env.reset()  # when the done is 'true' or in this episode we score 499,
        # reset the environment and start again
        state = np.reshape(state, [1, state_size])
        score = 0
        for time in range(500):
            score += 1
            epoch += 1
            # env.render()# to show the graphic interface
            action = agent.act(state)  # get action from the agent
            next_state, reward, done, _ = env.step(action)
            score_logger.add_angle(next_state[2], epoch)
            reward = reward if not done else -10  # if the in this time the pole didn't fall
            # we set reward as 1 else we set it as -10
            next_state = np.reshape(next_state, [1, state_size])
            agent.remember(state, action, reward, next_state, done)  # store the data in the memory
            state = next_state
            if done:
                print("episode: {}/{}, score: {}, e: {:.2}"
                      .format(e, EPISODES, time, agent.epsilon))
                score_logger.add_score(score, run)
                break
            if len(agent.memory) > batch_size:
                agent.replay(batch_size)  # do the update of the q-value and train the DNN
    #    agent.save("./save/cartpole-dqn.h5")
    score_logger.add_loss(agent.getLoss(), agent.getEpoch(), agent.getMeanloss())
    score_logger.plot()  # plot the graph
    print('Stable time: ', score_logger.calTime())
