/**
 * @file mainwindow.cpp
 * @author Levashova Anastasia <nastya.levashova.12@mail.ru>
 * @brief Содержит реализацию класса MainWindow проекта Server
 */
#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "QtWebSockets/qwebsocketserver.h"
#include "QtWebSockets/qwebsocket.h"
#include <QtCore/QDebug>
#include <QIntValidator>
#include <QIcon>

QT_USE_NAMESPACE

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    ui->lineEdit->setValidator(new QIntValidator);
    this->setWindowTitle("Server");
    db = QSqlDatabase::addDatabase("QSQLITE");
    QFile file("last_messages.sqlite");
    if (!file.exists()){
        qDebug() << "БД не существует";
        db.setDatabaseName("last_messages.sqlite");
        db.open();
        QSqlQuery query;
        QFile db_file(":/db/db_messages.txt");
        if (db_file.open(QIODevice::ReadOnly)){
            qDebug()<<"File is open!";
            qDebug()<<query.exec(db_file.readLine());
            db_file.close();
        } else {
            qDebug()<< "File is not open!";
        }
    } else {
        db.setDatabaseName("last_messages.sqlite");
        if (!db.open()) {
            qDebug() << "Не можем открыть БД";
            return;
        }
        qDebug()<<"БД открылась";
    }
    trayIcon = new QSystemTrayIcon(this);
    setWindowIcon(QIcon(":/images/pic_2.png"));
    trayIcon->setIcon(QIcon(":/images/pic_1.png"));

    QMenu * menu = new QMenu(this);
    QAction * viewWindow = new QAction("Развернуть окно", this);
    QAction * quitAction = new QAction("Выход", this);

    connect(viewWindow, SIGNAL(triggered()), this, SLOT(show()));
    connect(quitAction, SIGNAL(triggered()), this, SLOT(close()));

    menu->addAction(viewWindow);
    menu->addAction(quitAction);

    trayIcon->setContextMenu(menu);
    trayIcon->show();

    connect(trayIcon, SIGNAL(activated(QSystemTrayIcon::ActivationReason)),
                this, SLOT(iconActivated(QSystemTrayIcon::ActivationReason)));

    new_connection();

    connect(ui->pushButton, &QPushButton::clicked,
            this, &MainWindow::save_settings);
    connect(ui->pushButton_2, SIGNAL(clicked()), qApp, SLOT(quit()));
}

void MainWindow::closeEvent(QCloseEvent * event){
    if(this->isVisible() && flag==0){
        event->ignore();
        this->hide();
        QSystemTrayIcon::MessageIcon icon = QSystemTrayIcon::MessageIcon(QSystemTrayIcon::Information);
        trayIcon->showMessage("Server",
                              "Приложение свернуто в трей. Для того чтобы, "
                                     "развернуть окно приложения, щелкните по иконке приложения в трее",
                              icon,
                              2000);
    }
}

