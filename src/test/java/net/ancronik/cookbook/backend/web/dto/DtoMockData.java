package net.ancronik.cookbook.backend.web.dto;

import net.ancronik.cookbook.backend.data.model.RecipeCommentMockData;
import net.ancronik.cookbook.backend.data.model.RecipeMockData;
import net.ancronik.cookbook.backend.domain.assembler.RecipeBasicInfoDtoAssembler;
import net.ancronik.cookbook.backend.domain.assembler.RecipeCommentDtoAssembler;
import net.ancronik.cookbook.backend.domain.assembler.RecipeDtoAssembler;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

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
}
