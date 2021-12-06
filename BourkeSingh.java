// Connor Bourke --- CSE 111 --- Fall 2021
// STEP: Import required packages
import java.sql.*;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.File;
import java.util.Scanner;

public class BourkeSingh {
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

    private void getAllCoaches()
    {

        System.out.printf("\nname   |   team\n");

        try {
            String sql = "SELECT c.c_name, t.t_team FROM Coach c JOIN Team t ON c.c_teamid=t.t_id";

            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                String name = rs.getString(1);
                String team = rs.getString(2);

                System.out.printf("%s | %s\n", name, team);
            } 

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void getAllCupResults()
    {

        System.out.printf("\nyear   |   winning team\n");

        try {
            String sql = "SELECT w.w_year, t.t_team FROM WorldCup w JOIN Team t ON t.t_id=w.w_winner ORDER BY w_year DESC";

            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                String year = rs.getString(1);
                String team = rs.getString(2);

                System.out.printf("%s | %s\n", year, team);
            } 

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void insertMatch()
    {
        System.out.println("Please enter values for m_id, stage, m_city, m_hometeam, m_homegoals, m_awayteam, and m_awaygoals. (Be sure to press ENTER after each value)");

        Scanner scan = new Scanner(System.in);

        int m_id = Integer.valueOf(scan.nextLine());
        String stage = scan.nextLine();
        String m_city = scan.nextLine();
        int m_hometeam = Integer.valueOf(scan.nextLine());
        int m_homegoals = Integer.valueOf(scan.nextLine());
        int m_awayteam = Integer.valueOf(scan.nextLine());
        int m_awaygoals = Integer.valueOf(scan.nextLine());

        try {
            String sql = "INSERT INTO Match (m_id, stage, m_city, m_hometeam, m_homegoals, m_awayteam, m_awaygoals) VALUES(?,?,?,?,?,?,?)";
            
            PreparedStatement stmt = c.prepareStatement(sql);
    
            stmt.setInt(1, m_id);
            stmt.setString(2, stage);
            stmt.setString(3, m_city);
            stmt.setInt(4, m_hometeam);
            stmt.setInt(5, m_homegoals);
            stmt.setInt(6, m_awayteam);
            stmt.setInt(7, m_awaygoals);

            stmt.executeUpdate();
            c.commit();

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

    private void insertTeam()
    {
        System.out.println("Please enter values for t_id, t_team. (Be sure to press ENTER after each value)");

        Scanner scan = new Scanner(System.in);

        int t_id = Integer.valueOf(scan.nextLine());
        String t_team = scan.nextLine();

        try {
            String sql = "INSERT INTO Team (t_id, t_team) VALUES(?,?)";
            
            PreparedStatement stmt = c.prepareStatement(sql);
    
            stmt.setInt(1, t_id);
            stmt.setString(2, t_team);

            stmt.executeUpdate();
            c.commit();

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

    private void insertPlayer()
    {
        System.out.println("Please enter values for p_id, p_name. (Be sure to press ENTER after each value)");

        Scanner scan = new Scanner(System.in);

        int p_id = Integer.valueOf(scan.nextLine());
        String p_name = scan.nextLine();

        try {
            String sql = "INSERT INTO Player (p_id, p_name) VALUES(?,?)";
            
            PreparedStatement stmt = c.prepareStatement(sql);
    
            stmt.setInt(1, p_id);
            stmt.setString(2, p_name);

            stmt.executeUpdate();
            c.commit();

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

    private void insertCoach()
    {
        System.out.println("Please enter values for c_teamid, c_id, c_name. (Be sure to press ENTER after each value)");

        Scanner scan = new Scanner(System.in);

        int c_teamid = Integer.valueOf(scan.nextLine());
        int c_id = Integer.valueOf(scan.nextLine());
        String c_name = scan.nextLine();

        try {
            String sql = "INSERT INTO Coach (c_teamid, c_id, c_name) VALUES(?,?,?)";
            
            PreparedStatement stmt = c.prepareStatement(sql);
    
            stmt.setInt(1, c_teamid);
            stmt.setInt(2, c_id);
            stmt.setString(3, c_name);

            stmt.executeUpdate();
            c.commit();

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

    private void insertCup()
    {
        System.out.println("Please enter values for w_year, w_host, w_winner, w_second, w_third, w_numteams, matchesplayed, and w_goals. (Be sure to press ENTER after each value)");

        Scanner scan = new Scanner(System.in);

        String w_year = scan.nextLine();
        int w_host = Integer.valueOf(scan.nextLine());
        int w_winner = Integer.valueOf(scan.nextLine());
        int w_second = Integer.valueOf(scan.nextLine());
        int w_third = Integer.valueOf(scan.nextLine());
        int w_numteams = Integer.valueOf(scan.nextLine());
        int matchesplayed = Integer.valueOf(scan.nextLine());
        int w_goals = Integer.valueOf(scan.nextLine());

        try {
            String sql = "INSERT INTO WorldCup (w_year, w_host, w_winner, w_second, w_third, w_numteams, matchesplayed, w_goals) VALUES(?,?,?,?,?,?,?,?)";
            
            PreparedStatement stmt = c.prepareStatement(sql);
    
            stmt.setString(1, w_year);
            stmt.setInt(2, w_host);
            stmt.setInt(3, w_winner);
            stmt.setInt(4, w_second);
            stmt.setInt(5, w_third);
            stmt.setInt(6, w_numteams);
            stmt.setInt(7, matchesplayed);
            stmt.setInt(8, w_goals);

            stmt.executeUpdate();
            c.commit();

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

    private void retireCoach()
    {
        System.out.println("Please enter the c_id of the coach you wish to remove.");

        Scanner scan = new Scanner(System.in);

        int c_id = Integer.valueOf(scan.nextLine());

        try {
            String sql = "DELETE FROM Coach WHERE c_id = ?";
            
            PreparedStatement stmt = c.prepareStatement(sql);
    
            stmt.setInt(1, c_id);

            stmt.executeUpdate();
            c.commit();

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

    public static void main(String args[]) {
        BourkeSingh sj = new BourkeSingh();
        
        sj.openConnection("WorldCups.sqlite");

        System.out.println("Sentience acheived. Hello world. What is my purpose?");

        Scanner scan = new Scanner(System.in);
        int sentinel = 0;

        System.out.println("Type \"help\" to see a list of possible commands.");

        while (sentinel == 0)
        {
            String cmd = scan.nextLine();

            if(cmd.equals("stop"))
            {
                sentinel = 1;
            }
            else if(cmd.equals("help"))
            {
                System.out.println("stop : ends the program");
                System.out.println("get all coaches : returns the names and corresponding teams of all rows in the Coach table");
                System.out.println("get all cups : returns the winner for each year in the WorldCup table");
                System.out.println("Q3");
                System.out.println("Q4");
                System.out.println("insert into match : allows you to insert a row into the Match table");
                System.out.println("insert into team  : allows you to insert a row into the Team table");
                System.out.println("insert into player : allows you to insert a row into the Player table");
                System.out.println("insert into coach : allows you to insert a row into the Coach table");
                System.out.println("insert into cup : allows you to insert a row into the WorldCup table");
                System.out.println("Q10");
                System.out.println("Q11");
                System.out.println("Q12");
                System.out.println("Q13");
                System.out.println("Q14");
                System.out.println("Q15");
                System.out.println("retire coach : allows you to remove a row from the Coach table");
                System.out.println("Q17");
                System.out.println("Q18");
                System.out.println("Q19");
                System.out.println("Q20");
                System.out.println("Q21");
                System.out.println("Q22");
            }
            else if(cmd.equals("get all coaches"))
            {
                sj.getAllCoaches();
            }
            else if(cmd.equals("get all cups"))
            {
                sj.getAllCupResults();
            }
            else if(cmd.equals("Q3"))
            {
                sj.Q3();
            }
            else if(cmd.equals("Q4"))
            {
                sj.Q4();
            }
            else if(cmd.equals("insert into match"))
            {
                sj.insertMatch();
            }
            else if(cmd.equals("insert into team"))
            {
                sj.insertTeam();
            }
            else if(cmd.equals("insert into player"))
            {
                sj.insertPlayer();
            }
            else if(cmd.equals("insert into coach"))
            {
                sj.insertCoach();
            }
            else if(cmd.equals("insert into cup"))
            {
                sj.insertCup();
            }
            else if(cmd.equals("retire coach"))
            {
                sj.retireCoach();
            }


            System.out.println("\nPlease enter a command.\n");
        }

        scan.close();
        sj.closeConnection();
    }
}
