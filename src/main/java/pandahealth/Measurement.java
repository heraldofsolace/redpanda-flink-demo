package pandahealth;

import java.util.Objects;

public class Measurement {
    private String ID;
    private String type;
    private double weight;

    public Measurement() {}

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        this.ID = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String itemType) {
        this.type = itemType;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double itemWeight) {
        this.weight = itemWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Measurement that = (Measurement) o;
        return ID == that.ID
                && type.equals(that.type)
                && Double.compare(that.weight, weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, type, weight);
    }

    @Override
    public String toString() {
        return "Measurement{"
                + "ID="
                + ID
                + ", type="
                + type
                + ", weight="
                + weight
                + '}';
    }
}
