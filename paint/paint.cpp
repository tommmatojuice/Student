/**
 * @file paint.cpp
 * @author Levashova Anastasia <nastya.levashova.12@mail.ru>
 * @brief Содержит реализацию класса Paint
 */
#include "paint.h"
#include "ui_paint.h"
#include <QMenu>
#include <QGraphicsPixmapItem>

Paint::Paint(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::Paint)
{
    ui->setupUi(this);

    scene = new Paintscene();
    ui->graphicsView->setScene(scene);

    timer = new QTimer();
    connect(timer, &QTimer::timeout, this, &Paint::slotTimer);
    timer->start(100);

    QMenu * menu = new QMenu(this);
    QAction * red = new QAction("red", this);
    QAction * green = new QAction("green", this);
    QAction * blue = new QAction("blue", this);
    QAction * yellow = new QAction("yellow", this);
    QAction * black = new QAction("black", this);

    connect(green, SIGNAL(triggered()), this, SLOT(green()));
    connect(red, SIGNAL(triggered()), this, SLOT(red()));
    connect(blue, SIGNAL(triggered()), this, SLOT(blue()));
    connect(yellow, SIGNAL(triggered()), this, SLOT(yellow()));
    connect(black, SIGNAL(triggered()), this, SLOT(black()));

    menu->addAction(red);
    menu->addAction(green);
    menu->addAction(blue);
    menu->addAction(yellow);
    menu->addAction(black);

    ui->toolButton->setMenu(menu);

    connect( ui->pushButton, &QPushButton::clicked, this, &Paint::save);
    connect( ui->pushButton_2, &QPushButton::clicked, this, &Paint::open);
    connect( ui->pushButton_3, &QPushButton::clicked, this, &Paint::back_to_bord);
}

Paint::~Paint(){
    delete ui;
}

void Paint::slotTimer(){
    timer->stop();
    scene->setSceneRect(0,0, ui->graphicsView->width() - 20, ui->graphicsView->height() - 20);
    if(str_open!=""){
        scene->clear();
        QPixmap image("image.bmp");
        QSize PicSize(scene->width(), scene->height());
        image = image.scaled(PicSize,Qt::KeepAspectRatio);
        scene->addPixmap(image);
        scene->setSceneRect(0,0, image.width(), image.height());
    }
}

void Paint::resizeEvent(QResizeEvent *event){
    timer->start(100);
    QImage image(scene->width(), scene->height(), QImage::Format_ARGB32_Premultiplied);
    QPainter painter(&image);
    scene->render(&painter);
    image.save("image.bmp");
    QWidget::resizeEvent(event);
    if(str_open!=""){
        scene->clear();
        QPixmap image("image.bmp");
        QSize PicSize(scene->width(), scene->height());
        image = image.scaled(PicSize,Qt::KeepAspectRatio);
        scene->addPixmap(image);
        scene->setSceneRect(0,0, image.width(), image.height());
    }
}

void Paint::green(){
    scene->changeColour("green");
}

void Paint::red(){
    scene->changeColour("red");
}

void Paint::blue(){
    scene->changeColour("blue");
}

void Paint::yellow(){
    scene->changeColour("yellow");
}

void Paint::black(){
    scene->changeColour("black");
}


void Paint::save(){
    QString str = QFileDialog::getSaveFileName(this, tr("save file"),"", tr("Images Files (*.bmp *.png *.jpg *.)"));
    QImage image(scene->width(), scene->height(), QImage::Format_ARGB32_Premultiplied);
    image.fill(QColor(Qt::white).rgb());
    QPainter painter(&image);
    scene->render(&painter);
    qDebug()<<str;
    image.save(str);
}

void Paint::open(){
    QString str = QFileDialog::getOpenFileName(0, "Open Dialog", "", "*.bmp *.png *.jpg");
    if(!str.isEmpty()){
        str_open = str;
        scene->clear();
        QPixmap image(str_open);
        QSize PicSize(scene->width(), scene->height());
        image = image.scaled(PicSize,Qt::KeepAspectRatio);
        scene->addPixmap(image);
        scene->setSceneRect(0,0, image.width(), image.height());
    }
}

void Paint::back_to_bord(){
    scene->clear();
    str_open.clear();
}
