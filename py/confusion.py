#!usr/bin/python
# encoding: utf-8

from sklearn.metrics import confusion_matrix
from sklearn.metrics import classification_report
import codecs


def my_confusion_matrix(y_true, y_pred):
    labels = list(set(y_true))
    conf_mat = confusion_matrix(y_true, y_pred, labels=labels)
    print "confusion_matrix(left labels: y_true, up labels: y_pred):"
    print "labels\t",
    for i in range(len(labels)):
        print labels[i], "\t",
    print
    for i in range(len(conf_mat)):
        print i, "\t",
        for j in range(len(conf_mat[i])):
            print conf_mat[i][j], '\t',
        print
    print


def my_classification_report(y_true, y_pred):
    print "classification_report(left: labels):"
    print classification_report(y_true, y_pred)


def my_PCA(data):#data without target, just train data, without train target.
    from sklearn import decomposition
    pca_sklearn = decomposition.PCA()
    pca_sklearn.fit(data)
    main_var = pca_sklearn.explained_variance_
    print sum(main_var)*0.9
    import matplotlib.pyplot as plt
    n = 15
    plt.plot(main_var[:n])
    plt.show()


def calculate_confusion(o_file, p_file):
    with codecs.open(p_file, 'r', 'utf-8') as f:
        fl = f.readlines()
        y_pred = [int(x.strip()) for x in fl]
        # with codecs.open('output.txt', 'r', 'utf-8') as f:
        #     fl = f.readlines()
        #     for line in fl:
        #         segs = line.strip().split('\t')
        #         if float(segs[3]) > threshold:
        #             y_pred.append(1)
        #         else:
        #             y_pred.append(0)
        # y_pred = [int(x.strip()) for x in f.readlines() if x is not '']
    with codecs.open(o_file, 'r', 'utf-8') as ftest:
        y_true = [int(x.strip().split(' ')[0]) for x in ftest.readlines() if x is not '']
    my_confusion_matrix(y_true=y_true, y_pred=y_pred)
    my_classification_report(y_true, y_pred)


if __name__ == '__main__':
    calculate_confusion('svm_test', 'svm_test.predict')
