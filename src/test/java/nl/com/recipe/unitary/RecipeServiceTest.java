package nl.com.recipe.unitary;


import nl.com.recipe.endpoint.dto.RecipeDto;
import nl.com.recipe.endpoint.dto.RecipeFilterDto;
import nl.com.recipe.entity.Ingredients;
import nl.com.recipe.entity.Recipe;
import nl.com.recipe.exception.RecipeNotFoundException;
import nl.com.recipe.repository.RecipeDAO;
import nl.com.recipe.service.imp.RecipeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class RecipeServiceTest {


    @Mock
    private RecipeDAO recipeDAO;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    private RecipeDto recipeDtoId;

    private RecipeDto recipeDtoNoId;


    private Recipe recipe;


    public RecipeServiceTest() {
        setup();
    }

    public void setup() {
        recipeDtoId = RecipeDto.builder()
                .id("1")
                .name("testId")
                .serves(1)
                .ingredients(List.of(new Ingredients("teste", "on cup of")))
                .instructions("u have to do this")
                .vegetarian(true)
                .build();

        recipeDtoNoId = RecipeDto.builder()
                .name("testNoId")
                .serves(1)
                .ingredients(List.of(new Ingredients("teste", "on cup of")))
                .instructions("u have to do this")
                .vegetarian(true)
                .build();
        recipe = new Recipe();
        recipe.setId(recipeDtoId.id());

    }


    @Test
    @DisplayName("Test save a recipe without Id, should call the DAO to save")
    void testSaveSuccess() {
        recipeService.save(recipeDtoNoId);
        Mockito.verify(recipeDAO, Mockito.times(1)).save(Mockito.any(Recipe.class));
    }

    @Test
    @DisplayName("Test save a recipe with Id, should throw MethodNotAllowedException")
    void testSaveError() {
        var exception = Assertions.assertThrows(MethodNotAllowedException.class, () -> {
            recipeService.save(recipeDtoId);
        });
        Mockito.verify(recipeDAO, Mockito.times(0)).save(Mockito.any(Recipe.class));
    }


    @Test
    @DisplayName("Test find by Id, should return a recipeDTO ")
    void testFindById() {
        Mockito.when(recipeDAO.findByCriteria(Mockito.eq(null), Mockito.any(RecipeFilterDto.class)))
                .thenReturn(List.of(recipe));
        var resul = recipeService.findById(recipeDtoId.id());
        Assertions.assertEquals(resul.getClass(), RecipeDto.class);

    }

    @Test
    @DisplayName("Test find by Id, should throw RecipeNotFoundException")
    void testFindByIdError() {
        Mockito.when(recipeDAO.findByCriteria(Mockito.eq(null), Mockito.any(RecipeFilterDto.class)))
                .thenReturn(Collections.emptyList());
        var exception = Assertions.assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.findById("A");
        });
    }

    @Test
    @DisplayName("Test find by Id, should return a list of recipeDTO ")
    void testFindDtoId() {
        Mockito.when(recipeDAO.findByCriteria(Mockito.eq(null), Mockito.any(RecipeFilterDto.class)))
                .thenReturn(List.of(recipe));
        var resul = recipeService.findByDTO(0, 1, RecipeFilterDto.builder().build());

        Assertions.assertEquals(resul.getClass(), ArrayList.class);

    }

    @Test
    @DisplayName("Test update a recipe with Id, should throw MethodNotAllowedException")
    void testUpdateErrorId() {
        var exception = Assertions.assertThrows(MethodNotAllowedException.class, () -> {
            recipeService.update(null, recipeDtoNoId);
        });
        Mockito.verify(recipeDAO, Mockito.times(0)).save(Mockito.any(Recipe.class));
    }


    @Test
    @DisplayName("Test update a recipe with Id, should return the dto with the same id")
    void testUpdateSuccess() {
        Mockito.when(recipeDAO.findByCriteria(Mockito.eq(null), Mockito.any(RecipeFilterDto.class)))
                .thenReturn(List.of(recipe));
        var result = recipeService.update(recipeDtoId.id(), recipeDtoId);
        Assertions.assertEquals(result.id(), recipeDtoId.id());
        Mockito.verify(recipeDAO, Mockito.times(1)).save(Mockito.any(Recipe.class));
    }


    @Test
    @DisplayName("Test update a recipe with Id, but the recipe is notfound should throw RecipeNotFoundException")
    void testUpdateErrorNotFound() {
        Mockito.when(recipeDAO.findByCriteria(Mockito.eq(null), Mockito.any(RecipeFilterDto.class)))
                .thenReturn(Collections.emptyList());
        var exception = Assertions.assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.save(recipeDtoId);
        });
        Mockito.verify(recipeDAO, Mockito.times(0)).save(Mockito.any(Recipe.class));
    }
}
