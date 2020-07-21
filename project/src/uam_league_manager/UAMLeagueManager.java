/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uam_league_manager;

/**
 *
 * @author Sebastian
 */
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;


public class UAMLeagueManager implements LeagueManager {
    
    private static int cont;
    private final int numberOfClubs;
    private final ArrayList<FootballClub> league;
    private final Scanner scanner;
    private final ArrayList<Match> matches;
    private FileOutputStream fout;
    private FileInputStream fin;
    
    public UAMLeagueManager(int numberOfClubs) throws Exception{
        
        this.numberOfClubs = numberOfClubs;
        league = new ArrayList<>();
        matches = new ArrayList<>();
        scanner = new Scanner(System.in);
        displayMenu();
    }
    
    private void serializar(FootballClub club){
         /*  Serializador    */
         ObjectOutputStream oos;
        try{
            fout = new FileOutputStream("FootballClub.out");
            oos = new ObjectOutputStream(fout);   
            oos.writeObject(club);
            oos.close();
            System.out.println("Listo.");
            cont++;
       }catch(Exception ex){
           ex.printStackTrace();
           
       }
    }
    
    private FootballClub[] deserializar(){
        /*  Deserializador  */
        FootballClub[] club = new FootballClub[100];
        ObjectInputStream ois;
        int i = 0;
        
        try {
            fin = new FileInputStream("FootballClub.out");
            ois = new ObjectInputStream(fin);
            club[0] = (FootballClub) ois.readObject();
            while(i < cont){
                ois = new ObjectInputStream(fin);
                club[cont] = (FootballClub) ois.readObject();
                i++;
            }
            
            fin.close();
           
       } catch(Exception ex) {
           ex.printStackTrace();
           return null;
       }
        return club;
    }
    
    private void displayMenu() throws Exception{
        deserializar();
        /*  Fin Deserializador  */
        while(true) {
            System.out.println("*************   UAM     *********************\n");
            System.out.println("*************   TORNERO DE FUTBOL VARONIL     *********************");
            System.out.println("*************   TORNEO PANTERAS     *********************\n");
            System.out.println("MENU: \n");
            System.out.println("Crear un nuevo equipo y añadirlo a la liga (press 1)");
            System.out.println("Eliminar un equipo existente de la liga (press 2)");
            System.out.println("Mostrar estadisticas del equipo (press 3)");
            System.out.println("Mostrar Tabla de Torneo de Futbol Varonil (press 4)");
            System.out.println("Añadir un partido jugado (press 5)");
            System.out.println("Listar el calendario y encontrar un partido jugado (press 6)");
            System.out.println("Exit (press 7)");
            String line = scanner.nextLine();
            int command = 0;
            try {
                command = Integer.parseInt(line);
            } catch (NumberFormatException e) {
        }
            
            switch(command) {
                case 1 :
                   addTeam();
            break;
                case 2 :
                    deleteTeam();
                    break;
                case 3 :
                    displayStatistics();
                  break;
                case 4 :
                    displayLeagueTable();
                  break;
                case 5:
                    addPlayedMatch();
                   break;
                case 6:
                    displayCalendar();
                   break;
                case 7:
                   System.exit(0);
                   break;
            default:
            System.out.println("Tecla Incorrecta. Intente de Nuevo");
        }
        
    } 
   }
    
    private void addTeam() throws Exception{
        if(league.size() == numberOfClubs) {
            System.out.println("No se pueden añadir mas equipos a la liga\n");
            return;
        }
        
        FootballClub club = new FootballClub();
        System.out.println("Ingrese el nombre del equipo: ");
        String line = scanner.nextLine();
        club.setName(line);
        
        serializar(club);
        
        if(league.contains(club)){
            System.out.println("Este equipo ya esta añadido a la liga. Ingrese otro nombre del equipo");
            return;
        }
        System.out.println("\nIngrese la licenciatura a la que pertenece el equipo: ");
        line = scanner.nextLine();
        club.setLocation(line);
        /*Leer desde el archivo*/
        
        league.add(club);
        
    }
    
