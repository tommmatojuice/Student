/**
 * @file paintscene.cpp
 * @author Levashova Anastasia <nastya.levashova.12@mail.ru>
 * @brief Содержит реализацию класса Paintscene
 */
#include "paintscene.h"

Paintscene::Paintscene(QObject *parent) : QGraphicsScene(parent){}

Paintscene::~Paintscene(){}

void Paintscene::mousePressEvent(QGraphicsSceneMouseEvent *event)
{
    if(colour == "red"){
        addEllipse(event->scenePos().x() - 5, event->scenePos().y() - 5,
                   10, 10,
                   QPen(Qt::NoPen), QBrush(Qt::red));
    } else if(colour == "green"){
        addEllipse(event->scenePos().x() - 5, event->scenePos().y() - 5,
                   10, 10,
                   QPen(Qt::NoPen), QBrush(Qt::green));
    } else if(colour == "blue"){
        addEllipse(event->scenePos().x() - 5, event->scenePos().y() - 5,
                   10, 10,
                   QPen(Qt::NoPen), QBrush(Qt::blue));
    } else if(colour == "yellow"){
        addEllipse(event->scenePos().x() - 5, event->scenePos().y() - 5,
                   10, 10,
                   QPen(Qt::NoPen), QBrush(Qt::yellow));
    } else {
            addEllipse(event->scenePos().x() - 5, event->scenePos().y() - 5,
                       10, 10,
                       QPen(Qt::NoPen), QBrush(Qt::black));
    }
    previousPoint = event->scenePos();
}

void Paintscene::mouseMoveEvent(QGraphicsSceneMouseEvent *event)
{
    if(colour=="red"){
        addLine(previousPoint.x(), previousPoint.y(),
                event->scenePos().x(), event->scenePos().y(),
                QPen(Qt::red,10,Qt::SolidLine,Qt::RoundCap));
    } else if(colour=="green"){
        addLine(previousPoint.x(), previousPoint.y(),
                event->scenePos().x(), event->scenePos().y(),
                QPen(Qt::green,10,Qt::SolidLine,Qt::RoundCap));
    } else if(colour=="blue"){
        addLine(previousPoint.x(), previousPoint.y(),
                event->scenePos().x(),event->scenePos().y(),
                QPen(Qt::blue,10,Qt::SolidLine,Qt::RoundCap));
    } else if(colour=="yellow"){
        addLine(previousPoint.x(), previousPoint.y(),
                event->scenePos().x(), event->scenePos().y(),
                QPen(Qt::yellow,10,Qt::SolidLine,Qt::RoundCap));
    } else {
        addLine(previousPoint.x(), previousPoint.y(),
                event->scenePos().x(), event->scenePos().y(),
                QPen(Qt::black,10,Qt::SolidLine,Qt::RoundCap));
    }
    previousPoint = event->scenePos();
}

void Paintscene::changeColour(QString colour){
    this->colour = colour;
}


