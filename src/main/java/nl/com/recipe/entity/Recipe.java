package nl.com.recipe.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import java.util.List;
import java.util.Objects;

@Document("recipe")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Recipe {

    @Id
    private String id;

    private String name;
    private boolean vegetarian;

    private List<Ingredients> ingredients;
    @TextIndexed(weight = 5)
    private String instructions;
    private Integer serves;

    private String creationDate;

    @TextScore
    private Float score;


    public Recipe() {
    }

    public Recipe(String id, String name, boolean vegetarian, List<Ingredients> ingredients,
                  String instructions, Integer serves, String creationDate, Float score) {
        this.id = id;
        this.name = name;
        this.vegetarian = vegetarian;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.serves = serves;
        this.creationDate = creationDate;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getServes() {
        return serves;
    }

    public void setServes(Integer serves) {
        this.serves = serves;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return vegetarian == recipe.vegetarian && Objects.equals(id, recipe.id) && Objects.equals(name, recipe.name) && Objects.equals(ingredients, recipe.ingredients) && Objects.equals(instructions, recipe.instructions) && Objects.equals(serves, recipe.serves) && Objects.equals(creationDate, recipe.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, vegetarian, ingredients, instructions, serves, creationDate);
    }

    public static class RecipeFieldName {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String VEGETARIAN = "vegetarian";
        public static final String INGREDIENTS = "ingredients.name";
        public static final String INSTRUCTIONS = "instructions";
        public static final String SERVERS = "serves";
        public static final String CREATIONDATE = "creationDate";
    }


}
