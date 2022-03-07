package net.ancronik.cookbook.backend.web.dto;

import net.ancronik.cookbook.backend.StringTestUtils;
import net.ancronik.cookbook.backend.data.model.AuthorMockData;
import net.ancronik.cookbook.backend.data.model.MeasurementUnitMockData;
import net.ancronik.cookbook.backend.data.model.RecipeCommentMockData;
import net.ancronik.cookbook.backend.data.model.RecipeMockData;
import net.ancronik.cookbook.backend.domain.assembler.*;
import net.ancronik.cookbook.backend.web.dto.author.AuthorDto;
import net.ancronik.cookbook.backend.web.dto.recipe.*;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.ancronik.cookbook.backend.StringTestUtils.getRandomStringInLowerCase;
import static net.ancronik.cookbook.backend.StringTestUtils.random;

public class DtoMockData {


    public static List<RecipeDto> generateRandomMockDataForRecipeDto(int size) {
        RecipeDtoAssembler assembler = new RecipeDtoAssembler(new ModelMapper());


        return new ArrayList<>(assembler.toCollectionModel(RecipeMockData.generateRandomMockData(size)).getContent());
    }

    public static List<RecipeBasicInfoDto> generateRandomMockDataForRecipeBasicInfoDto(int size) {
        RecipeBasicInfoDtoAssembler assembler = new RecipeBasicInfoDtoAssembler(new ModelMapper());


        return new ArrayList<>(assembler.toCollectionModel(RecipeMockData.generateRandomMockData(size)).getContent());
    }


    public static List<RecipeCommentDto> generateRandomMockDataForRecipeCommentDto(int size) {
        RecipeCommentDtoAssembler assembler = new RecipeCommentDtoAssembler(new ModelMapper());


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

    public static List<MeasurementUnitDto> generateRandomMockDataForMeasurementUnitDto(int size) {
        MeasurementUnitDtoAssembler assembler = new MeasurementUnitDtoAssembler(new ModelMapper());


        return new ArrayList<>(assembler.toCollectionModel(MeasurementUnitMockData.generateRandomMockData(size)).getContent());
    }

    public static List<AuthorDto> generateRandomMockDataForAuthorDto(int size) {
        AuthorDtoAssembler assembler = new AuthorDtoAssembler(new ModelMapper());


        return new ArrayList<>(assembler.toCollectionModel(AuthorMockData.generateRandomMockData(size)).getContent());
    }
}
