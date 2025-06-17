
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class RaceStrategyTest {
   private RaceTeamManagement teamManagement;
   private RaceCar testCar;
   private Track streetTrack;
   private Track roadTrack;
   private Track ovalTrack;
   private StrategyOptimizer optimizer;

   @BeforeEach
   public void setUp() {
       teamManagement = new RaceTeamManagement();
       optimizer = new StrategyOptimizer();

       //Create test car
       testCar = new RaceCar("Lightning");

       //Create test tracks
       streetTrack = new Track("DaStreetz", "Street", 78, 3.337, 1.1, 1.3);
       roadTrack = new Track("HighWay420", "Road", 53, 5.793, 1.2, 0.8);
       ovalTrack = new Track("WeirdCircle", "Oval", 200, 4.023, 1.3, 1.1);
   }

   @DisplayName("Test Strategy in dry weather")
   @Test
   public void testDryWeather() {
       //Dry weather strategy with different car configurations
       testCar.customizeEngine(EngineType.TURBOCHARGED);
       testCar.customizeTyres(TyreType.SOFT);
       testCar.customizeAero(AeroPackage.DOWNFORCE_FOCUSED);

       RaceStrategy strategy = optimizer.optimizeStrategy(testCar, streetTrack, WeatherCondition.DRY);

       assertTrue(strategy.isValid());
       assertTrue(strategy.getTotalTime() > 0);
       assertTrue(strategy.getPitStops().size() > 0);
   }

   @DisplayName("Test strategy in wet weather")
   @Test
   public void testWetWeather() {
       //Wet weather strategy
       testCar.customizeEngine(EngineType.HYBRID);
       testCar.customizeTyres(TyreType.SOFT);
       testCar.customizeAero(AeroPackage.WET_WEATHER_PACKAGE);

       RaceStrategy strategy = optimizer.optimizeStrategy(testCar, roadTrack, WeatherCondition.WET);

       assertTrue(strategy.isValid());
       assertTrue(strategy.getTotalTime() > 0);

       //Check if strategy accounts for wet conditions
       List<PitStop> pitStops = strategy.getPitStops();
       for (PitStop stop : pitStops) {
           if (stop.isChangeTyres()) {
               assertEquals(
                       TyreType.SOFT, stop.getNewTyreType());
           }
       }
   }

   @DisplayName("Test speed on the track")
   @Test
   public void testSpeedTrack() {
       //Speed track strategy
       testCar.customizeEngine(EngineType.TURBOCHARGED);
       testCar.customizeTyres(TyreType.HARD);
       testCar.customizeAero(AeroPackage.LOW_DRAG_PACKAGE);

       RaceStrategy strategy = optimizer.optimizeStrategy(testCar, ovalTrack, WeatherCondition.DRY);

       assertTrue(strategy.isValid());
       assertTrue(strategy.getTotalTime() > 0);

       //Check if strategy accounts for oval track characteristics
       List<PitStop> pitStops = strategy.getPitStops();
       for (PitStop stop : pitStops) {
           if (stop.isChangeTyres()) {
               assertEquals(
                       TyreType.HARD, stop.getNewTyreType());
           }
       }
   }

   @DisplayName("Test Extremely wet weather")
   @Test
   public void testExtremeWet() {
       //Extreme wet weather strategy
       testCar.customizeEngine(EngineType.HYBRID);
       testCar.customizeTyres(TyreType.SOFT);
       testCar.customizeAero(AeroPackage.WET_WEATHER_PACKAGE);

       RaceStrategy strategy = optimizer.optimizeStrategy(testCar, streetTrack, WeatherCondition.EXTREME_WET);

       assertTrue(strategy.isValid());
       assertTrue(strategy.getTotalTime() > 0);

       double expectedTimeMultiplier = 1.2; //Expected increase in time in extreme weather
       assertTrue(
               strategy.getTotalTime() > streetTrack.getTotalLaps() * 90 * expectedTimeMultiplier);
   }

   @DisplayName("Test Fuel efficiency")
   @Test
   public void testFuelEfficiency() {
       //Fuel efficient strategy
       testCar.customizeEngine(EngineType.HYBRID);
       testCar.customizeTyres(TyreType.MEDIUM);
       testCar.customizeAero(AeroPackage.HYBRID_AERODYNAMICS);

       RaceStrategy strategy = optimizer.optimizeStrategy(testCar, roadTrack, WeatherCondition.DRY);

       assertTrue(strategy.isValid());
       assertTrue(strategy.getTotalTime() > 0);

       //Check for less pit stops due to better fuel efficiency
       List<PitStop> pitStops = strategy.getPitStops();
       assertTrue(
               pitStops.size() <= 2);
   }

   @DisplayName("Test consistent strategy")
   @Test
   public void testStrategyConsistency() {
       //strategy consistency across multiple runs
       testCar.customizeEngine(EngineType.STANDARD);
       testCar.customizeTyres(TyreType.MEDIUM);
       testCar.customizeAero(AeroPackage.STANDARD_AERODYNAMICS);

       RaceStrategy strategy1 = optimizer.optimizeStrategy(testCar, roadTrack, WeatherCondition.DRY);
       RaceStrategy strategy2 = optimizer.optimizeStrategy(testCar, roadTrack, WeatherCondition.DRY);

       //Check consistent results
       assertEquals(
               strategy1.getTotalTime(), strategy2.getTotalTime(), 0.1);
       assertEquals(
               strategy1.getPitStops().size(), strategy2.getPitStops().size());
   }

   @DisplayName("Test invalid conditions")
   @Test
   public void testInvalid() {
       //Invalid strategy
       testCar.customizeEngine(EngineType.TURBOCHARGED);
       testCar.customizeTyres(TyreType.SOFT);
       testCar.customizeAero(AeroPackage.LOW_DRAG_PACKAGE);

       //Make a new track with very high fuel consumption
       Track highConsumptionTrack = new Track("Test Track", "Road", 100, 5.0, 2.0, 2.0);
       RaceStrategy strategy = optimizer.optimizeStrategy(testCar, highConsumptionTrack, WeatherCondition.DRY);

       assertFalse(strategy.isValid());
   }

   @DisplayName("Test for Mixed track conditions")
   @Test
   public void testAdjustable() {
       //Strategy with Adjustable aerodynamics
       testCar.customizeEngine(EngineType.HYBRID);
       testCar.customizeTyres(TyreType.MEDIUM);
       testCar.customizeAero(AeroPackage.ADJUSTABLE_AERO);

       RaceStrategy strategy = optimizer.optimizeStrategy(testCar, roadTrack, WeatherCondition.DRY);

       assertTrue(strategy.isValid());
       assertTrue(strategy.getTotalTime() > 0);

       // Verify strategy accounts for mixed track conditions
       double expectedTime = roadTrack.getTotalLaps() * 90; // Base time
       assertTrue(
               strategy.getTotalTime() > expectedTime * 0.8 &&
                       strategy.getTotalTime() < expectedTime * 1.2);
   }

   @DisplayName("Test ground effect")
   @Test
   public void testGroundEffect() {
       //Strategy with Ground Effect aerodynamics
       testCar.customizeEngine(EngineType.TURBOCHARGED);
       testCar.customizeTyres(TyreType.MEDIUM);
       testCar.customizeAero(AeroPackage.GROUND_EFFECT);

       RaceStrategy strategy = optimizer.optimizeStrategy(testCar, roadTrack, WeatherCondition.DRY);

       assertTrue(strategy.isValid());
       assertTrue(strategy.getTotalTime() > 0);

       double expectedSpeed = 330;
       assertTrue(
               testCar.getOverallSpeed(roadTrack, WeatherCondition.DRY) >= expectedSpeed * 0.9);
   }

   @DisplayName("Test drag is accounted for")
   @Test
   public void testDrag() {
       //Strategy with Drag AeroPackage
       testCar.customizeEngine(EngineType.TURBOCHARGED);
       testCar.customizeTyres(TyreType.SOFT);
       testCar.customizeAero(AeroPackage.DRS_SYSTEM);

       RaceStrategy strategy = optimizer.optimizeStrategy(testCar, roadTrack, WeatherCondition.DRY);

       assertTrue(strategy.isValid());
       assertTrue(strategy.getTotalTime() > 0);

       //Check Drag
       double expectedTopSpeed = 345; // DRS system top speed
       assertTrue(
               testCar.getOverallSpeed(roadTrack, WeatherCondition.DRY) >= expectedTopSpeed * 0.9);
   }

   @DisplayName("Hybrid aerodynamics")
   @Test
   public void testHybrid() {
       //Strategy with Hybrid aerodynamics
       testCar.customizeEngine(EngineType.HYBRID);
       testCar.customizeTyres(TyreType.MEDIUM);
       testCar.customizeAero(AeroPackage.HYBRID_AERODYNAMICS);

       RaceStrategy strategy = optimizer.optimizeStrategy(testCar, roadTrack, WeatherCondition.DRY);

       assertTrue(strategy.isValid());
       assertTrue( strategy.getTotalTime() > 0);

       double speed = testCar.getOverallSpeed(roadTrack, WeatherCondition.DRY);
       double fuelEfficiency = testCar.getFuelConsumptionPerLap(roadTrack, WeatherCondition.DRY);
       double cornering = testCar.getCornering();

       //Check metrics
       assertTrue(
               speed >= 300 && speed <= 340);
       assertTrue(
               fuelEfficiency <= 2.0);
       assertTrue(
               cornering >= 5.0);
   }

   @Test
   @DisplayName("Pit stop time differences between fuel-only, tyres-only, and combined operations")
   void testPitStopTimes() {
       int testLap = 30;
       double fuelAmount = 60.0;

       PitStop fuelOnly = new PitStop(testLap, false, TyreType.MEDIUM, fuelAmount, 5.5);
       PitStop tyresOnly = new PitStop(testLap, true, TyreType.SOFT, 0.0, 5.5);
       PitStop fuelAndTyres = new PitStop(testLap, true, TyreType.SOFT, fuelAmount, 5.5);

       assertEquals(testLap, fuelOnly.getLap());
       assertEquals(fuelAmount, fuelOnly.getFuelAmount());
       assertFalse(fuelOnly.isChangeTyres());

       assertEquals(testLap, tyresOnly.getLap());
       assertEquals(0.0, tyresOnly.getFuelAmount());
       assertTrue(tyresOnly.isChangeTyres());

       assertEquals(testLap, fuelAndTyres.getLap());
       assertEquals(fuelAmount, fuelAndTyres.getFuelAmount());
       assertTrue(fuelAndTyres.isChangeTyres());

       assertTrue(fuelOnly.getTimeDelay() > 0);
       assertTrue(tyresOnly.getTimeDelay() > 0);
       assertTrue(fuelAndTyres.getTimeDelay() > 0);

       assertTrue(fuelOnly.getTimeDelay() < fuelAndTyres.getTimeDelay());
       assertTrue(tyresOnly.getTimeDelay() < fuelAndTyres.getTimeDelay());
   }

   @Test
   @DisplayName("Fuel management during race strategy")
   void testFuelManagement() {
       testCar.customizeEngine(EngineType.STANDARD);
       testCar.customizeTyres(TyreType.MEDIUM);
       testCar.customizeAero(AeroPackage.STANDARD_AERODYNAMICS);

       double maxCapacity = testCar.getMaxFuelCapacity();

       testCar.consumeFuel(50.0);
       assertTrue(testCar.getCurrentFuel() == maxCapacity - 50.0);

       testCar.refuel(30.0);
       assertTrue(testCar.getCurrentFuel() == maxCapacity - 20.0);
   }

   @Test
   @DisplayName("Fuel constraints during pit stops")
   void testFuelConstraints() {
       testCar.customizeEngine(EngineType.TURBOCHARGED);
       
       double maxCapacity = testCar.getMaxFuelCapacity();
       
       testCar.consumeFuel(200.0);
       assertTrue(testCar.getCurrentFuel() == 0.0);
       
       testCar.refuel(150.0);
       assertTrue(testCar.getCurrentFuel() == maxCapacity);
       
       testCar.consumeFuel(25.0);
       testCar.refuel(50.0);
       assertTrue(testCar.getCurrentFuel() == maxCapacity);
   }

   @Test
   @DisplayName("Fuel consumption affects race strategy")
   void testFuelStrategy() {
       testCar.customizeEngine(EngineType.HYBRID);
       testCar.customizeTyres(TyreType.MEDIUM);
       testCar.customizeAero(AeroPackage.LOW_DRAG_PACKAGE);
       
       double efficientFuelPerLap = testCar.getFuelConsumptionPerLap(roadTrack, WeatherCondition.DRY);
       RaceStrategy efficientStrategy = optimizer.optimizeStrategy(testCar, roadTrack, WeatherCondition.DRY);
       
       testCar.customizeEngine(EngineType.TURBOCHARGED);
       testCar.customizeTyres(TyreType.SOFT);
       testCar.customizeAero(AeroPackage.EXTREME_DOWNFORCE);
       
       double hungryFuelPerLap = testCar.getFuelConsumptionPerLap(roadTrack, WeatherCondition.DRY);
       RaceStrategy hungryStrategy = optimizer.optimizeStrategy(testCar, roadTrack, WeatherCondition.DRY);
       
       assertTrue(hungryFuelPerLap > efficientFuelPerLap);
       assertTrue(hungryStrategy.getPitStops().size() >= efficientStrategy.getPitStops().size());
   }

   @Test
   @DisplayName("Race strategy validity can be set")
   void testSetValid() {
       RaceStrategy strategy = new RaceStrategy();
       
       assertTrue(strategy.isValid());
       
       strategy.setValid(false);
       assertFalse(strategy.isValid());
       
       strategy.setValid(true);
       assertTrue(strategy.isValid());
   }
}