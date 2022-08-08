package nl.com.recipe.endpoint;

import nl.com.recipe.endpoint.dto.RecipeDto;
import nl.com.recipe.endpoint.dto.RecipeFilterDto;
import nl.com.recipe.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class RecipeEndpoint {

    private final RecipeService recipeService;

    public RecipeEndpoint(final RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<RecipeDto> getRecipes(@RequestParam Integer page,
                                      @RequestParam Integer size,
                                      RecipeFilterDto recipeDto) {
        return recipeService.findByDTO(page, size, recipeDto);
    }

    @PostMapping
    public ResponseEntity createRecipes(@RequestBody @Valid RecipeDto recipeDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.save(recipeDto));
    }

    @PutMapping("/{id}")
    public RecipeDto updateRecipe(@PathVariable String id, @RequestBody @Valid RecipeDto recipeDto) {
        return recipeService.update(id, recipeDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteRecipe(@PathVariable String id) {
        recipeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public RecipeDto getByName(@PathVariable String id) {
        return recipeService.findById(id);
    }


}
