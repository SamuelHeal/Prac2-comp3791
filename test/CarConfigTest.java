import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class CarConfigTest {
    
    private RaceCar testCar;
    
    @BeforeEach
    void setUp() {
        testCar = new RaceCar("Test Car");
    }
    
    @Test
    @DisplayName("Default car config is valid")
    void testStandard() {
        assertEquals(EngineType.STANDARD, testCar.getEngine().getType());
        assertEquals(TyreType.MEDIUM, testCar.getTyres().getType());
        assertEquals(AeroPackage.STANDARD_AERODYNAMICS, testCar.getAeroKit().getPackage());
    }
    
    @ParameterizedTest
    @EnumSource(EngineType.class)
    @DisplayName("All engine types can be configured")
    void testAllEngines(EngineType engineType) {
        testCar.customizeEngine(engineType);
        assertEquals(engineType, testCar.getEngine().getType());
    }
    
    @ParameterizedTest
    @EnumSource(TyreType.class)
    @DisplayName("All tyre types can be configured")
    void testAllTyres(TyreType tyreType) {
        testCar.customizeTyres(tyreType);
        assertEquals(tyreType, testCar.getTyres().getType());
    }
    
    @ParameterizedTest
    @EnumSource(AeroPackage.class)
    @DisplayName("All aero packages can be configured")
    void testAllAero(AeroPackage aeroPackage) {
        testCar.customizeAero(aeroPackage);
        assertEquals(aeroPackage, testCar.getAeroKit().getPackage());
    }
    
    
    @ParameterizedTest
    @CsvSource({
        "EXTREME_DOWNFORCE, 0.55, 270, 10",
        "LOW_DRAG_PACKAGE, 0.25, 350, 3",
        "DOWNFORCE_FOCUSED, 0.45, 290, 9",
        "DRS_SYSTEM, 0.28, 345, 4",
        "WET_WEATHER_PACKAGE, 0.38, 310, 7",
        "HYBRID_AERODYNAMICS, 0.31, 330, 6",
        "GROUND_EFFECT, 0.32, 330, 8",
        "ADJUSTABLE_AERO, 0.33, 325, 6",
        "STANDARD_AERODYNAMICS, 0.35, 320, 5"
    })
    @DisplayName("Aero packages have metrics as defined in enum")
    void testAeroPackageCharacteristics(AeroPackage aeroPackage, double expectedDrag, int expectedTopSpeed, int expectedCornering) {
        testCar.customizeAero(aeroPackage);
        AeroPackage pkg = testCar.getAeroKit().getPackage();
        
        assertEquals(expectedDrag, pkg.dragCoefficient);
        assertEquals(expectedTopSpeed, pkg.topSpeed);
        assertEquals(expectedCornering, pkg.corneringAbility);
    }
    
    @ParameterizedTest
    @CsvSource({
        "TURBOCHARGED, 0.8, 1.3",
        "HYBRID, 1.2, 1.1", 
        "STANDARD, 1.0, 1.0"
    })
    @DisplayName("Engines have metrics as defined in enum")
    void testEngineCharacteristics(EngineType engineType, double expectedFuelEff, double expectedSpeedMult) {
        testCar.customizeEngine(engineType);
        
        assertEquals(expectedFuelEff, testCar.getEngine().getFuelEfficiency());
        assertEquals(expectedSpeedMult, testCar.getEngine().getSpeedMultiplier());
    }
    
    @ParameterizedTest
    @CsvSource({
        "SOFT, 1.2, 0.7, 25",
        "MEDIUM, 1.0, 1.0, 40",
        "HARD, 0.9, 1.3, 60"
    })
    @DisplayName("Tyres have metrics as defined in enum")
    void testTyreCharacteristics(TyreType tyreType, double gripMult, double durabilityMult, int maxLaps) {
        testCar.customizeTyres(tyreType);
        TyreType type = testCar.getTyres().getType();
        
        assertEquals(gripMult, type.gripMultiplier);
        assertEquals(durabilityMult, type.durabilityMultiplier);
        assertEquals(maxLaps, type.maxLaps);
    }

    @ParameterizedTest
    @CsvSource({
        "EXTREME_DOWNFORCE, LOW_DRAG_PACKAGE",
        "DOWNFORCE_FOCUSED, DRS_SYSTEM",
        "WET_WEATHER_PACKAGE, LOW_DRAG_PACKAGE"
    })
    @DisplayName("Conflicting aero packages show performance trade-offs")
    void testAeroConflicts(AeroPackage highDownforce, AeroPackage lowDrag) {
        testCar.customizeAero(highDownforce);
        int highDownforceCornering = testCar.getAeroKit().getPackage().corneringAbility;
        int highDownforceSpeed = testCar.getAeroKit().getPackage().topSpeed;
        
        testCar.customizeAero(lowDrag);
        int lowDragCornering = testCar.getAeroKit().getPackage().corneringAbility;
        int lowDragSpeed = testCar.getAeroKit().getPackage().topSpeed;
        
        assertTrue(lowDragSpeed > highDownforceSpeed);
        assertTrue(highDownforceCornering > lowDragCornering);
    }
    
    @Test
    @DisplayName("Refuel constraints")
    void testRefuelConstraints() {
        double maxCapacity = testCar.getMaxFuelCapacity();
        
        testCar.refuel(200.0);
        assertEquals(maxCapacity, testCar.getCurrentFuel());
        
        testCar.consumeFuel(50.0);
        assertEquals(maxCapacity - 50.0, testCar.getCurrentFuel());
        
        testCar.consumeFuel(200.0);
        assertEquals(0.0, testCar.getCurrentFuel());
    }
    
    @ParameterizedTest
    @CsvSource({
        "HYBRID, LOW_DRAG_PACKAGE, <fuel",
        "TURBOCHARGED, EXTREME_DOWNFORCE, >fuel",
        "STANDARD, STANDARD_AERODYNAMICS, =fuel"
    })
    @DisplayName("Engine and aero combinations affect fuel consumption")
    void testEngineAeroFuelConsumption(EngineType engine, AeroPackage aero, String fuelComparison) {
        Track testTrack = new Track("Test Track", "Road", 50, 4.0, 1.0, 1.0);
        
        testCar.customizeEngine(engine);
        testCar.customizeAero(aero);
        
        double fuelConsumption = testCar.getFuelConsumptionPerLap(testTrack, WeatherCondition.DRY);
        assertTrue(fuelConsumption > 0);
        
        // Compare different engine/aero combinations using data-driven approach
        RaceCar standardCar = new RaceCar("Standard Comparison");
        double standardConsumption = standardCar.getFuelConsumptionPerLap(testTrack, WeatherCondition.DRY);

        switch (fuelComparison) {
            case ">fuel":
                assertTrue(fuelConsumption >= standardConsumption);
                break;
            case "<fuel":
                assertTrue(fuelConsumption <= standardConsumption);
                break;
            case "=fuel":
                assertEquals(fuelConsumption, standardConsumption);
                break;
            default:
                fail();
        }
    }
}

