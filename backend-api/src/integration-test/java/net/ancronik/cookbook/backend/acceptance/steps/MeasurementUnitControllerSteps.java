package net.ancronik.cookbook.backend.acceptance.steps;

import io.cucumber.java.en.And;
import net.ancronik.cookbook.backend.acceptance.AbstractSteps;

public class MeasurementUnitControllerSteps extends AbstractSteps {

    @And("^the client receives valid measurement units$")
    public void checkReceivedMeasurementUnits(){
        String body = latestResponse.getBody();

    }
}
