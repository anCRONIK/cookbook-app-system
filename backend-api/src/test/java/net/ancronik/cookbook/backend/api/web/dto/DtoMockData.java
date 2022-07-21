package net.ancronik.cookbook.backend.api.web.dto;

import net.ancronik.cookbook.backend.api.StringTestUtils;
import net.ancronik.cookbook.backend.api.data.model.AuthorMockData;
import net.ancronik.cookbook.backend.api.data.model.MeasurementUnitMockData;
import net.ancronik.cookbook.backend.api.data.model.RecipeCommentMockData;
import net.ancronik.cookbook.backend.api.data.model.RecipeMockData;
import net.ancronik.cookbook.backend.api.domain.assembler.*;
import net.ancronik.cookbook.backend.api.web.dto.author.AuthorModel;
import net.ancronik.cookbook.backend.api.web.dto.recipe.*;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.ancronik.cookbook.backend.api.StringTestUtils.getRandomStringInLowerCase;
import static net.ancronik.cookbook.backend.api.StringTestUtils.random;

public class DtoMockData {


    public static List<RecipeModel> generateRandomMockDataForRecipeModel(int size) {
        RecipeModelAssembler assembler = new RecipeModelAssembler(new ModelMapper());


        return new ArrayList<>(assembler.toCollectionModel(RecipeMockData.generateRandomMockData(size)).getContent());
    }

    public static List<RecipeBasicInfoModel> generateRandomMockDataForRecipeBasicInfoModel(int size) {
        RecipeBasicInfoModelAssembler assembler = new RecipeBasicInfoModelAssembler(new ModelMapper());


        return new ArrayList<>(assembler.toCollectionModel(RecipeMockData.generateRandomMockData(size)).getContent());
    }


    public static List<RecipeCommentModel> generateRandomMockDataForRecipeCommentModel(int size) {
        RecipeCommentModelAssembler assembler = new RecipeCommentModelAssembler(new ModelMapper());


        return new ArrayList<>(assembler.toCollectionModel(RecipeCommentMockData.generateRandomMockData(size)).getContent());
    }

    public static RecipeCreateRequest generateRandomMockDataForRecipeCreateRequest() {
        return new RecipeCreateRequest(
            StringTestUtils.getRandomStringInLowerCase(20),
            StringTestUtils.getRandomStringInLowerCase(200),
            StringTestUtils.generateRandomUrl(),
            StringTestUtils.generateRandomUrl(),
            generateRandomMockDataForIngredientDto(random.nextInt(10)),
            random.nextInt(600),
            StringTestUtils.getRandomStringInLowerCase(2000),
            random.nextInt(600),
            StringTestUtils.getRandomStringInLowerCase(2000),
            random.nextInt(5) + 1,
            getRandomStringInLowerCase(6)
        );
    }

    public static RecipeUpdateRequest generateRandomMockDataForRecipeUpdateRequest() {
        return new RecipeUpdateRequest(
            StringTestUtils.getRandomStringInLowerCase(20),
            StringTestUtils.getRandomStringInLowerCase(200),
            StringTestUtils.generateRandomUrl(),
            StringTestUtils.generateRandomUrl(),
            generateRandomMockDataForIngredientDto(random.nextInt(10)),
            random.nextInt(600),
            StringTestUtils.getRandomStringInLowerCase(2000),
            random.nextInt(600),
            StringTestUtils.getRandomStringInLowerCase(2000),
            random.nextInt(5) + 1,
            getRandomStringInLowerCase(6)
        );
    }


    public static List<IngredientDto> generateRandomMockDataForIngredientDto(int size) {
        List<IngredientDto> data = new ArrayList<>(size);

        Random random = new Random();

        for (int i = 0; i < size; ++i) {
            data.add(new IngredientDto(
                getRandomStringInLowerCase(random.nextInt(100)),
                "" + random.nextInt(100),
                getRandomStringInLowerCase(4)
            ));
        }

        return data;
    }

    public static List<MeasurementUnitModel> generateRandomMockDataForMeasurementUnitModel(int size) {
        MeasurementUnitModelAssembler assembler = new MeasurementUnitModelAssembler(new ModelMapper());


        return new ArrayList<>(assembler.toCollectionModel(MeasurementUnitMockData.generateRandomMockData(size)).getContent());
    }

    public static List<AuthorModel> generateRandomMockDataForAuthorModel(int size) {
        AuthorModelAssembler assembler = new AuthorModelAssembler(new ModelMapper());


        return new ArrayList<>(assembler.toCollectionModel(AuthorMockData.generateRandomMockData(size)).getContent());
    }
}
