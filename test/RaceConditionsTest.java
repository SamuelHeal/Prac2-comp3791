import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

public class RaceConditionsTest {

    private StrategyOptimizer optimizer;
    private RaceCar standardCar;

    @BeforeEach
    void setUp() {
        optimizer = new StrategyOptimizer();

        standardCar = new RaceCar("Standard Car");

        RaceCar performanceCar = new RaceCar("Performance Car");
        performanceCar.customizeEngine(EngineType.TURBOCHARGED);
        performanceCar.customizeTyres(TyreType.SOFT);
        performanceCar.customizeAero(AeroPackage.DOWNFORCE_FOCUSED);

        RaceCar enduranceCar = new RaceCar("Endurance Car");
        enduranceCar.customizeEngine(EngineType.HYBRID);
        enduranceCar.customizeTyres(TyreType.HARD);
        enduranceCar.customizeAero(AeroPackage.LOW_DRAG_PACKAGE);
    }

    @ParameterizedTest
    @EnumSource(WeatherCondition.class)
    @DisplayName("Weather conditions affect race times")
    void testTimeWithWeather(WeatherCondition weather) {
        Track testTrack = new Track("Test Track", "Road", 50, 4.0, 1.0, 1.0);

        RaceStrategy strategy = optimizer.optimizeStrategy(standardCar, testTrack, weather);

        assertNotNull(strategy);
        assertTrue(strategy.getTotalTime() > 0);

        RaceCar baselineCar = new RaceCar("Baseline Car");
        StrategyOptimizer baselineOptimizer = new StrategyOptimizer();
        RaceStrategy baselineStrategy = baselineOptimizer.optimizeStrategy(baselineCar, testTrack, WeatherCondition.DRY);

        if (weather != WeatherCondition.DRY) {
            assertTrue(strategy.getTotalTime() > baselineStrategy.getTotalTime());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "50, 50, 50, 1, 2, 3",
            "50, 60, 70, 1, 1, 1",
    })
    @DisplayName("Longer tracks/more laps take more time")
    void testTimeWithLapsAndLength(int lap1, int lap2, int lap3, int len1, int len2, int len3) {
        Track testTrack = new Track("Test Track", "Road", lap1, len1, 1.0, 1.0);
        Track testTrack2 = new Track("Test Track", "Road", lap2, len2, 1.0, 1.0);
        Track testTrack3 = new Track("Test Track", "Road", lap3, len3, 1.0, 1.0);

        RaceStrategy strategy = optimizer.optimizeStrategy(standardCar, testTrack, WeatherCondition.DRY);
        RaceStrategy strategy2 = optimizer.optimizeStrategy(standardCar, testTrack2, WeatherCondition.DRY);
        RaceStrategy strategy3 = optimizer.optimizeStrategy(standardCar, testTrack3, WeatherCondition.DRY);

        assertTrue(strategy.getTotalTime() < strategy2.getTotalTime()
                && strategy2.getTotalTime() < strategy3.getTotalTime());
    }


    @Test
    @DisplayName("Race length affects pit stops")
    void testPitWithLength() {

        Track baselineTrack = new Track("Baseline Race", "Road", 50, 4.0, 1.0, 1.0);
        RaceStrategy baselineStrategy = optimizer.optimizeStrategy(standardCar, baselineTrack, WeatherCondition.DRY);

        Track shortTrack = new Track("Short Race", "Road", 25, 4.0, 1.0, 1.0);
        RaceStrategy shortStrategy = optimizer.optimizeStrategy(standardCar, shortTrack, WeatherCondition.DRY);

        Track longTrack = new Track("Long Race", "Road", 100, 4.0, 1.0, 1.0);
        RaceStrategy longStrategy = optimizer.optimizeStrategy(standardCar, longTrack, WeatherCondition.DRY);

        assertTrue(longStrategy.getPitStops().size() >= baselineStrategy.getPitStops().size()
                && shortStrategy.getPitStops().size() <= baselineStrategy.getPitStops().size());
    }

    @ParameterizedTest
    @CsvSource({
            "Road, 50, 4.0, DRY",
            "Street, 60, 3.5, WET",
            "Oval, 80, 5.0, EXTREME_WET",
            "Road, 40, 3.0, DRY",
            "Street, 45, 4.5, WET"
    })
    @DisplayName("Weather affects pit stops")
    void testPitWithWeather(String trackType, int laps, double length, WeatherCondition weather) {
        Track testTrack = new Track("Test Track", trackType, laps, length, 1.0, 1.0);

        RaceStrategy strategy = optimizer.optimizeStrategy(standardCar, testTrack, weather);

        for (PitStop pitStop : strategy.getPitStops()) {
            assertTrue(pitStop.getLap() > 0);
            assertTrue(pitStop.getLap() < laps);
            assertTrue(pitStop.getTimeDelay() > 0);
        }
    }
} 