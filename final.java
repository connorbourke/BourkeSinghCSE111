// Connor Bourke --- CSE 111 --- Fall 2021
// STEP: Import required packages
import java.sql.*;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.File;

public class Database {
    private Connection c = null;
    private String dbName;
    private boolean isConnected = false;

    private void openConnection(String _dbName) {
        dbName = _dbName;

        if (false == isConnected) {
            System.out.println("++++++++++++++++++++++++++++++++++");
            System.out.println("Open database: " + _dbName);

            try {
                String connStr = new String("jdbc:sqlite:");
                connStr = connStr + _dbName;

                // STEP: Register JDBC driver
                Class.forName("org.sqlite.JDBC");

                // STEP: Open a connection
                c = DriverManager.getConnection(connStr);

                // STEP: Diable auto transactions
                c.setAutoCommit(false);

                isConnected = true;
                System.out.println("success");
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }

            System.out.println("++++++++++++++++++++++++++++++++++");
        }
    }

    private void closeConnection() {
        if (true == isConnected) {
            System.out.println("++++++++++++++++++++++++++++++++++");
            System.out.println("Close database: " + dbName);

            try {
                // STEP: Close connection
                c.close();

                isConnected = false;
                dbName = "";
                System.out.println("success");
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }

            System.out.println("++++++++++++++++++++++++++++++++++");
        }
    }

