package net.ancronik.cookbook.backend.web.dto;

import net.ancronik.cookbook.backend.data.model.RecipeMockData;
import net.ancronik.cookbook.backend.domain.assembler.RecipeDtoAssembler;
import net.ancronik.cookbook.backend.domain.assembler.RecipePreviewDtoAssembler;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class RecipeDtoMockData {


    public static List<RecipeDto> generateRandomMockDataForRecipeDto(int size) {
        RecipeDtoAssembler assembler = new RecipeDtoAssembler(new ModelMapper());


        return new ArrayList<>(assembler.toCollectionModel(RecipeMockData.generateRandomMockData(size)).getContent());
    }

    public static List<RecipePreviewDto> generateRandomMockDataForRecipePreviewDto(int size) {
        RecipePreviewDtoAssembler assembler = new RecipePreviewDtoAssembler(new ModelMapper());


        return new ArrayList<>(assembler.toCollectionModel(RecipeMockData.generateRandomMockData(size)).getContent());
    }

}
