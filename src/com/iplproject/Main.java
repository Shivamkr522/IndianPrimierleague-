package com.iplproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


public class Main {

    public static final int SET_ID = 0;
    public static final int SET_SEASON = 1;
    public static final int SET_CITY = 2;
    public static final int SET_DATE = 3;
    public static final int SET_TEAM1 = 4;
    public static final int SET_TEAM2 = 5;
    public static final int SET_TOSS_WINNER = 6;
    public static final int SET_TOSS_DECISION = 7;
    public static final int SET_RESULT = 8;
    public static final int SET_DL_APPLIED = 9;
    public static final int SET_WINNER = 10;
    public static final int SET_WIN_BY_RUNS = 11;
    public static final int SET_WIN_BY_WICKETS = 12;
    public static final int SET_PLAYER_OF_MATCH = 13;
    public static final int SET_VENUE = 14;
    public static final int SET_UMPIRE1 = 15;
    public static final int SET_UMPIRE2 = 16;
    public static final int SET_DELIVERY_INNING = 1;
    public static final int SET_DELIVERY_BATTING_TEAM = 2;
    public static final int SET_DELIVERY_BOWLING_TEAM = 3;
    public static final int SET_DELIVERY_OVER = 4;
    public static final int SET_DELIVERY_BALL = 5;
    public static final int SET_DELIVERY_BATSMAN = 6;
    public static final int SET_DELIVERY_NON_STRIKER = 7;
    public static final int SET_DELIVERY_BOWLER = 8;
    public static final int SET_DELIVERY_IS_SUPER_OVER = 9;
    public static final int SET_DELIVERY_WIDE_RUN = 10;
    public static final int SET_DELIVERY_BYE_RUNS = 11;
    public static final int SET_DELIVERY_LEG_BYE_RUNS = 12;
    public static final int SET_DELIVERY_NO_BALL_RUNS = 13;
    public static final int SET_DELIVERY_PENALTY_RUNS = 14;
    public static final int SET_DELIVERY_BATSMAN_RUNS = 15;
    public static final int SET_DELIVERY_EXTRA_RUNS = 16;
    public static final int SET_DELIVERY_TOTAL_RUNS = 17;
    public static final int SET_DELIVERY_PLAYER_DISMISSED = 18;
    public static final int SET_DELIVERY_DISMISSAL_KIND = 19;
    public static final int SET_DELIVERY_FIELDER = 20;

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
            double finalOver = BigDecimal.valueOf(findOvers).setScale(2, RoundingMode.HALF_UP).doubleValue();
            reducedOverForBowler.put((String) storeOver.getKey(),finalOver);
        }
        for(Map.Entry finalStore : storeBallerRun.entrySet()){
            double over = reducedOverForBowler.get(finalStore.getKey());
            int runs = (int) finalStore.getValue();
            double economy = runs/over;
            economicalBowler.put((String) finalStore.getKey(),economy);
        }
        double maximum = Collections.min(economicalBowler.values());

        for(Map.Entry mp : economicalBowler.entrySet()){
            if(mp.getValue().equals(maximum))
                System.out.println(mp.getKey()+ " "+ maximum);
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
            BufferedReader readDeliveries = new BufferedReader(new FileReader("/home/shivam/Project1/deliveries.csv"));
            String line;
            while((line = readDeliveries.readLine())!=null)
            {
                List<String> lineDeliveries = new ArrayList<>(Arrays.asList(line.split(",")));
                Delivery delivery = new Delivery();
                delivery.setMatchId(lineDeliveries.get(SET_ID));
                delivery.setInning(lineDeliveries.get(SET_DELIVERY_INNING));
                delivery.setBattingTeam(lineDeliveries.get(SET_DELIVERY_BATTING_TEAM));
                delivery.setBowlingTeam(lineDeliveries.get(SET_DELIVERY_BOWLING_TEAM));
                delivery.setOver(lineDeliveries.get(SET_DELIVERY_OVER));
                delivery.setBall(lineDeliveries.get(SET_DELIVERY_BALL));
                delivery.setBatsman(lineDeliveries.get(SET_DELIVERY_BATSMAN));
                delivery.setNonStriker(lineDeliveries.get(SET_DELIVERY_NON_STRIKER));
                delivery.setBowler(lineDeliveries.get(SET_DELIVERY_BOWLER));
                delivery.setIsSuperOver(lineDeliveries.get(SET_DELIVERY_IS_SUPER_OVER));
                delivery.setWideRuns(lineDeliveries.get(SET_DELIVERY_WIDE_RUN));
                delivery.setByeRuns(lineDeliveries.get(SET_DELIVERY_BYE_RUNS));
                delivery.setLegByeRuns(lineDeliveries.get(SET_DELIVERY_LEG_BYE_RUNS));
                delivery.setNoballRuns(lineDeliveries.get(SET_DELIVERY_NO_BALL_RUNS));
                delivery.setPenaltyRuns(lineDeliveries.get(SET_DELIVERY_PENALTY_RUNS));
                delivery.setBatsmanRuns(lineDeliveries.get(SET_DELIVERY_BATSMAN_RUNS));
                delivery.setExtraRuns(lineDeliveries.get(SET_DELIVERY_EXTRA_RUNS));
                delivery.setTotalRuns(lineDeliveries.get(SET_DELIVERY_TOTAL_RUNS));
                if(lineDeliveries.size()>SET_DELIVERY_TOTAL_RUNS+1)
                    delivery.setPlayerDismissed(lineDeliveries.get(SET_DELIVERY_PLAYER_DISMISSED));
                else
                    delivery.setPlayerDismissed("");
                if(lineDeliveries.size()>SET_DELIVERY_PLAYER_DISMISSED+1)
                    delivery.setDismissalKind(lineDeliveries.get(SET_DELIVERY_DISMISSAL_KIND));
                else
                    delivery.setDismissalKind("");
                if(lineDeliveries.size()>SET_DELIVERY_DISMISSAL_KIND+1)
                    delivery.setFielder(lineDeliveries.get(SET_DELIVERY_FIELDER));
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
            BufferedReader readMatches = new BufferedReader(new FileReader("/home/shivam/Project1/matches.csv"));
            String line;
            while((line=readMatches.readLine())!=null){
                List<String> lineMatches= new ArrayList<>(Arrays.asList(line.split(",")));
                Match match = new Match();
                match.setId(lineMatches.get(SET_ID));
                match.setSeason(lineMatches.get(SET_SEASON));
                match.setCity(lineMatches.get(SET_CITY));
                match.setDate(lineMatches.get(SET_DATE));
                match.setTeam1(lineMatches.get(SET_TEAM1));
                match.setTeam2(lineMatches.get(SET_TEAM2));
                match.setTossWinner(lineMatches.get(SET_TOSS_WINNER));
                match.setTossDecision(lineMatches.get(SET_TOSS_DECISION));
                match.setResult(lineMatches.get(SET_RESULT));
                match.setDlApplied(lineMatches.get(SET_DL_APPLIED));
                match.setWinner(lineMatches.get(SET_WINNER));
                match.setWinByRuns(lineMatches.get(SET_WIN_BY_RUNS));
                match.setWinByWickets(lineMatches.get(SET_WIN_BY_WICKETS));
                match.setPlayerOfMatch(lineMatches.get(SET_PLAYER_OF_MATCH));
                match.setVenue(lineMatches.get(SET_VENUE));
                if(lineMatches.size()>SET_VENUE+1){
                        match.setUmpire1(lineMatches.get(SET_UMPIRE1));
                }
                else{
                    match.setUmpire1("");
                }
                if(lineMatches.size()>SET_UMPIRE1+1){
                    match.setUmpire2(lineMatches.get(SET_UMPIRE2));
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
