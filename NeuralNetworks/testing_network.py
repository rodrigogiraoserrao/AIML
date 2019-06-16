from neural_network import NeuralNetwork, Sigmoid, ReLU, LeakyReLU
import numpy as np

## Can we train this neural network to always return 1?
sizes = (10, 1)
act_function = Sigmoid()
nn = NeuralNetwork(sizes, nonlinearities=act_function)
output = np.ones((1,1))
N = 10000
for i in range(N):
    inp = np.random.rand(10, 1)
    nn.forward(inp)
    nn.loss(output)
    nn.backprop()
inp = np.random.rand(10, 1)
print(nn.forward(inp))

## Train this neural network to tell if a number is odd or even
# use 1 for odd and use 0 for even
sizes = (10, 3, 1)
act_functions = [ReLU(), ReLU()]
nn = NeuralNetwork(sizes, nonlinearities=act_functions)
N = 10000
for i in range(N):
    inp = np.random.random_integers(0, 1, ((10, 1)))
    nn.forward(inp)
    nn.loss(inp[-1, :])
    nn.backprop()
tests = 200
rights = 0
for t in range(tests):
    inp = np.random.random_integers(0, 1, ((10, 1)))
    out = 0 if nn.forward(inp)[0, 0] < 0.5 else 1
    if out == inp[-1, 0]:
        rights += 1
print("Got {}/{} right".format(rights, tests))

## Can this neural network learn to "reverse" the number?
sizes = (10, 10)
act_function = LeakyReLU(0.01)
nn = NeuralNetwork(sizes, nonlinearities=act_function)
N = 10000
for i in range(N):
    inp = np.random.random_integers(0, 1, (10, 1))
    nn.forward(inp)
    nn.loss(inp[::-1])
    nn.backprop()
tests = 200
right_bits = 0
for t in range(tests):
    inp = np.random.random_integers(0, 1, (10, 1))
    out = nn.forward(inp)
    out[out < 0.5] = 0
    out[out >= 0.5] = 1
    right_bits += np.sum(out == inp[::-1])
print("Got {}/{} bit inversions right".format(right_bits, tests*sizes[0]))