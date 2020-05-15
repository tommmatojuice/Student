/**
 * @file mainwindow.h
 * @author Levashova Anastasia <nastya.levashova.12@mail.ru>
 * @brief Содержит объявление класса MainWindow
 */
#ifndef MAINWINDOW_H
#define MAINWINDOW_H
#include <QMainWindow>
#include "qcustomplot.h"
#include "stream.h"
#include <QCoreApplication>
#include <QDateTime>
#include <QTime>
#include <QVector>

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    /**
     * @brief MainWindow Конструктор класса MainWindow
     */
    explicit MainWindow(QWidget *parent = 0);

    /**
     * @brief MainWindow Деструктор класса MainWindow
     */
    ~MainWindow();

private:
    Ui::MainWindow *ui;
    QCustomPlot *customPlot;
    QCPGraph *graphic;
    QTimer *timer;
    QVector<double> array1, array2, array3;
    int flag=0, flag2=0;
    QThread *thread;
    Stream *stream;

signals:
    /**
     * @brief finished Сигнал о завершении работы класса
     */
    void finished();

private slots:
    /**
     * @brief updateArray Слот для обновления массива
     * @param array Массив чисел
     */
    void updateArray(QVector<double>* array);

    /**
     * @brief updateDiagramm Слот для обновления диаграммы
     */
    void updateDiagramm();

    /**
     * @brief on_pushButton_clicked Слот для запуска потока
     */
    void on_pushButton_clicked();

    /**
     * @brief randomBetween Слот для получения рандомного числа
     * @param low Левая граница рандома
     * @param high Правая граница рандома
     * @return Рандомное число
     */
    int randomBetween(int low, int high);
};

#endif // MAINWINDOW_H
