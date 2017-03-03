package lu.btsi.bragi.ros.client;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;
import lu.btsi.bragi.ros.client.connection.Client;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;
import lu.btsi.bragi.ros.models.message.MessageGet;
import lu.btsi.bragi.ros.models.message.MessageType;
import lu.btsi.bragi.ros.models.pojos.*;
import org.controlsfx.dialog.ExceptionDialog;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.jooq.types.UInteger;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created by gillesbraun on 27/02/2017.
 */
public class ProductsStage extends Stage {
    private static final String LANGUAGE = "en";
    private Client client;

    public static final String CURRENCY_SYMBOL = "€";

    @FXML private TextField textFieldPrice, textFieldTranslation;
    @FXML private Button buttonProductCategoryEdit, buttonDelete, buttonRefresh, buttonAddAllergen, buttonEditAllergen,
                         buttonAddTranslation, buttonEditLanguages, buttonAddProduct, buttonEditTranslation,
                         buttonDeleteAllergen;
    @FXML private ListView<Product> listProducts;
    @FXML private ListView<ProductLocalized> listTranslations;
    @FXML private ListView<AllergenLocalized> listAllergens;
    @FXML private ChoiceBox<ProductCategoryLocalized> choiceBoxProductCategory;
    @FXML private ChoiceBox<AllergenLocalized> choiceBoxAllergen;
    @FXML private ChoiceBox<Language> choiceBoxLanguage;
    @FXML private Label labelID;
    @FXML private VBox detailPane;

    private ObservableList<Language> languagesForChoiceBox;
    private ObservableList<ProductCategoryLocalized> productCategoriesForChoiceBox;
    private ObservableList<AllergenLocalized> allergensLocalizedForChoiceBox;
    private ObservableList<ProductLocalized> productLocalizedForListView;
    private ObservableList<AllergenLocalized> allergeneLocalizedForList;
    private List<AllergenLocalized> allAllergenesLocalized;

    public ProductsStage(Client client) throws IOException {
        this.client = client;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductsStage.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        setTitle("Products");
        setScene(new Scene(root));

        GlyphFont fa = GlyphFontRegistry.font("FontAwesome");
        buttonRefresh.setGraphic(fa.create(Glyph.REFRESH));
        buttonProductCategoryEdit.setGraphic(fa.create(Glyph.PENCIL));
        buttonAddAllergen.setGraphic(fa.create(Glyph.PLUS_CIRCLE));
        buttonEditAllergen.setGraphic(fa.create(Glyph.PENCIL));
        buttonEditLanguages.setGraphic(fa.create(Glyph.PENCIL));
        buttonAddTranslation.setGraphic(fa.create(Glyph.PLUS_CIRCLE));
        buttonAddProduct.setGraphic(fa.create(Glyph.PLUS_CIRCLE));
        buttonDelete.setGraphic(fa.create(Glyph.TRASH));
        buttonEditTranslation.setGraphic(fa.create(Glyph.CHECK_CIRCLE));
        buttonDeleteAllergen.setGraphic(fa.create(Glyph.TRASH));

        textFieldPrice.focusedProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> textFieldPrice.selectAll());
        });

        listProducts.getSelectionModel().selectedItemProperty().addListener(productSelected);
        listTranslations.getSelectionModel().selectedItemProperty().addListener(translationSelected);
        listAllergens.getSelectionModel().selectedItemProperty().addListener(allergeneSelected);

        loadData();
    }


    private void loadData() {
        Platform.runLater(() -> {
            // Get all Products
            client.sendWithAction(new MessageGet<>(Product.class), message -> {
                try {
                    Product previousSelected = listProducts.getSelectionModel().getSelectedItem();
                    Message<Product> decoded = new Message<>(message);
                    ObservableList<Product> products = FXCollections.observableList(decoded.getPayload());
                    listProducts.setItems(products);
                    System.out.println("previously selected: "+previousSelected);
                    if(previousSelected != null) {
                        Optional<Product> prev = products.stream()
                                .filter(p -> p.getId().equals(previousSelected.getId()))
                                .findFirst();
                        if(prev.isPresent()) {
                            listProducts.getSelectionModel().select(prev.get());
                        }
                    }
                } catch (MessageException e) {
                    e.printStackTrace();
                }
            });

            // Get Product Categories
            client.sendWithAction(new MessageGet<>(ProductCategoryLocalized.class), message -> {
                try {
                    Message<ProductCategoryLocalized> categories = new Message<>(message);
                    productCategoriesForChoiceBox = FXCollections.observableList(categories.getPayload());
                    productCategoriesForChoiceBox.setAll(
                            productCategoriesForChoiceBox.stream()
                            .filter(pcl -> pcl.getLanguageCode().equals(LANGUAGE))
                            .collect(toList())
                    );
                    choiceBoxProductCategory.setItems(productCategoriesForChoiceBox);
                } catch (MessageException e) {
                    e.printStackTrace();
                }
            });

            // Get Allergens in a specific Language
            client.sendWithAction(new MessageGet<>(AllergenLocalized.class), message -> {
                try {
                    Message<AllergenLocalized> messageAllergensLocalized = new Message<>(message);
                    allAllergenesLocalized = messageAllergensLocalized.getPayload();
                    List<AllergenLocalized> l_AllergensLocalized = messageAllergensLocalized.getPayload()
                            .stream()
                            .filter(all -> all.getLanguageCode().equals(LANGUAGE))
                            .collect(toList());
                    allergensLocalizedForChoiceBox = FXCollections.observableList(l_AllergensLocalized);
                    choiceBoxAllergen.setItems(allergensLocalizedForChoiceBox);

                    updateAllergensChoiceBox();
                } catch (MessageException e) {
                    e.printStackTrace();
                }
            });
        });
    }
