package gamestore.entities.game;

import gamestore.exeptions.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class AddGameDTO {
    private String title;
    private BigDecimal price;
    private float size;
    private String trailerId;
    private String thumbnailUrl;
    private String description;
    private String releaseDate;

    public AddGameDTO(String[] commandsPart) {
        this.title = commandsPart[1];
        this.price = BigDecimal.valueOf(Double.parseDouble(commandsPart[2]));
        this.size = Float.parseFloat(commandsPart[3]);
        this.trailerId = commandsPart[4];
        this.thumbnailUrl = commandsPart[5];
        this.description = commandsPart[6];

        this.releaseDate = commandsPart[7];

        this.validate();
    }
    private void validate() {
        char firstLetter = this.title.charAt(0);
    if (!Character.isUpperCase(firstLetter) || this.title.length() <3 || this.title.length() > 100){
        throw new ValidationException("Title must be start whit uppercase letter and must have length between 3 and 100 symbols");
    }
    if (price.compareTo(BigDecimal.ZERO) < 0){
        throw new ValidationException("Price must be a positive number.");
    }
    if (size < 0){
        throw new ValidationException("Size must be a positive number.");
    }
    if (trailerId.length() != 11){
        throw new ValidationException("ID must be exactly 11 characters.");
    }
    /*if((thumbnailUrl.startsWith("http://")) || (thumbnailUrl.startsWith("https://"))){

    }else {
        throw new ValidationException("Thumbnail URL must starting with http://, https://");
    }*/
    if(description.length() < 20){
        throw new ValidationException("Description must be at least 20 symbols");
    }
    }
    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public float getSize() {
        return size;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getDescription() {
        return description;
    }

    public String  getReleaseDate() {
        return releaseDate;
    }
}
