/**
 * @file mainwindow.h
 * @author Levashova Anastasia <nastya.levashova.12@mail.ru>
 * @brief Содержит объявление класса MainWindow
 */
#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <ActiveQt\QAxObject>

QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

/**
 * @brief The MainWindow class
 */
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

private:
    Ui::MainWindow *ui;
    QAxObject *mExcel;
    QAxObject *workbook;
    QAxObject *StatSheet;
    QAxObject *cell;

private slots:
    /**
     * @brief cellSelected Слот для записи значения из ячейки QTableWidget в файл xlsx
     * @param nRow Номер строки
     * @param nCol Номер столбца
     */
    void cellSelected(int nRow, int nCol);
};
#endif // MAINWINDOW_H
