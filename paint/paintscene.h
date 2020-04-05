/**
 * @file paintscene.h
 * @author Levashova Anastasia <nastya.levashova.12@mail.ru>
 * @brief Содержит объявление класса Paintscene
 */
#ifndef PAINTSCENE_H
#define PAINTSCENE_H
#include <QGraphicsScene>
#include <QGraphicsSceneMouseEvent>
#include <QTimer>
#include <QDebug>

class Paintscene : public QGraphicsScene
{
    Q_OBJECT

public:
    /**
     * @brief Paint Конструктор класса Paintscene
     */
    explicit Paintscene(QObject *parent = 0);

    /**
     * @brief Paint Деструктор класса Paintscene
     */
    ~Paintscene();

    /**
     * @brief Paint Метод для изменения цвета кисти
     */
    void changeColour(QString colour);

private:
    QPointF previousPoint;
    QString colour;

    /**
     * @brief mousePressEvent Метод для рисования
     * @param event Событие мыши
     */
    void mousePressEvent(QGraphicsSceneMouseEvent *event);

    /**
     * @brief mouseMoveEvent Метод для рисования
     * @param event Событие мыши
     */
    void mouseMoveEvent(QGraphicsSceneMouseEvent *event);
};
#endif // PAINTSCENE_H