;
    private final ChangeListener<? super Product> productSelected = new ChangeListener<Product>() {
        @Override
        public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product product) {
            if(product != null) {
                detailPane.setDisable(false);
                buttonDelete.setDisable(false);
                labelID.setText(product.getId()+"");

                // Get ProductCategory from ID for selected Product
                Optional<ProductCategoryLocalized> productCategoryActual = choiceBoxProductCategory.getItems()
                        .stream()
                        .filter(
                                productCategory -> productCategory.getProductCategoryId().equals(product.getProductCategoryId()))
                        .findFirst();
                // Select the current Category
                if(productCategoryActual.isPresent()) {
                    choiceBoxProductCategory.getSelectionModel().select(productCategoryActual.get());
                }

                // Set the current price
                textFieldPrice.setText(String.valueOf(product.getPrice()));

                // Get the allergens
                client.sendWithAction(new MessageGet<>(ProductAllergen.class), message -> {
                    try {
                        List<ProductAllergen> payload = new Message<ProductAllergen>(message).getPayload();
                        List<UInteger> allergeneIDs = payload.stream()
                                .filter(productAllergen -> productAllergen.getProductId().equals(product.getId()))
                                .map(ProductAllergen::getAllergenId)
                                .collect(toList());

                        // Put all Allergens for this product in the list
                        allergeneLocalizedForList = FXCollections.observableList(
                                allAllergenesLocalized.stream()
                                        .filter(allergenLocalized -> allergeneIDs.contains(allergenLocalized.getAllergenId()))
                                        .filter(aL -> aL.getLanguageCode().equals(LANGUAGE))
                                        .collect(toList())
                        );
                        listAllergens.setItems(allergeneLocalizedForList);

                        // Put all not used Allergens in the choiceBox
                        allergensLocalizedForChoiceBox.setAll(
                                allAllergenesLocalized.stream()
                                    .filter(allergenLocalized -> allergenLocalized.getLanguageCode().equals(LANGUAGE))
                                    .filter(allergenLocalized -> ! allergeneIDs.contains(allergenLocalized.getAllergenId()))
                                    .collect(toList())
                        );

                        updateAllergensChoiceBox();
                    } catch (MessageException e) {
                        new ExceptionDialog(e).show();
                    }
                });

                // Get the translations
                client.sendWithAction(new MessageGet<>(ProductLocalized.class), message -> {
                    try {
                        List<ProductLocalized> payload = new Message<ProductLocalized>(message).getPayload();
                        List<ProductLocalized> collect = payload.stream()
                                .filter(productLocalized -> productLocalized.getProductId().equals(product.getId()))
                                .collect(toList());
                        productLocalizedForListView = FXCollections.observableList(collect);
                        listTranslations.setItems(productLocalizedForListView);
                    } catch (MessageException e) {
                        e.printStackTrace();
                    }
                });

                // Get Languages
                client.sendWithAction(new MessageGet<>(Language.class), message -> {
                    try {
                        List<Language> mLanguages = new Message<Language>(message).getPayload();
                        languagesForChoiceBox = FXCollections.observableList(mLanguages);
                        choiceBoxLanguage.setItems(languagesForChoiceBox);
                        updateTranslationChoiceBox();
                    } catch (MessageException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                buttonDelete.setDisable(true);
                detailPane.setDisable(true);
                labelID.setText("");
                textFieldPrice.setText("");
                allergeneLocalizedForList.clear();
                productLocalizedForListView.clear();
                textFieldTranslation.setText("");
            }
        }
    };

    private final ChangeListener<? super ProductLocalized> translationSelected = new ChangeListener<ProductLocalized>() {
        @Override
        public void changed(ObservableValue<? extends ProductLocalized> observable, ProductLocalized oldValue, ProductLocalized productLocalized) {
            if(productLocalized != null) {
                textFieldTranslation.setText(productLocalized.getLabel());
            }
            buttonEditTranslation.setDisable(productLocalized == null);
        }
    };

    private final ChangeListener<? super AllergenLocalized> allergeneSelected = new ChangeListener<AllergenLocalized>() {
        @Override
        public void changed(ObservableValue<? extends AllergenLocalized> observable, AllergenLocalized oldValue, AllergenLocalized allergenLocalized) {
            buttonDeleteAllergen.setDisable(allergenLocalized == null);
        }
    };

    public void buttonEditTranslationPressed(ActionEvent event) {
        ProductLocalized selectedItem = listTranslations.getSelectionModel().getSelectedItem();
        if(selectedItem != null && textFieldTranslation.getText().trim().length() > 0) {
            selectedItem.setLabel(textFieldTranslation.getText());
            listTranslations.refresh();
        }
    }

    public void buttonDeleteAllergenPressed(ActionEvent event) {
        AllergenLocalized selectedAllergenLocalized = listAllergens.getSelectionModel().getSelectedItem();
        Product selectedProduct = listProducts.getSelectionModel().getSelectedItem();
        if(selectedAllergenLocalized != null && selectedProduct != null) {
            ProductAllergen productAllergen = new ProductAllergen();
            productAllergen.setAllergenId(selectedAllergenLocalized.getAllergenId());
            productAllergen.setProductId(selectedProduct.getId());

            Message<ProductAllergen> deleteProdAllerg =
                    new Message<>(MessageType.Delete, productAllergen, ProductAllergen.class);
            client.send(deleteProdAllerg.toString());
            loadData();
        }
    }

    public void buttonRefreshPressed(ActionEvent event) {
        loadData();
    }

    public void buttonAddProductPressed(ActionEvent event) {
        Dialog<Pair<ProductCategoryLocalized, Double>> dialog = new Dialog<>();
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addButtonType);

        Label catL = new Label("Product Category");
        ChoiceBox<ProductCategoryLocalized> choiceBoxPC = new ChoiceBox<>(productCategoriesForChoiceBox);
        HBox cat = new HBox(14, catL, choiceBoxPC);
        cat.setAlignment(Pos.CENTER_LEFT);

        Label priceL = new Label("Price");
        TextField textFieldPrice = new TextField("0.00");
        textFieldPrice.focusedProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> textFieldPrice.selectAll());
        });
        HBox price = new HBox(14, priceL, textFieldPrice);
        price.setAlignment(Pos.CENTER_LEFT);

        Label header = new Label("Add Product");
        header.setFont(Font.font(18));
        VBox root = new VBox(14, header, cat, price);
        root.setPadding(new Insets(14));
        dialog.getDialogPane().setContent(root);

        dialog.setResultConverter(param -> {
            if(param.equals(addButtonType)) {
                return new Pair<>(choiceBoxPC.getValue(), Double.valueOf(textFieldPrice.getText()));
            }
            return null;
        });

        Optional<Pair<ProductCategoryLocalized, Double>> result = dialog.showAndWait();

        if(result.isPresent() && result.get().getKey() != null && result.get().getValue() != null) {
            Product product = new Product();
            product.setPrice(BigDecimal.valueOf(result.get().getValue()));
            product.setProductCategoryId(result.get().getKey().getProductCategoryId());
            Message<Product> createMsg = new Message<>(MessageType.Create, product, Product.class);
            client.send(createMsg.toString());
            loadData();
        }
    }

    public void buttonAddAllergenPressed(ActionEvent event) {
        // Search for Allergen by ID
        Optional<AllergenLocalized> selectedAllergen = allergensLocalizedForChoiceBox
                .stream()
                .filter(a -> a.getAllergenId().equals(choiceBoxAllergen.getValue().getAllergenId()))
                .findFirst();
        if(selectedAllergen.isPresent()) {
            allergeneLocalizedForList.add(selectedAllergen.get());
        }
        updateAllergensChoiceBox();
    }

    public void buttonUpdatePressed(ActionEvent event) {
        Product p = listProducts.getSelectionModel().getSelectedItem();
        if(p != null) {
            p.setPrice(BigDecimal.valueOf(Double.valueOf(textFieldPrice.getText())));
            p.setProductCategoryId(choiceBoxProductCategory.getValue().getProductCategoryId());
            Message<Product> productMessage = new Message<>(MessageType.Update, p, Product.class);
            client.send(productMessage.toString());

            allergeneLocalizedForList.forEach(allergenLocalized -> {
                ProductAllergen productAllergen = new ProductAllergen();
                productAllergen.setProductId(p.getId());
                productAllergen.setAllergenId(allergenLocalized.getAllergenId());
                Message<ProductAllergen> message = new Message<>(MessageType.Create, productAllergen, ProductAllergen.class);
                client.send(message.toString());
            });

            Message<ProductLocalized> message = new Message<>(MessageType.Create, productLocalizedForListView, ProductLocalized.class);
            client.send(message.toString());
        }
    }

    public void buttonDeletePressed(ActionEvent event) {
        Product p;
        if((p = listProducts.getSelectionModel().getSelectedItem()) != null) {
            Message<Product> productMessage = new Message<>(MessageType.Delete, p, Product.class);
            client.send(productMessage.toString());
            loadData();
        }
    }

    public void buttonAddTranslationPressed(ActionEvent event) {
        if(textFieldTranslation.getText().trim().length() == 0 ||
                choiceBoxLanguage.getValue() == null)
            return;
        ProductLocalized productLocalized = new ProductLocalized();
        productLocalized.setLabel(textFieldTranslation.getText());
        productLocalized.setProductId(listProducts.getSelectionModel().getSelectedItem().getId());
        productLocalized.setLanguageCode(choiceBoxLanguage.getValue().getCode());
        productLocalizedForListView.add(productLocalized);
        textFieldTranslation.setText("");
        textFieldTranslation.requestFocus();

        updateTranslationChoiceBox();
    }

    private void updateAllergensChoiceBox() {
        List<UInteger> existingAllergens;
        if(allergeneLocalizedForList != null) {
            // Gather list of already added Allergens, only ID
             existingAllergens = allergeneLocalizedForList.stream()
                    .map(AllergenLocalized::getAllergenId)
                    .collect(toList());
        } else {
            existingAllergens = new ArrayList<>();
        }
        // Replace list with all that are not already in the list
        allergensLocalizedForChoiceBox.setAll(
                allAllergenesLocalized.stream()
                    .filter(aL -> aL.getLanguageCode().equals(LANGUAGE))
                    .filter(allergenL -> ! existingAllergens.contains(allergenL.getAllergenId()))
                    .collect(toList())
        );
        choiceBoxAllergen.setDisable(allergensLocalizedForChoiceBox.isEmpty());
        buttonAddAllergen.setDisable(allergensLocalizedForChoiceBox.isEmpty());
    }

    private void updateTranslationChoiceBox() {
        List<String> languagesTranslated = productLocalizedForListView.stream()
                .map(ProductLocalized::getLanguageCode)
                .collect(toList());

        // Set choiceBox items to those that haven't been used
        languagesForChoiceBox.setAll(
                languagesForChoiceBox.stream()
                        .filter(language -> !languagesTranslated.contains(language.getCode()))
                        .collect(toList())
        );

        // Disable Translation adding when no languagesForChoiceBox available
        choiceBoxLanguage.setDisable(languagesForChoiceBox.isEmpty());
        buttonAddTranslation.setDisable(languagesForChoiceBox.isEmpty());
    }

    public void buttonEditLanguagesPressed(ActionEvent event) {
        try {
            LanguagesStage languagesStage = new LanguagesStage(client);
            languagesStage.initOwner(getOwner());
            languagesStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buttonProductCategoryEditPressed(ActionEvent event) {
        try {
            ProductCategoriesStage productCategoriesStage = new ProductCategoriesStage(client);
            productCategoriesStage.initOwner(getOwner());
            productCategoriesStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buttonEditAllergenPressed(ActionEvent event) {
        try {
            AllergensStage allergensStage = new AllergensStage(client);
            allergensStage.initOwner(getOwner());
            allergensStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
