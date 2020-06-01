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
    ui->tableWidget->setRowCount(30);
    ui->tableWidget->setColumnCount(15);
    ui->tableWidget->setHorizontalHeaderLabels(QStringList()<<"A"<<"B"<<"C"<<"D"<<"E"<<"F"<<"G"<<"H"<<"I"<<"J"<<"K"<<"L"<<"M"<<"N"<<"O");

    mExcel = new QAxObject("Excel.Application", this);
    QAxObject *workbooks = mExcel->querySubObject("Workbooks");
    workbook = workbooks->querySubObject("Open(const QString&)", "C:\\excel\\test.xlsx");
    QAxObject *mSheets = workbook->querySubObject("Sheets");
    QAxObject* sheet = mSheets->querySubObject("Item(int)", 1);
    QString name = sheet->dynamicCall("Name()").toString();
    StatSheet = mSheets->querySubObject("Item(const QVariant&)", QVariant(name));

    connect(ui->tableWidget, SIGNAL(cellChanged(int, int)), this, SLOT(cellSelected(int, int)));
}

MainWindow::~MainWindow() {
    delete ui;
    workbook->dynamicCall("Save()");
    workbook->dynamicCall("Close()");
    mExcel->dynamicCall("Quit()");
}

void MainWindow::cellSelected(int nRow, int nCol) {
    cell = StatSheet->querySubObject("Cells(QVariant,QVariant)", nRow+1, nCol+1);
    cell->setProperty("Value", ui->tableWidget->model()->data(ui->tableWidget->currentIndex()).toString());
}
