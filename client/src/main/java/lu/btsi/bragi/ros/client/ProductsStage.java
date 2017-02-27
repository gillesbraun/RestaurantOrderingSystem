package lu.btsi.bragi.ros.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lu.btsi.bragi.ros.client.connection.Client;
import lu.btsi.bragi.ros.models.pojos.AllergenLocalized;
import lu.btsi.bragi.ros.models.pojos.ProductCategoryLocalized;
import lu.btsi.bragi.ros.models.pojos.ProductLocalized;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.io.IOException;

/**
 * Created by gillesbraun on 27/02/2017.
 */
public class ProductsStage extends Stage {
    private Client client;

    @FXML private TextField textFieldPrice, textFieldTranslation;
    @FXML private Button buttonProductCategoryEdit, buttonDelete, buttonRefresh, buttonAddAllergen, buttonEditAllergen,
                         buttonAddTranslation, buttonEditLanguages, buttonAddProduct;
    @FXML private ListView<ProductLocalized> listProducts, listTranslations;
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
    }

    private void loadData() {

    }

    public void buttonRefreshPressed(ActionEvent event) {
        loadData();
    }

    public void buttonAddProductPressed(ActionEvent event) {

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
