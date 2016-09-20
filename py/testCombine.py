#!/usr/bin/python
# encoding: utf-8
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_extraction.text import CountVectorizer
from confusion import calculate_confusion
import jieba
jieba.load_userdict("userdict.txt")
import os
import codecs
import random
import re
import sys


def load_dic():
    dicf = codecs.open('negative.txt', 'r', 'utf-8')
    fl = dicf.readlines()
    neg_dic = map(lambda w: w.strip(), fl)
    dicf.close()
    dicf = codecs.open('sensitive.txt', 'r', 'utf-8')
    fl = dicf.readlines()
    sensitive_dic = map(lambda w: w.strip(), fl)
    dicf.close()
    dicf = codecs.open('command.txt', 'r', 'utf-8')
    fl = dicf.readlines()
    command_dic = map(lambda w: w.strip(), fl)
    dicf.close()
    return neg_dic,sensitive_dic,command_dic


def preprocess_tfidf(file):
    wf = codecs.open('tf.idf', 'w', 'utf-8')
    con = codecs.open('content.txt', 'w', 'utf-8')
    with codecs.open(file, 'r', 'utf-8') as f:
        fl = f.readlines()
        for line in fl:
            if len(line.split('\t')) >= 6:
                content = line.split('\t')[5].strip()
            else:
                content = ' '
            if len(content) >= 2:
                wf.write(line)
                con.write(content+'\n')
    wf.close()
    con.close()


def read_thresh():
    thresh_dic = {}
    with codecs.open('thresh.txt', 'r', 'utf-8') as rf:
        fl = rf.readlines()
        for line in fl:
            key = line.split(' ')[0]
            val = float(line.split(' ')[1].strip())
            thresh_dic[key] = val
    return thresh_dic


def tf_idf(label_file):
    preprocess_tfidf(label_file)
    window_size = 6
    step_size = 4
    thresh = read_thresh()
    key_map = {}
    dic = {}
    file = 'tf.idf'
    corpus = []
    index = []
    with open(file, 'r') as rf:
        fl = rf.readlines()
        if len(fl) <= window_size:
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
                nl = ''
                for l in fl[offset:offset+window_size]:
                    dic[info].append(l.split('\t')[1])
                    nl += ' '.join(jieba.cut(l.split('\t')[5]))
                corpus.append(nl)
            else:
                nl = ''
                for l in fl[offset:]:
                    dic[info].append(l.split('\t')[1])
                    nl += ' '.join(jieba.cut(l.split('\t')[5]))
                corpus.append(' '.join(map(lambda x: ' '.join(jieba.cut(x.split('\t')[5])),
                                           fl[offset:])))
    vectorizer = CountVectorizer()  # 该类会将文本中的词语转换为词频矩阵，矩阵元素a[i][j] 表示j词在i类文本下的词频
    transformer = TfidfTransformer()  # 该类会统计每个词语的tf-idf权值
    # 第一个fit_transform是计算tf-idf，第二个fit_transform是将文本转为词频矩阵
    tfidf = transformer.fit_transform(vectorizer.fit_transform(corpus))
    word = vectorizer.get_feature_names()  # 获取词袋模型中的所有词语
    weight = tfidf.toarray()  # 将tf-idf矩阵抽取出来，元素a[i][j]表示j词在i类文本中的tf-idf权重
    ignore_words = read_ignore()
    for i in range(len(weight)):  # 打印每类文本的tf-idf词语权重，第一个for遍历所有文本，第二个for便利某一类文本下的词语权重
        for j in range(len(word)):
            if word[j] in thresh.keys():
                threshold = thresh[word[j]]
            else:
                threshold = 0.6
            if float(weight[i][j]) > threshold and word[j] not in ignore_words:
                member_id = index[i].split(' ')[0]
                if member_id not in key_map.keys():
                    key_map[member_id]={}
                for time in dic[index[i]]:
                    if time not in key_map[member_id].keys():
                        key_map[member_id][time]=[]
                    key_map[member_id][time].append(word[j])
                # repeat_file.write(index[i]+'\t'+word[j]+'\t'+str(weight[i][j])+'\n')
                print str(i), word[j], str(weight[i][j])
    return key_map


