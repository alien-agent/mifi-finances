package org.dvpashkevich.mifiwallet.models;

public class Category implements java.io.Serializable {
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return name.equals(category.name);
    }
}