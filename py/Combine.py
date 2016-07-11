#!/usr/bin/python
# encoding: utf-8
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_extraction.text import CountVectorizer
from confusion import calculate_confusion
import jieba
import os
import codecs
import random
import re
import sys


def load_neg_dic():
    dicf = codecs.open('negative.txt', 'r', 'utf-8')
    fl = dicf.readlines()
    neg_dic = map(lambda w: w.strip(), fl)
    dicf.close()
    return neg_dic


def preprocess_tfidf(file):
    wf = codecs.open('tf.idf', 'w', 'utf-8')
    with codecs.open(file, 'r', 'utf-8') as f:
        fl = f.readlines()
        for line in fl:
            if len(line.split('\t')) >= 6:
                content = line.split('\t')[5].strip()
            else:
                content = ' '
            if len(content) >= 2:
                wf.write(line)
    wf.close()


def tf_idf(label_file):
    preprocess_tfidf(label_file)
    window_size = 30
    step_size = 15
    threshold = 0.45
    key_map = {}
    dic = {}
    file = 'tf.idf'
    corpus = []
    index = []
    with open(file, 'r') as rf:
        fl = rf.readlines()
        if len(fl) <= window_size:
            print 'error: length of fl is too short!'
            return key_map
        for offset in range(0, len(fl), step_size):
            line = fl[offset]
            if len(line) < 1:
                continue
            member_id = line.split('\t')[0]
            info = member_id+' '+line.split('\t')[1]
            index.append(info)
            if info not in dic.keys():
                dic[info] = []
            if offset+window_size < len(fl):
                for l in fl[offset:offset+window_size]:
                    dic[info].append(l.split('\t')[1])
                corpus.append(' '.join(map(lambda x: ' '.join(jieba.cut(x.split('\t')[5])),
                                           fl[offset:offset+window_size])))
            else:
                for l in fl[offset:]:
                    dic[info].append(l.split('\t')[1])
                corpus.append(' '.join(map(lambda x: ' '.join(jieba.cut(x.split('\t')[5])),
                                           fl[offset:])))
    vectorizer = CountVectorizer()  # 该类会将文本中的词语转换为词频矩阵，矩阵元素a[i][j] 表示j词在i类文本下的词频
    transformer = TfidfTransformer()  # 该类会统计每个词语的tf-idf权值
    # 第一个fit_transform是计算tf-idf，第二个fit_transform是将文本转为词频矩阵
    tfidf = transformer.fit_transform(vectorizer.fit_transform(corpus))
    word = vectorizer.get_feature_names()  # 获取词袋模型中的所有词语
    weight = tfidf.toarray()  # 将tf-idf矩阵抽取出来，元素a[i][j]表示j词在i类文本中的tf-idf权重
    for i in range(len(weight)):  # 打印每类文本的tf-idf词语权重，第一个for遍历所有文本，第二个for便利某一类文本下的词语权重
        for j in range(len(word)):
            if float(weight[i][j]) > threshold:
                member_id = index[i].split(' ')[0]
                if member_id not in key_map.keys():
                    key_map[member_id]={}
                for time in dic[index[i]]:
                    if time not in key_map[member_id].keys():
                        key_map[member_id][time]=[]
                    key_map[member_id][time].append(word[j])
                # repeat_file.write(index[i]+'\t'+word[j]+'\t'+str(weight[i][j])+'\n')
    return key_map


# def sentence_repeat(content):
#     if has_repeat_word(content):
#         return '1'
#     else:
#         return '0'


def read_ignore():
    with codecs.open('ignore.txt', 'r', 'utf-8') as f:
        return [x.strip() for x in f.readlines()]


def sentence_repeat(content):
    content = '@'.join(jieba.cut(content))
    content = content.split('@')
    repeat_times = 2
    ignore_words = read_ignore()
    word_map = {}
    for word in content:
        if word not in word_map.keys():
            word_map[word] = 1
        else:
            word_map[word] += 1

    for word in word_map.keys():
        if word in ignore_words:
            return '0'
        if word_map[word] >= repeat_times and len(word)>=2:
            return '1'
    return '0'


