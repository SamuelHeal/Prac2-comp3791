// Track Class
class Track {
    private String name;
    private String type;
    private int totalLaps;
    private double lapLength;
    private double fuelMultiplier;
    private double tyreWearMultiplier;
    
    public Track(String name, String type, int laps, double length, double fuel, double wear) {
        this.name = name;
        this.type = type;
        this.totalLaps = laps;
        this.lapLength = length;
        this.fuelMultiplier = fuel;
        this.tyreWearMultiplier = wear;
    }
    
    public String getName() { return name; }
    public String getType() { return type; }
    public int getTotalLaps() { return totalLaps; }
    public double getLapLength() { return lapLength; }
    public double getFuelMultiplier() { return fuelMultiplier; }
    public double getTyreWearMultiplier() { return tyreWearMultiplier; }
}
