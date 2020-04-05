/**
 * @file paint.h
 * @author Levashova Anastasia <nastya.levashova.12@mail.ru>
 * @brief Содержит объявление класса Paint
 */
#ifndef PAINT_H
#define PAINT_H
#include <QWidget>
#include <QTimer>
#include <QResizeEvent>
#include <QMainWindow>
#include <QGraphicsSceneMouseEvent>
#include <QFileDialog>
#include <paintscene.h>

namespace Ui {
class Paint;
}

class Paint : public QWidget
{
    Q_OBJECT

public:
    /**
     * @brief Paint Конструктор класса Paint
     */
    explicit Paint(QWidget *parent = 0);

    /**
     * @brief Paint Деструктор класса Paint
     */
    ~Paint();

private:
    Ui::Paint *ui;
    QTimer *timer;
    Paintscene *scene;
    QString str_open = "";

private:
    /**
     * @brief resizeEvent Переопределение события изменения размера окна
     * @param event Событие размера изменения окна
     */
    void resizeEvent(QResizeEvent * event);

    /**
     * @brief save Метод для сохранения изображения
     */
    void save();

    /**
     * @brief open Метод для открытия изображения
     */
    void open();

    /**
     * @brief open Метод для очисти экрана
     */
    void back_to_bord();

private slots:
    /**
     * @brief slotTimer Таймер
     */
    void slotTimer();

    /**
     * @brief green Слот для выставления зеленого цвета
     */
    void green();

    /**
     * @brief red Слот для выставления красного цвета
     */
    void red();

    /**
     * @brief blue Слот для выставления синего цвета
     */
    void blue();

    /**
     * @brief blue Слот для выставления желтого цвета
     */
    void yellow();

    /**
     * @brief black Слот для выставления черного цвета
     */
    void black();
};
#endif // PAINT_H
