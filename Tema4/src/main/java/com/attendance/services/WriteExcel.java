package com.attendance.services;

import com.attendance.data.model.Attendence;
import com.attendance.data.model.Classroom;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WriteExcel {

    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat times;
    private String inputFile;
    private ByteArrayOutputStream stream;

    public void setOutputFile(String inputFile) {
        this.inputFile = inputFile;
        this.stream = new ByteArrayOutputStream();
    }

    public void write(Classroom classroom) throws IOException, WriteException {
//        File file = new File(inputFile);
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(stream, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet excelSheet = workbook.getSheet(0);
        createLabel(excelSheet);
        createContent(excelSheet, classroom);

        workbook.write();
        workbook.close();
//        var tempFile=new FileOutputStream(inputFile);
//        stream.writeTo(tempFile);
        stream.close();
//        tempFile.close();
    }

    public byte[] getStream() {

        return stream.toByteArray();
    }

    private void createLabel(WritableSheet sheet)
            throws WriteException {
        // Lets create a times font
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        // Define the cell format
        times = new WritableCellFormat(times10pt);
        // Lets automatically wrap the cells
        times.setWrap(true);

        // create create a bold font with unterlines
        WritableFont times10ptBoldUnderline = new WritableFont(
                WritableFont.TIMES, 10, WritableFont.BOLD, false,
                UnderlineStyle.SINGLE);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        // Lets automatically wrap the cells
        timesBoldUnderline.setWrap(true);

        CellView cv = new CellView();
        cv.setFormat(times);
        cv.setFormat(timesBoldUnderline);
        cv.setAutosize(true);

        // Write a few headers
        addCaption(sheet, 0, 0, "Person");
        addCaption(sheet, 1, 0, "Total presences");


    }

    private void createContent(WritableSheet sheet, Classroom classroom) throws WriteException,
            RowsExceededException {
        // Write a few number
        var pres = classroom.getAttendencesOnDay();
        var i = 0;
        for (Map.Entry<String, List<Attendence>> pair : pres.entrySet()) {
            addNumber(sheet, 1, i, pair.getValue().size());
            addLabel(sheet, 0, i, pair.getKey());
            i++;
        }

    }

    private void addCaption(WritableSheet sheet, int column, int row, String s)
            throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, timesBoldUnderline);
        sheet.addCell(label);
    }

    private void addNumber(WritableSheet sheet, int column, int row,
                           Integer integer) throws WriteException, RowsExceededException {
        Number number;
        number = new Number(column, row, integer, times);
        sheet.addCell(number);
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s)
            throws WriteException, RowsExceededException {
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }

}