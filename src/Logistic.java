import java.util.Vector;

/**
 * Created by Pavilion on 03/03/2016.
 * This class is the calculation of coefficients
 */
public class Logistic {

    private Vector<Double> coefficients;

    public Logistic() {
    }

    public Vector<Double> getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(Vector<Double> coefficients) {
        this.coefficients = coefficients;
    }
}
