package lu.btsi.bragi.ros.client.editViews;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import lu.btsi.bragi.ros.client.connection.Client;
import lu.btsi.bragi.ros.client.settings.Config;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;
import lu.btsi.bragi.ros.models.message.MessageGet;
import lu.btsi.bragi.ros.models.pojos.Language;
import lu.btsi.bragi.ros.models.pojos.Location;
import lu.btsi.bragi.ros.models.pojos.ProductCategory;
import lu.btsi.bragi.ros.models.pojos.ProductCategoryLocalized;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.http.client.fluent.Request;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static lu.btsi.bragi.ros.models.message.MessageType.*;

/**
 * Created by gillesbraun on 03/03/2017.
 */
public class ProductCategoriesStage extends Stage {
    private Client client;

    @FXML private Label labelID;
    @FXML private Button buttonRefreshProductCategories, buttonAddProductCategories, buttonDeleteProductCategories,
        buttonAddTranslation, buttonEditTranslation, buttonSaveImageURLPressed, buttonEditLocations, buttonChooseImage;
    @FXML private ListView<ProductCategory> listViewProductCategories;
    @FXML private ListView<ProductCategoryLocalized> listViewTranslations;
    @FXML private TextField textFieldTranslation;
    @FXML private ChoiceBox<Language> choiceBoxLanguage;
    @FXML private ChoiceBox<Location> choiceBoxLocation;
    @FXML private VBox detailPane;
    @FXML private ImageView imageView;

    private ObservableList<ProductCategory> productsForList;
    private ObservableList<ProductCategoryLocalized> productCaterogiesLocalizedForList;
    private ObservableList<Language> languagesForChoiceBox;
    private ObservableList<Location> locationsForChoiceBox;

