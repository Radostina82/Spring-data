package exam.model.dto;

import exam.model.WarrantyType;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class ImportLaptopDTO {
    @Size(min = 9)
    private String macAddress;
    @Positive
    private float cpuSpeed;
    @Min(value = 8)
    @Max(value = 128)
    private int ram;
    @Min(value = 128)
    @Max(value = 1024)
    private int storage;
    @Size(min = 10)
    private String description;
    @Positive
    private BigDecimal price;
    @NotNull
    private WarrantyType warrantyType;

    private NameDTO shop;


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

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public WarrantyType getWarrantyType() {
        return warrantyType;
    }

    public NameDTO getShop() {
        return shop;
    }
}
