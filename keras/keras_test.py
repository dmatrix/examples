import numpy as np
import argparse
import mlflow
import mlflow.sklearn

from keras import Sequential
from keras.layers import Dense, Dropout

from time import time

# Generate dummy data for training and test set
#
def gen_data(input_dim=20, bsize=1000):

    # Generate dummy data for training and test set
    x_train = np.random.random((bsize, input_dim))
    y_train = np.random.randint(2, size=(bsize, 1))
    x_test = np.random.random((int(bsize * 0.10) , input_dim))
    y_test = np.random.randint(2, size=(int(bsize * 0.10), 1))

    return [x_train, y_train, x_test, y_test]

#
#build a  a network
#
def build_model(in_dim=20, drate=0.5, out=64):
    mdl = Sequential()
    mdl.add(Dense(out, input_dim=in_dim, activation='relu'))
    if drate:
        mdl.add(Dropout(drate))
    mdl.add(Dense(out, activation='relu'))
    if drate:
        mdl.add(Dropout(drate))
    mdl.add(Dense(1, activation='sigmoid'))

    return mdl
#
# compile a network
#
def compile_and_run_model(mdl, train_data, epochs=20, batch_size=128):
    #
    # compile the model
    #
    mdl.compile(loss='binary_crossentropy',
              optimizer='rmsprop',
              metrics=['accuracy'])
    #
    # train the model
    #
    mdl.fit(train_data[0], train_data[1],
          epochs=epochs,
          batch_size=bs,
          verbose=0)

    #
    # evaluate the network
    #
    score = mdl.evaluate(train_data[2], train_data[3], batch_size=bs)
    print('Test loss:', score[0])
    print('Test accuracy:', score[1])

    print("Predictions for Y:")
    print(mdl.predict(data[2]))

    return ([score[0], score[1]])

if __name__ == '__main__':

    drop_rate= 0.5
    input_dim = 20
    bs = 1000
    output = 64
    epochs = 20
    batch_size = 128

    parser = argparse.ArgumentParser()
    parser.add_argument("--drop_rate", help="Drop rate", nargs='?', action='store', default=0.5, type=float)
    parser.add_argument("--input_dim", help="Input dimension for the network.", action='store', nargs='?', default=20, type=int)
    parser.add_argument("--bs", help="Number of rows or size of the tensor", action='store', nargs='?', default=1000, type=int)
    parser.add_argument("--output", help="Output from First & Hidden Layers", action='store',  nargs='?', default=64, type=int)
    parser.add_argument("--train_batch_size", help="Training Batch Size", nargs='?', action='store', default=128, type=int)
    parser.add_argument("--epochs", help="Number of epochs for training", nargs='?', action='store', default=20, type=int)

    args = parser.parse_args()
    print("drop_rate", args.drop_rate)
    print("input_dim", args.input_dim)
    print("size", args.bs)
    print("output", args.output)
    print("train_batch_size", args.train_batch_size)
    print("epochs", args.epochs)

    data = gen_data(input_dim=input_dim, bsize=bs)
    model = build_model(in_dim=input_dim, drate=drop_rate, out=output)

    start_time: float = time()
    with mlflow.start_run():
        results = compile_and_run_model(model, data, epochs=epochs, batch_size=bs)
        mlflow.log_param("drop_rate", args.drop_rate)
        mlflow.log_param("input_dim", args.input_dim)
        mlflow.log_param("size", args.bs)
        mlflow.log_param("output", args.output)
        mlflow.log_param("train_batch_size", args.train_batch_size)
        mlflow.log_param("epochs", args.epochs)
        mlflow.log_param("loss", results[0])
        mlflow.log_param("acc", results[1])

    end_time: float = time()
    
    print("Run time = %d" % (end_time-start_time))
