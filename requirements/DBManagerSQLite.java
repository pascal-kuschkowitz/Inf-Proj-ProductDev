/**
  *
  * Beschreibung
  *
  * @version 1.0 vom 01.03.2018
  * @author Ylva Brandt
  */

import java.sql.*;
import java.io.PrintWriter;

public class DBManagerSQLite {
  
  // Anfang Attribute
  /** der Name der Datenbank */
  private String dbName = "schule_erweitert";
  
  /** ein Objekt f&uuml;r die Verbindung zur Datenbank*/
  private Connection verbindung;
  
  /** ein Objekt zum Absenden von SQL-Anfragen*/
  private Statement statement;
  // Ende Attribute
  
  /** erzeugt ein DBManagerSQLite-Objekt f&uuml;r die Datenbank schule_erweitert und stellt eine Verbindung zur Datenbank her*/
  public DBManagerSQLite() {
    //Status Informationen des DriverManagers ausgeben
    DriverManager.setLogWriter(new PrintWriter(System.out));
    
    //Treiber laden
    try{
      Class.forName("org.sqlite.JDBC");
    }catch(ClassNotFoundException e )
    {
      System.err.println( "Keine Treiber-Klasse!" );
      return;
    }
    
    //Connection- und Statement-Objekt erzeugen
    try{
      verbindung = DriverManager.getConnection("jdbc:sqlite:" + dbName+".db");
      statement = verbindung.createStatement();
    }catch(SQLException e){
      e.printStackTrace();
    } 
  }
      
  /** erzeugt ein DBManagerSQLite-Objekt f&uuml;r die angegebene Datenbank und stellt eine Verbindung zur Datenbank her
  * @param dbName der Name der Datenbank
  */
  public DBManagerSQLite(String dbName) {
    this.dbName = dbName;
    //Status Informationen des DriverManagers ausgeben
    DriverManager.setLogWriter(new PrintWriter(System.out));
    
     //Treiber laden
    try{
      Class.forName("org.sqlite.JDBC");
    }catch(ClassNotFoundException e )
    {
      System.err.println( "Keine Treiber-Klasse!" );
      return;
    }
    
    //Connection- und Statement-Objekt erzeugen
    try{
      verbindung = DriverManager.getConnection("jdbc:sqlite:"+dbName + ".db");
      statement = verbindung.createStatement();
    }
    catch(SQLException e){
       System.out.println(e.toString());
       e.printStackTrace();
    }
  }
  
  
  // Anfang Methoden
  
  /**
  * f&uuml;hrt die &uuml;bergebene SQL-Anfrage aus
  * @param sqlAnfrage Die SQL-Anfrage, die ausgef&uuml;hrt werden soll, als Zeichenkette.
  * @return Das Ergebnis der SQL-Anfrage als zweidimensionale Reihung vom Typ Zeichenkette. Die oberste Zeile der Reihung enth&auml;lt die &Uuml;berschriften der Spalten. Danach folgt pro Datensatz eine Zeile mit den entsprechenden Werten. 
   * Diese werden unabh&auml;ngig von den Datentypen der Datenbank als Zeichenkette gespeichert. Enth&auml;lt eine Zelle in der Datenbank den Wert null, wird die Zeichenkette "null" in das entsprechende Feld der zweidimensionalen Reihung geschrieben.</br>
   * Schl&auml;gt der Versuch die SQL-Anfrage zu stellen fehl, enth&auml;lt die zweidimensionale Reihung nur ein Feld mit dem Inhalt "Fehler".</br>
   * Der erste Index der Reihung gibt die Zeile an, der zweite die Spalte.
  */    
  public String[][] sqlAnfrageAusfuehren(String sqlAnfrage){
    //R&uuml;ckgabe vorbereiten
    String[][] ergebnis = new String[1][1];
    ergebnis[0][0] = "Fehler";
    try{
      //falls die Verbindung schon wieder geschlossen wurde, muss sie neu aufgebaut werden und ein neues Statement-Objekt muss erzeugt werden.
      if (verbindung == null || verbindung.isClosed()) {
        System.out.println("neue Verbindung herstellen");
        verbindung = DriverManager.getConnection("jdbc:sqlite:"+dbName + ".db");
        statement = verbindung.createStatement();
      }
      
      //Das Ergebnis kommt als Java-Objekt vom Typ ResultSet zur&uuml;ck. Der Inhalt wird in die zweidimensionale Reihung &uuml;bertragen und dies dann zur&uuml;ckgegeben.
      ResultSet myResultSet = statement.executeQuery(sqlAnfrage);
      ResultSetMetaData myRSMetaData = myResultSet.getMetaData();
      
      //zun&auml;chst muss die Gr&ouml;ße der ben&ouml;tigten Zeilen und Spalten in der Reihung bestimmt werden.  
      int spalten  = myRSMetaData.getColumnCount();
      int zeilen = 1;
      while(myResultSet.next()){
        zeilen++;
      }
      ergebnis = new String[zeilen][spalten];
      myResultSet.close();
          
      //Da das Resultset in Verbindung mit SQLite nur einmal durchlaufen werden kann, muss
      // die Anfrage hier nocheinmal gestellt werden, um ein neues Resultset zu erhalten, 
      // das nun in die Reihung &uuml;bertragen werden kann.
      myResultSet = statement.executeQuery(sqlAnfrage);
      myRSMetaData = myResultSet.getMetaData();
      
            
      //die erste Zeile mit den Spaltennamen f&uuml;llen  
      int zeilenzaehler = 0;  
      for (int i = 1; i <= spalten; i++) {
        ergebnis[0][i-1] = myRSMetaData.getColumnName(i);
      } // end of for
      zeilenzaehler++;
      
      //die Datens&auml;tze &uuml;bertragen
      while (myResultSet.next()) {
        for(int i = 1; i <= spalten; i++){
          ergebnis[zeilenzaehler][i-1] = myResultSet.getString(i);
          
          //enth&auml;lt eine Zelle in der Datenbank den Wert null, wird die Zeichenkette "null" in das entsprechende Felde der zweidimensionalen Reihung geschrieben.
          if(myResultSet.wasNull()) ergebnis[zeilenzaehler][i-1] = "null";
        }
        zeilenzaehler++; 
      } // end of while
      myResultSet.close();
      
    }catch(SQLException e){
      e.printStackTrace();
    }
    return ergebnis; 
  }
  