    public ProductCategoriesStage(Client client) throws IOException {
        this.client = client;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductCategoriesStage.fxml"));
        loader.setController(this);
        Parent root = loader.load();

        setTitle("Product Categories");
        setScene(new Scene(root));

        GlyphFont fa = GlyphFontRegistry.font("FontAwesome");
        buttonRefreshProductCategories.setGraphic(fa.create(Glyph.REFRESH));
        buttonAddProductCategories.setGraphic(fa.create(Glyph.PLUS_CIRCLE));
        buttonDeleteProductCategories.setGraphic(fa.create(Glyph.TRASH));

        buttonAddTranslation.setGraphic(fa.create(Glyph.PLUS_CIRCLE));
        buttonEditTranslation.setGraphic(fa.create(Glyph.CHECK_CIRCLE));
        buttonEditLocations.setGraphic(fa.create(Glyph.PENCIL));

        detailPane.setDisable(true);

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(150);

        textFieldTranslation.setOnKeyReleased(textFieldTranslationKeyReleased);

        listViewProductCategories.getSelectionModel().selectedItemProperty().addListener(productCategorySelected);
        listViewProductCategories.setCellFactory(param -> new ListCell<ProductCategory>() {
            @Override
            protected void updateItem(ProductCategory productCategory, boolean empty) {
                super.updateItem(productCategory, empty);
                if(productCategory == null) {
                    setText(null);
                } else {
                    String label = productCategory.toString();
                    try {
                        ProductCategoryLocalized translation = productCategory.getProductCategoryTranslation(Config.getInstance().generalSettings.getLanguage());
                        label += ": " + translation.getLabel();
                    } catch (Exception ignored) {}
                    setText(label);
                }
            }
        });
        listViewTranslations.getSelectionModel().selectedItemProperty().addListener(translationSelected);
        listViewTranslations.setCellFactory(param -> new ListCell<ProductCategoryLocalized>() {
            @Override
            protected void updateItem(ProductCategoryLocalized item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null) {
                    setText(null);
                } else {
                    setText(item.getLanguageCode() + ": " + item.getLabel());
                }
            }
        });
        loadData();
    }

    private final ChangeListener<? super ProductCategory> productCategorySelected = new ChangeListener<ProductCategory>() {
        @Override
        public void changed(ObservableValue<? extends ProductCategory> observable, ProductCategory oldValue, ProductCategory selectedPC) {
            detailPane.setDisable(selectedPC == null);
            loadTranslations();
            if(selectedPC == null) {
                productCaterogiesLocalizedForList.clear();
                languagesForChoiceBox.clear();
                textFieldTranslation.clear();
                labelID.setText("-");
                imageView.setImage(new Image(getClass().getResource("/noimage.png").toString()));
            } else {
                imageView.setImage(new Image("http://"+client.getRemoteIPAdress() + ":8888"  + selectedPC.getImageUrl()));
                loadTranslations();
                labelID.setText(selectedPC.getId()+"");
                selectCurrentLocation();
            }
        }
    };

    private void selectCurrentLocation() {
        ProductCategory selectedItem = listViewProductCategories.getSelectionModel().getSelectedItem();
        if(selectedItem == null)
            return;
        Optional<Location> locationForCurrent = locationsForChoiceBox.stream()
                .filter(l -> l.getId().equals(selectedItem.getLocationId()))
                .findFirst();
        locationForCurrent.ifPresent(location -> {
            choiceBoxLocation.getSelectionModel().select(location);
        });
    }

    private final ChangeListener<? super ProductCategoryLocalized> translationSelected = (observable, oldValue, selectedPCL) -> {
        if(selectedPCL != null) {
            textFieldTranslation.setText(selectedPCL.getLabel());
        } else {
            textFieldTranslation.clear();
            buttonEditTranslation.setDisable(true);
        }
    };

    // Translation TextField on key release
    private final EventHandler<? super KeyEvent> textFieldTranslationKeyReleased = event -> {
        ProductCategoryLocalized selectedPCL = listViewTranslations.getSelectionModel().getSelectedItem();
        buttonEditTranslation.setDisable(selectedPCL == null || selectedPCL.getLabel().equals(textFieldTranslation.getText()));
    };

    private void loadData() {
        // Get Categories
        client.sendWithAction(new MessageGet<>(ProductCategory.class), message -> {
            try {
                // Get previously selected
                ProductCategory prevItem = listViewProductCategories.getSelectionModel().getSelectedItem();

                List<ProductCategory> allProducts = new Message<ProductCategory>(message).getPayload();
                productsForList = FXCollections.observableList(allProducts);
                listViewProductCategories.setItems(productsForList);

                if(prevItem != null) {
                    // Get instance of prev obj by id
                    Optional<ProductCategory> newPrevious = allProducts.stream()
                            .filter(pc -> pc.getId().equals(prevItem.getId()))
                            .findFirst();
                    if(newPrevious.isPresent()) {
                        listViewProductCategories.getSelectionModel().select(newPrevious.get());
                    }
                }
            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
        loadTranslations();
        loadLocations();
    }

    private void loadTranslations() {
        ProductCategory selectedPC = listViewProductCategories.getSelectionModel().getSelectedItem();
        if(selectedPC == null)
            return;
        // Load translations for selected ProductCategory
        client.sendWithAction(new MessageGet<>(ProductCategoryLocalized.class), message -> {
            try {
                List<ProductCategoryLocalized> allProductCaterogiesLocalized = new Message<ProductCategoryLocalized>(message).getPayload();
                productCaterogiesLocalizedForList = FXCollections.observableList(allProductCaterogiesLocalized);
                productCaterogiesLocalizedForList.setAll(
                        allProductCaterogiesLocalized.stream()
                                .filter(pcl -> pcl != null && pcl.getProductCategoryId().equals(selectedPC.getId()))
                                .collect(toList())
                );
                listViewTranslations.setItems(productCaterogiesLocalizedForList);
                updateLanguageChoiceBox();
            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateLanguageChoiceBox() {
        ProductCategory selectedProductCategory = listViewProductCategories.getSelectionModel().getSelectedItem();
        if(selectedProductCategory == null)
            return;

        client.sendWithAction(new MessageGet<>(Language.class), message -> {
            try {
                List<Language> languages = new Message<Language>(message).getPayload();

                // Get IDs of languages that have translations for current product
                List<String> langCodesTranslated = productCaterogiesLocalizedForList.stream()
                        .filter(pcl -> pcl.getProductCategoryId().equals(selectedProductCategory.getId()))
                        .map(ProductCategoryLocalized::getLanguageCode)
                        .collect(toList());

                // Gather list of not used languages
                languagesForChoiceBox = FXCollections.observableList(
                    languages.stream()
                        .filter(l -> ! langCodesTranslated.contains(l.getCode()))
                        .collect(toList())
                );
                choiceBoxLanguage.setItems(languagesForChoiceBox);
                choiceBoxLanguage.setDisable(languagesForChoiceBox.isEmpty());
                buttonAddTranslation.setDisable(languagesForChoiceBox.isEmpty());
            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadLocations() {
        client.sendWithAction(new MessageGet<>(Location.class), message -> {
            List<Location> payload = null;
            try {
                payload = new Message<Location>(message).getPayload();
                locationsForChoiceBox = FXCollections.observableList(payload);
                choiceBoxLocation.setItems(locationsForChoiceBox);
                selectCurrentLocation();
            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
    }

    public void buttonRefreshProductCategoriesPressed(ActionEvent evt) {
        loadData();
    }

    public void buttonAddProductCategoriesPressed(ActionEvent evt) {
        ChoiceDialog<Location> locationChoiceDialog = new ChoiceDialog<>();
        locationChoiceDialog.getItems().addAll(locationsForChoiceBox);
        locationChoiceDialog.setHeaderText("Specify production location");
        locationChoiceDialog.setContentText("Please specify the location where\n the products of this category are made.");
        Optional<Location> location = locationChoiceDialog.showAndWait();

        if(location.isPresent()) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setLocationId(location.get().getId());
            client.send(new Message<>(Create, productCategory, ProductCategory.class).toString());
            loadData();
        }
    }

    public void buttonDeleteProductCategoriesPressed(ActionEvent evt) {
        ProductCategory selectedPC = listViewProductCategories.getSelectionModel().getSelectedItem();
        if(selectedPC != null) {
            client.send(new Message<>(Delete, selectedPC, ProductCategory.class).toString());
            loadData();
        }
    }

    public void buttonAddTranslationPressed(ActionEvent evt) {
        ProductCategory selectedPC = listViewProductCategories.getSelectionModel().getSelectedItem();
        if(textFieldTranslation.getText().trim().length() == 0 ||
                choiceBoxLanguage.getValue() == null ||
                selectedPC == null)
            return;
        ProductCategoryLocalized pcl = new ProductCategoryLocalized();
        pcl.setProductCategoryId(selectedPC.getId());
        pcl.setLanguageCode(choiceBoxLanguage.getValue().getCode());
        pcl.setLabel(textFieldTranslation.getText());
        client.send(new Message<>(Create, pcl, ProductCategoryLocalized.class).toString());
        productCaterogiesLocalizedForList.add(pcl);
        textFieldTranslation.clear();
        loadTranslations();
    }

    public void buttonEditTranslationPressed(ActionEvent evt) {
        ProductCategoryLocalized selectedPCL = listViewTranslations.getSelectionModel().getSelectedItem();
        if(selectedPCL != null && textFieldTranslation.getText().trim().length() > 0) {
            selectedPCL.setLabel(textFieldTranslation.getText());
            textFieldTranslation.clear();
            listViewTranslations.refresh();
            client.send(new Message<>(Update, selectedPCL, ProductCategoryLocalized.class).toString());
            loadTranslations();
        }
    }

    public void buttonSavePressed(ActionEvent evt) {
        ProductCategory selectedPC = listViewProductCategories.getSelectionModel().getSelectedItem();
        if(selectedPC != null) {
            selectedPC.setLocationId(choiceBoxLocation.getValue().getId());
            client.send(new Message<>(Update, selectedPC, ProductCategory.class).toString());
            loadData();
        }
    }

    public void buttonEditLocationsPressed(ActionEvent evt) {
        try {
            LocationsStage locationsStage = new LocationsStage(client);
            locationsStage.initOwner(this);
            locationsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buttonChooseImagePressed() {
        ProductCategory selectedCategory = listViewProductCategories.getSelectionModel().getSelectedItem();
        if(selectedCategory == null)
            return;
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fc.showOpenDialog(this);
        if(file != null) {
            try {
                ImageCropperStage imageCropperStage = new ImageCropperStage(client, file);
                imageCropperStage.initOwner(this);
                imageCropperStage.showAndWait();
                System.err.println("If there were errors above (IllegalArgumentException), that is normal. Bug in controlsFX");
                WritableImage image = imageCropperStage.getImage();
                if(image != null) {
                    File rostemp = File.createTempFile("rostemp", ".png");
                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                    bufferedImage = Thumbnails.of(bufferedImage).size(500, 500).asBufferedImage();
                    ImageIO.write(bufferedImage, "png", rostemp);
                    String url = "http://" + client.getRemoteIPAdress() + ":8888/?category=" + selectedCategory.getId();
                    Request.Post(url)
                            .bodyStream(new FileInputStream(rostemp))
                            .execute();
                    rostemp.delete();
                    selectedCategory.setImageUrl("/?category=" + selectedCategory.getId());
                    client.send(new Message<>(Update, selectedCategory, ProductCategory.class));
                    loadData();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
