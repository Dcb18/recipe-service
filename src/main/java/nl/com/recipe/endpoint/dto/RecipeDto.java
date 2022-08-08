package nl.com.recipe.endpoint.dto;

import nl.com.recipe.entity.Ingredients;

import javax.validation.constraints.*;
import java.util.List;

public record RecipeDto(
        String id, @NotBlank String name,
        @NotNull boolean vegetarian,
        @NotEmpty List<Ingredients> ingredients,
        @NotBlank String instructions,
        @Min(1) @NotNull Integer serves,
        @Null String creationDate) {

    public static RecipeDtoBuilder builder() {
        return new RecipeDtoBuilder();
    }

    public static class RecipeDtoBuilder {
        private String id;
        private String name;
        private boolean vegetarian;
        private List<Ingredients> ingredients;
        private String instructions;
        private Integer serves;

        private String creationDate;

        public RecipeDtoBuilder id(String id) {
            this.id = id;
            return this;
        }

        public RecipeDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RecipeDtoBuilder vegetarian(boolean vegetarian) {
            this.vegetarian = vegetarian;
            return this;
        }

        public RecipeDtoBuilder ingredients(List<Ingredients> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public RecipeDtoBuilder instructions(String instructions) {
            this.instructions = instructions;
            return this;
        }

        public RecipeDtoBuilder serves(Integer serves) {
            this.serves = serves;
            return this;
        }

        public RecipeDtoBuilder creationDate(String creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public RecipeDto build() {
            return new RecipeDto(id, name, vegetarian, ingredients, instructions, serves, creationDate);
        }


    }


}
