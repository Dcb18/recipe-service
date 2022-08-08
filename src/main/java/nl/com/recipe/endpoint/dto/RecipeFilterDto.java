package nl.com.recipe.endpoint.dto;

import java.util.List;

public record RecipeFilterDto(String id, String name, Boolean vegetarian, List<String> ingredientsNames,
                              String instructions, Integer serves, List<String> ignoredIngredients) {

    public static RecipeFilterDtoBuilder builder() {
        return new RecipeFilterDtoBuilder();
    }

    public static class RecipeFilterDtoBuilder {
        private String id;
        private String name;
        private Boolean vegetarian;
        private List<String> ingredientsNames;

        private List<String> ignoredIngredients;
        private String instructions;
        private Integer serves;

        private String creationDate;

        public RecipeFilterDtoBuilder id(String id) {
            this.id = id;
            return this;
        }

        public RecipeFilterDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RecipeFilterDtoBuilder vegetarian(Boolean vegetarian) {
            this.vegetarian = vegetarian;
            return this;
        }

        public RecipeFilterDtoBuilder ignoredIngredients(List<String> ignoredIngredients) {
            this.ignoredIngredients = ignoredIngredients;
            return this;
        }

        public RecipeFilterDtoBuilder ingredientsNames(List<String> ingredientsNames) {
            this.ingredientsNames = ingredientsNames;
            return this;
        }

        public RecipeFilterDtoBuilder instructions(String instructions) {
            this.instructions = instructions;
            return this;
        }

        public RecipeFilterDtoBuilder serves(Integer serves) {
            this.serves = serves;
            return this;
        }

        public RecipeFilterDtoBuilder creationDate(String creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public RecipeFilterDto build() {
            return new RecipeFilterDto(id, name, vegetarian, ingredientsNames, instructions, serves, ignoredIngredients);
        }


    }


}
