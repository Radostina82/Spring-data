package exam.model.dto;

import javax.validation.constraints.Size;
import java.io.Serializable;

public class NameDTO implements Serializable {
    @Size(min = 2)
    private String name;

    public String getName() {
        return name;
    }
}
