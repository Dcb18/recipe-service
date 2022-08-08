package nl.com.recipe.service;

import nl.com.recipe.endpoint.dto.RecipeDto;
import nl.com.recipe.endpoint.dto.RecipeFilterDto;

import java.util.List;


public interface RecipeService {

    RecipeDto save(RecipeDto recipeDto);

    List<RecipeDto> findByDTO(Integer page, Integer size, RecipeFilterDto recipeDto);

    RecipeDto update(String name, RecipeDto recipeDto);

    void delete(String id);

    RecipeDto findById(String name);

}
