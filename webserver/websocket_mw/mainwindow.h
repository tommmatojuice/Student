/**
 * @file mainwindow.h
 * @author Levashova Anastasia <nastya.levashova.12@mail.ru>
 * @brief Содержит объявление класса MainWindow проекта Server
 */
#ifndef MAINWINDOW_H
#define MAINWINDOW_H
#include <QMainWindow>
#include <QObject>
#include <QtNetwork>
#include <QVector>
#include <QFile>
#include <QMap>
#include <QDebug>
#include <vector>
#include <QSettings>
#include <QMessageBox>
#include <QCloseEvent>
#include <QSystemTrayIcon>
#include <QAction>
#include <QPushButton>
#include <QApplication>
#include <QSqlDatabase>
#include <QSqlQuery>

QT_FORWARD_DECLARE_CLASS(QWebSocketServer)
QT_FORWARD_DECLARE_CLASS(QWebSocket)

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

private:
    QSqlDatabase db;
    Ui::MainWindow *ui;
    QWebSocketServer *server;
    QSystemTrayIcon  * trayIcon;
    QVector <QString> last_messages;
    QMap<QWebSocket*, QString> clients_map;
    int port;
    int flag=0;

protected:
    /**
     * @brief closeEvent Переопределенная функция closeEvent для изменения поведения приложения,
     * чтобы оно сворачивалось в трей, когда хочет пользователь
     * @param event Событие закрытия приложения
     */
    void closeEvent(QCloseEvent * event);

private slots:
    /**
     * @brief connectUser Слот для подключения клиента
     */
    void connectUser();

    /**
     * @brief runClientAction Слот для обработки сообщений пользователей
     */
    void runClientAction(QString message);

    /**
     * @brief save_last_messages Слот для записи последний 10 сообщений в файл
     * @param message Текст сообщения
     */
    void save_last_messages(QString message);

    /**
     * @brief save_settings Слот для сохранения настроек порта в .ini файл
     */
    void save_settings();

    /**
     * @brief message Слот для вывода сообщения пользователю
     * @param message Текст сообщения
     */
    void message(QString message);

    /**
     * @brief new_connection Слот для переподключения сервера при изменении номера порта
     */
    void new_connection();

    /**
     * @brief iconActivated Слот, который будет принимать сигнал от события
     * нажатия на иконку приложения в трее
     * @param reason Событие нажатия на иконку приложения в трее
     */
    void iconActivated(QSystemTrayIcon::ActivationReason reason);
};
#endif // MAINWINDOW_H
