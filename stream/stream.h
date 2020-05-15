/**
 * @file stream.h
 * @author Levashova Anastasia <nastya.levashova.12@mail.ru>
 * @brief Содержит объявление класса Stream
 */
#ifndef STREAM_H
#define STREAM_H
#include <QObject>

class Stream : public QObject
{
    Q_OBJECT
public:
    /**
     * @brief Stream Конструктор класса Stream
     * @param array Массив чисел
     */
    explicit Stream(QVector<double> *array, QObject *parent = nullptr);

private:
    QVector<double> array1;

signals:
    /**
     * @brief finished Сигнал завершения работы
     */
    void finished();

    /**
     * @brief sendArray Метод для передачи массива в другой поток
     * @param array Миссив чисел
     */
    void sendArray(QVector<double> *array);

public slots:
    /**
     * @brief start Слот выполняющий сортировку массива
     */
    void start();
};

#endif // STREAM_H
