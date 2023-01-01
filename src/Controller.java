import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.io.File;
import java.util.ArrayList;

public class Controller {
    public static void main(String[] args) throws SQLException {
        try {
            Connection dsquesterConn = dbConnection();
            populateAssignmentTable();
            //String query = "";
            //executeQuery(query, dsquesterConn);
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

    // CODE BELOW IS NOT IN USE YET
//    /**
//     * @param query    is the chosen SQL query
//     * @param dsqConnn is the connection to the database
//     * @throws SQLException
//     */
//    public static void executeQuery(String query, Connection dsqConnn) throws SQLException {
//        PreparedStatement statement = dsqConnn.prepareStatement(query);
//        ResultSet results = statement.executeQuery();
//        while (results.next()) {
//            for (int x = 1; x <= results.getMetaData().getColumnCount(); x++) {
//                System.out.print("Data imported.");
//            }
//            System.out.println();
//        }
//    }


    /**
     * populates the drug side effect assignment table based on the similar attributes in the files
     *
     * @throws IOException
     */
    public static void populateAssignmentTable() throws IOException {
        // String meddraFilePath = "Tailored .csv Files/meddra_all_se.csv";
        String meddraFilePath = ".csv/AllMeddra.csv";
        // code sourced from: https://stackoverflow.com/questions/18009416/how-to-count-total-rows-in-csv-using-java
        BufferedReader bufferedReaderMeddra = new BufferedReader(new FileReader(meddraFilePath));
        // String seFilePath = "Tailored .csv Files/side_effect.csv";
        String seFilePath = ".csv/SideEffectTable.csv";
        BufferedReader bufferedReaderSE = new BufferedReader(new FileReader(seFilePath));

        String row;
        String drugId;
        String terminologyFormat;
        String sideEffect;
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
            sideEffect = columns[1];
            terminologyFormat = columns[2];
            String entry = terminologyFormat + ", " + sideEffect;
            sideEffectList.add(entry);
        }
        // compare the two lists
        // TODO:
        // if (contains("LT"))
        for (int x = 0; x < drugList.size(); x++) {
            for (int y = 0; y < sideEffectList.size(); y++) {
                if (sideEffectList.get(y).split(",")[0] == drugList.get(x).split(", ")[1] &&
                        sideEffectList.get(y).split(",")[1] == drugList.get(x).split(", ")[2]) {
                    System.out.println("Match! Drug ID = " + drugList.get(x).split(", ")[0] + ", Side Effect: " +
                            sideEffectList.get(y).split(", ")[0] + ", " + sideEffectList.get(y).split(", ")[1]);
                    // values below will be used to populate the assignment table
                    String drugIdVal = drugList.get(x).split(", ")[2];
                    String seVal = sideEffectList.get(y).split(",")[1];
                    // more code needed in order to populate data.
                }
            }
        }
    }
}
