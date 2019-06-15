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
        # store the intermediate computations to enable computing the gradients
        self._intermediates = []
        # store the gradients to enable batch gradient updates
        self._grads = []
        for W, b in zip(self._weight_matrices, self._bias_vectors):
            self._grads.append((np.zeros(W.shape), np.zeros(b.shape)))
        # store the number of backpasses we already did, so we can average
        self._passes_done = 0

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

        self._intermediates = [(None, x[::, ::])]
        acc = x
        for mat, bias in zip(self._weight_matrices, self._bias_vectors):
            pre_nonlin = np.dot(mat, acc) + bias
            ## apply leaky-ReLU non-linearity
            post_nonlin = np.array(pre_nonlin)
            post_nonlin[pre_nonlin < 0] = 0.1*pre_nonlin[pre_nonlin < 0]
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

        # compute the gradients in a backward fassion
        self._passes_done += 1
        for t in range(self._depth - 2, -1, -1):
            # row vector
            if t + 1 == self._depth - 1:
                dLdpos = net_outs.T - out.T # row
            else:
                Wfront = self._weight_matrices[t+1] # matrix
                preFront = self._intermediates[t+2][0] # column
                dfdpre = np.ones(preFront.shape) # column
                dfdpre[preFront < 0] = 0.1
                dLdpos = np.dot((dLdpos*dfdpre.T), Wfront)
            pos = self._intermediates[t][1] # column
            preFront = self._intermediates[t+1][0] # column
            dfdpre = np.ones(preFront.shape) # column
            dfdpre[preFront < 0] = 0.1
            dposdW = np.dot(dfdpre, pos.T)
            dLdW = dLdpos.T*dposdW
            dLdb = dfdpre*dLdpos.T
            # accumulate
            prevW, prevb = self._grads[t]
            # print("preFront {}".format(preFront))
            # print("dfdpre {}".format(dfdpre))
            # print("pos.T {}".format(pos.T))
            # print("dposdW {}".format(dposdW))
            # print("dLdpos {}".format(dLdpos))
            # print("dLdW {}".format(dLdW))
            self._grads[t] = (prevW + dLdW, prevb + dLdb)

        return loss

    def backprop(self):
        """
        Alters the weights and biases with the gradients stored
        """
        for i in range(self._depth - 1):
            self._weight_matrices[i] -= 0.01*self._grads[i][0]/self._passes_done
            self._bias_vectors[i] -= 0.01*self._grads[i][1]/self._passes_done
            self._grads[i] = (np.zeros(self._grads[i][0].shape),
                                np.zeros(self._grads[i][1].shape))
        self._passes_done = 0

if __name__ == "__main__":
    import matplotlib.pyplot as plt

    nn = NeuralNetwork((3, 2))
    Wout = np.array([[-0.169, 0.516, 0.282],
                    [-0.052, 0.401, -0.028]])
    bout = np.array([[1.344],[-0.65]])
    nn._weight_matrices = [Wout]
    nn._bias_vectors = [bout]
    print(nn._weight_matrices)
    print(nn._bias_vectors)

    inp = np.array([[0.33], [1.26], [-0.12]])
    out = np.array([[.3], [.4]])

    print(nn.forward(inp))
    print(nn.loss(out))
    print(nn._grads)

    nn = NeuralNetwork((3,4,2))
    losses = []
    for _ in range(3000):
        for _ in range(20):
            nn.forward(np.random.rand(3,1))
            nn.loss(np.ones((2,1)))
        nn.forward(np.random.rand(3,1))
        losses.append(nn.loss(np.ones((2,1))))
        nn.backprop()
    print(nn.forward(np.random.rand(3,1)))
    plt.plot(losses)
    plt.show()