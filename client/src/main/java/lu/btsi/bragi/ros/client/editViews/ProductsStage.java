package lu.btsi.bragi.ros.client.editViews;

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
import lu.btsi.bragi.ros.client.connection.ConnectionManager;
import lu.btsi.bragi.ros.client.settings.Config;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;
import lu.btsi.bragi.ros.models.message.MessageGet;
import lu.btsi.bragi.ros.models.pojos.*;
import org.controlsfx.control.PopOver;
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
import static lu.btsi.bragi.ros.models.message.MessageType.*;

/**
 * Created by gillesbraun on 27/02/2017.
 */
public class ProductsStage extends Stage {
    @FXML private TextField textFieldPrice, textFieldTranslation;
    @FXML private Button buttonProductCategoryEdit, buttonDelete, buttonRefresh, buttonAddAllergen, buttonEditAllergen,
                         buttonAddTranslation, buttonAddProduct, buttonEditTranslation, buttonLocationEdit,
                         buttonDeleteAllergen, buttonAddTranslationAllLanguages, buttonUpdate, buttonRemoveLocation;
    @FXML private ListView<Product> listProducts;
    @FXML private ListView<ProductLocalized> listTranslations;
    @FXML private ListView<AllergenLocalized> listAllergens;
    @FXML private ChoiceBox<ProductCategoryLocalized> choiceBoxProductCategory;
    @FXML private ChoiceBox<AllergenLocalized> choiceBoxAllergen;
    @FXML private ChoiceBox<Language> choiceBoxLanguage;
    @FXML private ChoiceBox<Location> choiceBoxLocation;
    @FXML private Label labelID;
    @FXML private VBox detailPane;

    private ObservableList<Language> languagesForChoiceBox = FXCollections.observableArrayList();
    private ObservableList<ProductCategoryLocalized> productCategoriesForChoiceBox = FXCollections.observableArrayList();
    private ObservableList<AllergenLocalized> allergensLocalizedForChoiceBox = FXCollections.observableArrayList();
    private ObservableList<ProductLocalized> productLocalizedForListView = FXCollections.observableArrayList();
    private ObservableList<AllergenLocalized> allergeneLocalizedForList = FXCollections.observableArrayList();
    private ObservableList<Location> locationsForChoiceBox = FXCollections.observableArrayList();
    private List<AllergenLocalized> allAllergenesLocalized;
    private List<Language> allLanguages;
    private List<ProductCategoryLocalized> allProductCategoriesLocalized;