    private void deleteTeam() {
        System.out.println("Ingrese el nombre del equipo: ");
        String line = scanner.nextLine();
         for(FootballClub club : league) {
             if(club.getName().equals(line)){
                 league.remove(club);
                 System.out.println("Equipo "+ club.getName()+" se elimino");
                 cont--;
                 return;
             }
         }
         System.out.println("No hay informacion del equipo en la liga");
    }
    
    private void displayStatistics() {
        
        System.out.println("Ingrese el nombre del equipo: ");
        String line = scanner.nextLine();
         for (FootballClub club : league) {
             if(club.getName().equals(line)){
                 System.out.println("Equipo " + club.getName()+ " partidos ganados: " + club.getWinCount());
                 System.out.println("Equipo " + club.getName()+ " partidos perdidos: " + club.getDefeatCount());
                 System.out.println("Equipo " + club.getName()+ " partidos empatados: " + club.getDrawCount());
                 System.out.println("Equipo " + club.getName()+ " goles anotados: " + club.getScoredGoalsCount());
                 System.out.println("Equipo " + club.getName()+ " goles recividos: " + club.getReceivedGoalsCount());
                 System.out.println("Equipo " + club.getName()+ " puntos: " + club.getPoints());
                 System.out.println("Equipo " + club.getName()+ " partidos jugados: " + club.getMatchesPlayed());
                 return;
             }
         }
         System.out.println("No hay informacion del equipo en la liga");
    } 
    
    private void displayLeagueTable() {
        
        Collections.sort(league, new CustomComparator());
        for(FootballClub club : league) {
            System.out.println("Equipo: " + club.getName()+" Puntos: "+ club.getPoints()+" diferencia de goles: "+ (club.getScoredGoalsCount()-club.getReceivedGoalsCount()));
    }    
  }
    
    private void addPlayedMatch(){
        System.out.println("Ingrese la fecha en formato (format mm-dd-yyyy): ");
        String line = scanner.nextLine();
        Date date;
        try {
            date = new SimpleDateFormat("MM-dd-yyyy").parse(line);
        } catch (ParseException ex) {
            System.out.println("Tiene que ingresar la fecha en el formato mm-dd-yyyy");
            return;
        }
        System.out.println("Ingrese el equipo local: ");
        line = scanner.nextLine();
        FootballClub home = null;
          for(FootballClub club : league){
              if(club.getName().equals(line))
                  home = club;
          }  
          if (home == null) {
              System.out.println("No hay informacion del equipo en la liga");
              return;
          }
          System.out.println("Ingrese el equipo visitante: ");
          line = scanner.nextLine();
          FootballClub away = null;
           for(FootballClub club : league){
              if(club.getName().equals(line))
                  away = club;
          } 
           if (away == null) {
              System.out.println("No hay informacion del equipo en la liga");
              return;
          }
           
           System.out.println("Ingrese los goles del equipo local: ");
           line = scanner.nextLine();
           int homeGoals = -1;
             try {
                 homeGoals = Integer.parseInt(line);                
             } catch (Exception e) { 
    }
         if (homeGoals == -1) {
             System.out.println("Necesita ingresar el numero de goles anotados por el equipo");
             return;
         }
         
         System.out.println("Ingrese los goles del equipo visitante: ");
           line = scanner.nextLine();
           int awayGoals = -1;
             try {
                 awayGoals = Integer.parseInt(line);                
             } catch (Exception e) { 
    }
         if (awayGoals == -1) {
             System.out.println("Necesita ingresar el numero de goles anotados por el equipo");
             return;
         }
         
         
         Match match = new Match();
         match.setDate(date);
         match.setTeamA(home);
         match.setTeamB(away);
         match.setTeamAScore(awayGoals);
         match.setTeamBScore(homeGoals);
         matches.add(match);
         home.setScoredGoalsCount(home.getScoredGoalsCount()+homeGoals);
         away.setScoredGoalsCount(away.getScoredGoalsCount()+awayGoals);
         home.setRecievedGoalsCount(home.getReceivedGoalsCount()+awayGoals);
         away.setRecievedGoalsCount(away.getReceivedGoalsCount()+homeGoals);
         home.setMatchesPlayed(home.getMatchesPlayed()+1);
         away.setMatchesPlayed(away.getMatchesPlayed()+1);
         
         if (homeGoals > awayGoals) {            
             home.setPoints(home.getPoints()+3);
             home.setWinCount(home.getWinCount()+1);
             away.setDefeatCount(away.getDefeatCount()+1);
         }
         
         else if (homeGoals < awayGoals) {            
             away.setPoints(away.getPoints()+3);
             away.setWinCount(away.getWinCount()+1);
             home.setDefeatCount(home.getDefeatCount()+1);
         }
         else {
             home.setPoints(home.getPoints()+1);
             away.setPoints(away.getPoints()+1);
             home.setDrawCount(home.getDrawCount()+1);
             away.setDrawCount(away.getDrawCount()+1);
         }      
    } 
    
