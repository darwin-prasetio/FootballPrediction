import javax.xml.bind.ValidationEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Pavilion on 03/03/2016.
 * This class is the estimation of coefficients using Newton's Method
 */
public class Logistic {

    //all is in matrix to simpify calculation
    private List<List<Double>> coefficients; //beta values, Nx1 matrix
    private List<List<Double>> matchResults; //y, Nx1 matrix
    private List<List<Double>> variables; //X, Nx(p+1) matrix
    private List<List<Double>> probabilities; //p, Nx1 matrix
    private List<List<Double>> likelihoods; //W = p(1-p), NxN matrix

    public void setVariables(List<List<Double>> matrix) {
        variables=matrix;
    }

    public List<List<Double>> getVariables() {
        return variables;
    }

    public Logistic() {
        coefficients = new ArrayList<>(5);
        matchResults = new ArrayList<>();
        variables = new ArrayList<List<Double>>();
        likelihoods = new ArrayList<List<Double>>();
        probabilities = new ArrayList<>();
    }

    public List<List<Double>> getCoefficients() {
        return coefficients;
    }

    public List<List<Double>> transposeMatrix(List<List<Double>> matrix) {
        Double[][] tempResult = new Double[matrix.get(0).size()][matrix.size()];
        for(int i=0;i<matrix.size();i++) {
            for(int j=0;j<matrix.get(0).size();j++) {
                tempResult[j][i] = matrix.get(i).get(j);
            }
        }

        List<List<Double>> result = new ArrayList<>();
        for(int i=0;i<matrix.get(0).size();i++) {
            List<Double> row = new ArrayList<>();
            for(int j=0;j<matrix.size();j++) {
                row.add(tempResult[i][j]);
            }
            result.add(row);
        }
        return result;
    }

    public void setCoefficients(List<List<Double>> coefficients) {
        this.coefficients = coefficients;
    }

    public List<List<Double>> inverse (List<List<Double>> matrix) {
        List<List<Double>> result = multiplyByConstant(transposeMatrix(cofactor(matrix)), (1.0 / determinant(matrix)));
            return result;
    }

    private List<List<Double>> multiplyByConstant(List<List<Double>> lists, double v) {
        List<List<Double>> result = new ArrayList<>();
        for(int i=0;i<lists.size();i++) {
            List<Double> row = new ArrayList<>();
            for(int j=0;j<lists.get(0).size();j++) {
                row.add(lists.get(i).get(j)*v);
            }
            result.add(row);
        }
        return result;
    }

    public void estimateCoefficients(Vector<String> instances) {
        setInitialCoefficients();
        parseInstances(instances);
        newtonRaphson();
    }

    private void newtonRaphson() {
        for(int i=0;i<matchResults.size();i++) { //iteration as much as data
            System.out.println("iteration:" + i);
            calculateP(coefficients,variables);
            calculateW(probabilities);
            coefficients = matrixAddition(coefficients, matrixMultiplication(matrixMultiplication(inverse(matrixMultiplication(transposeMatrix(variables), matrixMultiplication(likelihoods, variables))), transposeMatrix(variables)), matrixSubtraction(matchResults, probabilities)));
        }
    }

    public List<List<Double>> matrixAddition(List<List<Double>> coefficients, List<List<Double>> matrix) {
        List<List<Double>> newCoefficients = new ArrayList<>();
        for(int i=0;i<matrix.size();i++) {
            List<Double> row = new ArrayList<>(1);
            row.add(coefficients.get(i).get(0)+matrix.get(i).get(0));
            newCoefficients.add(row);
        }
        return newCoefficients;
    }

    private void calculateP(List<List<Double>> coefficients, List<List<Double>> variables) {
        probabilities = new ArrayList<>();
        for(int i=0;i<variables.size();i++) {
            List<Double> probability = new ArrayList<>();
            probability.add(calculateProbability(coefficients,variables.get(i).get(0),variables.get(i).get(1),variables.get(i).get(2),variables.get(i).get(3),variables.get(i).get(4)));
            probabilities.add(probability);
        }
    }

    private void calculateW(List<List<Double>> probabilities) {
        likelihoods = new ArrayList<>();
        for(int i=0;i<probabilities.size();i++) {
            Double likelihood = calculateLikelihood(probabilities.get(i));
            List<Double> row = new ArrayList<>(probabilities.size());
            for(int j=0;j<probabilities.size();j++) {
                row.add(0.0);
            }
            row.set(i,likelihood);
            likelihoods.add(row);
        }
    }

