/**
 * @file mainwindow.h
 * @author Levashova Anastasia <nastya.levashova.12@mail.ru>
 * @brief Содержит объявление класса MainWindow проекта Client
 */
#ifndef MAINWINDOW_H
#define MAINWINDOW_H
#include <QMainWindow>
#include <QtNetwork>
#include <QColor>
#include <QAbstractSocket>
#include <QMessageBox>
#include <QTime>
#include <QDebug>
#include <QtWebSockets/QWebSocket>
#include <QIntValidator>


QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    /**
     * @brief Конструктор класса MainWindow
     */
    MainWindow(QWidget *parent = nullptr);

    /**
     * @brief Деструктор класса MainWindow
     */
    ~MainWindow();

private slots:
    /**
     * @brief execButtonAction Слот для отправки сообщения на сервер
     */
    void execButtonAction();

    /**
     * @brief readSocket Слот для принятия сообщения от сервера
     */
    void readSocket(QString message);

    /**
     * @brief get_nick Слот для получения ника из lineEdit
     */
    void get_nick();

    /**
     * @brief get_nick Слот для получения номера порта из lineEdit
     */
    void get_port();

    /**
     * @brief error Слот для вывода ошибки
     * @param message Текст ошибки
     */
    void error(QString message);

    /**
     * @brief getRandomColor Слот для получени рандомного цвета
     * @return Цвет в HTML
     */
    QString getRandomColor();

private:
    QString nick;
    QString port;
    QString spare_nick;
    int dis_flag=0;
    int con_flag=0;
    QString colour;
    QWebSocket socketClient;
    QUrl url;
    Ui::MainWindow *ui;
};
#endif // MAINWINDOW_H
