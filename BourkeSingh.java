// Connor Bourke --- Partap Singh --- CSE 111 --- Fall 2021
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

    private void getAllPlayers()
    {

        System.out.printf("\nname   |   team\n");

        try {
            String sql = "SELECT p.p_name, t.t_team FROM Player p JOIN LinkPlayerAndTeams L on L.p_id = p.p_id JOIN Team t ON l.t_id=t.t_id";

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

    private void getAllTeams()
    {

        System.out.printf("\n team\n");

        try {
            String sql = "SELECT t_team FROM Team";

            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                String team = rs.getString(1);

                System.out.printf("%s\n", team);
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

    private void bestPlayerOnTeam()
    {

        System.out.println("Please enter a team name. (t_team)");

        Scanner scan = new Scanner(System.in);

        String t_name = scan.nextLine();

        System.out.printf("\n name  |  goals\n");

        try {
            String sql = "SELECT p_name, ps.ps_goals FROM Player JOIN PlayerStats ps ON p_id=ps.ps_playerid "+
            "WHERE p_name IN (SELECT p_name FROM Player p join LinkPlayerAndTeams L on l.p_id=p.p_id "+
            "JOIN Team t ON l.t_id=t.t_id WHERE t.t_team LIKE ?) "+
            "ORDER BY ps.ps_goals DESC LIMIT 1";

            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(1, "%" + t_name + "%");

            System.out.println("\nAfter setString\n" + sql + "\n");

            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                String name = rs.getString(1);
                int goals = rs.getInt(2);

                System.out.printf("%s | %s\n", name, goals);
            } 

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void numOfMatches()
    {

        System.out.printf("\nmatches | team\n");

        try {
            String sql = "SELECT count(t.t_team) AS '# of matches played', t.t_team "+
            "FROM Team t "+
            "JOIN Match m ON m.m_hometeam=t.t_id "+
            "GROUP BY t.t_team "+
            "ORDER BY count(t.t_team) DESC LIMIT 1";

            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                int num = rs.getInt(1);
                String team = rs.getString(2);

                System.out.printf("%s | %s\n", num, team);
            } 

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void playersFromTeam()
    {

        System.out.println("Please enter a team name. (t_team)");

        Scanner scan = new Scanner(System.in);

        String t_name = scan.nextLine();

        System.out.printf("\n  name  |  team\n");

        try {
            String sql = "SELECT p.p_name, t.t_team "+
            "FROM Player p JOIN LinkPlayerAndTeams L on L.p_id=p.p_id "+
            "JOIN Team t ON L.t_id=t.t_id "+
            "WHERE t.t_team LIKE ?";

            PreparedStatement stmt = c.prepareStatement(sql);
            
            stmt.setString(1, "%" + t_name + "%");

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

    private void cupWithMostGoals()
    {

        System.out.printf("\nyear | goals\n");

        try {
            String sql = "SELECT w_year, max(w_goals) FROM WorldCup";

            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                String year = rs.getString(1);
                int goals = rs.getInt(2);

                System.out.printf("%s | %s\n", year, goals);
            } 

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void playerWithMostGoals()
    {

        System.out.printf("\n name  |  goals\n");

        try {
            String sql = "SELECT p.p_name, ps.ps_goals FROM Player p JOIN PlayerStats ps "+
                "ON p.p_id=ps.ps_playerid ORDER BY  ps.ps_goals DESC LIMIT 1";

            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                String name = rs.getString(1);
                int goals = rs.getInt(2);

                System.out.printf("%s | %s\n", name, goals);
            } 

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void teamWithMostCups()
    {

        System.out.printf("\nwins | team\n");

        try {
            String sql = "SELECT count(w.w_winner) AS 'Most Wins' , t.t_team FROM WorldCup w "+
            "JOIN Team t ON t.t_id=w.w_winner GROUP BY w.w_winner "+
            "ORDER BY count(w.w_winner) DESC LIMIT 1";

            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                int wins = rs.getInt(1);
                String team = rs.getString(2);

                System.out.printf("%s | %s\n", wins, team);
            } 

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void topFiveTeams()
    {
        System.out.printf("\nplace | team\n");

        try {
            String sql = "SELECT (ROW_NUMBER() OVER (ORDER BY t_team)) AS 'Place' , t_team AS 'Country' "+
            "FROM (SELECT t_team FROM Team JOIN WorldCup w ON w.w_winner=t_id  GROUP BY w.w_winner "+
            "ORDER BY count(w.w_winner) DESC LIMIT 5)";

            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                int place = rs.getInt(1);
                String team = rs.getString(2);

                System.out.printf("%s | %s\n", place, team);
            } 

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
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

    private void dropWorstPlayers()
    {
        System.out.println("Please enter threshold for elimination in goals.\n(Ex: Enter 5 to drop players with fewer than 5 goals)");

        Scanner scan = new Scanner(System.in);

        int goals = Integer.valueOf(scan.nextLine());

        try {
            String sql = "DELETE FROM Player WHERE p_id IN (SELECT p_id "+
            "FROM Player, PlayerStats WHERE p_id = ps_playerid AND ps_goals < ?)";
            
            PreparedStatement stmt = c.prepareStatement(sql);
    
            stmt.setInt(1, goals);

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

    private void updateCoachTeam()
    {
        System.out.println("Please enter the new c_teamid and the c_name. (Be sure to press ENTER after each value)");

        Scanner scan = new Scanner(System.in);

        int c_teamid = Integer.valueOf(scan.nextLine());
        String c_name = scan.nextLine();

        try {
            String sql = "UPDATE Coach SET c_teamid = ? WHERE c_name LIKE ?";
            
            PreparedStatement stmt = c.prepareStatement(sql);
    
            stmt.setInt(1, c_teamid);
            stmt.setString(2, "%" + c_name + "%");

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

    private void updatePlayerCups()
    {
        System.out.println("Please enter the p_id of the player to update");

        Scanner scan = new Scanner(System.in);

        int p_id = Integer.valueOf(scan.nextLine());

        try {
            String sql = "UPDATE PlayerStats SET ps_cupsplayed = (ps_cupsplayed + 1) WHERE ps_playerid IN "+
            "(SELECT ps_playerid FROM Player, PlayerStats WHERE p_id = ps_playerid AND p_id = ?)";
            
            PreparedStatement stmt = c.prepareStatement(sql);
    
            stmt.setInt(1, p_id);

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

    private void homeTeamStats()
    {

        System.out.printf("\n  team  |  city  | year | goals\n");

        try {
            String sql = "SELECT t.t_team, m.m_city, w.w_year, m.m_homegoals FROM Match m "+
            "JOIN LinkWorldCupAndMatch L ON l.m_id=m.m_id "+
            "JOIN WorldCup w ON w.w_year=l.w_year "+
            "JOIN Team t ON t.t_id=m.m_hometeam";

            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                String team = rs.getString(1);
                String city = rs.getString(2);
                String year = rs.getString(3);
                int goals = rs.getInt(4);

                System.out.printf("%s | %s | %s | %s\n", team, city, year, goals);
            } 

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void timesHosted()
    {
        System.out.printf("\nhosted | team\n");

        try {
            String sql = "Select Count (w.w_host) as 'hostCount', t.t_team from match m "+
            "join LinkWorldCupAndMatch L on l.m_id=m.m_id join WorldCup w on w.w_year=l.w_year "+
            "join Team t on t.t_id=w.w_host group by w.w_host ORDER BY hostCount DESC, w.w_host ASC";

            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                int hosted = rs.getInt(1);
                String team = rs.getString(2);

                System.out.printf("%s | %s\n", hosted, team);
            } 

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void customQuery()
    {
        System.out.printf("\nEnter your custom SQLite query as a single line. (Does not support SELECT statements)\n");

        Scanner scan = new Scanner(System.in);

        String sql = scan.nextLine();

        try
        {
            PreparedStatement stmt = c.prepareStatement(sql);

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
            System.out.println("\nPlease enter a command.\n");

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
                System.out.println("get all players : returns the names and corresponding teams of all rows in the Player table");
                System.out.println("get all teams : returns the team names of all rows in the Team table");
                System.out.println("insert into match : allows you to insert a row into the Match table");
                System.out.println("insert into team  : allows you to insert a row into the Team table");
                System.out.println("insert into player : allows you to insert a row into the Player table");
                System.out.println("insert into coach : allows you to insert a row into the Coach table");
                System.out.println("insert into cup : allows you to insert a row into the WorldCup table");
                System.out.println("best player : returns the best player from your team of choice");
                System.out.println("most matches : returns the team with the most matches ever played");
                System.out.println("players on team : returns all players from your team of choice");
                System.out.println("cup with most goals : returns the year with the most goals in the WorldCup table");
                System.out.println("player with most goals : returns the player with the most goals");
                System.out.println("team with most cups : returns the number of wins and name of the team that has won the most World Cups");
                System.out.println("top five : returns the top five teams based on World Cups won");
                System.out.println("retire coach : allows you to remove a row from the Coach table");
                System.out.println("drop worst : removes the players that have fewer goals than a number of your choice");
                System.out.println("update coach team : updates the team id for a row in the Coach table");
                System.out.println("update player cups : adds 1 to a player's cupsplayed stat");
                System.out.println("home team : gives the team name, city, year, and goals for each row in Match");
                System.out.println("times hosted : returns the number of times each country has hosted a World Cup");
                System.out.println("custom : allows you to submit a custom UPDATE/INSERT/DELETE query");
            }
            else if(cmd.equals("get all coaches"))
            {
                sj.getAllCoaches();
            }
            else if(cmd.equals("get all cups"))
            {
                sj.getAllCupResults();
            }
            else if(cmd.equals("get all players"))
            {
                sj.getAllPlayers();
            }
            else if(cmd.equals("get all teams"))
            {
                sj.getAllTeams();
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
            else if(cmd.equals("best player"))
            {
                sj.bestPlayerOnTeam();
            }
            else if(cmd.equals("most matches"))
            {
                sj.numOfMatches();
            }
            else if(cmd.equals("players on team"))
            {
                sj.playersFromTeam();
            }
            else if(cmd.equals("cup with most goals"))
            {
                sj.cupWithMostGoals();
            }
            else if(cmd.equals("player with most goals"))
            {
                sj.playerWithMostGoals();
            }
            else if(cmd.equals("team with most cups"))
            {
                sj.teamWithMostCups();
            }
            else if(cmd.equals("top five"))
            {
                sj.topFiveTeams();
            }
            else if(cmd.equals("retire coach"))
            {
                sj.retireCoach();
            }
            else if(cmd.equals("drop worst"))
            {
                sj.dropWorstPlayers();
            }
            else if(cmd.equals("update coach team"))
            {
                sj.updateCoachTeam();
            }
            else if(cmd.equals("update player cups"))
            {
                sj.updatePlayerCups();
            }
            else if(cmd.equals("home team"))
            {
                sj.homeTeamStats();
            }
            else if(cmd.equals("times hosted"))
            {
                sj.timesHosted();
            }
            else if(cmd.equals("custom"))
            {
                sj.customQuery();
            }
            else
                System.out.println("\nInavild Command");

        }

        scan.close();
        sj.closeConnection();
    }
}
