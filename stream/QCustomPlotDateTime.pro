#-------------------------------------------------
#
# Project created by QtCreator 2015-12-27T17:19:10
#
#-------------------------------------------------

QT       += core gui printsupport

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = QCustomPlotDateTime
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp \
    qcustomplot.cpp \
    stream.cpp

HEADERS  += mainwindow.h \
    qcustomplot.h \
    stream.h

FORMS    += mainwindow.ui
