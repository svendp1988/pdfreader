package be.pxl.jav.model;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class MontlyReportPdfWriter {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DecimalFormat DECIMAL_FORMAT = (DecimalFormat) DecimalFormat.getInstance(Locale.getDefault());
	private static final Font CAT_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 18,
			Font.BOLD);
	private static final Font SUB_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 16,
			Font.BOLD);


	public static void createAndSaveDocument(MonthlyReportInformation monthlyExpensesReportInformation, Path outputFile) {
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(outputFile.toFile()));
			document.open();
			addMetaData(document);
			addContent(document, monthlyExpensesReportInformation);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void addMetaData(Document document) {
		document.addTitle("Inkomsten en uitgaven");
		document.addSubject("Using iText");
		document.addKeywords("Java, PDF, iText");
		document.addAuthor("Your name");
	}

	private static void addContent(Document document, MonthlyReportInformation reportInformation) throws DocumentException {
		Anchor anchor = new Anchor(reportInformation.getFullName() + " " + reportInformation.getFullDate(), CAT_FONT);
		anchor.setName("Maandoverzicht");
		Chapter catPart = new Chapter(new Paragraph(anchor), 1);

		Paragraph paraIncoming = new Paragraph("Inkomsten", SUB_FONT);
		Section subCatPart = catPart.addSection(paraIncoming);
		subCatPart.add(createTable(reportInformation.getIncoming()));
		addEmptyLine(paraIncoming, 2);

		Paragraph paraOutgoing = new Paragraph("Uitgaven", SUB_FONT);
		Section subCatPart2 = catPart.addSection(paraOutgoing);
		subCatPart2.add(createTable(reportInformation.getOutgoing()));
		addEmptyLine(paraOutgoing, 2);

		Paragraph paraResults = new Paragraph("Resultaat", SUB_FONT);
		Section result = catPart.addSection(paraResults);
		result.add(new Paragraph("Totale inkomen: " + reportInformation.getTotalIncoming()));
		result.add(new Paragraph("Totale uitgaven: " + reportInformation.getTotalOutgoing()));
		result.add(new Paragraph("Verschil: " + reportInformation.getDifference()));

		document.add(catPart);
	}

	private static PdfPTable createTable(List<Transaction> transactions) {
		PdfPTable table = new PdfPTable(4);

		PdfPCell c1 = new PdfPCell(new Phrase("Datum"));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(c1);

		PdfPCell c2 = new PdfPCell(new Phrase("Category"));
		c2.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(c2);

		PdfPCell c3 = new PdfPCell(new Phrase("Bedrag"));
		c3.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(c3);

		PdfPCell c4 = new PdfPCell(new Phrase("Naam"));
		c4.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(c4);

		table.setHeaderRows(1);

		for (Transaction transaction : transactions) {
			table.addCell(DATE_FORMAT.format(transaction.getDate()));
			table.addCell(transaction.getCategory());
			table.addCell(DECIMAL_FORMAT.format(transaction.getAmount()));
			table.addCell(transaction.getName());
		}

		return table;
	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	public static DecimalFormat getDecimalFormat() {
		return DECIMAL_FORMAT;
	}

}
