package loader;

import java.io.BufferedReader;

/**
 * Created by Pavilion on 27/02/2016.
 * Class to handle read csv file
 */
public class ReadFile {

    private final String FOLDER="dataset/";
    private String TrainDataSet;
    private String TestDataSet;
    private String[] instances;

    public ReadFile() {

    }

    public void setTrainDataSet(String dataSetName) {
        TrainDataSet=FOLDER+dataSetName;
    }

    public void parseFile() {
        BufferedReader reader = null;
        
    }
}
