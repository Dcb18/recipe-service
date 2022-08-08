package nl.com.recipe.integration;

import nl.com.recipe.endpoint.dto.RecipeDto;
import nl.com.recipe.entity.Ingredients;
import nl.com.recipe.service.RecipeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RecipeService recipeService;
    private List<String> idToBeDelete = new ArrayList<>();


    @Test
    @DisplayName("Test save a recipe, should respond 201")
    public void saveTest() throws URISyntaxException {
        var resultChocolateCake = restTemplate.postForEntity(appURI(), recipeChocolateCake(), RecipeDto.class);

        var resultSteak = restTemplate.postForEntity(appURI(), recipeSteak(), RecipeDto.class);

        var resultCorn = restTemplate.postForEntity(appURI(), recipeCorn(), RecipeDto.class);

        Assertions.assertEquals(HttpStatus.CREATED, resultChocolateCake.getStatusCode());
        Assertions.assertEquals(HttpStatus.CREATED, resultSteak.getStatusCode());
        Assertions.assertEquals(HttpStatus.CREATED, resultCorn.getStatusCode());

        idToBeDelete.add(resultChocolateCake.getBody().id());
        idToBeDelete.add(resultSteak.getBody().id());
        idToBeDelete.add(resultCorn.getBody().id());

    }

    @Test
    @DisplayName("Test save a recipe, should respond 400")
    public void saveTestMissingField() throws URISyntaxException {
        var resultChocolateCake = restTemplate.postForEntity(appURI(), recipeDtoMissingField(), RecipeDto.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, resultChocolateCake.getStatusCode());
    }

    @Test
    @DisplayName("Test update a recipe, should respond 200 and recipe changed")
    public void putTest() throws URISyntaxException {
        var resultChocolateCake = restTemplate.postForEntity(appURI(), recipeChocolateCake(), RecipeDto.class);

        Assertions.assertEquals(HttpStatus.CREATED, resultChocolateCake.getStatusCode());
        var recipeDto1 = resultChocolateCake.getBody();
        idToBeDelete.add(resultChocolateCake.getBody().id());

        restTemplate.put(appURI() + recipeDto1.id(), recipeChocolateCakeChanged(recipeDto1.id()), RecipeDto.class);

        var result2 = restTemplate.getForEntity(appURI() + recipeDto1.id(), RecipeDto.class);

        Assertions.assertEquals(HttpStatus.OK, result2.getStatusCode());
        Assertions.assertEquals(result2.getBody(), recipeChocolateCakeChanged(recipeDto1.id()));

    }


    @Test
    @DisplayName("Test save and search for a recipe by id, should respond 200 and the recipe")
    public void getByIdTestTest() throws URISyntaxException {
        var resultChocolateCake = restTemplate.postForEntity(appURI(), recipeChocolateCake(), RecipeDto.class);

        Assertions.assertEquals(HttpStatus.CREATED, resultChocolateCake.getStatusCode());
        var recipeDto1 = resultChocolateCake.getBody();
        idToBeDelete.add(resultChocolateCake.getBody().id());

        var result2 = restTemplate.getForEntity(appURI() + recipeDto1.id(), RecipeDto.class);

        Assertions.assertEquals(HttpStatus.OK, result2.getStatusCode());
        Assertions.assertEquals(result2.getBody(), resultChocolateCake.getBody());

    }

    @Test
    @DisplayName("Test save and search for a recipe by some filter, should respond 200 and the recipe")
    public void getByCriteriaTestTest() throws URISyntaxException {
        var resultSteak = restTemplate.postForEntity(appURI(), recipeSteak(), RecipeDto.class);

        var resultChocolateCake = restTemplate.postForEntity(appURI(), recipeChocolateCake(), RecipeDto.class);

        var resultCorn = restTemplate.postForEntity(appURI(), recipeCorn(), RecipeDto.class);


        Assertions.assertEquals(HttpStatus.CREATED, resultChocolateCake.getStatusCode());
        Assertions.assertEquals(HttpStatus.CREATED, resultSteak.getStatusCode());
        Assertions.assertEquals(HttpStatus.CREATED, resultCorn.getStatusCode());

        idToBeDelete.add(Objects.requireNonNull(resultChocolateCake.getBody()).id());
        idToBeDelete.add(Objects.requireNonNull(resultSteak.getBody()).id());
        idToBeDelete.add(Objects.requireNonNull(resultCorn.getBody()).id());

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        var result1 = restTemplate.exchange(urlTemplateFilterTextSearch(),
                HttpMethod.GET, entity, RecipeDto[].class, filterTextSearch());

        Assertions.assertTrue(Objects.requireNonNull(result1.getBody()).length > 0);


    }


    @AfterEach
    public void removeTestedRecipes() {
        idToBeDelete.forEach(recipe -> recipeService.delete(recipe));
        idToBeDelete.clear();
    }


    public String appURI() throws URISyntaxException {
        return String.valueOf(new URI("http://localhost:" + port + "/v1/api/"));
    }

    public RecipeDto recipeChocolateCake() {
        return RecipeDto.builder().name("Easy chocolate cake")
                .vegetarian(true)
                .ingredients(List.of(new Ingredients("milk", "1 cup")))
                .instructions("""
                        For the chocolate icing, heat the chocolate and cream in a saucepan over a
                        low heat until the chocolate melts. Remove the pan from the heat and whisk the
                        mixture until smooth, glossy and thickened. Set aside to cool for 1–2 hours, or
                        until thick enough to spread over the cake..
                        """)
                .serves(5)
                .build();
    }

    public RecipeDto recipeDtoMissingField() {
        return RecipeDto.builder().name("Easy chocolate cake")
                .vegetarian(true)
                .ingredients(List.of(new Ingredients("milk", "1 cup")))
                .instructions("""
                        For the chocolate icing, heat the chocolate and cream in a saucepan over a
                        low heat until the chocolate melts. Remove the pan from the heat and whisk the
                        mixture until smooth, glossy and thickened. Set aside to cool for 1–2 hours, or
                        until thick enough to spread over the cake..
                        """)
                .build();
    }

    public RecipeDto recipeSteak() {
        return RecipeDto.builder().name("Easy steak")
                .vegetarian(false)
                .ingredients(List.of(new Ingredients("steak", "1 cup of"), new Ingredients("onion", "3g")))
                .instructions("""
                        Heat oil in a large skillet over medium heat. Add onion; and cook until soft and translucent,
                        about 3 minutes. Stir in corn kernels. Cook, stirring occasionally, until kernels are tender,
                         about 5 minutes. Stir in cream cheese and water until cream cheese is melted. Stir in tomatoes,
                          parsley, and basil. Season with salt and black pepper.
                           """)
                .serves(2)
                .build();
    }

    public RecipeDto recipeCorn() {
        return RecipeDto.builder().name("Easy corn")
                .vegetarian(true)
                .ingredients(List.of(new Ingredients("oil", "a little")))
                .instructions("""
                        warm 2 tablespoons oil in a frying pan over medium-high warm. Gently fry salmon pieces
                        until easily flaked with a fork, about 5 minutes. Drain on paper towels and set aside.
                           """)
                .serves(1)
                .build();
    }

    public RecipeDto recipeChocolateCakeChanged(String id) {
        return RecipeDto.builder()
                .id(id)
                .name("Easy chocolate cake")
                .vegetarian(true)
                .ingredients(List.of(new Ingredients("milk", "1 cup"),
                        new Ingredients("egg", "2 cup"),
                        new Ingredients("chocolate", "bar")))
                .instructions("""
                        For the chocolate icing, heat the chocolate and cream in a saucepan over a
                        low heat until the chocolate melts. Remove the pan from the heat and whisk the
                        mixture until smooth, glossy and thickened. Set aside to cool for 1–2 hours, or
                        until thick enough to spread over the cake..
                        """)
                .build();
    }

    public String urlTemplateFilterTextSearch() throws URISyntaxException {
        return UriComponentsBuilder.fromHttpUrl(appURI())
                .queryParam("page", "{page}")
                .queryParam("size", "{size}")
                .queryParam("instructions", "{instructions}")
                .encode()
                .toUriString();
    }

    public Map<String, Object> filterTextSearch() throws URISyntaxException {
        Map<String, Object> params = new HashMap<>();
        params.put("page", 0);
        params.put("size", 3);
        params.put("instructions", "heat the chocolate");
        params.put("vegetarian", true);


        return params;

    }


}
