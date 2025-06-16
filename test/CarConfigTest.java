import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
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
    @DisplayName("Aero packages show performance trade-offs")
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
    @DisplayName("Engine upgrades improve performance")
    void testEngineUpgrades() {
        Engine engine = new Engine(EngineType.STANDARD);
        
        double initialSpeed = engine.getSpeedMultiplier();
        double initialFuelEff = engine.getFuelEfficiency();
        double initialAcceleration = engine.getEffectiveAcceleration();
        
        engine.upgrade();
        
        assertTrue(engine.getSpeedMultiplier() > initialSpeed);
        assertTrue(engine.getFuelEfficiency() > initialFuelEff);
        assertTrue(engine.getEffectiveAcceleration() < initialAcceleration);
    }
    
    @Test
    @DisplayName("Aero upgrades improve performance")
    void testAeroUpgrades() {
        AerodynamicKit aero = new AerodynamicKit(AeroPackage.STANDARD_AERODYNAMICS);
        
        double initialDrag = aero.getEffectiveDragCoefficient();
        int initialDownforce = aero.getEffectiveDownforce();
        int initialTopSpeed = aero.getEffectiveTopSpeed();
        double initialFuelEff = aero.getEffectiveFuelEfficiency();
        int initialCornering = aero.getEffectiveCorneringAbility();
        
        aero.upgrade();
        
        assertTrue(aero.getEffectiveDragCoefficient() < initialDrag);
        assertTrue(aero.getEffectiveDownforce() > initialDownforce);
        assertTrue(aero.getEffectiveTopSpeed() > initialTopSpeed);
        assertTrue(aero.getEffectiveFuelEfficiency() > initialFuelEff);
        assertTrue(aero.getEffectiveCorneringAbility() >= initialCornering);
    }
    
    @Test
    @DisplayName("Multiple upgrades show progressive improvement")
    void testProgressiveUpgrades() {
        Engine engine = new Engine(EngineType.STANDARD);
        AerodynamicKit aero = new AerodynamicKit(AeroPackage.STANDARD_AERODYNAMICS);
        
        double speed1 = engine.getSpeedMultiplier();
        double drag1 = aero.getEffectiveDragCoefficient();
        
        engine.upgrade();
        aero.upgrade();
        
        double speed2 = engine.getSpeedMultiplier();
        double drag2 = aero.getEffectiveDragCoefficient();
        
        engine.upgrade();
        aero.upgrade();
        
        double speed3 = engine.getSpeedMultiplier();
        double drag3 = aero.getEffectiveDragCoefficient();
        
        assertTrue(speed1 < speed2);
        assertTrue(speed2 < speed3);
        assertTrue(drag1 > drag2);
        assertTrue(drag2 > drag3);
    }
}

