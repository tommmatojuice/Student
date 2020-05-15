/**
 * @file mainwindow.cpp
 * @author Levashova Anastasia <nastya.levashova.12@mail.ru>
 * @brief Содержит реализацию класса MainWindow
 */
#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    QTime midnight(0,0,0);
    qsrand(midnight.secsTo(QTime::currentTime()));

    customPlot = new QCustomPlot();
    ui->gridLayout->addWidget(customPlot,0,0,1,1);

    customPlot->xAxis->setTickLabelType(QCPAxis::ltNumber);
    customPlot->xAxis->setTickLabelFont(QFont(QFont().family(), 8));
    customPlot->yAxis->setTickLabelFont(QFont(QFont().family(), 8));
    customPlot->xAxis->setAutoTickStep(true);
    customPlot->xAxis2->setVisible(true);
    customPlot->yAxis2->setVisible(true);
    customPlot->xAxis2->setTicks(false);
    customPlot->yAxis2->setTicks(false);
    customPlot->xAxis2->setTickLabels(false);
    customPlot->yAxis2->setTickLabels(false);

    graphic = new QCPGraph(customPlot->xAxis, customPlot->yAxis);
    customPlot->addPlottable(graphic);
    QPen pen;
    pen.setWidth(2);
    pen.setColor(Qt::red);
    graphic->setPen(pen);
    graphic->setAntialiased(false);
    graphic->setLineStyle(QCPGraph::lsImpulse);

    connect(ui->pushButton, &QPushButton::clicked, this, &MainWindow::on_pushButton_clicked);

    for(int i=0; i<500000; i++){
        array3.push_back(randomBetween(0, 100000));
    }
    for (int i = 0; i < 501; i++){
        array1.push_back(i);
        array2.push_back(0);
    }
    updateDiagramm();
    thread = new QThread;

    timer = new QTimer();
    connect(timer, SIGNAL(timeout()), this, SLOT(updateDiagramm()));
    timer->start(3000);
}

MainWindow::~MainWindow(){
    emit finished();
    delete ui;
}

void MainWindow::updateArray(QVector<double>* array){
    std::copy(array->begin(), array->end(), array3.begin());
}

void MainWindow::updateDiagramm(){
    array2.clear();
    array2.push_back(0);
    double sum=0;
    int g=0;
    for (int h = 0; h < array3.size(); h++){
        if(g<1000){
            sum+=array3.at(h);
            g++;
        } else {
            array2.push_back(sum/1000);
            sum=0;
            h--;
            g=0;
        }
        if(h==array3.size()-1)
            array2.push_back(sum/1000);
    }
    graphic->setData(array1, array2);
    customPlot->rescaleAxes();
    customPlot->replot();
}

void MainWindow::on_pushButton_clicked()
{
    if(!flag){
        ui->pushButton->setEnabled(0);
        if(flag2){
            delete stream;
            array3.clear();
            for(int i=0; i<500000; i++){
                array3.push_back(randomBetween(0, 100000));
            }
            updateDiagramm();
        }
        stream = new Stream(&array3);
        connect(thread, SIGNAL(started()), stream, SLOT(start()));
        connect(stream, SIGNAL(finished()), thread, SLOT(terminate()));
        connect(this, SIGNAL(finished()), thread, SLOT(deleteLater()));
        connect(stream, &Stream::finished, [&](){ui->pushButton->setEnabled(1);});
        connect(stream, SIGNAL(sendArray(QVector<double>*)), this, SLOT(updateArray(QVector<double>*)), Qt::DirectConnection);
        stream->moveToThread(thread);
        thread->start();
        flag=1;
    } else {
        flag=0;
        flag2=1;
    }
}

int MainWindow::randomBetween(int low, int high){
    return (qrand() % ((high + 1) - low) + low);
}


