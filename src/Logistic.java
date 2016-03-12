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
        newtonRaphson();
    }

    private void newtonRaphson() {

    }

    public List<List<Double>> matrixMultiplication(List<List<Double>> A, List<List<Double>> B) {
        Double[][] result = new Double[A.size()][B.get(0).size()];
        Double sum=0.0;
        for(int c=0;c<A.size();c++) {
            for(int d=0;d<B.get(0).size();d++) {
                for(int k=0;k<B.size();k++) {
                    sum += A.get(c).get(k) * B.get(k).get(d);
                }
                result[c][d]=sum;
                sum=0.0;
            }
        }

        List<List<Double>> resultMatrix = new ArrayList<>();

        for(int j=0;j<A.size();j++) {
            List<Double> resultRow = new ArrayList<>();
            for (int i = 0; i < B.get(0).size(); i++) {
                resultRow.add(result[j][i]);
            }
            resultMatrix.add(resultRow);
        }
        return resultMatrix;
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
            for(int j=0;j<5;j++) {
                rowOfLikelihood.add(0.0);
            }
            System.out.println(rowOfLikelihood.get(0));
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
        for(int i=0;i<5;++i) {
            coefficients.add(0.0);
        }
    }
}
