package com.attendance.services;

import com.attendance.data.model.Classroom;
import com.attendance.dto.StudentWithPresenceAndNotes;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Number;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class WriteExcel {

    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat times;
    private String inputFile;
    private ByteArrayOutputStream stream;

    public void setOutputFile(String inputFile) {
        this.inputFile = inputFile;
        this.stream = new ByteArrayOutputStream();
    }

    public void write(List<StudentWithPresenceAndNotes> students) throws IOException, WriteException {
//        File file = new File(inputFile);
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(stream, wbSettings);
        workbook.createSheet("Students", 0);
        workbook.createSheet("Notes", 1);
        workbook.createSheet("Presences", 2);
        WritableSheet excelSheet = workbook.getSheet(0);
        WritableSheet excelSheet2 = workbook.getSheet(1);
        WritableSheet excelSheet3 = workbook.getSheet(2);
        createLabel(excelSheet);
        createLabel(excelSheet2);
        createLabel(excelSheet3);
//        createContent(excelSheet, students);
        writeStudents(excelSheet, students);
        writeNotes(excelSheet2, students);
        writeDates(excelSheet3, students);
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


    }

    private void createContent(WritableSheet sheet, List<StudentWithPresenceAndNotes> classroom) throws WriteException,
            RowsExceededException {
        // Write a few number
//        var pres = classroom.getStudents();
//        var i = 0;
//        for (Map.Entry<String, Set<String>> pair : pres.entrySet()) {
//            addNumber(sheet, 1, i, pair.getValue().size());
//            addLabel(sheet, 0, i, pair.getKey());
//            i++;
//        }
    }

    private void writeStudents(WritableSheet sheet, List<StudentWithPresenceAndNotes> students) throws WriteException {
        addLabel(sheet, 0, 0, "First name");
        addLabel(sheet, 1, 0, "Last name");
        addLabel(sheet, 3, 0, "Email");
        for (int i = 0; i < students.size(); i++) {
            addLabel(sheet, 0, i + 1, students.get(i).getFirstName());
            addLabel(sheet, 1, i + 1, students.get(i).getLastName());
            addLabel(sheet, 3, i + 1, students.get(i).getEmail());
        }
    }

    private void writeNotes(WritableSheet sheet, List<StudentWithPresenceAndNotes> students) throws WriteException {
        for (int i = 0; i < students.size(); i++) {
            addLabel(sheet, 0, i, students.get(i).getFirstName() + " " + students.get(i).getLastName());
            var temp = students.get(i).getNotes();
            for (int j = 0; j < temp.size(); j++) {
                addNumber(sheet, j + 1, i, temp.get(j));
            }
        }
    }

    private void writeDates(WritableSheet sheet, List<StudentWithPresenceAndNotes> students) throws WriteException {
        List<String> dates = students.stream().map(StudentWithPresenceAndNotes::getDates).flatMap(Collection::stream).distinct().sorted().collect(Collectors.toList());
        for (int i = 0; i < dates.size(); i++) {
            addLabel(sheet, i + 1, 0, dates.get(i));
        }
        for (int i = 0; i < students.size(); i++) {
            addLabel(sheet, 0, i, students.get(i).getFirstName() + " " + students.get(i).getLastName());
            var temp = new ArrayList<>(students.get(i).getDates());
            for (String s : temp) {
                addLabel(sheet, dates.indexOf(s) + 1 , i, "p");
            }
        }
    }

    private void addCaption(WritableSheet sheet, int column, int row, String s)
            throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, timesBoldUnderline);
        sheet.addCell(label);
    }

    private void addNumber(WritableSheet sheet, int column, int row,
                           Double integer) throws WriteException, RowsExceededException {
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