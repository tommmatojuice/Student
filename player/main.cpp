/**
 * @file main.cpp
 * @author Levashova Anastasia <nastya.levashova.12@mail.ru>
 * @brief Содержит функцию main
 */
#include "mainwindow.h"
#include <QApplication>
#include <QtMultimedia/QMediaPlayer>
#include <QtMultimedia/QMediaPlaylist>
#include <QtMultimediaWidgets/QVideoWidget>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainWindow w;
    w.show();
    return a.exec();
}
