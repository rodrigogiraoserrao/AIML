from neural_network import NeuralNetwork, LeakyReLU, Sigmoid, ReLU, CrossEntropyLoss
import matplotlib.pyplot as plt
import numpy as np
import csv

np.random.seed(123456789)

MNIST_TRAIN = "mnistdata/mnist_train.csv"
MNIST_TEST = "mnistdata/mnist_test.csv"

def read_mnist_file(filepath):
    labels = []
    images = []
    with open(filepath, "r") as file:
        reader = csv.reader(file)
        # skip header line
        reader.__next__()
        for line in reader:
            labels.append(int(line[0]))
            images.append(list(map(int, line[1:])))
    # each column is an image
    images = np.array(images, dtype=np.long).T
    labels = np.array(labels)
    return images, labels

# create the architecture
nn = NeuralNetwork((784, 200, 40, 10), nonlinearities=LeakyReLU(0.01), lr=0.01)
# nn = NeuralNetwork((784, 200, 40, 10), nonlinearities=LeakyReLU(0.01),
#                                     loss_function=CrossEntropyLoss(), lr=0.01)

print("Loading data...")
images, labels = read_mnist_file(MNIST_TRAIN)
# normalize the images
images = images/255
print(images.shape)
print(labels.shape)

# go once over the training data
N = 1
losses = []
print_every = 200
for i in range(N):
    print("Epoch {}".format(i+1))

    for col in range(images.shape[1]):
        nn.forward(images[:, col])
        ## use with MSE loss
        expected = np.zeros((10, 1))
        expected[labels[col]] = 1
        ## use with cross entropy loss
        # expected = labels[col]
        l = nn.loss(expected)
        if (col+1) % print_every == 0:
            print("\t{:5}% of this epoch, loss at {:.5}".format(round(100*col/images.shape[1], 2), l))
            losses.append(l)
        nn.backprop()

print("Loading test data")
images, labels = read_mnist_file(MNIST_TEST)
rights = 0
wrongs = 0
for col in range(images.shape[1]):
    res = nn.forward(images[:, col])
    predicted = np.argmax(res)
    if predicted == labels[col]:
        rights += 1
    else:
        wrongs += 1

accuracy = round(100*rights/(rights+wrongs), 2)
# 92.78% with seed 123456789 for cross entropy loss
# 85.39% with seed 123456789 for cross entropy loss
print("Total accuracy of {}%".format(accuracy))
plt.plot(losses)
plt.title("Loss over training; final test accuracy of {}%".format(accuracy))
plt.show()