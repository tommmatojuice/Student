/**
 * @file mainwindow.cpp
 * @author Levashova Anastasia <nastya.levashova.12@mail.ru>
 * @brief Содержит реализацию класса MainWindow проекта Client
 */
#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent),
      ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    ui->lineEdit_3->setValidator(new QIntValidator);
    connect(ui->pushButton_4, &QPushButton::clicked,
            this, &MainWindow::get_port);
    connect(ui->pushButton_2, &QPushButton::clicked,
            this, &MainWindow::get_nick);
    connect(ui->pushButton, &QPushButton::clicked,
            this, &MainWindow::execButtonAction);
    connect(ui->pushButton_3, &QPushButton::clicked,[&](){ socketClient.close();
                                                           dis_flag = 1;});
}

void MainWindow::get_nick(){
    spare_nick = ui->lineEdit_2->text();
    colour = getRandomColor();
    socketClient.sendTextMessage(spare_nick.toUtf8());
}

void MainWindow::get_port(){
    socketClient.close();
    ui->lineEdit_2->setReadOnly(1);
    ui->pushButton_2->setEnabled(0);
    ui->lineEdit->setReadOnly(1);
    ui->pushButton->setEnabled(0);
    ui->lineEdit->clear();
    ui->lineEdit_2->clear();
    if(dis_flag==0){
        con_flag=0;
        ui->textEdit->clear();
        socketClient.close();
        disconnect(&socketClient, &QWebSocket::textMessageReceived,
                this, &MainWindow::readSocket);
        port = ui->lineEdit_3->text();
        QUrl url("ws://localhost:"+port);
        socketClient.open(QUrl(url));;
        connect(&socketClient, &QWebSocket::textMessageReceived,
                this, &MainWindow::readSocket);
    } else {
        error("Вы были отключены от сервера!");
    }
}

MainWindow::~MainWindow(){
    delete ui;
}

void MainWindow::execButtonAction(){
    if(dis_flag==0){
        socketClient.sendTextMessage((colour+"<"+nick+"> "+ui->lineEdit->text()).toUtf8());
        qDebug()<<ui->lineEdit->text();
    } else {
        error("Вы были отключены от сервера!");
    }
}

void MainWindow::readSocket(QString message){
    if(message=="Connected"){
        con_flag=1;
        error("Вы подключились к порту: "+port);
        ui->lineEdit_2->setReadOnly(0);
        ui->pushButton_2->setEnabled(1);
    }
    if(con_flag==1){
        if(message=="1"){
            error("Такой ник уже существует!");
            spare_nick = nick;
        } else {
            nick = spare_nick;
            if(message=="0"){
                ui->lineEdit->setReadOnly(0);
                ui->pushButton->setEnabled(1);
            } else {
                if(message[0]=="#"){
                   QString user_colour = message.mid(0, message.indexOf("<"));
                   QString data_nick = message.mid(message.indexOf("<")+1, message.indexOf(">")-message.indexOf("<")-1);
                   message.remove(0, message.indexOf(">")+1);
                   ui->textEdit->append("");
                   ui->textEdit->insertPlainText("<");
                   ui->textEdit->insertHtml("<span style=\"color:"+user_colour+";\">"+data_nick+"</span>");
                   ui->textEdit->insertHtml(">"+message);
                } else {
                    if((!message.isEmpty()) && (message!="Connected")){
                        message.remove(message.indexOf("\n"), message.indexOf("\n"));
                        ui->textEdit->append(message);
                    }
                }
            }
        }
    }
}

void MainWindow::error(QString message){
    QMessageBox msgBox;
    msgBox.setText(message);
    msgBox.exec();
    msgBox.setWindowFlags(Qt::CustomizeWindowHint | Qt::WindowTitleHint | Qt::WindowStaysOnTopHint);
    return;
}

QString MainWindow::getRandomColor(){
    QTime midnight(0,0,0);
    qsrand(midnight.secsTo(QTime::currentTime()));
    QString letters = "0123456789ABCDEF";
    QString color = "#";
    for (int i = 0; i < 6; i++) {
       color += letters[qrand() % 16];
    }
    return color;
}
