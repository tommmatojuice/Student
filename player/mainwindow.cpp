/**
 * @file mainwindow.cpp
 * @author Levashova Anastasia <nastya.levashova.12@mail.ru>
 * @brief Содержит реализацию класса MainWindow
 */
#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    playListModel = new QStandardItemModel(this);
    ui->tableView->setModel(playListModel);
    playListModel->setHorizontalHeaderLabels(QStringList()<< tr("Video Track")<< tr("File Path"));
    ui->tableView->hideColumn(1);
    ui->tableView->verticalHeader()->setVisible(false);
    ui->tableView->setSelectionBehavior(QAbstractItemView::SelectRows);
    ui->tableView->setSelectionMode(QAbstractItemView::SingleSelection);
    ui->tableView->setEditTriggers(QAbstractItemView::NoEditTriggers);
    ui->tableView->horizontalHeader()->setStretchLastSection(true);

    videoWidget = new QVideoWidget(this);
    player = new QMediaPlayer(videoWidget);
    playlist = new QMediaPlaylist(player);
    player->setPlaylist(playlist);
    playlist->setPlaybackMode(QMediaPlaylist::Loop);

    ui->gridLayout->addWidget(videoWidget);

    player->setVideoOutput(videoWidget);
    player->setVolume(50);

    slider = new QSlider(Qt::Horizontal);
    slider->setFixedSize(546, 22);
    ui->statusbar->addPermanentWidget(slider);

    time_1 = new QLabel();
    time_1->setText("00:00:00");
    ui->statusbar->addPermanentWidget(time_1);

    QLabel* r = new QLabel();
    r->setText("/");
    ui->statusbar->addPermanentWidget(r);

    time_2 = new QLabel();
    time_2->setText("00:00:00");
    ui->statusbar->addPermanentWidget(time_2);

    QLabel* volume = new QLabel();
    volume->setText("Volume");
    ui->statusbar->addPermanentWidget(volume);

    QSlider* vol_slider = new QSlider(Qt::Horizontal);
    vol_slider->setFixedSize(189, 22);
    vol_slider->setValue(50);
    ui->statusbar->addPermanentWidget(vol_slider);

    readvideo();

    connect(ui->back, &QPushButton::clicked, playlist, &QMediaPlaylist::previous);
    connect(ui->next, &QPushButton::clicked, playlist, &QMediaPlaylist::next);
    connect(ui->play, &QPushButton::clicked, player, &QMediaPlayer::play);
    connect(ui->pause, &QPushButton::clicked, player, &QMediaPlayer::pause);
    connect(ui->stop, &QPushButton::clicked, player, &QMediaPlayer::stop);
    connect(ui->files, &QPushButton::clicked, this, &MainWindow::open_file);

    connect(ui->tableView, &QTableView::doubleClicked, [this](const QModelIndex &index){
        playlist->setCurrentIndex(index.row());
        player->play();
        slider->setValue(value);
        player->setPosition(value);
        flag=1;
    });

    connect(playlist, &QMediaPlaylist::currentIndexChanged, [this](int index){
        ui->label->setText(playListModel->data(playListModel->index(index, 0)).toString());
        path = playListModel->data(playListModel->index(index, 1)).toString();
    });

    connect(player, &QMediaPlayer::durationChanged, slider, &QSlider::setMaximum);
    connect(player, &QMediaPlayer::positionChanged, slider, &QSlider::setValue);
    connect(slider, &QSlider::sliderMoved, player, &QMediaPlayer::setPosition);
    connect(player, SIGNAL(positionChanged(qint64)), SLOT(getSliderPosition(qint64)));
    connect(vol_slider, &QSlider::sliderMoved, player, &QMediaPlayer::setVolume);
}

MainWindow::~MainWindow()
{
    addvideo(videos);
    delete ui;
    delete playListModel;
    delete playlist;
    delete player;
}

void MainWindow::open_file(){
    QStringList file = QFileDialog::getOpenFileNames(this, tr("Open files"), QString(), tr("Video Files (*.mp4 *.avi *.mkv)"));
    foreach (QString filePath, file) {
        if(!videos.count(filePath)){
            QList<QStandardItem *> items;
            items.append(new QStandardItem(QDir(filePath).dirName()));
            items.append(new QStandardItem(filePath));
            videos[filePath] = QTime(0,0,0);
            playListModel->appendRow(items);
            playlist->addMedia(QUrl(filePath));
        }
    }
    player->play();
}

QTime MainWindow::seconds(qint64 n){
    int nHours = (n/(60*60*1000));
    int nMinutes = ((n%(60*60*1000))/(60*1000));
    int nSeconds = ((n%(60*1000))/1000);
    return QTime(nHours, nMinutes, nSeconds);
}

void MainWindow::getSliderPosition(qint64 n){
    if(flag==0){
        value = milseconds(videos.value(path));
        qDebug()<<1;
    }
    time_1->setText(seconds(n).toString("hh:mm:ss"));
    videos[path] = seconds(n);
    int nDuration = slider->maximum();
    time_2->setText(seconds(nDuration).toString("hh:mm:ss"));
    flag=0;
}

void MainWindow::addvideo(QMap <QString, QTime> videos){
    QFile file("video_list.txt");
    if (file.open(QIODevice::WriteOnly)){
        qDebug()<<"File is open!";
        foreach(QString key,videos.keys()){
            if(key!=""){
                file.write(key.toUtf8());
                file.write("\n");
                file.write(videos.value(key).toString().toUtf8());
                file.write("\n");
            }
        }
        file.close();
    } else {
        qDebug()<< "File is not open!";
    }
}

void MainWindow::readvideo(){
    QFile file("video_list.txt");
    if (file.open(QIODevice::ReadOnly)){
        qDebug()<<"File is open!";
        int i=0;
        QString name;
        QString time;
            while(!file.atEnd())
            {
                if(i%2==0){
                    name = file.readLine();
                    name.remove(name.size()-1, 2);
                    QList<QStandardItem *> items;
                    items.append(new QStandardItem(QDir(name).dirName()));
                    items.append(new QStandardItem(name));
                    playListModel->appendRow(items);
                    playlist->addMedia(QUrl(name));
                    i++;
                } else {
                    time = file.readLine();
                    videos[name] = QTime(QStringRef(&time, 0, 2).toInt(),QStringRef(&time, 3, 2).toInt(),QStringRef(&time, 6, 2).toInt());
                    i++;
                }
            }
        file.close();
    } else {
        qDebug()<< "File is not open!";
    }
}

int MainWindow::milseconds(QTime time){
    int n = time.hour()*(60*60*1000) + time.minute()*60000 + time.second()*1000;
    return n;
}
