import numpy as np

class NeuralNetwork(object):
    def __init__(self, sizes):
        """
        Initializes a fully connected neural network with len(sizes) layers,
        where sizes[0] is the input size, sizes[-1] is the output size and
        all the values in between are sizes of hidden layers
        """
        self._sizes = sizes
        self._depth = len(sizes)

        # we assume inputs will be column vectors
        self._weight_matrices = [
            # variance
            (1/np.sqrt(sizes[idx]*sizes[idx+1])) * \
                # matrices with random entries
                np.random.randn(sizes[idx+1], sizes[idx]) for idx in range(self._depth - 1)
        ]
        self._bias_vectors = [
            # variance
            (1/np.sqrt(sizes[idx]*sizes[idx+1])) * \
                # vector with random entries
                np.random.randn(sizes[idx+1], 1) for idx in range(self._depth - 1)
        ]

    def forward(self, x):
        x = np.array(x)
        if len(x.shape) == 1:
            x = np.expand_dims(x, -1)
        elif len(x.shape) == 2:
            if x.shape[0] != self._sizes[0] and x.shape[1] == self._sizes[0]:
                x = x.T
            else:
                ValueError("Unexpected input of shape {}".format(x.shape))
        else:
            ValueError("Input has too many dimensions ({})".format(len(x.shape)))
            
        for mat, bias in zip(self._weight_matrices, self._bias_vectors):
            acc = np.dot(mat, acc) + bias
        return acc

if __name__ == "__main__":
    nn = NeuralNetwork((2, 5, 2))
    print(nn._weight_matrices)
    print(nn._bias_vectors)

    print(nn.forward(np.array([1,2])))