    public ProductsStage() throws IOException {
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
        buttonRemoveLocation.setGraphic(fa.create(Glyph.MINUS));
        buttonLocationEdit.setGraphic(fa.create(Glyph.PENCIL));
        buttonAddTranslation.setGraphic(fa.create(Glyph.PLUS_CIRCLE));
        buttonAddProduct.setGraphic(fa.create(Glyph.PLUS_CIRCLE));
        buttonDelete.setGraphic(fa.create(Glyph.TRASH));
        buttonEditTranslation.setGraphic(fa.create(Glyph.CHECK_CIRCLE));
        buttonDeleteAllergen.setGraphic(fa.create(Glyph.TRASH));
        buttonAddTranslationAllLanguages.setGraphic(fa.create(Glyph.GLOBE));
        buttonEditTranslation.setDisable(true);

        textFieldPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Double.valueOf(newValue.replace(',','.'));
                buttonUpdate.setDisable(false);
            } catch (NumberFormatException ignore) {
                buttonUpdate.setDisable(true);
            }
        });

        Label label = new Label("Add this translation for all languages");
        HBox popOverContent = new HBox(label);
        popOverContent.setPadding(new Insets(0, 10, 0, 10));
        PopOver popOver = new PopOver(popOverContent);
        popOver.setDetachable(false);
        buttonAddTranslationAllLanguages.disableProperty().bind(buttonAddTranslation.disabledProperty());
        buttonAddTranslationAllLanguages.hoverProperty().addListener((observable, oldValue, isHover) -> {
            if(isHover) {
                popOver.show(buttonAddTranslationAllLanguages, -15);
            } else {
                popOver.hide();
            }
        });

        textFieldPrice.focusedProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> textFieldPrice.selectAll());
        });

        listProducts.getSelectionModel().selectedItemProperty().addListener(productSelected);
        listProducts.setCellFactory(param -> new ListCell<Product>() {
                    @Override
                    protected void updateItem(Product item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item == null) {
                            setText(null);
                            return;
                        }
                        ProductLocalized productInLanguage = null;
                        try {
                            productInLanguage = item.getProductInLanguage(Config.getInstance().generalSettings.getLanguage());
                        } catch (Exception e) {}
                        String price = Config.getInstance().formatCurrency(item.getPrice().doubleValue());
                        String str = String.format("%s - %s", productInLanguage != null ? productInLanguage.getLabel() : item, price);
                        setText(str);
                    }
                }
        );
        listTranslations.getSelectionModel().selectedItemProperty().addListener(translationSelected);
        listTranslations.setCellFactory(param -> new ListCell<ProductLocalized>(){
            @Override
            protected void updateItem(ProductLocalized item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? null : item.getLanguageCode() + ": " + item.getLabel());
            }
        });
        listAllergens.getSelectionModel().selectedItemProperty().addListener(allergeneSelected);

        choiceBoxLocation.setItems(locationsForChoiceBox);

        choiceBoxLanguage.setItems(languagesForChoiceBox);
        loadData();
    }


    private void loadData() {
        Platform.runLater(() -> {
            // Get all Products for left listView
            ConnectionManager.getInstance().sendWithAction(new MessageGet<>(Product.class), message -> {
                try {
                    Product previousSelected = listProducts.getSelectionModel().getSelectedItem();
                    Message<Product> decoded = new Message<>(message);
                    ObservableList<Product> products = FXCollections.observableList(decoded.getPayload());
                    listProducts.setItems(products);
                    listProducts.refresh();
                    if(previousSelected != null)
                        listProducts.getSelectionModel().select(previousSelected);
                } catch (MessageException e) {
                    e.printStackTrace();
                }
            });

            // Get Product Categories for display in choicebox
            ConnectionManager.getInstance().sendWithAction(new MessageGet<>(ProductCategoryLocalized.class), message -> {
                try {
                    allProductCategoriesLocalized = new Message<ProductCategoryLocalized>(message).getPayload();
                    productCategoriesForChoiceBox.setAll(
                            allProductCategoriesLocalized.stream()
                            .filter(pcl -> pcl.getLanguageCode().equals(Config.getInstance().generalSettings.getLanguage().getCode()))
                            .collect(toList())
                    );
                    choiceBoxProductCategory.setItems(productCategoriesForChoiceBox);
                    updateProductCategoryChoiceBox();
                } catch (MessageException e) {
                    e.printStackTrace();
                }
            });

            // Get Localized Allergens for display in choicebox
            ConnectionManager.getInstance().sendWithAction(new MessageGet<>(AllergenLocalized.class), message -> {
                try {
                    Message<AllergenLocalized> messageAllergensLocalized = new Message<>(message);
                    allAllergenesLocalized = messageAllergensLocalized.getPayload();
                    List<AllergenLocalized> l_AllergensLocalized = messageAllergensLocalized.getPayload()
                            .stream()
                            .filter(all -> all.getLanguageCode().equals(Config.getInstance().generalSettings.getLanguage().getCode()))
                            .collect(toList());
                    allergensLocalizedForChoiceBox = FXCollections.observableList(l_AllergensLocalized);
                    choiceBoxAllergen.setItems(allergensLocalizedForChoiceBox);

                    updateAllergensChoiceBox();
                } catch (MessageException e) {
                    e.printStackTrace();
                }
            });

            // Get all languages
            ConnectionManager.getInstance().sendWithAction(new MessageGet<>(Language.class), message -> {
                try {
                    allLanguages = new Message<Language>(message).getPayload();
                } catch (MessageException e) {
                    e.printStackTrace();
                }
            });

            // Get all locations
            ConnectionManager.getInstance().sendWithAction(new MessageGet<>(Location.class), message -> {
                try {
                    locationsForChoiceBox.setAll(new Message<Location>(message).getPayload());
                    updateLocationChoiceBox();
                } catch (MessageException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private void updateLocationChoiceBox() {
        Product product = listProducts.getSelectionModel().getSelectedItem();
        if(product != null) {
            // Select Location if it is already saved
            Location location = product.getLocation();
            if (location != null) {
                choiceBoxLocation.getSelectionModel().select(location);
            } else {
                choiceBoxLocation.getSelectionModel().clearSelection();
            }
        }
    }

    private final ChangeListener<? super Product> productSelected = new ChangeListener<Product>() {
        @Override
        public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product product) {
            if(product != null) {
                detailPane.setDisable(false);
                buttonDelete.setDisable(false);
                labelID.setText(product.getId()+"");

                // Set the current price
                textFieldPrice.setText(String.valueOf(product.getPrice()));

                // Get the allergens in the interface language
                allergeneLocalizedForList = FXCollections.observableList(
                        product.getProductAllergen().stream()
                        .map(ProductAllergen::getAllergen)
                        .flatMap(a -> a.getAllergenLocalized().stream()
                                .filter(aL -> aL.getLanguageCode().equals(Config.getInstance().generalSettings.getLanguage().getCode())))
                        .collect(toList())
                );
                listAllergens.setItems(allergeneLocalizedForList);

                updateAllergensChoiceBox();

                updateLocationChoiceBox();
                // Get the translations
                productLocalizedForListView = FXCollections.observableList(product.getProductLocalized());
                listTranslations.setItems(productLocalizedForListView);

                // Update language choicebox
                updateTranslationChoiceBox();

                updateProductCategoryChoiceBox();
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

    private void updateProductCategoryChoiceBox() {
        Product product = listProducts.getSelectionModel().getSelectedItem();
        if(product == null)
            return;
        // Get ProductCategory from ID for selected Product
        UInteger productCategoryId = product.getProductCategoryId();
        productCategoriesForChoiceBox.stream()
                .filter(pcl -> pcl.getProductCategoryId().equals(productCategoryId))
                .findFirst().ifPresent(found -> choiceBoxProductCategory.getSelectionModel().select(found));
    }

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
            textFieldTranslation.setText("");
            textFieldTranslation.requestFocus();

            ConnectionManager.getInstance().send(new Message<>(Update, selectedItem, ProductLocalized.class));
            buttonEditTranslation.setDisable(true);
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
                    new Message<>(Delete, productAllergen, ProductAllergen.class);
            ConnectionManager.getInstance().send(deleteProdAllerg);
            loadData();
        }
    }

    public void buttonRefreshPressed(ActionEvent event) {
        loadData();
    }

    public void buttonAddProductPressed(ActionEvent event) {
        Dialog<Pair<ProductCategoryLocalized, Double>> dialog = new Dialog<>();
        dialog.initOwner(this);
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
            Message<Product> createMsg = new Message<>(Create, product, Product.class);
            ConnectionManager.getInstance().send(createMsg);
            loadData();
        }
    }

    public void buttonAddAllergenPressed(ActionEvent event) {
        Product selectedProduct = listProducts.getSelectionModel().getSelectedItem();
        if(choiceBoxAllergen.getValue() == null || selectedProduct == null)
            return;
        AllergenLocalized allergenLocalized = choiceBoxAllergen.getValue();

        ProductAllergen productAllergen = new ProductAllergen();
        productAllergen.setAllergenId(allergenLocalized.getAllergenId());
        productAllergen.setProductId(selectedProduct.getId());
        ConnectionManager.getInstance().send(new Message<>(Create, productAllergen, ProductAllergen.class));
        allergeneLocalizedForList.add(allergenLocalized);
        loadData();
        updateAllergensChoiceBox();
    }

    public void buttonUpdatePressed(ActionEvent event) {
        Product product = listProducts.getSelectionModel().getSelectedItem();
        if(product != null &&
                textFieldPrice.getText().trim().length() > 0 &&
                choiceBoxProductCategory.getValue() != null) {

            String priceStr = textFieldPrice.getText().replace(',','.').replaceAll("-", "");
            product.setPrice(BigDecimal.valueOf(Double.valueOf(priceStr)));
            product.setProductCategoryId(choiceBoxProductCategory.getValue().getProductCategoryId());
            Location location = choiceBoxLocation.getValue();
            if(location != null) {
                product.setLocationId(location.getId());
            }

            Message<Product> productMessage = new Message<>(Update, product, Product.class);
            ConnectionManager.getInstance().send(productMessage);
            loadData();
        }
    }

    public void buttonDeletePressed(ActionEvent event) {
        Product p;
        if((p = listProducts.getSelectionModel().getSelectedItem()) != null) {
            Message<Product> productMessage = new Message<>(Delete, p, Product.class);
            ConnectionManager.getInstance().send(productMessage);
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
        ConnectionManager.getInstance().send(new Message<>(Create, productLocalized, ProductLocalized.class));
        textFieldTranslation.setText("");
        textFieldTranslation.requestFocus();

        updateTranslationChoiceBox();
    }

    private void updateAllergensChoiceBox() {
        // Get Allergens not yet added (for choicebox)
        allergensLocalizedForChoiceBox = FXCollections.observableList(
                allAllergenesLocalized.stream()
                        .filter(aL -> ! allergeneLocalizedForList.stream().map(AllergenLocalized::getAllergenId).collect(toList()).contains(aL.getAllergenId()))
                        .filter(aL -> aL.getLanguageCode().equals(Config.getInstance().generalSettings.getLanguage().getCode()))
                        .collect(toList())
        );
        choiceBoxAllergen.setItems(allergensLocalizedForChoiceBox);
        choiceBoxAllergen.setDisable(allergensLocalizedForChoiceBox.isEmpty());
        buttonAddAllergen.setDisable(allergensLocalizedForChoiceBox.isEmpty());
    }

    private void updateTranslationChoiceBox() {
        List<String> languagesPresent = productLocalizedForListView.stream().map(ProductLocalized::getLanguageCode).collect(toList());
        languagesForChoiceBox.setAll(
                allLanguages.stream()
                .filter(l -> ! languagesPresent.contains(l.getCode()))
                .collect(toList()
        ));

        // Disable Translation adding when no languagesForChoiceBox available
        choiceBoxLanguage.setDisable(languagesForChoiceBox.isEmpty());
        buttonAddTranslation.setDisable(languagesForChoiceBox.isEmpty());
    }

    public void buttonAddTranslationAllLanguagesPressed(ActionEvent event) {
        Product product = listProducts.getSelectionModel().getSelectedItem();
        if(product != null && textFieldTranslation.getText().trim().length() > 0) {
            ArrayList<ProductLocalized> translations = new ArrayList<>();
            for (Language language : languagesForChoiceBox) {
                ProductLocalized productLocalized = new ProductLocalized();
                productLocalized.setLabel(textFieldTranslation.getText());
                productLocalized.setProductId(product.getId());
                productLocalized.setLanguageCode(language.getCode());
                translations.add(productLocalized);
            }
            ConnectionManager.getInstance().send(new Message<>(Create, translations, ProductLocalized.class));
            textFieldTranslation.clear();
            loadData();
            updateTranslationChoiceBox();
        }
    }

    public void buttonRemoveLocationPressed(ActionEvent e) {
        Product product = listProducts.getSelectionModel().getSelectedItem();
        if(product != null) {
            product.setLocationId(null);
            product.setLocation(null);
            updateLocationChoiceBox();
        }
    }

    public void buttonProductCategoryEditPressed(ActionEvent event) {
        try {
            ProductCategoriesStage productCategoriesStage = new ProductCategoriesStage();
            productCategoriesStage.initOwner(getOwner());
            productCategoriesStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buttonEditAllergenPressed(ActionEvent event) {
        try {
            AllergensStage allergensStage = new AllergensStage();
            allergensStage.initOwner(getOwner());
            allergensStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buttonLocationEditPressed(ActionEvent event) {
        try {
            LocationsStage locationsStage = new LocationsStage();
            locationsStage.initOwner(getOwner());
            locationsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
