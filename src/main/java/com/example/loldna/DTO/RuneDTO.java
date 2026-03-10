package com.example.loldna.DTO;

public class RuneDTO {

    private Integer id;
    private String name;
    private String icon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Data Dragon relative icon path, e.g. "perk-images/Styles/Precision/PressTheAttack/PressTheAttack.png".
     */
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