    public List<List<Double>> matrixSubtraction(List<List<Double>> matchResults, List<List<Double>> probabilities) {
        List<List<Double>> result = new ArrayList<>();
        for(int i=0;i<matchResults.size();i++) {
            List<Double> row = new ArrayList<>();
            row.add(matchResults.get(i).get(0) - probabilities.get(i).get(0));
            result.add(row);
        }
        return result;
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
//        int i=0;
        for(String instance:instances) {
            String[] attributes = instance.split(",");
            List<Double> matchResult = new ArrayList<>();
            //adding match results to matchResults Vector, ignore draw results
            if(attributes[6].equals("H")) {
                matchResult.add(1.0);
            }
            else if(attributes[6].equals("A")) {
                matchResult.add(0.0);
            }
            matchResults.add(matchResult);

            //adding team strength to variables
            List<Double> variable = new ArrayList<>();
            variable.add(1.0); //intercept assumption
            variable.add(Double.valueOf(attributes[23])); //home offense
            variable.add(Double.valueOf(attributes[24])); //home defense
            variable.add(Double.valueOf(attributes[25])); //away offense
            variable.add(Double.valueOf(attributes[26])); //away defense
            variables.add(variable);
        }
    }

    private Double calculateLikelihood(List<Double> probability) {
        return probability.get(0)*(1-probability.get(0));
    }

    private Double calculateProbability(List<List<Double>> coefficients,Double interceptConstant, Double homeOffense, Double homeDefense, Double awayOffense, Double awayDefense) {
        Double sum = -1.0*(coefficients.get(0).get(0)*interceptConstant + coefficients.get(1).get(0)*homeOffense + coefficients.get(2).get(0)*homeDefense +
                coefficients.get(3).get(0)*awayOffense + coefficients.get(4).get(0)*awayDefense);
        return 1/(1+Math.exp(sum));
    }

    private void setInitialCoefficients() {
        for(int i=0;i<5;++i) {
            List<Double> coefficient = new ArrayList<>();
            coefficient.add(0.0);
            coefficients.add(coefficient);
        }
    }

    private Double determinant(List<List<Double>> matrix) {
        if(matrix.size()==2) {
            return ((matrix.get(0).get(0) * matrix.get(1).get(1)) - (matrix.get(0).get(1) * matrix.get(1).get(0)));
        }
        Double sum = 0.0;
        for(int i=0;i < matrix.get(0).size(); ++i) {
            sum += changeSign(i) * matrix.get(0).get(i) * determinant(createSubMatrix(matrix, 0, i));
        }
        return sum;
    }

    private Double changeSign(int i) {
        return (i%2==0?1.0:-1.0);
    }

    private List<List<Double>> createSubMatrix(List<List<Double>> matrix, int excludingRow, int excludingCol) {
        Double[][] mat = new Double[matrix.size()-1][matrix.get(0).size()-1];
        int r = -1;
        for(int i=0;i<matrix.size();i++) {
            if(i==excludingRow)
                continue;
                r++;
                int c=-1;
            for(int j=0;j<matrix.get(0).size();j++) {
                if(j==excludingCol)
                    continue;
                mat[r][++c] = matrix.get(i).get(j);
            }
        }

        List<List<Double>> resultMatrix = new ArrayList<>();

        for(int j=0;j<mat.length;j++) {
            List<Double> resultRow = new ArrayList<>();
            for (int i = 0; i < mat[0].length; i++) {
                resultRow.add(mat[j][i]);
            }
            resultMatrix.add(resultRow);
        }
        return resultMatrix;
    }

    private List<List<Double>> cofactor(List<List<Double>> matrix) {
        Double[][] mat = new Double[matrix.size()][matrix.get(0).size()];
        for(int i=0;i<matrix.size();i++) {
            for(int j=0;j<matrix.get(0).size();j++) {
                mat[i][j] = changeSign(i) * changeSign(j) * determinant(createSubMatrix(matrix,i,j));
            }
        }
        List<List<Double>> resultMatrix = new ArrayList<>();

        for(int j=0;j<mat.length;j++) {
            List<Double> resultRow = new ArrayList<>();
            for (int i = 0; i < mat[0].length; i++) {
                resultRow.add(mat[j][i]);
            }
            resultMatrix.add(resultRow);
        }
        return resultMatrix;
    }
}
