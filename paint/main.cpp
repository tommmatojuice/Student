/**
 * @file main.cpp
 * @author Levashova Anastasia <nastya.levashova.12@mail.ru>
 * @brief Содержит функцию main
 */
#include "paint.h"
#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    Paint w;
    w.show();
    return a.exec();
}
