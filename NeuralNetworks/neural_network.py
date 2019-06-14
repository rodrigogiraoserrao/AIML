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
        self._intermediates = []

    def forward(self, x):
        """
        Given a (column) vector x, performs a forward pass with the given
        vector; returns the result of the forward pass.
        """
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

        self._intermediates = []
        acc = x
        for mat, bias in zip(self._weight_matrices, self._bias_vectors):
            pre_nonlin = np.dot(mat, acc) + bias
            ## apply the ReLU non-linearity
            post_nonlin = np.zeros(pre_nonlin.shape)
            post_nonlin[pre_nonlin > 0] = pre_nonlin[pre_nonlin > 0]
            self._intermediates.append((pre_nonlin, post_nonlin))
            acc = post_nonlin[::, ::]
        return acc

    def loss(self, output):
        """
        Taken a (column) vector with the expected result, compute
        the mean squared error with respect to the last forward pass.
        Also stores the gradients induced by the expected outcome.
        Returns 1/2(sum((y_hat - y)^2))
        """
        out = np.array(output)
        if len(out.shape) == 1:
            out = np.expand_dims(out, -1)
        elif len(out.shape) == 2:
            if out.shape[0] != self._sizes[0] and out.shape[1] == self._sizes[0]:
                out = out.T
            else:
                ValueError("Unexpected input of shape {}".format(out.shape))
        else:
            ValueError("Input has too many dimensions ({})".format(len(out.shape)))
        net_outs = self._intermediates[-1][-1]
        loss = 0.5*np.sum((out - net_outs)**2)

        return loss

if __name__ == "__main__":
    nn = NeuralNetwork((2, 5, 2))
    print(nn._weight_matrices)
    print(nn._bias_vectors)

    print(nn.forward(np.array([1,2])))
    print(nn._intermediates)