def context_repeat(sentence, keyword_map):
    member_id = sentence.split('\t')[0]
    time = sentence.split('\t')[1]
    content = sentence.split('\t')[5]
    if member_id in keyword_map.keys():
        if time in keyword_map[member_id].keys():
            for keyword in keyword_map[member_id][time]:
                if keyword in content:
                    return '1'
    return '0'


def neg_word_counts(neg_dic, sentences):
    cnt = 0
    max_cnt = 2
    for sen in sentences:
        sen = sen.split('\t')[5]
        for neg_word in neg_dic:
            pattern = re.compile(neg_word)
            match = pattern.match(sen)
            if match:
                cnt += 1
                break
        # for seg in jieba.cut(sen.split('\t')[6]):
        #     if seg in neg_dic:
        #         cnt += 1
    if cnt >= max_cnt:
        return '1'
    else:
        return '0'


def context_repeat_counts(sentences, keyword_map):
    cnt = 0
    max_cnt = 2
    for sentence in sentences:
        member_id = sentence.split('\t')[0]
        time = sentence.split('\t')[1]
        content = sentence.split('\t')[5]
        if member_id in keyword_map.keys():
            if time in keyword_map[member_id].keys():
                for keyword in keyword_map[member_id][time]:
                    if keyword in content:
                        cnt += 1
    if cnt >= max_cnt:
        return '1'
    else:
        return '0'


def has_neg_word(neg_dic, sentence):
    features = ''
    sentence = sentence.split('\t')[5]
    for neg_word in neg_dic:
        pattern = re.compile(neg_word)
        match = pattern.match(sentence)
        if match:
            features += ',1'
        else:
            features += ',0'
        # flag = False
        # for seg in jieba.cut(sentence):
        #     if neg_word == seg:
        #         flag = True
        #         features += ',1'
        #         break
        # if not flag:
        #     features += ',0'
    return features[1:]


def simple_audio_features(sentence):
    member_id = sentence.split('\t')[0]
    link = sentence.split('\t')[3]
    record_id = link.split('/')[len(link.split('/')) - 1]
    fl = open('audio/' + member_id + '/' + record_id.split('.')[0] + '.arff').readlines()
    if 'unknown' in fl[-1]:
        return fl[-1].split(',')[1]+','+fl[-1].split(',')[169]+','+fl[-1].split(',')[181]
    else:
        print sentence, fl[-1]
        return ''


def audio_features(sentence):
    member_id = sentence.split('\t')[0]
    link = sentence.split('\t')[4]
    record_id = link.split('/')[len(link.split('/'))-1]
    fl = open('audio/'+member_id+'/'+record_id.split('.')[0]+'.arff').readlines()
    if 'unknown' in fl[-1]:
        return ','.join(fl[-1].split(',')[1:-1])
    else:
        print sentence, fl[-1]
        return ''


def audio_context_features(sentences):
    result = ''
    for i in range(len(sentences)):
        line = sentences[i]
        re = audio_features(line)
        if len(re) > 2:
            result += ',' + re
        else:
            raise ValueError
    return result[1:]


def get_content_length(sentence):
    return str(len(sentence.split('\t')[5].strip())/len(u'一'))


def xiaole_word(lines):
    sentences = map(lambda x: x.split('\t')[4], lines)
    repeat = '0'
    for i in range(3):
        if sentences[i] == sentences[3]:
            repeat = '1'
            break
    shake_sentence = [u'我快被摇晕了', u'救命啊!晃得我好晕啊',u'倒着不舒服,我都看不到你的脸了',]
    shake = '0'
    for s in shake_sentence:
        if s in sentences[3]:
            shake = '1'
            break
    wrong_tip = 0
    for sen in sentences:
        if 'wrong_tip' in sen:
            wrong_tip += 1
    return repeat+','+shake+','+str(wrong_tip)