def read_ignore():
    with codecs.open('ignore.txt', 'r', 'utf-8') as f:
        return [x.strip() for x in f.readlines()]


# 声音是否有情绪

# 指令是否有情绪
def has_sensitive_word(cai,line,sen_dic):
    if cai == '0':
        return '0'
    sentence = line.split('\t')[5]
    for s_word in sen_dic:
        pattern = re.compile(s_word)
        match = pattern.match(sentence)
        if match:
            return '1'
    return '0'


# 指令前后文重复
def sensitive_repeat(sentence, sentences, sen_dic):
    for s_word in sen_dic:
        pattern = re.compile(s_word)
        match = pattern.match(sentence)
        if match:
            for sen in sentences:
                sen = sen.split('\t')[5]
                double_match = pattern.match(sen)
                if double_match:
                    return '1'
    return '0'


def is_Chinese(word):
    return word >= u'\u4e00' and word<=u'\u9fa5'


# 一句内的多次重复(比如,关机关机关机)
def sentence_repeat(sen):
    xiaole = sen.split('\t')[4]
    content = sen.split('\t')[5]
    content = '@'.join(jieba.cut(content))
    content = content.split('@')
    repeat_times = 3
    ignore_words = read_ignore()
    word_map = {}
    for word in content:
        if is_Chinese(word) and word not in ignore_words:
            if word not in word_map.keys():
                word_map[word] = 1
            else:
                word_map[word] += 1

    for word in word_map.keys():
        if word_map[word] >= repeat_times and len(word)>=2 and word not in xiaole:
            return '1'
    return '0'


# 该句出现重复词
def context_repeat(sentence, keyword_map):
    member_id = sentence.split('\t')[0]
    time = sentence.split('\t')[1]
    content = sentence.split('\t')[5]
    xiaole_word = sentence.split('\t')[4][0:-2]
    if member_id in keyword_map.keys():
        if time in keyword_map[member_id].keys():
            for keyword in keyword_map[member_id][time]:
                if keyword in content and keyword not in xiaole_word:
                    return '1'
    return '0'


# 前后文多次重复
def context_repeat_counts(sentences, keyword_map):
    cnt = 0
    max_cnt = 2
    for sentence in sentences:
        member_id = sentence.split('\t')[0]
        time = sentence.split('\t')[1]
        content = sentence.split('\t')[5]
        xiaole_word = sentence.split('\t')[4]
        if member_id in keyword_map.keys():
            if time in keyword_map[member_id].keys():
                for keyword in keyword_map[member_id][time]:
                    if keyword in content and keyword not in xiaole_word:
                        cnt += 1
    if cnt >= max_cnt:
        return '1'
    else:
        return '0'


# 一句内的消极情感
def has_neg_word(neg_dic, sentence):
    features = ''
    sentence = sentence.split('\t')[5]
    for neg_word in neg_dic:
        pattern = re.compile(neg_word)
        match = pattern.match(sentence)
        if match:
            return '1'
    return '0'


# 前后文的消极情感
def neg_word_counts(neg_dic, sentences):
    cnt = 0
    max_cnt = 1
    for sen in sentences:
        sen = sen.split('\t')[5]
        for neg_word in neg_dic:
            pattern = re.compile(neg_word)
            match = pattern.match(sen)
            if match:
                cnt += 1
                break
    if cnt >= max_cnt:
        return '1'
    else:
        return '0'

# 小乐的话是否重复或是否有错误提示或是否被摇晃
def xiaole_word(lines):
    sentences = map(lambda x: x.split('\t')[4], lines)
    cur_sen = sentences[-1]
    repeat = '0'
    last = len(lines)-2
    if last>0:
        if sentences[last] == cur_sen:
            return '1'
    shake_sentence = [u'我快被摇晕了', u'救命啊!晃得我好晕啊', u'倒着不舒服,我都看不到你的脸了', ]
    shake = '0'
    for s in shake_sentence:
        if s in sentences[-1]:
            return '1'
    if 'wrong_tip' in cur_sen:
        return '1'
    return '0'


def read_caikang():
    with codecs.open('annotation.txt', 'r', 'utf-8') as f:
        alist = [x.strip() for x in f.readlines()]
    return alist


