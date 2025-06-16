import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Edgecases {
    AeroPackage aeroPackage;
    AerodynamicKit aerodynamicKit;

    Engine engine;
    EngineType engineType;

    PitStop pitstop;
    TyreType tyreType;
    Tyres tyres;

    Track testTrack;

    RaceCar raceCar;

    RaceStrategy raceStrategy;

    @Nested
    @DisplayName("Aeropackage and AerodynamicKit")
    class AeroPackageAndAerodynamicKitTest {
        @BeforeEach
        void setUp() {
            aeroPackage = AeroPackage.values()[0];
            aerodynamicKit = new AerodynamicKit(aeroPackage);
        }

        @DisplayName("Test the upgrades don't go above 3")
        @Test
        public void testAerodynamickitMaxUpgradeLevel() {
            aerodynamicKit.upgrade(); //2
            assertEquals(2, aerodynamicKit.getUpgradeLevel());
            aerodynamicKit.upgrade(); //3
            assertEquals(3, aerodynamicKit.getUpgradeLevel());
            aerodynamicKit.upgrade(); //stay at 3
            assertEquals(3, aerodynamicKit.getUpgradeLevel());
        }

        @DisplayName("Test getDisplayName")
        @Test
        public void testGetDisplayName() {
            assertEquals("STANDARD_AERODYNAMICS",aeroPackage.getDisplayName());
        }

        @DisplayName("Test values for standard Aerodynamics")
        @Test
        public void testStandardAerodynamics() {
            assertAll(
                    () -> assertEquals(0.35, aerodynamicKit.getEffectiveDragCoefficient()),
                    () -> assertEquals(150, aerodynamicKit.getEffectiveDownforce()),
                    () -> assertEquals(320,aerodynamicKit.getEffectiveTopSpeed()),
                    () -> assertEquals(12.0,aerodynamicKit.getEffectiveFuelEfficiency()),
                    () -> assertEquals(5,aerodynamicKit.getEffectiveCorneringAbility())
            );
        }

    }

    @Nested
    @DisplayName("Tyres")
    class TyreTest {
        @BeforeEach
        void setUp() {
            tyreType = TyreType.values()[0];
            tyres = new Tyres(tyreType);
        }

        @DisplayName("Test default cornering multiplier")
        @Test
        void testTyreDefaultCorneringMultiplier() {
            assertEquals(1.2,tyreType.corneringMultiplier,0.001);
        }

        @DisplayName("Test wear cant be negative")
        @Test
        void testTyreWearCantBeNegative() {
            tyres.addWear(-100.0);
            assertEquals(0,tyres.getCurrentWear(),0.001);
        }

        @DisplayName("Test wear cant be negative")
        @Test
        void testTyreWearCantBeTooHigh() {
            tyres.addWear(10000.0);
            assertEquals(100,tyres.getCurrentWear(),0.001);
        }

        @DisplayName("Test wear cant be negative")
        @Test
        void testTyreGripMultiplierCantBeNegative() {
            tyres.addWear(100000.0);
            assertEquals(0,tyres.getGripMultiplier(),0.001);
        }


    }



    @Nested
    @DisplayName("Pitstop")
    class PitstopTest {
        @DisplayName("Test pitstop values can't be extreme/unrealistic")
        @Test
        void testPitStopHighExtremeValues(){
            assertThrows(IllegalArgumentException.class, () ->new PitStop(1000,false,tyreType,300,400));

        }

        @DisplayName("Test pitstop no negative values")
        @Test
        void testPitStopNoNegativeValues(){
            assertThrows(IllegalArgumentException.class, () -> new PitStop(-1,false,tyreType,-100,-1));
        }

        @DisplayName("Test pitstop constructor")
        @Test
        void testPitTopContructor(){
            pitstop = new PitStop(57,false,tyreType,100,4);
            assertAll(
                    () -> assertEquals(57,pitstop.getLap()),
                    () -> assertEquals(false,pitstop.isChangeTyres()),
                    () -> assertEquals(tyreType,pitstop.getNewTyreType()),
                    () -> assertEquals(100,pitstop.getFuelAmount()),
                    () -> assertEquals(4,pitstop.getLap())
            );
        }


    }

    @Nested
    @DisplayName("Engine")
    class EngineTest {
        @BeforeEach
        void setUp() {
            engineType = EngineType.values()[0];
            engine = new Engine(engineType);
        }

        @DisplayName("Test 5 as max upgrade level")
        @Test
        void testEngineMaxUpgradeLevel() {
            engine.upgrade();
            assertEquals(2, engine.getUpgradeLevel());
            engine.upgrade();
            assertEquals(3, engine.getUpgradeLevel());
            engine.upgrade();
            assertEquals(4, engine.getUpgradeLevel());
            engine.upgrade();
            assertEquals(5, engine.getUpgradeLevel());
            engine.upgrade();
            assertEquals(5, engine.getUpgradeLevel());
        }

        @DisplayName("Test engine class getters")
        @Test
        void testEngineGetters() {
            int upgradeLevel = engine.getUpgradeLevel();
            assertAll(
                    () -> assertEquals(engineType.speedMultiplier* (1 + (upgradeLevel - 1) * 0.1),engine.getSpeedMultiplier()),
                    () -> assertEquals(engineType.fuelEfficiency* (1 + (upgradeLevel - 1) * 0.1),engine.getFuelEfficiency()),
                    () -> assertEquals(engineType.acceleration,engine.getAcceleration()),
                    () -> assertEquals(engineType.acceleration* (1 - 0.03 * (upgradeLevel - 1)),engine.getEffectiveAcceleration())
            );
        }

    }

    @Nested
    @DisplayName("Track Tests")
    class TrackTest {
        @BeforeEach
        void setUp() {
            testTrack = new Track("test", "testtype",40,5.67,100,4);
        }

        @DisplayName("Test nonstandard type")
        @Test
        void testTrackNonStandardType(){
            assertThrows(IllegalArgumentException.class, () -> new Track("test", "testtype",-1,-1,-1,-1));
        }

        @DisplayName("Test precision values are the same when retrived")
        @Test
        void precisionValuesForTrackLength(){
            testTrack = new Track("test", "testtype",10,5.123456789,-1,-1);
            assertEquals(5.123456789,testTrack.getLapLength());
        }

        @DisplayName("Test that negative values cant be used in the constructor")
        @Test
        void invalidNegativeValues(){
            assertThrows(IllegalArgumentException.class, () -> new Track("test", "testtype",-1,-1,-1,-1));
        }

        @DisplayName("Test constructor values ")
        @Test
        void testTrackConstructorValues() {
            assertAll(
                    () -> assertEquals(40,testTrack.getTotalLaps()),
                    () -> assertEquals(5.67,testTrack.getLapLength()),
                    () -> assertEquals(100, testTrack.getFuelMultiplier()),
            () -> assertEquals(4,testTrack.getTyreWearMultiplier())
            );
        }
    }

    @Nested
    @DisplayName("RaceStrategy Tests")
    class raceStrategyTest {
        PitStop pitStop1;
        PitStop pitStop2;
        PitStop pitStop3;

        @BeforeEach
        void setUp() {
            raceStrategy = new RaceStrategy();
            pitStop1 = new PitStop(5,false,tyreType,100,2);
            pitStop2 = new PitStop(5,true,tyreType,100,2);

            raceStrategy.addPitStop(pitStop1);
            raceStrategy.addPitStop(pitStop2);

        }

        @DisplayName("Test race strategy constructor")
        @Test
        void testRaceStrategyConstructor() {
            assertAll(
                    () ->  assertEquals(0.0, raceStrategy.getTotalTime()),
                    () ->  assertTrue( raceStrategy.isValid())
            );
        }

        @DisplayName("Test negative values throw IllegalArg exp")
        @Test
        void testNegativeThrowsException(){
            assertThrows(IllegalArgumentException.class, ()-> new PitStop(-5,false,tyreType,100,2));
        }

        @DisplayName("Test negative values throw IllegalArg exp")
        @Test
        void testRaceStrategyTotalTimeCantBeNegative() {
            assertThrows(IllegalArgumentException.class, ()-> raceStrategy.setTotalTime(-1));
        }

        @DisplayName("Test pitstop getters are the same as given")
        @Test
        void testRaceStrategyPitStop() {
            assertAll(
                    () -> assertEquals(pitStop1,raceStrategy.getPitStops().get(0)),
                    () -> assertEquals(pitStop2,raceStrategy.getPitStops().get(1)),
                    () -> assertEquals(pitStop3, raceStrategy.getPitStops().get(2))
            );
        }

        @DisplayName("Test null values throw IllegalArg exp")
        @Test
        void testNullValues(){
            assertThrows(NullPointerException.class, () -> raceStrategy.addPitStop(null));
        }
    }


    @Nested
    @DisplayName("RaceCar tests")
    class RaceCarTest {

        @BeforeEach
        void setUp() {
            raceCar = new RaceCar("test car");
        }

        @DisplayName("Test constructor")
        @Test
        void raceCarTestConstructor() {
            assertAll(
                    () -> assertEquals(EngineType.STANDARD,raceCar.getEngine().getType()),
                    () -> assertEquals(TyreType.MEDIUM,raceCar.getTyres().getType()),
                    () -> assertEquals(AeroPackage.STANDARD_AERODYNAMICS,raceCar.getAeroKit().getPackage()),
                    () -> assertEquals(100, raceCar.getMaxFuelCapacity())
            );
        }


    }


    @Nested
    @DisplayName("Test Strategy Optimizer")
    class StrategyOptimizerTest {

        @DisplayName("Test  null values throw Illegal arg exception")
        @Test
        void testStrategyOptimizerNullValues() {
            assertThrows(IllegalArgumentException.class, () -> new StrategyOptimizer().optimizeStrategy(null, null, null));
        }


    }

}