void MainWindow::iconActivated(QSystemTrayIcon::ActivationReason reason){
    switch (reason){
        case QSystemTrayIcon::Trigger:
                if(!this->isVisible()){
                    this->show();
                } else {
                    this->hide();
                }
            break;
        default:
            break;
    }
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::connectUser()
{
    QWebSocket* socket = server->nextPendingConnection();
    connect(socket, &QWebSocket::textMessageReceived,
            this, &MainWindow::runClientAction);
    connect(socket, &QWebSocket::stateChanged, this, [&](QAbstractSocket::SocketState state){
        if(state==QAbstractSocket::SocketState::UnconnectedState){
            int i=0;
            foreach(QWebSocket* key,clients_map.keys()){
               if(!key->isValid()){
                   clients_map.erase(clients_map.begin() + i);
               }
               i++;
            }
            QWebSocket *pClient;
            foreach(QWebSocket* key,clients_map.keys()){
               pClient = qobject_cast<QWebSocket *>(key);
               pClient->sendTextMessage("Someone left the chat!");
            }
        }
    });
    QWebSocket *Client;
    Client = qobject_cast<QWebSocket *>(socket);
    Client->sendTextMessage("Connected");
    QSqlQuery query;
    QString message = "";
    query.exec("SELECT count(*) FROM messages");
    query.next();
    int n=query.value(0).toInt();
    int last;
    if(n>10){
        last=n-10;
    } else {
        last=0;
    }
    query.exec("SELECT * FROM messages");
    for(int i=0; i<n;i++){
        QWebSocket *pClient = qobject_cast<QWebSocket*>(socket);
        query.next();
        if(i>=last){
            message+=query.value(0).toString();
            message+="<";
            message+=query.value(1).toString();
            message+="> ";
            message+=query.value(2).toString();
            pClient->sendTextMessage(message);
            message.clear();
        }
    }
    clients_map.insert(socket,"");
    QWebSocket *pClient;
    foreach(QWebSocket* key,clients_map.keys()){
       pClient = qobject_cast<QWebSocket*>(key);
       pClient->sendTextMessage("We have a new user!");
    }
    foreach(QWebSocket* key,clients_map.keys()){
       qDebug()<<key;
    }
}

void MainWindow::runClientAction(QString message)
{
    QWebSocket *clientSocket = qobject_cast<QWebSocket*>(sender());
    if(message[0]!='#'){
        int k=0;
        foreach(QWebSocket* key,clients_map.keys()){
            if(clients_map.value(key) == message){
                k++;
            }
        }
        if(k>0){
            clientSocket->sendTextMessage("1");
        } else {
            clientSocket->sendTextMessage("0");
            int i = 0;
            foreach(QWebSocket* key,clients_map.keys()){
               if(key == clientSocket){
                   clients_map.erase(clients_map.begin() + i);
                   clients_map.insert(clientSocket, message);
               }
               i++;
            }
        }
    } else {
        save_last_messages(message);
        QWebSocket *pClient;
        foreach(QWebSocket* key,clients_map.keys()){
           pClient = qobject_cast<QWebSocket *>(key);
           pClient->sendTextMessage(message);
        }
    }
}

void MainWindow::save_last_messages(QString message){
    QString colour = message.mid(0, message.indexOf("<"));
    QString nick = message.mid(message.indexOf("<")+1, message.indexOf(">")-message.indexOf("<")-1);
    message.remove(0, message.indexOf(">")+1);
    QSqlQuery query;
    qDebug()<<query.prepare("INSERT INTO messages (colour, nick, message) VALUES (:colour, :nick, :message)");
    query.bindValue(":colour", colour);
    query.bindValue(":nick", nick);
    query.bindValue(":message", message);
    qDebug()<<query.exec();
}

void MainWindow::save_settings(){
    QWebSocket *pClient;
    foreach(QWebSocket* key,clients_map.keys()){
       pClient = qobject_cast<QWebSocket*>(key);
       pClient->sendTextMessage("Server is not available!");
       key->close();
    }
    server->close();
    clients_map.clear();
    last_messages.clear();
    port = ui->lineEdit->text().toInt();
    QSettings settings ("settings.ini",QSettings::IniFormat);
    settings.beginGroup("Settings");
    settings.setValue("port", port);
    message("Настройки успешно сохранены!");
    new_connection();
}

void MainWindow::new_connection(){
    QSettings settings ("settings.ini",QSettings::IniFormat);
    settings.beginGroup("Settings");
    port = settings.value("port").toInt();
    if(port==0){
        ui->lineEdit->setText("1337");
    } else {
        ui->lineEdit->setText(QString::number(port));
    }
    server = new QWebSocketServer("Server", QWebSocketServer::NonSecureMode, this);
    qDebug()<<(connect(server, &QWebSocketServer::newConnection,
            this, &MainWindow::connectUser));
    if (server->listen(QHostAddress::Any, settings.value("port",int(1337)).toInt())) {
        qDebug() << "Port:" << settings.value("port",int(1337)).toInt();
        ui->lineEdit_2->setText("Server started");
    } else {
        ui->lineEdit_2->setText("Unable to start server");
    }
}

void MainWindow::message(QString message){
    QMessageBox msgBox;
    msgBox.setText(message);
    msgBox.exec();
    msgBox.setWindowFlags(Qt::CustomizeWindowHint | Qt::WindowTitleHint | Qt::WindowStaysOnTopHint);
    return;
}
