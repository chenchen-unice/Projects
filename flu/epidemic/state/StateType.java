package flu.epidemic.state;

public enum StateType {
    HEALTHY("healthy"),
    INFECTED("infected"),
    SICK("sick"),
    CONTAGIOUS("contagious"),
    RECOVERING("recovering"),
    DEAD("dead");
    
    private String state = "";
    
    StateType(String state){
    this.state = state;
    }
    
    public String toString(){
        return this.state;
    }
    
    public boolean isEquals(StateType state) { 
        return this.state.equalsIgnoreCase(state.toString()); 
        }
}
