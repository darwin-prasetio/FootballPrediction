import javax.xml.bind.ValidationEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Pavilion on 03/03/2016.
 * This class is the estimation of coefficients using Newton's Method
 */
public class Logistic {

    private List<Double> coefficients;
    private List<Double> matchResults; //y
    private List<List<Double>> variables; //X
    private List<Double> probabilities; //p
    private List<List<Double>> likelihoods; //W = p(1-p), NxN matrix

    public void setVariables(List<List<Double>> matrix) {
        variables=matrix;
    }

    public List<List<Double>> getVariables() {
        return variables;
    }

    public Logistic() {
        coefficients = new ArrayList<Double>(5);
        matchResults = new ArrayList<>();
        variables = new ArrayList<List<Double>>();
        likelihoods = new ArrayList<List<Double>>();
        probabilities = new ArrayList<>();
    }

    public List<Double> getCoefficients() {
        return coefficients;
    }

    public void transposeMatrix(List<List<Double>> matrix) {
        for(int i=0;i<matrix.size();++i) {
            for(int j=i+1;j<matrix.size();++j) {
                Double temp = matrix.get(i).get(j);
                matrix.get(i).set(j,matrix.get(j).get(i));
                matrix.get(j).set(i,temp);
            }
        }
    }

    public void setCoefficients(Vector<Double> coefficients) {
        this.coefficients = coefficients;
    }

    public void estimateCoefficients(Vector<String> instances) {
        setInitialCoefficients();
        parseInstances(instances);
    }

    private void parseInstances(Vector<String> instances) {
        int i=0;
        for(String instance:instances) {
            String[] attributes = instance.split(";");

            //adding match results to matchResults Vector, ignore draw results
            if(attributes[6].equals("H")) {
                matchResults.add(1.0);
            }
            else if(attributes[6].equals("A")) {
                matchResults.add(0.0);
            }

            //adding team strength to variables
            List<Double> teamStrength = new ArrayList<>();
            teamStrength.add(Double.valueOf(attributes[22])); //home offense
            teamStrength.add(Double.valueOf(attributes[23])); //home defense
            teamStrength.add(Double.valueOf(attributes[24])); //away offense
            teamStrength.add(Double.valueOf(attributes[25])); //away defense
            variables.add(teamStrength);

            //calculate probability and add to vector probability
            Double probability = calculateProbability(coefficients,Double.valueOf(attributes[22]),Double.valueOf(attributes[23])
                                                        ,Double.valueOf(attributes[24]),Double.valueOf(attributes[25]));
            probabilities.add(probability);


            Double likelihood = calculateLikelihood(probability);
            List<Double> rowOfLikelihood= new ArrayList<>(instances.size());
            rowOfLikelihood.set(i,likelihood);
            likelihoods.add(rowOfLikelihood);
            i++;
        }
    }

    private Double calculateLikelihood(Double probability) {
        return probability*(1-probability);
    }

    private Double calculateProbability(List<Double> coefficients, Double homeOffense, Double homeDefense, Double awayOffense, Double awayDefense) {
        return 1/(1+(1/Math.exp(coefficients.get(0)+coefficients.get(1)*homeOffense+coefficients.get(2)*homeDefense+
                        coefficients.get(3)*awayOffense+coefficients.get(4)*awayDefense)));
    }

    private void setInitialCoefficients() {
        for(int i=0;i<coefficients.size();++i) {
            coefficients.add(0.0);
        }
    }
}
