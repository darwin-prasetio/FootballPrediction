package Prediction;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Pavilion on 03/03/2016.
 * This class is the estimation of coefficients using Newton's Method
 */
public class Logistic {

    //all is in matrix to simplify calculation
    private List<List<Double>> coefficients; //beta values, Nx1 matrix
    private List<List<Double>> matchResults; //y, Nx1 matrix
    private List<List<Double>> variables; //X, Nx(p+1) matrix
    private List<List<Double>> probabilities; //p, Nx1 matrix
    private List<List<Double>> likelihoods; //W = p(1-p), NxN matrix
    private Vector<Team> teams;

    public Logistic() {
        coefficients = new ArrayList<>(5);
        matchResults = new ArrayList<>();
        variables = new ArrayList<>();
        likelihoods = new ArrayList<>();
        probabilities = new ArrayList<>();
        teams = new Vector<>();
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
                row.add(new Double(tempResult[i][j]));
            }
            result.add(row);
        }
        return result;
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
                row.add(new Double(lists.get(i).get(j).doubleValue()*v));
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
            coefficients = matrixAddition(coefficients, matrixMultiplication(matrixMultiplication
                    (inverse(matrixMultiplication(transposeMatrix(variables), matrixMultiplication(likelihoods, variables))),
                            transposeMatrix(variables)), matrixSubtraction(matchResults, probabilities)));
        }
    }

    private List<List<Double>> matrixAddition(List<List<Double>> coefficients, List<List<Double>> matrix) {
        List<List<Double>> newCoefficients = new ArrayList<>();
        for(int i=0;i<matrix.size();i++) {
            List<Double> row = new ArrayList<>();
            row.add(new Double(coefficients.get(i).get(0).doubleValue()+matrix.get(i).get(0).doubleValue()));
            newCoefficients.add(row);
        }
        return newCoefficients;
    }

    private void calculateP(List<List<Double>> coefficients, List<List<Double>> variables) {
        probabilities = new ArrayList<>();
        for(int i=0;i<variables.size();i++) {
            List<Double> probability = new ArrayList<>();
            probability.add(new Double(calculateProbability(coefficients,variables.get(i).get(0),variables.get(i).get(1)
                    ,variables.get(i).get(2),variables.get(i).get(3),variables.get(i).get(4)/*,variables.get(i).get(5),
                    variables.get(i).get(6)*/)));
            probabilities.add(probability);
        }
    }

    private void calculateW(List<List<Double>> probabilities) {
        likelihoods = new ArrayList<>();
        for(int i=0;i<probabilities.size();i++) {
            Double likelihood = new Double(calculateLikelihood(probabilities.get(i)));
            List<Double> row = new ArrayList<>(probabilities.size());
            for(int j=0;j<probabilities.size();j++) {
                row.add(0.0);
            }
            row.set(i,likelihood);
            likelihoods.add(row);
        }
    }

    private List<List<Double>> matrixSubtraction(List<List<Double>> matchResults, List<List<Double>> probabilities) {
        List<List<Double>> result = new ArrayList<>();
        for(int i=0;i<matchResults.size();i++) {
            List<Double> row = new ArrayList<>();
            row.add(matchResults.get(i).get(0).doubleValue() - probabilities.get(i).get(0).doubleValue());
            result.add(row);
        }
        return result;
    }

    private List<List<Double>> matrixMultiplication(List<List<Double>> A, List<List<Double>> B) {
        Double[][] result = new Double[A.size()][B.get(0).size()];
        Double sum=new Double(0.0);
        for(int c=0;c<A.size();c++) {
            for(int d=0;d<B.get(0).size();d++) {
                for(int k=0;k<B.size();k++) {
                    sum += A.get(c).get(k).doubleValue() * B.get(k).get(d).doubleValue();
                }
                result[c][d]=sum;
                sum=0.0;
            }
        }

        List<List<Double>> resultMatrix = new ArrayList<>();

        for(int j=0;j<A.size();j++) {
            List<Double> resultRow = new ArrayList<>();
            for (int i = 0; i < B.get(0).size(); i++) {
                resultRow.add(new Double(result[j][i]));
            }
            resultMatrix.add(resultRow);
        }
        return resultMatrix;
    }

    public void parseInstances(Vector<String> instances) {
        matchResults = new ArrayList<>();
        variables = new ArrayList<>();
        teams = new Vector<>();
        for(String instance:instances) {
            String[] attributes = instance.split(",");
            List<Double> matchResult = new ArrayList<>();

            //adding match results to matchResults Vector, ignore draw results
            if(attributes[6].equals("H")) {
                matchResult.add(new Double(1.0));
            }
            else if(attributes[6].equals("A")) {
                matchResult.add(new Double(0.0));
            }
            matchResults.add(matchResult);

            //adding team strength to variables
            List<Double> variable = new ArrayList<>();
            variable.add(new Double(1.0)); //interceptConstant
            variable.add(new Double(Double.valueOf(attributes[23]))); //home offense
            variable.add(new Double(Double.valueOf(attributes[24]))); //home defense
            variable.add(new Double(Double.valueOf(attributes[25]))); //away offense
            variable.add(new Double(Double.valueOf(attributes[26]))); //away defense
//            variable.add(new Double(Double.valueOf(attributes[13]))); //home shots on target
//            variable.add(new Double(Double.valueOf(attributes[14]))); //away SoT
            variables.add(variable);

            Team team = new Team(attributes[2],Double.valueOf(attributes[23]), Double.valueOf(attributes[24]));
            if(!sameTeam(teams, team)) {
                teams.add(team);
            }
        }
    }

    private boolean sameTeam(Vector<Team> teams, Team team) {
        for(Team a : teams) {
            if(a.getName().equals(team.getName())) {
                return true;
            }
        }
        return false;
    }

    private Double calculateLikelihood(List<Double> probability) {
        return new Double(probability.get(0)*(1.0-probability.get(0)));
    }

    private Double calculateProbability(List<List<Double>> coefficients,Double interceptConstant, Double homeOffense, Double homeDefense,
                                        Double awayOffense, Double awayDefense/*, Double homeOnTarget, Double awayOnTarget*/) {
        Double sum = new Double(-1.0*(coefficients.get(0).get(0)*interceptConstant + coefficients.get(1).get(0)*homeOffense + coefficients.get(2).get(0)*homeDefense +
                coefficients.get(3).get(0)*awayOffense + coefficients.get(4).get(0)*awayDefense /*+ coefficients.get(5).get(0)* homeOnTarget +
                coefficients.get(6).get(0)*awayOnTarget*/));
        if(sum.equals(-0.0))
            sum = new Double(0.0);
        return new Double(1.0/(1.0+Math.exp(sum)));
    }

    private void setInitialCoefficients() {
        for(int i=0;i<5;++i) {
            List<Double> coefficient = new ArrayList<>();
            coefficient.add(new Double(0.0));
            coefficients.add(coefficient);
        }
    }

    private Double determinant(List<List<Double>> matrix) {
        if(matrix.size()==1) {
            return new Double(matrix.get(0).get(0));
        }
        if(matrix.size()==2) {
            return new Double((matrix.get(0).get(0).doubleValue() * matrix.get(1).get(1).doubleValue()) -
                    (matrix.get(0).get(1).doubleValue() * matrix.get(1).get(0).doubleValue()));
        }
        Double sum = new Double(0.0);
        for(int i=0;i < matrix.get(0).size(); ++i) {
            sum += new Double(changeSign(i).doubleValue() * matrix.get(0).get(i).doubleValue() *
                    determinant(createSubMatrix(matrix, 0, i)).doubleValue());
        }
        return sum;
    }

    private Double changeSign(int i) {
        if(i%2==0) {
            return  new Double(1.0);
        }
        else {
            return new Double(-1.0);
        }
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
                resultRow.add(new Double(mat[j][i]));
            }
            resultMatrix.add(resultRow);
        }
        return resultMatrix;
    }

    private List<List<Double>> cofactor(List<List<Double>> matrix) {
        Double[][] mat = new Double[matrix.size()][matrix.get(0).size()];
        for(int i=0;i<matrix.size();i++) {
            for(int j=0;j<matrix.get(0).size();j++) {
                mat[i][j] = new Double(changeSign(i).doubleValue() * changeSign(j).doubleValue() *
                        determinant(createSubMatrix(matrix,i,j)).doubleValue());
            }
        }
        List<List<Double>> resultMatrix = new ArrayList<>();

        for(int j=0;j<mat.length;j++) {
            List<Double> resultRow = new ArrayList<>();
            for (int i = 0; i < mat[0].length; i++) {
                resultRow.add(new Double(mat[j][i]));
            }
            resultMatrix.add(resultRow);
        }
        return resultMatrix;
    }

    public Double testAccuracy() {
        List<Double> predict = new ArrayList<>();
        for(int i=0;i<variables.size();i++) {
            if(calculateProbability(coefficients, variables.get(i).get(0), variables.get(i).get(1),
                    variables.get(i).get(2), variables.get(i).get(3), variables.get(i).get(4)/*, variables.get(i).get(5),
                    variables.get(i).get(6)*/)>0.5)
                predict.add(new Double(1.0));
            else
                predict.add(new Double(0.0));
        }

        //count
        double sum = 0.0;
        for(int i=0;i<matchResults.size();i++) {
            if(predict.get(i).equals(matchResults.get(i).get(0))) {
                sum++;
            }
        }
        return sum/matchResults.size();
    }

    public Vector<Team> getTeams() {
        return teams;
    }
}
