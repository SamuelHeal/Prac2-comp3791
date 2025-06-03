class RaceStrategy {
    private List<PitStop> pitStops;
    private double totalTime;
    private boolean isValid;
    
    public RaceStrategy() {
        this.pitStops = new ArrayList<>();
        this.totalTime = 0.0;
        this.isValid = true;
    }
    
    public void addPitStop(PitStop stop) {
        pitStops.add(stop);
    }
    
    public List<PitStop> getPitStops() { return pitStops; }
    public double getTotalTime() { return totalTime; }
    public void setTotalTime(double time) { this.totalTime = time; }
    public boolean isValid() { return isValid; }
    public void setValid(boolean valid) { this.isValid = valid; }
}