package net.ancronik.cookbook.backend.web.dto;

import net.ancronik.cookbook.backend.data.model.RecipeMockData;
import net.ancronik.cookbook.backend.domain.assembler.RecipeDtoAssembler;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class RecipeDtoMockData {


    public static List<RecipeDto> generateRandomMockData(int size) {
        RecipeDtoAssembler assembler = new RecipeDtoAssembler(new ModelMapper());


        return new ArrayList<>(assembler.toCollectionModel(RecipeMockData.generateRandomMockData(size)).getContent());
    }

}
