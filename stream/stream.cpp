/**
 * @file stream.cpp
 * @author Levashova Anastasia <nastya.levashova.12@mail.ru>
 * @brief Содержит реализацию класса Stream
 */
#include "stream.h"

Stream::Stream(QVector<double> *array, QObject *parent) : QObject(parent){
    for(auto v:*array){
        array1.push_back(v);
    }
}

void Stream::start(){
    double temp;
    for (int i = 0; i < array1.size() - 1; i++) {
        for (int j = 0; j < array1.size() - i - 1; j++) {
            if (array1[j] > array1[j + 1]) {
                temp = array1[j];
                array1[j] = array1[j + 1];
                array1[j + 1] = temp;
                emit sendArray(&array1);
            }
        }
    }
    emit finished();
}
