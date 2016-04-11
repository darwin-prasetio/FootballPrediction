package Prediction;

/**
 * Created by Pavilion on 11/04/2016.
 */
public class Team {
    private String name;
    private Double offense;
    private Double defense;

    public Team(String name, Double offense, Double defense) {
        this.name = name;
        this.offense = offense;
        this.defense = defense;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getOffense() {
        return offense;
    }

    public void setOffense(Double offense) {
        this.offense = offense;
    }

    public Double getDefense() {
        return defense;
    }

    public void setDefense(Double defense) {
        this.defense = defense;
    }
}
