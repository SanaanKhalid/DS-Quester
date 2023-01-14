import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Controller {
    public static void main(String[] args) throws SQLException {
        try {
            Connection dsquesterConn = dbConnection();
            populateAssignmentTable(dsquesterConn);
            // executeQuery(query, dsquesterConn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up connection with database in MySQL Workbench
     *
     * @return a connection to the DS Quester database
     * @throws SQLException
     */
    public static Connection dbConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/dsquester";
        String user = "root";
        String password = "dataBas3";
        Connection dsquesterConn = DriverManager.getConnection(url, user, password);
        return dsquesterConn;
    }

    /**
     * @param query         is the chosen SQL query
     * @param dsquesterConn is the connection to the database
     * @throws SQLException
     */
    public static void executeQuery(String query, Connection dsquesterConn) throws SQLException {
        PreparedStatement statement = dsquesterConn.prepareStatement(query);
        int rowCount = statement.executeUpdate();
        System.out.println();
        System.out.println("Success - " + rowCount + " rows affected.");
    }

    /**
     * populates the drug side effect assignment table based on the similar attributes in the files
     *
     * @throws IOException
     */
    public static void populateAssignmentTable(Connection dsquesterConn) throws IOException, SQLException {
        String meddraFilePath = ".csv/AllMeddra.csv";
        // code sourced from: https://stackoverflow.com/questions/18009416/how-to-count-total-rows-in-csv-using-java
        BufferedReader bufferedReaderMeddra = new BufferedReader(new FileReader(meddraFilePath));
        String seFilePath = ".csv/SideEffectTable.csv";
        BufferedReader bufferedReaderSE = new BufferedReader(new FileReader(seFilePath));

        String row;
        String drugId;
        String terminologyFormat;
        String sideEffect;
        String seId;
        ArrayList<String> drugList = new ArrayList<String>();
        while ((row = bufferedReaderMeddra.readLine()) != null) {
            String[] columns = row.split(",");
            drugId = columns[0];
            terminologyFormat = columns[3];
            sideEffect = columns[5];
            String entry = drugId + ", " + terminologyFormat + ", " + sideEffect;
            drugList.add(entry);
        }
        ArrayList<String> sideEffectList = new ArrayList<String>();
        while ((row = bufferedReaderSE.readLine()) != null) {
            String[] columns = row.split(",");
            sideEffect = columns[4];
            seId = columns[0];
            terminologyFormat = columns[2];
            String entry = terminologyFormat + ", " + sideEffect + ", " + seId;
            sideEffectList.add(entry);
        }

        //side effect entry: LT, Abdominal pain
        // drug list entry: drug id, LLT, abdominal pain
        for (int x = 0; x < drugList.size(); x++) {
            for (int y = 0; y < sideEffectList.size(); y++) {

                // if the TERMINOLOGY FORMAT matches and if the side effect matches
                if ((sideEffectList.get(y).split(", ")[0].equalsIgnoreCase(drugList.get(x).split(", ")[1]) &&
                        sideEffectList.get(y).split(", ")[1].equalsIgnoreCase(drugList.get(x).split(", ")[2])) ||

                        // or if it's comparing LLT and LT  (which are the same) and if the side effect matches
                        (sideEffectList.get(y).split(", ")[0].equalsIgnoreCase("LT") &&
                                drugList.get(x).split(", ")[1].equalsIgnoreCase("LLT") &&
                                sideEffectList.get(y).split(", ")[1].equalsIgnoreCase(drugList.get(x).split(", ")[2]))) {

                    // print out the drug id, and side effect and  its terminology format
                    System.out.println("Match! Drug ID = " + drugList.get(x).split(", ")[0] + ", Side Effect: " +
                            sideEffectList.get(y).split(", ")[0] + ", " + sideEffectList.get(y).split(", ")[1]);

                    // values below will be used to populate the assignment table
                    String drugIdVal = drugList.get(x).split(", ")[0];
                    int seIdVal = Integer.parseInt(sideEffectList.get(y).split(", ")[2]);

                    String populateQuery = "INSERT INTO drug_se_assignment VALUES (\"" + drugIdVal + "\", " + seIdVal + ");";
                    System.out.println(populateQuery);
                    try {
                        // send query to method to populate table
                        executeQuery(populateQuery, dsquesterConn);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // end of if statement
                }
            }
        }
    }
}


// reimport the drug data (mixed values)
// reimport the side effect data (only a few values are present)
// foreign key issue
// data truncation
// Data truncation: Data too long for column 'drug_id' at row 1
// Match! Drug ID = CID100000085, Side Effect: LT, Abdominal cramps
//INSERT INTO drug_se_assignment VALUES ("CID100000085", 1000005);