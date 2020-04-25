/**
 * @file mainwindow.h
 * @author Levashova Anastasia <nastya.levashova.12@mail.ru>
 * @brief Содержит объявление класса MainWindow
 */
#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QtMultimedia/QMediaPlayer>
#include <QtMultimedia/QMediaPlaylist>
#include <QtMultimediaWidgets/QVideoWidget>
#include <QLabel>
#include <QToolButton>
#include <QStandardItemModel>
#include <QFileDialog>
#include <QTime>
#include <QSlider>
#include <QMap>
#include <QFileDialog>

QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    /**
     * @brief MainWindow Конструктор класса MainWindow
     */
    MainWindow(QWidget *parent = nullptr);

    /**
     * @brief MainWindow Деструктор класса MainWindow
     */
    ~MainWindow();

private slots:
    /**
     * @brief open_file Слот для обработки добавления треков через диалоговое окно
     */
    void open_file();

    /**
     * @brief seconds Слот для перевода миллисекунд в формат "часы, минуты, секунды"
     * @param n Количество миллисекунд
     * @return Объект класса QTime
     */
    QTime seconds(qint64 n);

    /**
     * @brief setSliderPosition Слот для получания текущего положения видео и продолжительности видео
     * @param n Текущего положения видео в миллисекундах
     */
    void getSliderPosition(qint64 n);

    /**
     * @brief addvideo Слот для записи всех проигрываемых видео в файл
     * @param videos Map с парами "путь к файлу - время остановки"
     */
    void addvideo(QMap <QString, QTime> videos);

    /**
     * @brief readvideo Слот для чтения всех предыдущих видео из файла
     */
    void readvideo();

    /**
     * @brief milseconds Слот для перевода времени в миллисекунды
     * @param time Время
     * @return Время в миллисекундах
     */
    int milseconds(QTime time);

private:
    Ui::MainWindow *ui;
    QMediaPlayer *player;
    QVideoWidget *videoWidget;
    QMediaPlaylist *playlist;
    QStandardItemModel *playListModel;
    QSlider* slider;
    QLabel* time_1;
    QLabel* time_2;
    QMap <QString, QTime> videos;
    QString path;
    int value = 0;
    int flag = 0;
};
#endif // MAINWINDOW_H
