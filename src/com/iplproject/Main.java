package com.iplproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


public class Main {

    public static final int ID = 0;
    public static final int MATCH_SEASON = 1;
    public static final int MATCH_CITY = 2;
    public static final int MATCH_DATE = 3;
    public static final int MATCH_TEAM1 = 4;
    public static final int MATCH_TEAM2 = 5;
    public static final int MATCH_TOSS_WINNER = 6;
    public static final int MATCH_TOSS_DECISION = 7;
    public static final int MATCH_RESULT = 8;
    public static final int MATCH_DL_APPLIED = 9;
    public static final int MATCH_WINNER = 10;
    public static final int MATCH_WIN_BY_RUNS = 11;
    public static final int MATCH_WIN_BY_WICKETS = 12;
    public static final int MATCH_PLAYER_OF_MATCH = 13;
    public static final int MATCH_VENUE = 14;
    public static final int MATCH_UMPIRE1 = 15;
    public static final int MATCH_UMPIRE2 = 16;
    public static final int DELIVERY_INNING = 1;
    public static final int DELIVERY_BATTING_TEAM = 2;
    public static final int DELIVERY_BOWLING_TEAM = 3;
    public static final int DELIVERY_OVER = 4;
    public static final int DELIVERY_BALL = 5;
    public static final int DELIVERY_BATSMAN = 6;
    public static final int DELIVERY_NON_STRIKER = 7;
    public static final int DELIVERY_BOWLER = 8;
    public static final int DELIVERY_IS_SUPER_OVER = 9;
    public static final int DELIVERY_WIDE_RUN = 10;
    public static final int DELIVERY_BYE_RUNS = 11;
    public static final int DELIVERY_LEG_BYE_RUNS = 12;
    public static final int DELIVERY_NO_BALL_RUNS = 13;
    public static final int DELIVERY_PENALTY_RUNS = 14;
    public static final int DELIVERY_BATSMAN_RUNS = 15;
    public static final int DELIVERY_EXTRA_RUNS = 16;
    public static final int DELIVERY_TOTAL_RUNS = 17;
    public static final int DELIVERY_PLAYER_DISMISSED = 18;
    public static final int DELIVERY_DISMISSAL_KIND = 19;
    public static final int DELIVERY_FIELDER = 20;

    public static void main(String[] args) {

        List<Match> matches = readMatchesData();
        List<Delivery> deliveries = readDeliveriesData();
        calculateMatchesPlayed(matches);
        System.out.println();
        matchesWonByTeamOverYears(matches);
        System.out.println();
        extraRunsPerTeamIn2016(matches,deliveries);
        System.out.println();
        topEconomicalBowler2015(matches,deliveries);
        System.out.println();
        matchesTiedInYearsInBetweenTeams(matches);
        System.out.println();


    }

    private static void matchesTiedInYearsInBetweenTeams(List<Match> matches) {
        for(Match match : matches){
            if(match.getResult().equals("tie")){
                System.out.println(match.getSeason() + " "+ match.getTeam1() + " " + match.getTeam2() + " " + match.getResult());
            }
        }
    }

