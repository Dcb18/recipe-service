package nl.com.recipe.service.imp;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.com.recipe.endpoint.dto.RecipeDto;
import nl.com.recipe.endpoint.dto.RecipeFilterDto;
import nl.com.recipe.entity.Recipe;
import nl.com.recipe.exception.RecipeNotFoundException;
import nl.com.recipe.repository.RecipeDAO;
import nl.com.recipe.service.RecipeService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.MethodNotAllowedException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeDAO recipeDAO;

    public RecipeServiceImpl(RecipeDAO recipeDAO) {
        this.recipeDAO = recipeDAO;
    }


    @Override
    public RecipeDto save(RecipeDto recipeDto) {

        if (recipeDto.id() != null) {
            throw new MethodNotAllowedException(HttpMethod.POST, null);
        }
        var recipe = dtoToRecipe(recipeDto);
        recipe.setCreationDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        recipeDAO.save(recipe);
        return recipeToDTO(recipe);
    }

    @Override
    public RecipeDto findById(String id) {
        return recipeToDTO(recipeDAO.findByCriteria(null, RecipeFilterDto.builder().id(id).build())
                .stream().findFirst().orElseThrow(RecipeNotFoundException::new));
    }

    @Override
    public List<RecipeDto> findByDTO(final Integer page, final Integer size, RecipeFilterDto recipeFilterDto) {
        return recipeDAO.findByCriteria(setUpPageRequest(page, size), recipeFilterDto)
                .stream()
                .map(this::recipeToDTO)
                .collect(Collectors.toList());

    }

    @Override
    public RecipeDto update(String id, RecipeDto recipeDto) {
        if (recipeDto.id() == null) {
            throw new MethodNotAllowedException(HttpMethod.PUT, null);
        }

        var oldRecipe = findById(id);
        var newRecipe = dtoToRecipe(recipeDto);

        newRecipe.setId(oldRecipe.id());
        newRecipe.setCreationDate(oldRecipe.creationDate());
        recipeDAO.save(newRecipe);
        return recipeToDTO(newRecipe);
    }

    @Override
    public void delete(String id) {
        recipeDAO.removeById(id);
    }


    private Recipe dtoToRecipe(RecipeDto recipeDto) {
        return new ObjectMapper().convertValue(recipeDto, Recipe.class);

    }

    private RecipeDto recipeToDTO(Recipe recipe) {
        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .convertValue(recipe, RecipeDto.class);

    }

    private PageRequest setUpPageRequest(final Integer page, final Integer size) {
        if (ObjectUtils.isEmpty(page) || ObjectUtils.isEmpty(size)) {
            return null;
        }
        return PageRequest.of(page, size);
    }


}