  /**
  * f&uuml;hrt die &uuml;bergebene SQL-Einf&uuml;ge-Anweisung aus
  * @param sql Die SQL-Anweisung, die ausgef&uuml;hrt werden soll, als Zeichenkette.
  * @return Die Anzahl der betroffenen Datens&auml;tze. Der R&uuml;ckgabewert ist 0, wenn die Anweisung keine &Auml;nderung in der Datenbank bewirkt hat. 
  * Der R&uuml;ckgabewert -1 zeigt an, dass ein Fehler aufgetreten ist.
  */   
  public int datensatzEinfuegen(String sql){
    int ergebnis = -1;
    try{
      //falls die Verbindung schon wieder geschlossen wurde, muss sie neu aufgebaut werden und ei neues Statement-Objekt muss erzeugt werden.
      if (verbindung == null || verbindung.isClosed()) {
        verbindung = DriverManager.getConnection("jdbc:sqlite:"+dbName + ".db");
        statement = verbindung.createStatement();
      }
    //Die SQL-Anweisung zum Einf&uuml;gen wird ausgef&uuml;hrt und das Ergebnis durchgereicht.    
    ergebnis = statement.executeUpdate(sql);
    }catch(SQLException e){
       e.printStackTrace();
    }  
    return ergebnis;  
    } 
  
     /**
  * f&uuml;hrt die &uuml;bergebene SQL-Update-Anweisung aus
  * @param sql Die SQL-Anweisung, die ausgef&uuml;hrt werden soll, als Zeichenkette
  * @return Die Anzahl der betroffenen Datens&auml;tze. Der R&uuml;ckgabewert ist 0, wenn die Anweisung keine &Auml;nderung in der Datenbank bewirkt hat. 
  * Der R&uuml;ckgabewert -1 zeigt an, dass ein Fehler aufgetreten ist.
  */  
    public int datensatzAendern(String sql){
    int ergebnis = -1;
    try{
      //falls die Verbindung schon wieder geschlossen wurde, muss sie neu aufgebaut werden und ein neues Statement-Objekt muss erzeugt werden.
      if (verbindung == null || verbindung.isClosed()) {
        verbindung = DriverManager.getConnection("jdbc:sqlite:"+dbName + ".db");
        statement = verbindung.createStatement();
      }
      
    //Die SQL-Anweisung zum &Auml;ndern wird ausgef&uuml;hrt und das Ergebnis durchgereicht.
    ergebnis = statement.executeUpdate(sql);
    }catch(Exception e){
      e.printStackTrace();
    }  
    return ergebnis;  
    }
  
   /**
  * f&uuml;hrt die &uuml;bergebene SQL-L&ouml;sch-Anweisung aus
  * @param sql Die SQL-Anweisung, die ausgef&uuml;hrt werden soll, als Zeichenkette
  * @return Die Anzahl der betroffenen Datens&auml;tze. Der R&uuml;ckgabewert ist 0, wenn die Anweisung keine &Auml;nderung in der Datenbank bewirkt hat. 
  * Der R&uuml;ckgabewert -1 zeigt an, dass ein Fehler aufgetreten ist.
  */  
   public int datensatzLoeschen(String sql){
    int ergebnis = -1;
    try{
      //falls die Verbindung schon wieder geschlossen wurde, muss sie neu aufgebaut werden und ei neues Statement-Objekt muss erzeugt werden.
      if (verbindung == null || verbindung.isClosed()) {
        verbindung = DriverManager.getConnection("jdbc:sqlite:"+dbName + ".db");
        statement = verbindung.createStatement();
      }
      
    //Die SQL-Anweisung zum L&ouml;schen wird ausgef&uuml;hrt und das Ergebnis durchgereicht.  
    ergebnis = statement.executeUpdate(sql);
    }catch(Exception e){
      e.printStackTrace();
    }  
    return ergebnis;  
    }
  // Ende Methoden

} // end of DBManager

