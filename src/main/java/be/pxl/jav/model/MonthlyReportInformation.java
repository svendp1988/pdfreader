package be.pxl.jav.model;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MonthlyReportInformation {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM yyyy");

    private String fullName;
    private Month month;
    private int year;
    private List<Transaction> incoming;
    private List<Transaction> outgoing;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Transaction> getIncoming() {
        return incoming;
    }

    public void setIncoming(List<Transaction> incoming) {
        this.incoming = incoming;
    }

    public List<Transaction> getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(List<Transaction> outgoing) {
        this.outgoing = outgoing;
    }

    public double getTotalIncoming() {
        return incoming.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalOutgoing() {
        return outgoing.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getDifference() {
        return getTotalIncoming() - getTotalOutgoing();
    }

    public String getFullDate() {
        LocalDate date = LocalDate.of(year, month, 1);
        return date.format(DATE_FORMATTER);
    }
}
