package be.pxl.jav;

import be.pxl.jav.model.Account;
import be.pxl.jav.model.MonthlyReportInformation;
import be.pxl.jav.model.MontlyReportPdfWriter;
import be.pxl.jav.model.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class App {
    private static final Logger LOGGER = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        if (args.length != 3) {
            LOGGER.error("Expected 3 arguments: File URL, Month, Year.");
            return;
        }
        Path input = Path.of(args[0]);
        Month month = Month.of(Integer.parseInt(args[1]));
        int year = Integer.parseInt(args[2]);
        Map<Account, List<Transaction>> transactionsByAccount = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(input)) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] inputSplit = line.split(";");
                String iban = inputSplit[2];
                String firstname = inputSplit[0];
                String name = inputSplit[1];
                Account account = new Account(iban, firstname, name);
                String category = inputSplit[3];
                double amount = MontlyReportPdfWriter.getDecimalFormat().parse(inputSplit[4]).doubleValue();
                String transactionName = inputSplit[5];
                String detail = inputSplit[6];
                LocalDate date = LocalDate.parse(inputSplit[7], DateTimeFormatter.ofPattern("d/MM/yyyy"));
                Transaction transaction = new Transaction(date, amount, category, transactionName, detail);
                List<Transaction> transactions = new ArrayList<>();
                transactions.add(transaction);
                if (!transactionsByAccount.containsKey(account)) {
                    transactionsByAccount.put(account, transactions);
                }
                transactionsByAccount.get(account).addAll(transactions);
            }
        } catch (IOException | ParseException e) {
            LOGGER.error(e);
        }
        transactionsByAccount.forEach((key, value) -> value.forEach(key::addTransaction));
        List<Account> accounts = new ArrayList<>(transactionsByAccount.keySet());
        List<MonthlyReportInformation> reportInformations = new ArrayList<>();
        accounts.forEach(
                account -> {
                    List<Transaction> transactionsForDate = account.getTransactions().stream()
                            .filter(transaction -> transaction.isMonthAndYear(month, year))
                            .collect(toList());
                    List<Transaction> incoming = transactionsForDate.stream()
                            .filter(Transaction::isIncome)
                            .collect(toList());
                    List<Transaction> outgoing = transactionsForDate.stream()
                            .filter(Transaction::isExpense)
                            .collect(toList());
                    MonthlyReportInformation reportInformation = new MonthlyReportInformation();
                    reportInformation.setYear(year);
                    reportInformation.setMonth(month);
                    reportInformation.setIncoming(incoming);
                    reportInformation.setOutgoing(outgoing);
                    reportInformation.setFullName(account.getFullName());
                    reportInformations.add(reportInformation);
                }
        );
        reportInformations.forEach(reportInformation -> {
            Path basePath = Path.of(System.getProperty("user.home"), "school", "JavaAdv2", "week2_bestanden", "output", "pdfreaderout");
            Path finalPath = Path.of(basePath + "_" + reportInformation.getFullName());
            if (Files.notExists(finalPath)) {
                try {
                    Files.createFile(finalPath);
                } catch (IOException e) {
                    LOGGER.error(e);
                }
            }
            MontlyReportPdfWriter.createAndSaveDocument(reportInformation, finalPath);
        });
    }
}
