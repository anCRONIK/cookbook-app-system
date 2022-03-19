package net.ancronik.cookbook.backend.acceptance;


import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.TestTypes;
import org.junit.jupiter.api.Tag;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("net.ancronik.cookbook.backend")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "net.ancronik.cookbook.backend")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@Tag(TestTypes.ACCEPTANCE)
public class AcceptanceTestRunner {

}