    private void displayCalendar() {
        
        System.out.println("Ingrese el año: ");
        String line = scanner.nextLine();
         int Y = -7777;
           try {
               Y = Integer.parseInt(line);
           } catch (Exception e) { 
    }
         if (Y == -7777) {
             System.out.println("Necesita ingresar el año");
             return;
         }
    
          System.out.println("Ingrese el Mes: ");
          line = scanner.nextLine();
          int M = 0;
           try {
               M = Integer.parseInt(line);
           } catch (Exception e) { 
    }
         if (M == 0) {
             System.out.println("Necesita ingresar el mes");
             return;
         }
         
         String[] months = {
            "",
             "Enero", "Febrero", "Marzo",
             "Abril", "Mayo", "Junio",
             "Julio", "Agosto", "Septiembre",
             "Octubre", "Noviembre", "Diciembre"  
         };
         
         int[] days = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
         
         if (M == 2 && isLeapYear(Y)) days[M] = 29;
         
         System.out.println("    " + months[M] + " " + Y);
         System.out.println("D  L  M  M  J  V  S");
         
         int d = day(M, 1, Y);
         String space = "";
         
         for (int i = 0; i < d; i++)
             System.out.print("   ");
         for (int i = 1; i <= days[M]; i++) {
             if (i<10)
                 System.out.print(i +"  ");
             else 
                 System.out.print(i+" ");
             if (((i + d) % 7 == 0) || (i == days[M])) System.out.println();
         }
         
         System.out.println("Ingrese el dia: ");
         line = scanner.nextLine();
         int D = 0;
           try {
               D= Integer.parseInt(line);
           }  catch (Exception e) {             
           }
       if (D == 0 || days[M] < D) {
           System.out.println("Tiene que ingresar el dia y el mes");
           return;
       }  
       
       Calendar cal = Calendar.getInstance();
       cal.set(Y, M-1, D);
       for (Match m : matches) {
           Calendar cal2 = Calendar.getInstance();
           cal2.setTime(m.getDate());
            if (cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) || cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) {
                System.out.println(m.getTeamA().getName()+ " "+m.getTeamAScore() + " : "+ m.getTeamBScore()+ " "+ m.getTeamB().getName());
            }
       }   
    } 
    
    public int day(int M, int D, int Y) {
        int y = Y - (14 - M) / 12;
        int x = y + y/4 - y/100 + y/400;
        int m = M + 12 * ((14-M) / 12) - 2;
        int d = (D + x + (31*m)/12) % 7;
        return d;
    }
    
    public boolean isLeapYear(int year) {
        
        if ((year % 4 ==0) && (year % 100 !=0 )) return true;
        if (year % 400 == 0) return true;
        return false;  
    }
} 
