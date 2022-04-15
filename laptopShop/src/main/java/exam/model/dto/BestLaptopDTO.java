package exam.model.dto;

import exam.model.Shop;

import java.math.BigDecimal;

public class BestLaptopDTO {
    private String macAddress;
    private float cpuSpeed;
    private int ram;
    private int storage;
    private BigDecimal price;
    private Shop shop;


    public BestLaptopDTO(String macAddress, float cpuSpeed, int ram, int storage, BigDecimal price, Shop shop) {
        this.macAddress = macAddress;
        this.cpuSpeed = cpuSpeed;
        this.ram = ram;
        this.storage = storage;
        this.price = price;
        this.shop = shop;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public float getCpuSpeed() {
        return cpuSpeed;
    }

    public int getRam() {
        return ram;
    }

    public int getStorage() {
        return storage;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Shop getShop() {
        return shop;
    }

    @Override
    public String toString() {
        return String.format("Laptop - %s%n" +
                "*Cpu speed - %.2f%n" +
                "**Ram - %d%n" +
                "***Storage - %d%n" +
                "****Price - %.2f%n" +
                "#Shop name - %s%n" +
                "##Town - %s%n", this.getMacAddress(),this.getCpuSpeed(), this.getRam(),
                this.getStorage(), this.getPrice(), this.getShop().getName(), this.getShop().getTown().getName() );
    }
}
