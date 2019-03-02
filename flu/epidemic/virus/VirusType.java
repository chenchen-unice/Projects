package flu.epidemic.virus;

public enum VirusType {
    H1N1("H1N1"),
    H5N1("H5N1");
    
    private String name = "";
    
    
    VirusType(String name) {
       this.name = name;
    }

    public String toString() {
        return this.name;
    }
     
}
