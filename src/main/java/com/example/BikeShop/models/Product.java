package com.example.BikeShop.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

//Товар
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idProduct;

    @NotBlank(message = "Наименование товара не должно быть пустым или состоять из одних лишь пробелов")
    @Size(min = 1, max = 30, message = "Наименование товара должно быть от 1 до 30 символов")
    private String name;

    @Min(value = 1, message = "Цена товара должна быть больше 0")
    @NotNull(message = "Цена товара не должна быть пустой")
    private int price;

    @NotNull(message = "Дата поступления не должна быть пустой")
    @Past(message = "Дата поступления не должна быть будущей")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dateReceipt;

    @Min(value = 0, message = "Количество товара должно быть больше или равно 0")
    @NotNull(message = "Количество товара не должно быть пустым")
    private int count;

    @Min(value = 0, message = "Срок гарантии должен быть больше или равно 0")
    @NotNull(message = "Срок гарантии не должен быть пустым")
    private int warrantyPeriod;

    private boolean active;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "imageId")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "categoryId", referencedColumnName = "idCategory")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "colorId", referencedColumnName = "idColor")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "storageId", referencedColumnName = "idStorage")
    private Storage storage;

    @ManyToOne
    @JoinColumn(name = "supplierId", referencedColumnName = "idSupplier")
    private Supplier supplier;

    public Product() {
    }

    public Product(String name, int price, Date dateReceipt, int count, int warrantyPeriod, boolean active,
                   Image image, Category category, Color color, Storage storage, Supplier supplier) {
        this.name = name;
        this.price = price;
        this.dateReceipt = dateReceipt;
        this.count = count;
        this.warrantyPeriod = warrantyPeriod;
        this.active = active;
        this.image = image;
        this.category = category;
        this.color = color;
        this.storage = storage;
        this.supplier = supplier;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getDateReceipt() {
        return dateReceipt;
    }

    public void setDateReceipt(Date dateReceipt) {
        this.dateReceipt = dateReceipt;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}