    private void createTable() {
        System.out.println("++++++++++++++++++++++++++++++++++");
        System.out.println("Create table");

        try
        {
            Statement stmt = c.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS warehouse(" +
                "w_warehousekey DECIMAL(9,0) NOT NULL," +
			    "w_name CHAR(100) NOT NULL," +
			    "w_capacity DECIMAL(6,0) NOT NULL," +
                "w_suppkey DECIMAL(9,0) NOT NULL," +
			    "w_nationkey DECIMAL(2,0) NOT NULL" +
                ")";

            stmt.executeUpdate(sql);

            c.commit();

            System.out.println("success");
        }
        catch(SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try
            {
                c.rollback();
            }
            catch(SQLException e1)
            {
                System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
            }
        }

        System.out.println("++++++++++++++++++++++++++++++++++");
    }

    private void insertIntoTable(int _whkey, String _name, int _capacity, int _suppkey, int _nationkey) {

        try
        {
            String sql = "INSERT INTO warehouse VALUES (?, ?, ?, ?, ?)";
    
            PreparedStatement stmt = c.prepareStatement(sql);
    
            stmt.setInt(1, _whkey);
            stmt.setString(2, _name);
            stmt.setInt(3, _capacity);
            stmt.setInt(4, _suppkey);
            stmt.setInt(5, _nationkey);

            stmt.executeUpdate();
    
            c.commit();
            stmt.close();
        }
        catch(SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try
           {
                c.rollback();
           }
            catch(SQLException e1)
            {
                System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
            }
        }
    }

    private void populateTable() {
        System.out.println("++++++++++++++++++++++++++++++++++");
        System.out.println("Populate table");

        System.out.println("success");
        System.out.println("++++++++++++++++++++++++++++++++++");
    }

    private void dropTable() {
        System.out.println("++++++++++++++++++++++++++++++++++");
        System.out.println("Drop table");

        try
        {
            Statement stmt = c.createStatement();

            String sql = "DROP TABLE warehouse";

            stmt.executeUpdate(sql);

            c.commit();

            System.out.println("success");

        }
        catch(SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try
            {
                c.rollback();
            }
            catch(SQLException e1)
            {
                System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
            }
        }

        System.out.println("++++++++++++++++++++++++++++++++++");
    }

    private void Q1() {
        System.out.println("++++++++++++++++++++++++++++++++++");
        System.out.println("Q1");

        try {
            String sql = "SELECT w_warehousekey, w_name, w_capacity, w_suppkey, w_nationkey " +
                "FROM warehouse " +
                "ORDER BY w_warehousekey";

            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            FileWriter writer = new FileWriter("output/1.out", false);
            PrintWriter printer = new PrintWriter(writer);
                
            printer.printf("%10s %-40s %10s %10s %10s\n", "wId", "wName", "wCap", "sId", "nId");
            while(rs.next())
            {
                int wId = rs.getInt(1);
                String wName = rs.getString(2);
                int wCap = rs.getInt(3);
                int sId = rs.getInt(4);
                int nId = rs.getInt(5);

                printer.printf("%10s %-40s %10s %10s %10s\n", wId, wName, wCap, sId, nId);
            } 

            printer.close();
            writer.close();
            System.out.println("success");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        System.out.println("++++++++++++++++++++++++++++++++++");
    }

    private void Q2() {
        System.out.println("++++++++++++++++++++++++++++++++++");
        System.out.println("Q2");

        try {
            String sql = "SELECT n_name, COUNT(w_warehousekey), SUM(w_capacity) " +
                "FROM nation, warehouse " +
                "WHERE w_nationkey = n_nationkey " +
                "GROUP BY n_name " +
                "ORDER BY COUNT(w_warehousekey) DESC, SUM(w_capacity) DESC, n_name DESC";

            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            FileWriter writer = new FileWriter("output/2.out", false);
            PrintWriter printer = new PrintWriter(writer);

            printer.printf("%-40s %10s %10s\n", "nation", "numW", "totCap");
            while(rs.next())
            {
                String nation = rs.getString(1);
                int numW = rs.getInt(2);
                int totCap = rs.getInt(3);

                printer.printf("%-40s %10s %10s\n", nation, numW, totCap);
            } 


            printer.close();
            writer.close();
            System.out.println("success");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        System.out.println("++++++++++++++++++++++++++++++++++");
    }

    private void Q3() {
        System.out.println("++++++++++++++++++++++++++++++++++");
        System.out.println("Q3");

        try {
            File fn = new File("input/3.in");
            FileReader reader = new FileReader(fn);
            BufferedReader in = new BufferedReader(reader);
            String nation = in.readLine();
            in.close();

            String sql = "SELECT s_name, n_name, w_name " +
                "FROM supplier, nation, warehouse " +
                "WHERE n_nationkey = s_nationkey " +
                "AND s_suppkey = w_suppkey " +
                "AND w_name LIKE ?";

            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, ("%" + nation + "%"));
            ResultSet rs = stmt.executeQuery();

            FileWriter writer = new FileWriter("output/3.out", false);
            PrintWriter printer = new PrintWriter(writer);

            printer.printf("%-20s %-20s %-40s\n", "supplier", "nation", "warehouse");
            while(rs.next())
            {
                String supplier = rs.getString(1);
                String nat = rs.getString(2);
                String warehouse = rs.getString(3);

                printer.printf("%-20s %-20s %-40s\n", supplier, nat, warehouse);
            } 


            printer.close();
            writer.close();
            System.out.println("success");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        System.out.println("++++++++++++++++++++++++++++++++++");
    }

    private void Q4() {
        System.out.println("++++++++++++++++++++++++++++++++++");
        System.out.println("Q4");

        try {
            File fn = new File("input/4.in");
            FileReader reader = new FileReader(fn);
            BufferedReader in = new BufferedReader(reader);
            String region = in.readLine();
            int cap = Integer.parseInt(in.readLine());
            in.close();

            String sql = "SELECT w_name, w_capacity " +
                "FROM warehouse, region, nation " +
                "WHERE r_regionkey = n_regionkey " +
                "AND n_nationkey = w_nationkey " +
                "AND r_name = ? " +
                "AND w_capacity > ? " +
                "ORDER BY w_capacity DESC";

            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, region);
            stmt.setInt(2, cap);
            ResultSet rs = stmt.executeQuery();
            
            FileWriter writer = new FileWriter("output/4.out", false);
            PrintWriter printer = new PrintWriter(writer);

            printer.printf("%-40s %10s\n", "warehouse", "capacity");
            while(rs.next())
            {
                String warehouse = rs.getString(1);
                int capacity = rs.getInt(2);

                printer.printf("%-40s %10s\n", warehouse, capacity);
            } 


            printer.close();
            writer.close();
            System.out.println("success");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        System.out.println("++++++++++++++++++++++++++++++++++");
    }

    private void Q5() {
        System.out.println("++++++++++++++++++++++++++++++++++");
        System.out.println("Q5");

        try {
            File fn = new File("input/5.in");
            FileReader reader = new FileReader(fn);
            BufferedReader in = new BufferedReader(reader);
            String nation = in.readLine();
            in.close();

            String sql = "SELECT r_name, SUM(allregions.totalCapacity) "+
                "FROM "+
                    "( "+
                    "SELECT r_name, SUM(w_capacity) as totalCapacity "+
                    "FROM region, warehouse, supplier, nation n1, nation n2 "+
                    "WHERE warehouse.w_suppkey = supplier.s_suppkey "+
                    "AND warehouse.w_nationkey = n1.n_nationkey "+
                    "AND supplier.s_nationkey = n2.n_nationkey "+
                    "AND n1.n_regionkey = region.r_regionkey "+
                    "AND n2.n_name = ? "+
                    "GROUP BY r_name "+
                    "UNION "+
                    "SELECT DISTINCT r_name, 0 AS totalCapacity "+
                    "FROM region "+
                    "WHERE r_name NOT IN "+
                        "( "+
                        "SELECT r_name "+
                        "FROM region, warehouse, supplier, nation n1, nation n2 "+
                        "WHERE warehouse.w_suppkey = supplier.s_suppkey "+
                        "AND warehouse.w_nationkey = n1.n_nationkey "+
                        "AND supplier.s_nationkey = n2.n_nationkey "+
                        "AND n1.n_regionkey = region.r_regionkey "+
                        "AND n2.n_name = ? "+
                        ") "+
                        "GROUP BY r_name "+
                    ") AS allregions "+
                "GROUP BY r_name";

            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, nation);
            stmt.setString(2, nation);
            ResultSet rs = stmt.executeQuery();

            FileWriter writer = new FileWriter("output/5.out", false);
            PrintWriter printer = new PrintWriter(writer);

            printer.printf("%-20s %20s\n", "region", "capacity");
            while(rs.next())
            {
                String region = rs.getString(1);
                int capacity = rs.getInt(2);

                printer.printf("%-20s %20s\n", region, capacity);
            } 

            printer.close();
            writer.close();
            System.out.println("success");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        System.out.println("++++++++++++++++++++++++++++++++++");
    }


    public static void main(String args[]) {
        Database sj = new Database();
        
        sj.openConnection("WorldCups.sqlite");

        

/*
        sj.dropTable();
        sj.createTable();
        sj.populateTable();

        sj.Q1();
        sj.Q2();
        sj.Q3();
        sj.Q4();
        sj.Q5();
*/
        sj.closeConnection();
    }
}