    private static void topEconomicalBowler2015(List<Match> matches, List<Delivery> deliveries) {
        List<String> saveIdsFromDeliveries = new ArrayList<>();
        for(Match match : matches){
            if(match.getSeason().equals("2015")){
                saveIdsFromDeliveries.add(match.getId());
            }
        }
        HashMap<String,Integer> storeBallerCount = new HashMap<>();
        HashMap<String,Integer> storeBallerRun = new HashMap<>();
        for(Delivery delivery : deliveries){
            if(saveIdsFromDeliveries.contains(delivery.getMatchId())){
                if(storeBallerCount.containsKey(delivery.getBowler())){
                    int value = storeBallerCount.get(delivery.getBowler());
                    storeBallerCount.put(delivery.getBowler(),value+1);
                }
                else{
                    storeBallerCount.put(delivery.getBowler(),1);
                }
                if(storeBallerRun.containsKey(delivery.getBowler())){
                    int value = storeBallerRun.get(delivery.getBowler());
                    storeBallerRun.put(delivery.getBowler(),value+Integer.parseInt(delivery.getTotalRuns()));
                }
                else{
                    storeBallerRun.put(delivery.getBowler(),Integer.parseInt(delivery.getTotalRuns()));
                }
            }
        }
        HashMap<String,Double> economicalBowler = new HashMap<>();
        HashMap<String,Double> reducedOverForBowler = new HashMap<>();
        for(Map.Entry storeOver : storeBallerCount.entrySet()){
            int findOver = (int)(storeOver.getValue());
            int findRestBalls = findOver%6;
            double findOvers = (double)findOver/6;
            double precisionNumber = (double)findRestBalls/6;
            findOvers+=precisionNumber;
            reducedOverForBowler.put((String) storeOver.getKey(),findOvers);
        }
        for(Map.Entry finalStore : storeBallerRun.entrySet()){
            double over = reducedOverForBowler.get(finalStore.getKey());
            int runs = (int) finalStore.getValue();
            double economy = runs/over;
            economicalBowler.put((String) finalStore.getKey(),economy);
        }
        double minimum = Collections.min(economicalBowler.values());

        for(Map.Entry mp : economicalBowler.entrySet()){
            if(mp.getValue().equals(minimum))
                System.out.println(mp.getKey()+ " "+String.format("%.2f",minimum) );
        }
    }

    private static void extraRunsPerTeamIn2016(List<Match> matches, List<Delivery> deliveries) {
        List<String> saveIdsFromMatches = new ArrayList<>();
        for(Match match : matches){
            if(match.getSeason().equals("2016")){
                saveIdsFromMatches.add(match.getId());
            }
        }
        HashMap<String,Integer> extraRuns = new HashMap<>();
        for(Delivery delivery : deliveries){
            if(saveIdsFromMatches.contains(delivery.getMatchId())){
                if(extraRuns.containsKey(delivery.getBattingTeam())){
                    int value = extraRuns.get(delivery.getBattingTeam());
                    extraRuns.put(delivery.getBattingTeam(),value+Integer.parseInt(delivery.getExtraRuns()));
                }
                else{
                    extraRuns.put(delivery.getBattingTeam(),Integer.parseInt(delivery.getExtraRuns()));
                }
            }
        }
        for(Map.Entry m: extraRuns.entrySet()){
            System.out.println(m.getKey()+" "+m.getValue());
        }

    }

    private static void matchesWonByTeamOverYears(List<Match> matches) {
        HashMap<String,Integer> teamWonOverYears = new HashMap<>();
        for(Match match : matches){
            if(teamWonOverYears.containsKey(match.getSeason()+" "+ match.getWinner())){
                int value = teamWonOverYears.get(match.getSeason()+" "+ match.getWinner());
                teamWonOverYears.put(match.getSeason()+" "+ match.getWinner(),value+1);
            }
            else{
                teamWonOverYears.put(match.getSeason()+" "+ match.getWinner(),1);
            }
        }
        for(Map.Entry m : teamWonOverYears.entrySet()){
            System.out.println(m.getKey()+" "+m.getValue());
        }
    }

    private static void calculateMatchesPlayed(List<Match> matches) {

        HashMap<String,Integer> mapStoringPlayedMatch = new HashMap<>();
        for(Match match : matches){
            if(mapStoringPlayedMatch.containsKey(match.getSeason())){
                int value = mapStoringPlayedMatch.get(match.getSeason());
                mapStoringPlayedMatch.put(match.getSeason(),value+1);
            }
            else{
                mapStoringPlayedMatch.put(match.getSeason(),1);
            }
        }

        System.out.println(mapStoringPlayedMatch);
    }

