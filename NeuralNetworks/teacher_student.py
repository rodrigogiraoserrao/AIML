from neural_network import NeuralNetwork, LeakyReLU, Sigmoid, ReLU, CrossEntropyLoss
from random import shuffle, seed
import matplotlib.pyplot as plt
import numpy as np
import csv

seed(-12)
np.random.seed(124)

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
nn = NeuralNetwork((784, 200, 40, 10), nonlinearities=LeakyReLU(0.01),
                                    loss_function=CrossEntropyLoss(), lr=0.01)

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
indices = [i for i in range(images.shape[1])]
for i in range(N):
    shuffle(indices)
    print("Epoch {}".format(i+1))

    for idx, col in enumerate(indices):
        nn.forward(images[:, col])
        expected = labels[col]
        l = nn.loss(expected)
        if (idx+1) % print_every == 0:
            print("\t{:5}% of this epoch, loss at {:.5}".format(round(100*idx/images.shape[1], 2), l))
            losses.append(l)
        nn.backprop()

print("Loading test data")
test_images, test_labels = read_mnist_file(MNIST_TEST)
rights = 0
wrongs = 0
for col in range(test_images.shape[1]):
    res = nn.forward(test_images[:, col])
    predicted = np.argmax(res)
    if predicted == test_labels[col]:
        rights += 1
    else:
        wrongs += 1

# teacher got 94.97% accuracy
accuracy = round(100*rights/(rights+wrongs), 2)
print("Total accuracy of {}%".format(accuracy))

# create the student
student = NeuralNetwork((784, 30, 20, 10), nonlinearities=LeakyReLU(0.01), lr=0.01)
# go once over the training data
N = 1
losses = []
print_every = 200
for i in range(N):
    shuffle(indices)
    print("Epoch {}".format(i+1))

    for idx, col in enumerate(indices):
        expected = nn.forward(images[:, col])
        student.forward(images[:, col])
        l = student.loss(expected)
        if (idx+1) % print_every == 0:
            print("\t{:5}% of this epoch, loss at {:.5}".format(round(100*idx/images.shape[1], 2), l))
            losses.append(l)
        student.backprop()

# check the accuracy of the student!
rights = 0
wrongs = 0
for col in range(test_images.shape[1]):
    res = student.forward(test_images[:, col])
    predicted = np.argmax(res)
    if predicted == test_labels[col]:
        rights += 1
    else:
        wrongs += 1

# student model got 92.71% accuracy
accuracy = round(100*rights/(rights+wrongs), 2)
print("Student accuracy of {}%".format(accuracy))