package lu.btsi.bragi.ros.server.controller;

import com.google.inject.Inject;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;

import java.util.Optional;

/**
 * Created by gillesbraun on 15/02/2017.
 */
public class MainController {

    @Inject TableController tableController;
    @Inject WaiterController waiterController;
    @Inject LanguageController languageController;
    @Inject AllergenController allergenController;
    @Inject AllergenLocalizedController allergenLocalizedController;
    @Inject ProductController productController;
    @Inject ProductLocalizedController productLocalizedController;
    @Inject ProductCategoryController productCategoryController;
    @Inject ProductCategoryLocalizedController productCategoryLocalizedController;
    @Inject ProductAllergenController productAllergenController;
    @Inject LocationController locationController;
    @Inject OrderController orderController;
    @Inject ProductPriceForOrderController productPriceForOrderController;
    @Inject InvoiceController invoiceController;

    public Optional<Message> sendToRightController(String message) throws ClassNotFoundException, MessageException {
        return Controller.sendToController(message);
    }
}