    private static List<Delivery> readDeliveriesData() {
        List<Delivery> deliveries = new ArrayList<>();
        try{
            BufferedReader readDeliveries = new BufferedReader(new FileReader("Data/deliveries.csv"));
            String line;
            while((line = readDeliveries.readLine())!=null)
            {
                List<String> lineDeliveries = new ArrayList<>(Arrays.asList(line.split(",")));
                Delivery delivery = new Delivery();
                delivery.setMatchId(lineDeliveries.get(ID));
                delivery.setInning(lineDeliveries.get(DELIVERY_INNING));
                delivery.setBattingTeam(lineDeliveries.get(DELIVERY_BATTING_TEAM));
                delivery.setBowlingTeam(lineDeliveries.get(DELIVERY_BOWLING_TEAM));
                delivery.setOver(lineDeliveries.get(DELIVERY_OVER));
                delivery.setBall(lineDeliveries.get(DELIVERY_BALL));
                delivery.setBatsman(lineDeliveries.get(DELIVERY_BATSMAN));
                delivery.setNonStriker(lineDeliveries.get(DELIVERY_NON_STRIKER));
                delivery.setBowler(lineDeliveries.get(DELIVERY_BOWLER));
                delivery.setIsSuperOver(lineDeliveries.get(DELIVERY_IS_SUPER_OVER));
                delivery.setWideRuns(lineDeliveries.get(DELIVERY_WIDE_RUN));
                delivery.setByeRuns(lineDeliveries.get(DELIVERY_BYE_RUNS));
                delivery.setLegByeRuns(lineDeliveries.get(DELIVERY_LEG_BYE_RUNS));
                delivery.setNoballRuns(lineDeliveries.get(DELIVERY_NO_BALL_RUNS));
                delivery.setPenaltyRuns(lineDeliveries.get(DELIVERY_PENALTY_RUNS));
                delivery.setBatsmanRuns(lineDeliveries.get(DELIVERY_BATSMAN_RUNS));
                delivery.setExtraRuns(lineDeliveries.get(DELIVERY_EXTRA_RUNS));
                delivery.setTotalRuns(lineDeliveries.get(DELIVERY_TOTAL_RUNS));
                if(lineDeliveries.size()> DELIVERY_TOTAL_RUNS +1)
                    delivery.setPlayerDismissed(lineDeliveries.get(DELIVERY_PLAYER_DISMISSED));
                else
                    delivery.setPlayerDismissed("");
                if(lineDeliveries.size()> DELIVERY_PLAYER_DISMISSED +1)
                    delivery.setDismissalKind(lineDeliveries.get(DELIVERY_DISMISSAL_KIND));
                else
                    delivery.setDismissalKind("");
                if(lineDeliveries.size()> DELIVERY_DISMISSAL_KIND +1)
                    delivery.setFielder(lineDeliveries.get(DELIVERY_FIELDER));
                else
                    delivery.setFielder("");
                deliveries.add(delivery);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return deliveries;
    }

    private static List<Match> readMatchesData() {
        List<Match> matches = new ArrayList<>();
        try{
            BufferedReader readMatches = new BufferedReader(new FileReader("Data/matches.csv"));
            String line;
            while((line=readMatches.readLine())!=null){
                List<String> lineMatches= new ArrayList<>(Arrays.asList(line.split(",")));
                Match match = new Match();
                match.setId(lineMatches.get(ID));
                match.setSeason(lineMatches.get(MATCH_SEASON));
                match.setCity(lineMatches.get(MATCH_CITY));
                match.setDate(lineMatches.get(MATCH_DATE));
                match.setTeam1(lineMatches.get(MATCH_TEAM1));
                match.setTeam2(lineMatches.get(MATCH_TEAM2));
                match.setTossWinner(lineMatches.get(MATCH_TOSS_WINNER));
                match.setTossDecision(lineMatches.get(MATCH_TOSS_DECISION));
                match.setResult(lineMatches.get(MATCH_RESULT));
                match.setDlApplied(lineMatches.get(MATCH_DL_APPLIED));
                match.setWinner(lineMatches.get(MATCH_WINNER));
                match.setWinByRuns(lineMatches.get(MATCH_WIN_BY_RUNS));
                match.setWinByWickets(lineMatches.get(MATCH_WIN_BY_WICKETS));
                match.setPlayerOfMatch(lineMatches.get(MATCH_PLAYER_OF_MATCH));
                match.setVenue(lineMatches.get(MATCH_VENUE));
                if(lineMatches.size()> MATCH_VENUE +1){
                        match.setUmpire1(lineMatches.get(MATCH_UMPIRE1));
                }
                else{
                    match.setUmpire1("");
                }
                if(lineMatches.size()> MATCH_UMPIRE1 +1){
                    match.setUmpire2(lineMatches.get(MATCH_UMPIRE2));
                }
                else {
                    match.setUmpire2("");
                }

                matches.add(match);

            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return matches;
    }


}
