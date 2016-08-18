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


def load_neg_dic():
    dicf = codecs.open('negative.txt', 'r', 'utf-8')
    fl = dicf.readlines()
    neg_dic = map(lambda w: w.strip(), fl)
    dicf.close()
    dicf = codecs.open('sensitive.txt', 'r', 'utf-8')
    fl = dicf.readlines()
    sensitive_dic = map(lambda w: w.strip(), fl)
    dicf.close()
    return neg_dic,sensitive_dic


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
    window_size = 20
    step_size = 10
    thresh = read_thresh()
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
            threshold = 0
            if word[j] in thresh.keys():
                threshold = thresh[word[j]]
            else:
                threshold = 0.5
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
                if sen==sentence:
                    continue
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
    repeat_times = 2
    ignore_words = read_ignore()
    word_map = {}
    for word in content:
        if is_Chinese(word):
            if word not in word_map.keys():
                word_map[word] = 1
            else:
                word_map[word] += 1

    for word in word_map.keys():
        if word_map[word] >= repeat_times and len(word)>=2 and word not in xiaole:
            return '1'
    return '0'


# 前后文多次重复
def context_repeat(sentence, keyword_map):
    member_id = sentence.split('\t')[0]
    time = sentence.split('\t')[1]
    content = sentence.split('\t')[5]
    xiaole_word = sentence.split('\t')[4]
    if member_id in keyword_map.keys():
        if time in keyword_map[member_id].keys():
            for keyword in keyword_map[member_id][time]:
                if keyword in content and keyword not in xiaole_word:
                    return '1'
    return '0'


def context_repeat_counts(sentences, keyword_map):
    cnt = 0
    max_cnt = 3
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
    max_cnt = 2
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
    wrong_tip = '0'
    if 'wrong_tip' in cur_sen:
        return '1'
    return '0'

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




def classifyByModule(label_f, suffix):
    rf = codecs.open(label_f, 'r', 'utf-8')
    test_f = codecs.open('svm_test', 'r', 'utf-8')
    testfl = test_f.readlines()
    test_f.close()
    rfl = rf.readlines()
    rf.close()
    log_dic={}
    with codecs.open('predict_'+suffix+'.txt', 'r', 'utf-8') as f:
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


def list_wrong_case(f1, f2):
    fl = open('labeled.txt', 'r').readlines()
    right_case=[]
    false_false = []
    false_true = []
    right_fea = []
    feature = []
    false_feature = []
    with codecs.open(f1, 'r', 'utf-8') as f1:
        fl1 = f1.readlines()
    with codecs.open(f2, 'r', 'utf-8') as f2:
        fl2 = f2.readlines()
    for i in range(1482):
        if fl1[i].split(' ')[0]==fl2[i].strip()=='1':      # right case
            # id = int(ids[i+int(1485-len(fl1))].strip())
            right_fea.append(fl1[i])
            right_case.append('\t'.join(fl[i].split('\t')[4:]))
        elif fl1[i].split(' ')[0]=='1' and fl2[i].strip()=='0':     # false negative case
            # id = int(ids[i+int(1485-len(fl1))].strip())
            false_false.append('\t'.join(fl[i].split('\t')[4:]))
            feature.append(fl1[i])
        elif fl1[i].split(' ')[0]=='0' and fl2[i].strip()=='1':     # false positive case
            # id = int(ids[i+int(1485-len(fl1))].strip())
            false_true.append('\t'.join(fl[i].split('\t')[4:]))
            false_feature.append(fl1[i])
    print 'Right case:'
    for i in range(len(right_case)):
        print right_case[i], right_fea[i]
    print 'False negative:'
    for i in range(len(false_false)):
        print false_false[i], feature[i]
    print 'False positive:'
    for i in range(len(false_true)):
        print false_true[i], false_feature[i]


def extract_features(label_file):
    keyword_map = tf_idf(label_file)
    neg_dic,sen_dic = load_neg_dic()
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

        # 1声音是否有情绪
        feature += caikang[i] + ','

        # 2指令是否有情绪
        feature += has_sensitive_word(caikang[i],fl[i],sen_dic)+','

        # 3指令前后文重复
        feature += sensitive_repeat(fl[i].split('\t')[5],fl[start:end],sen_dic) + ','

        # 4一句内的多次重复(比如,关机关机关机)
        feature += sentence_repeat(fl[i]) + ','

        # 5前后文多次重复
        feature += context_repeat(fl[i], keyword_map) + ','
        #feature += context_repeat_counts(fl[start:end], keyword_map) + ','

        # 6一句内的消极情感
        feature += has_neg_word(neg_dic, fl[i]) + ','

        # 7前后文的消极情感
        feature += neg_word_counts(neg_dic, fl[start:end]) + ','

        # 8小乐的话是否重复或是否有错误提示或是否被摇晃
        feature += xiaole_word(fl[start:i+1])+','

        # 9文本长度
        #feature += get_content_length(fl[i]) + ','

        feature += fl[i].split('\t')[5].strip().split(' ')[0]+','
        # try:
        #     audio = audio_context_features(fl[start:end])
        #     feature += audio+','
        #     data.append(map(lambda x: num(x.strip()), audio.split(',')))
        # except ValueError:
        #     print ValueError.message
        caikangs.append(caikang[i])
        # data.append(map(lambda x: num(x.strip()), feature.split(',')[:-1]))
        #label = '1' if fl[i].split('\t')[6].strip()=='1' else '0'
        label = '0'
        labels.append(label)
        feature += label
        #weka_list.append(feature + '\n')
        svm_list.append(convert2svm(feature))
        #maxent_list.append(writeIntoMaxent(feature))
    # pca = my_PCA(data)
    # create_pca_arff(pca, caikangs, labels)
    shuffle = range(len(labels))
    random.shuffle(shuffle)
    weka_shuffle = []
    svm_shuffle = []
    maxent_shuffle = []
    for i in range(len(labels)):
        #weka_shuffle.append(weka_list[shuffle[i]])
        svm_shuffle.append(svm_list[shuffle[i]])
        #maxent_shuffle.append(maxent_list[shuffle[i]])
    #write_maxent(maxent_shuffle)

    # 乱序,用来生成训练集和验证集
    # write_svm(svm_shuffle)

    # 顺序,用来生成测试集
    write_svm(svm_list)


def my_test(suffix):
    wf = codecs.open('predict_'+suffix+'.txt', 'w', 'utf-8')
    max_cnt = 2
    with codecs.open('svm_test', 'r', 'utf-8') as f:
        fl = f.readlines()

        for line in fl:
            cnt = 0
            feas = line.strip().split(' ')[1:-1]
            #length = int(line.strip().split(' ')[len(line.strip().split(' '))-1].split(':')[1])
            #cai_audio = feas[0].split(':')[1]
            # if length <= 2 and cai_audio=='1':
            #     wf.write('1\n')
            #     continue
            for feature in feas:
                if feature.split(':')[1]=='1':
                    cnt += 1

            if cnt >= max_cnt:
                wf.write('1\t'+line)
            else:
                wf.write('0\t'+line)
    wf.close()


if __name__ == '__main__':
    suffix = sys.argv[1]
    labelf = 'labeled.txt'
    extract_features(labelf)
    my_test(suffix)
    # runSVM()
    # classifyByModule(labelf, suffix)
    #calculate_confusion('svm_test', 'annotation.txt')
    #list_wrong_case('svm_test', 'predict_'+suffix+'.txt')
    #calculate_confusion('svm_test', 'predict_'+suffix+'.txt')




