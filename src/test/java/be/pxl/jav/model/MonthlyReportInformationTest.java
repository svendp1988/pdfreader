package be.pxl.jav.model;

import junit.framework.TestCase;

import java.time.Month;

public class MonthlyReportInformationTest extends TestCase {
    MonthlyReportInformation reportInformation = new MonthlyReportInformation();

    public void testFormatDate() {
        reportInformation.setMonth(Month.JANUARY);
        reportInformation.setYear(2021);
        String result = reportInformation.getFullDate();
        assertEquals("januari 2021", result);
    }
}