def convert2svm(feature):
    segs = feature.split(',')
    cate = segs[len(segs) - 1]
    fea=''
    for i in range(1,len(segs)):
        fea += ' '+str(i)+':'+segs[i-1]
    result = cate + fea + '\n'
    return result


def repeat_order(command_dic, fl):
    if len(fl) >= 2:
        sen = fl[-1].split('\t')[5]
        last_sen = fl[-2].split('\t')[5]
        for command in command_dic:
            pattern = re.compile(command)
            if pattern.match(sen) and pattern.match(last_sen):
                return '1'
    return '0'


def extract_features(label_file):
    keyword_map = tf_idf(label_file)
    neg_dic,sen_dic,command_dic = load_dic()
    f = codecs.open(label_file, 'r', 'utf-8')
    fl = f.readlines()
    f.close()
    related_numbers = 3
    svm_list = []
    caikang = read_caikang()
    labels = []
    caikangs = []
    for i in range(len(fl)):
        start = i - related_numbers if i>=related_numbers else 0
        feature = ''
        # 1声音是否有情绪
        feature += caikang[i] + ','
        # 2指令是否有情绪
        feature += has_sensitive_word(caikang[i],fl[i],sen_dic)+','
        # 3指令前文重复
        feature += sensitive_repeat(fl[i].split('\t')[5],fl[start:i],sen_dic) + ','
        # 4一句内的多次重复(比如,关机关机关机)
        feature += sentence_repeat(fl[i]) + ','
        # 5一句内出现高频词
        feature += context_repeat(fl[i], keyword_map) + ','
        # 6上文出现高频词
        feature += context_repeat_counts(fl[start:i], keyword_map) + ','
        # 7一句内的消极情感
        feature += has_neg_word(neg_dic, fl[i]) + ','
        # 8上文(包含该句)消极情感词
        feature += neg_word_counts(neg_dic, fl[start:i+1]) + ','
        # 9小乐的话是否重复或是否有错误提示或是否被摇晃
        feature += xiaole_word(fl[start:i+1])+','
        # 10连续两次点播资源或命令
        feature += repeat_order(command_dic, fl[start:i+1]) + ','

        feature += fl[i].split('\t')[5].strip().split(' ')[0]+','
        caikangs.append(caikang[i])
        # data.append(map(lambda x: num(x.strip()), feature.split(',')[:-1]))
        #label = '1' if fl[i].split('\t')[6].strip()=='1' else '0'
        label = '0'
        labels.append(label)
        feature += label
        svm_list.append(convert2svm(feature))
    with codecs.open('svm_test', 'w', 'utf-8') as wf:
        wf.writelines(svm_list)


def my_test(suffix):
    wf = codecs.open('predict_'+suffix+'.txt', 'w', 'utf-8')
    max_cnt = 2
    with codecs.open('svm_test', 'r', 'utf-8') as f:
        fl = f.readlines()
        label = []
        details = []
        for line in fl:
            cnt = 0
            feas = line.strip().split(' ')[1:-1]
            for feature in feas:
                if feature.split(':')[1]=='1':
                    cnt += 1
            if cnt >= max_cnt:
                label.append('2')
                details.append(line)
                # wf.write('1\t'+line)
            else:
                # wf.write('0\t'+line)
                label.append('0')
                details.append(line)
        for i in range(len(label)):
            if label[i] == '2':
                if i >= 1 and label[i-1] == '0':
                    label[i-1] = '1'
                if i+1 < len(label) and label[i+1] == '0':
                    label[i+1] = '1'
        print len(label)
        for i in range(len(label)):
            if label[i] == '1' or label[i] == '2':
                for j in range (2,5):
                    if i+j < len(label):
                        if label[i+j]=='1' or label[i+j]=='2':
                            for k in range(1,j):
                                if label[i+k]=='0':
                                    label[i+k]='1'
        for i in range(len(label)):
            wf.write(label[i]+'\t'+fl[i])
    wf.close()


if __name__ == '__main__':
    suffix = sys.argv[1]
    labelf = 'labeled.txt'
    extract_features(labelf)
    my_test(suffix)




