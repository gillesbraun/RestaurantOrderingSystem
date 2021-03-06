CREATE Table IF NOT EXISTS `Table`(
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE Table IF NOT EXISTS Waiter(
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE Table IF NOT EXISTS Invoice(
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  paid TINYINT NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE Table IF NOT EXISTS `Order`(
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  processing TINYINT NOT NULL DEFAULT 0,
  processing_done TINYINT NOT NULL DEFAULT 0,
  table_id INT UNSIGNED NOT NULL,
  waiter_id INT UNSIGNED NOT NULL,
  invoice_id INT UNSIGNED,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_Order_Table
    FOREIGN KEY (table_id)
    REFERENCES `Table` (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

  CONSTRAINT fk_Order_Waiter
    FOREIGN KEY (waiter_id)
    REFERENCES Waiter (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

  CONSTRAINT fk_Order_Invoice
  FOREIGN KEY (invoice_id)
  REFERENCES Invoice (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE Location(
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  description VARCHAR(50) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE Table IF NOT EXISTS Language(
  code CHAR(2) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Product_Category (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  image_url VARCHAR(500),
  location_id INT UNSIGNED NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_ProductCategory_Location
    FOREIGN KEY (location_id)
    REFERENCES Location (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Product_Category_Localized (
  product_category_id INT UNSIGNED,
  language_code CHAR(2),
  label VARCHAR(255) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (product_category_id, language_code),

  CONSTRAINT fk_ProductCategoryLocalized_Language
  FOREIGN KEY (language_code)
  REFERENCES Language (code)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

  CONSTRAINT fk_ProductCategoryLocalized_ProductCategory
  FOREIGN KEY (product_category_id)
  REFERENCES Product_Category (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE Table IF NOT EXISTS Product(
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  price DECIMAL(10,2) NOT NULL,
  product_category_id INT UNSIGNED NOT NULL,
  location_id INT UNSIGNED,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_Product_Location
    FOREIGN KEY (location_id)
    REFERENCES Location (id)
    ON UPDATE CASCADE
    ON DELETE SET NULL,

  CONSTRAINT fk_Product_ProductCategory
  FOREIGN KEY (product_category_id)
  REFERENCES Product_Category (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE Table IF NOT EXISTS Product_Localized (
  product_id INT UNSIGNED AUTO_INCREMENT,
  language_code CHAR(2),
  label VARCHAR(255) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (product_id, language_code),

  CONSTRAINT fk_ProductLocalized_Product
    FOREIGN KEY (product_id)
    REFERENCES Product (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

  CONSTRAINT fk_ProductLocalized_Language
    FOREIGN KEY (language_code)
    REFERENCES Language (code)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE Table IF NOT EXISTS Allergen(
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE Table IF NOT EXISTS Allergen_Localized (
  allergen_id INT UNSIGNED,
  language_code CHAR(2),
  label VARCHAR(500),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (allergen_id, language_code),

  CONSTRAINT fk_AllergenLocalized_Allergen
  FOREIGN KEY (allergen_id)
  REFERENCES Allergen (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

  CONSTRAINT fk_AllergenLocalized_Language
  FOREIGN KEY (language_code)
  REFERENCES Language (code)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE Table IF NOT EXISTS Product_Price_For_Order (
  product_id INT UNSIGNED,
  order_id INT UNSIGNED,
  price_per_product DECIMAL(10, 2) NOT NULL,
  quantity INT UNSIGNED NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (product_id, order_id),

  CONSTRAINT fk_ProductPriceForOrder_Product
    FOREIGN KEY (product_id)
    REFERENCES Product (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

  CONSTRAINT fk_ProductPriceForOrder_Order
    FOREIGN KEY (order_id)
    REFERENCES `Order` (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE Table IF NOT EXISTS Product_Allergen(
  allergen_id INT UNSIGNED,
  product_id INT UNSIGNED,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (allergen_id, product_id),

  CONSTRAINT fk_ProductAllergen_Allergen
  FOREIGN KEY (allergen_id)
  REFERENCES Allergen (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

  CONSTRAINT fk_ProductAllergen_Product
  FOREIGN KEY (product_id)
  REFERENCES Product (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);