def add_negative(feature):
    segs = feature.split(',')
    result = ''
    for i in range(len(segs)-1):
        num = abs(float(segs[i]))
        neg = '0' if float(segs[i]) >= 0 else '1'
        result += str(i*2)+':'+str(num)+' '+str(2*i+1)+':'+neg+' '
    return result


def writeIntoMaxent(feature):
    segs = feature.split(',')
    cate = segs[len(segs)-1]
    result = cate+' '+add_negative(feature)+'\n'
    return result


def my_PCA(data):#data without target, just train data, without train target.
    from sklearn.decomposition import PCA
    pca = PCA(n_components=10)
    return pca.fit_transform(data)


def num(s):
    try:
        return int(s)
    except ValueError:
        return float(s)


def read_caikang():
    with codecs.open('annotation.txt', 'r', 'utf-8') as f:
        alist = [x.strip() for x in f.readlines()]
    return alist


def create_pca_arff(pca,caikang,labels):
    header = '@relation pca_arff\n'
    id_attri = '@attribute id numeric\n'
    n_samples = len(pca)
    n_com = len(pca[0])
    with codecs.open('pca.arff', 'w', 'utf-8') as wf:
        wf.write(header)
        wf.write(id_attri)
        for i in range(n_com):
            wf.write('@attribute pca_'+str(i+1)+' numeric\n')
        wf.write('@attribute caikang {0,1}\n')
        wf.write('@attribute class {0,1}\n@data\n')
        for i in range(n_samples):
            wf.write(str(i+4)+','+','.join([str(x) for x in pca[i]])+','+caikang[i]+','+labels[i]+'\n')




# write into maxent file
def write_maxent(maxent_list):
    # random.shuffle(maxent_list)
    maxent_file = codecs.open('maxent_combined_train.txt', 'w', 'utf-8')
    train_size = int(len(maxent_list)*0.66)
    train_list = maxent_list[0:train_size]
    test_list = maxent_list[train_size:]
    train_file = codecs.open('train.txt', 'w', 'utf-8')
    test_file = codecs.open('test.txt', 'w', 'utf-8')
    train_file.writelines(train_list)
    test_file.writelines(test_list)
    maxent_file.writelines(maxent_list)
    maxent_file.close()
    train_file.flush()
    train_file.close()
    test_file.flush()
    test_file.close()


def convert2svm(feature):
    segs = feature.split(',')
    cate = segs[len(segs) - 1]
    fea=''
    for i in range(1,len(segs)):
        fea += ' '+str(i)+':'+segs[i-1]
    result = cate + fea + '\n'
    return result


def write_svm(svm_list):
    with codecs.open('svm_test', 'w', 'utf-8') as wf:
        wf.writelines(svm_list)
    # random.shuffle(svm_list)
    #train_size = int(len(svm_list)*0.66)
    #train_list = svm_list[0:train_size]
    #test_list = svm_list[train_size:]
    #with codecs.open('svm_train', 'w', 'utf-8') as f:
    #    f.writelines(train_list)
    #with codecs.open('svm_test', 'w', 'utf-8') as f:
    #    f.writelines(test_list)


