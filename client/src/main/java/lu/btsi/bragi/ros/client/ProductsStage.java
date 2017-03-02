package lu.btsi.bragi.ros.client;

import javafx.application.Platform;
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
import lu.btsi.bragi.ros.models.pojos.AllergenLocalized;
import lu.btsi.bragi.ros.models.pojos.Product;
import lu.btsi.bragi.ros.models.pojos.ProductCategory;
import lu.btsi.bragi.ros.models.pojos.ProductCategoryLocalized;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Created by gillesbraun on 27/02/2017.
 */
public class ProductsStage extends Stage {
    private Client client;

    @FXML private TextField textFieldPrice, textFieldTranslation;
    @FXML private Button buttonProductCategoryEdit, buttonDelete, buttonRefresh, buttonAddAllergen, buttonEditAllergen,
                         buttonAddTranslation, buttonEditLanguages, buttonAddProduct;
    @FXML private ListView<Product> listProducts, listTranslations;
    @FXML private ListView<AllergenLocalized> listAllergens;
    @FXML private ChoiceBox<ProductCategoryLocalized> choiceBoxProductCategory;
    @FXML private ChoiceBox<AllergenLocalized> choiceBoxAllergen;
    @FXML private Label labelID;
    @FXML private VBox detailPane;

    public ProductsStage(Client client) throws IOException {
        this.client = client;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductsStage.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        setTitle("Products");
        setScene(new Scene(root));

        GlyphFont fa = GlyphFontRegistry.font("FontAwesome");
        buttonRefresh.setGraphic(fa.create(FontAwesome.Glyph.REFRESH));
        buttonProductCategoryEdit.setGraphic(fa.create(FontAwesome.Glyph.PENCIL));
        buttonAddAllergen.setGraphic(fa.create(FontAwesome.Glyph.PLUS_CIRCLE));
        buttonEditAllergen.setGraphic(fa.create(FontAwesome.Glyph.PENCIL));
        buttonEditLanguages.setGraphic(fa.create(FontAwesome.Glyph.PENCIL));
        buttonAddTranslation.setGraphic(fa.create(FontAwesome.Glyph.PLUS_CIRCLE));
        buttonAddProduct.setGraphic(fa.create(FontAwesome.Glyph.PLUS_CIRCLE));
        buttonDelete.setGraphic(fa.create(FontAwesome.Glyph.TRASH));
    }

    private void loadData() {
        Platform.runLater(() -> {
            Message getProducts = new MessageGet<>(Product.class);
            client.sendWithAction(getProducts, message -> {
                try {
                    Message<Product> decoded = new Message<Product>(message);
                    ObservableList<Product> products = FXCollections.observableList(decoded.getPayload());
                    listProducts.setItems(products);
                } catch (MessageException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public void buttonRefreshPressed(ActionEvent event) {
        loadData();
    }

    public void buttonAddProductPressed(ActionEvent event) {
        Message get = new MessageGet<>(ProductCategory.class);
        client.sendWithAction(get, message -> {
            try {
                List<ProductCategory> payload = new Message<ProductCategory>(message).getPayload();
                ObservableList<ProductCategory> productCategories = FXCollections.observableList(payload);

                Dialog<Pair<ProductCategory, Double>> dialog = new Dialog<>();
                ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addButtonType);

                Label catL = new Label("Product Category");
                ChoiceBox<ProductCategory> choiceBoxPC = new ChoiceBox<>(productCategories);
                HBox cat = new HBox(14, catL, choiceBoxPC);
                cat.setAlignment(Pos.CENTER_LEFT);

                Label priceL = new Label("Price");
                TextField textFieldPrice = new TextField("0.00");
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

                Optional<Pair<ProductCategory, Double>> result = dialog.showAndWait();

                result.ifPresent(res -> {
                    Product product = new Product();
                    product.setPrice(BigDecimal.valueOf(result.get().getValue()));
                    product.setProductCategoryId(result.get().getKey().getId());
                    Message<Product> createMsg = new Message<>(MessageType.Create, product, Product.class);
                    client.send(createMsg.toString());
                    loadData();
                });

            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
    }


    public void buttonProductCategoryEditPressed(ActionEvent event) {

    }

    public void buttonAddAllergenPressed(ActionEvent event) {

    }

    public void buttonUpdatePressed(ActionEvent event) {

    }

    public void buttonDeletePressed(ActionEvent event) {

    }

    public void buttonEditAllergenPressed(ActionEvent event) {

    }

    public void buttonAddTranslationPressed(ActionEvent event) {

    }

    public void buttonEditLanguagesPressed(ActionEvent event) {

    }
}
