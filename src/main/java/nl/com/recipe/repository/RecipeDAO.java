package nl.com.recipe.repository;

import nl.com.recipe.endpoint.dto.RecipeFilterDto;
import nl.com.recipe.entity.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class RecipeDAO {

    private final MongoTemplate mongoTemplate;

    public RecipeDAO(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void save(final Recipe recipe) {
        mongoTemplate.save(recipe);
    }

    public void removeById(final String name) {
        ;
        var recipeFilterDto = RecipeFilterDto.builder().id(name).build();
        mongoTemplate.remove(setupCriteria(null, recipeFilterDto), Recipe.class);
    }

    public List<Recipe> findByCriteria(final Pageable pageable, final RecipeFilterDto recipeFilterDto) {

        return mongoTemplate.find(setupCriteria(pageable, recipeFilterDto), Recipe.class);
    }

    private Query setupCriteria(final Pageable pageable, final RecipeFilterDto recipeFilterDto) {
        Query query;
        if (StringUtils.hasText(recipeFilterDto.instructions())) {
            TextCriteria criteria = TextCriteria
                    .forDefaultLanguage()
                    .matching(recipeFilterDto.instructions());

            query = TextQuery.queryText(criteria).sortByScore();
        } else {
            query = new Query();
        }
        if (StringUtils.hasText(recipeFilterDto.id())) {
            query.addCriteria(Criteria.where(Recipe.RecipeFieldName.ID).is(recipeFilterDto.id()));
        }

        if (StringUtils.hasText(recipeFilterDto.name())) {
            query.addCriteria(Criteria.where(Recipe.RecipeFieldName.NAME).is(recipeFilterDto.name()));
        }

        if (!ObjectUtils.isEmpty(recipeFilterDto.vegetarian())) {
            query.addCriteria(Criteria.where(Recipe.RecipeFieldName.VEGETARIAN).is(recipeFilterDto.vegetarian()));
        }

        if (!ObjectUtils.isEmpty(recipeFilterDto.ingredientsNames())) {
            query.addCriteria(Criteria.where(Recipe.RecipeFieldName.INGREDIENTS).all(recipeFilterDto.ingredientsNames()));
        }

        if (!ObjectUtils.isEmpty(recipeFilterDto.ignoredIngredients())) {
            query.addCriteria(Criteria.where(Recipe.RecipeFieldName.INGREDIENTS).nin(recipeFilterDto.ignoredIngredients()));
        }

        if (!ObjectUtils.isEmpty(recipeFilterDto.serves())) {
            query.addCriteria(Criteria.where(Recipe.RecipeFieldName.SERVERS).is(recipeFilterDto.serves()));
        }

        if (pageable != null) {
            query.with(pageable);
        }

        return query;
    }
}