def extract_features(label_file):
    keyword_map = tf_idf(label_file)
    neg_dic = load_neg_dic()
    f = codecs.open(label_file, 'r', 'utf-8')
    fl = f.readlines()
    f.close()
    related_numbers = 3
    maxent_list = []
    weka_list = []
    svm_list = []
    data = []
    caikang = read_caikang()
    labels = []
    caikangs = []
    for i in range(len(fl)):
        start = i - related_numbers if i>=related_numbers else 0
        end = i + related_numbers+1 if i+related_numbers < len(fl) else len(fl)
        feature = ''
        feature += caikang[i] + ','
        feature += sentence_repeat(fl[i].split('\t')[5]) + ','
        feature += context_repeat(fl[i], keyword_map) + ','
        feature += context_repeat_counts(fl[start:end], keyword_map) + ','
        feature += has_neg_word(neg_dic, fl[i]) + ','
        feature += neg_word_counts(neg_dic, fl[start:end]) + ','
        feature += get_content_length(fl[i]) + ','
        # feature += xiaole_word(fl[start:end])+','
        # try:
        #     audio = audio_context_features(fl[start:end])
        #     feature += audio+','
        #     data.append(map(lambda x: num(x.strip()), audio.split(',')))
        # except ValueError:
        #     print ValueError.message
        caikangs.append(caikang[i])
        # data.append(map(lambda x: num(x.strip()), feature.split(',')[:-1]))
        # label = '1' if fl[i].split('\t')[7] == '1' or fl[i].split('\t')[7]=='1' else '0'
        label = '0'
        labels.append(label)
        feature += label
        weka_list.append(feature + '\n')
        svm_list.append(convert2svm(feature))
        maxent_list.append(writeIntoMaxent(feature))
    # pca = my_PCA(data)
    # create_pca_arff(pca, caikangs, labels)
    shuffle = range(len(labels))
    random.shuffle(shuffle)
    weka_shuffle = []
    svm_shuffle = []
    maxent_shuffle = []
    for i in range(len(labels)):
        weka_shuffle.append(weka_list[shuffle[i]])
        svm_shuffle.append(svm_list[shuffle[i]])
        maxent_shuffle.append(maxent_list[shuffle[i]])
    #write_maxent(maxent_shuffle)

    # 乱序,用来生成训练集和验证集
    # write_svm(svm_shuffle)

    # 顺序,用来生成测试集
    write_svm(svm_list)


def classifyByModule(label_f):
    rf = codecs.open(label_f, 'r', 'utf-8')
    test_f = codecs.open('svm_test', 'r', 'utf-8')
    testfl = test_f.readlines()
    test_f.close()
    rfl = rf.readlines()
    rf.close()
    log_dic={}
    with codecs.open('svm_test.predict', 'r', 'utf-8') as f:
        fl = f.readlines()
        for i in range(len(fl)):
            if fl[i].strip() == '1':
                id = int(testfl[i].split(' ')[1].split(':')[1])
                module = rfl[id-1].split('\t')[2]
                if module not in log_dic.keys():
                    log_dic[module] = []
                log_dic[module].append(rfl[id-1])
    for mod in log_dic.keys():
        with codecs.open('abnormal_'+mod+'.log', 'w', 'utf-8') as f:
            for line in log_dic[mod]:
                f.write(line+'\n')



def runSVM():
    # os.popen('/Users/linchuan/Downloads/libsvm-3.21/svm-train svm_total')
    os.popen('/home/libsvm-3.21/svm-predict svm_test svm_total.model svm_test.predict')


def my_test(suffix):
    wf = codecs.open('svm_test_'+suffix+'.predict', 'w', 'utf-8')
    max_cnt = 2
    with codecs.open('svm_test', 'r', 'utf-8') as f:
        fl = f.readlines()
        for line in fl:
            cnt = 0
            feas = line.strip().split(' ')[1:]
            length = line.strip().split(' ')[len(line.strip().split(' '))-1].split(':')[1]
            cai_audio = feas[0].split(':')[1]
            if length <= 2 and cai_audio=='1':
                wf.write('1\n')
                continue
            for feature in feas:
                if feature.split(':')[1]=='1':
                    cnt += 1
                    if cnt == max_cnt:
                        wf.write('1\n')
                        break
            if cnt != max_cnt:
                wf.write('0\n')
    wf.close()


if __name__ == '__main__':
    suffix = sys.argv[1]
    labelf = 'labeled.txt'
    extract_features(labelf)
    my_test(suffix)
    # runSVM()
    # classifyByModule(labelf)
    # calculate_confusion('svm_test', 'annotation.txt')
    calculate_confusion('svm_test', 'svm_test_'+suffix+'.predict')




