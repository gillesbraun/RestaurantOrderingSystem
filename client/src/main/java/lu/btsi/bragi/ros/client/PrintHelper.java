package lu.btsi.bragi.ros.client;

import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import lu.btsi.bragi.ros.models.pojos.Invoice;

import java.io.IOException;

/**
 * Created by gillesbraun on 29/03/2017.
 */
public class PrintHelper {

    public static void printInvoice(Invoice invoice, Window owner) throws IOException {
        try {
            Printer printer = Printer.getDefaultPrinter();
            PrinterJob printerJob = PrinterJob.createPrinterJob(printer);
            printerJob.showPrintDialog(owner);
            VBox node = new PrintableInvoice(invoice).getNode();
            node.setStyle("-fx-font-family: \"Arial\";");
            printerJob.printPage(node);
            printerJob.endJob();
        } catch (NullPointerException npe) {
            Dialog d = new Alert(
                    Alert.AlertType.WARNING,
                    "No Printer could be found. The order is marked as paid, but you can printInvoice it again" +
                            "by going to View > Invoices Arvchive.");
            d.initOwner(owner);
            d.show();
        }
    }
}
