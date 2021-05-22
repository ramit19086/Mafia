import java.io.CharConversionException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Random;

public class Game {
    private int N;
    private int mafiaN;
    private int detectiveN;
    private int healerN;
    private int commonerN;
    private int NotmafiaN;
    private int alive;
    private int mafiaalive;
    private int detectivealive;
    private int healeralive;

    int[] a;

    private final Scanner sc=new Scanner(System.in);

    private final Random rand=new Random();

    private ArrayList<Character> players=new ArrayList<>();
    private ArrayList<Mafia> m=new ArrayList<>();
    private ArrayList<Detective> d=new ArrayList<>();
    private ArrayList<Commoner> c=new ArrayList<>();
    private ArrayList<Healer> h=new ArrayList<>();

    user u=new user();



    Game(){

    }

    public void start(){
        System.out.println("Welcome to Mafia");
        getnoofplayers();
        assigncharacter();
        int round=0;
        while(play(++round)!=1){}
        System.out.println("These players were mafias");
        for(Mafia ma:m)
            ma.display();
        System.out.println("These players were Detectives");
        for(Detective dd:d)
            dd.display();
        System.out.println("These players were Healers");
        for (Healer hh:h)
            hh.display();
        System.out.println("These were commoners");
        for (Commoner cc:c)
            cc.display();
        return;
    }

    private int play(int i){

        System.out.println("Round "+ i);

        System.out.println(alive+" players are remaining");

        for(Character c:players){
            if(c!=null&&c.getDead()==0){
                c.display();
            }
        }

        int kill=mafiakill();
        System.out.println("Mafias have decided their Target");

        int voteornot=detectivecheck();
        System.out.println("Detectives have tested");

        heal();
        System.out.println("Healers have chosen someone to heal");

        System.out.println("---End of Actions---");

        dothemath(kill);

        voteout(voteornot);

        System.out.println("End of round "+i);

        return checkgame();



    }


    private int checkgame(){
        if(2*mafiaalive>=alive){
            System.out.println("Game Over\nMafia's have won ");
            return 1;
        }
        else if(mafiaalive<=0){
            System.out.println("Game Over\nMafia's have lost");
            return 1;
        }
        return 0;
    }

    private void voteout(int v){

        if(v!=0){
            System.out.println("Player"+v+" was the mafia and checked by the detectives.");
            System.out.println("Player"+v+" has been voted out");
            Character c= players.get(v);
            if(c.equals(u.getUser())){
                System.out.println("User have been Voted out");
            }
            c.setHP(0);
            c.setDead(1);
            alive--;
            mafiaalive--;
            return;
        }
        if(u.getUser().getDead()==0){
            System.out.println("Select a person to vote out");
            int kill=sc.nextInt();
            while(!(kill>0&&kill<N+1)) {
                System.out.println("Select a person to vote out");
                kill=sc.nextInt();
            }
            Character c=players.get(kill);
            while(c.getDead()==1){
                System.out.println("You cannot vote a dead player");
                System.out.println("Select a person to vote out");
                kill=sc.nextInt();
                while(!(kill>0&&kill<N+1)) {
                    System.out.println("Select a person to vote out");
                    kill=sc.nextInt();
                }
                c=players.get(kill);
            }
            c.setVote(c.getVote()+1);
        }
        for(Character p:players) {
            if(p!=null&&p.getDead()==0){
                int kill = 1 + rand.nextInt(N);
                Character c = players.get(kill);
                while (c.getDead() == 1|kill==p.getPlayerno()) {
                    kill = 1 + rand.nextInt(N);
                    c = players.get(kill);
                }
                c.setVote(c.getVote()+1);
            }
        }
        ArrayList<Character> forsort =new ArrayList<>(players);
        Collections.sort(forsort);
        if(forsort.get(0).getVote()==forsort.get(1).getVote()){
            System.out.println("Votes are Tied Vote Again");
            clearvote();
            voteout(0);
            return;
        }
        clearvote();
        Character c=forsort.get(0);
        if(c.equals(u.getUser())){
            System.out.println("User have been Voted out");
        }
        c.setDead(1);
        c.setHP(0);
        alive--;
        if(c.getRole().compareTo("healer")==0)
            healeralive--;
        if(c.getRole().compareTo("detective")==0)
            detectivealive--;
        if(c.getRole().compareTo("mafia")==0)
            mafiaalive--;
        System.out.println("Player"+c.getPlayerno()+" has been voted out");
    }


    private void clearvote(){
        for (Character cc:c) if(cc!=null)cc.setVote(0);
    }

    private void dothemath(int kill)
    {
        int totalmafiaHP=0;

        Character c=players.get(kill);

        int t=0;
        for(Mafia ma:m){
            if(ma.getDead()==0){
                if(ma.getHP()>0) t++;
                totalmafiaHP+=ma.getHP();
            }
        }
        double toreduce=c.getHP();
        double bysingle=toreduce/(double)mafiaalive;

        if(c.getHP()<=totalmafiaHP){
            c.setDead(1);
            c.setHP(0);
            alive--;
            if(c.getRole().compareTo("healer")==0)
                healeralive--;
            if(c.getRole().compareTo("detective")==0)
                detectivealive--;
            if(c.equals(u.getUser())){
                System.out.println("User have been Voted out");
            }
            System.out.println("Player"+kill+" has died");
            while(toreduce>0) {
                t=0;
                for (Mafia ma : m) {
                    if (ma.getDead() == 0) {
                        if (ma.getHP() >= bysingle && toreduce > 0) {
                            ma.setHP(ma.getHP() - bysingle);
                            toreduce -= bysingle;
                            t++;
                        } else if (toreduce > 0) {
                            toreduce -= ma.getHP();
                            ma.setHP(0);
                        }
                    }
                }
                bysingle=toreduce/(double)t;
            }

        }

        else
        {
            System.out.println("Noone has died");

            while(t>0) {
                t=0;
                for (Mafia ma : m) {

                    if (ma.getDead() == 0) {
                        if (ma.getHP() >= bysingle && toreduce > 0) {
                            ma.setHP(ma.getHP() - bysingle);
                            toreduce -= bysingle;
                            t++;
                        } else if (toreduce > 0) {
                            toreduce -= ma.getHP();
                            ma.setHP(0);
                        }
                    }
                }
                bysingle=toreduce/(double)t;
            }
        }



    }

    private void heal(){
        if(healeralive<=0) return;
        int kill;
        if(u.getUser().getDead()==0&&u.getUser().getRole().compareTo("healer")==0){
            System.out.println("Enter player you want to heal");
            kill=sc.nextInt();
            while(!(kill>0&&kill<N+1)) {
                System.out.println("Enter player you want to heal");
                kill=sc.nextInt();
            }
            Character c=players.get(kill);
            while(c.getDead()==1){
                System.out.println("you cannot heal a dead person");
                System.out.println("Enter player you want to heal");
                kill=sc.nextInt();
                c=players.get(kill);
            }
            c.setHP(c.getHP()+500);
        }
        else {
             kill = 1 + rand.nextInt(N);
            Character c = players.get(kill);
            while (c.getDead() != 0) {
                kill = 1 + rand.nextInt(N);
                c = players.get(kill);
            }
            c.setHP(c.getHP()+500);
        }
    }



    private int detectivecheck(){
        if(detectivealive<=0)
            return 0;
        int kill=0;
        if(u.getUser().getRole().compareTo("detective")==0&&u.getUser().getDead()==0){
            System.out.println("Enter player you want to test");
            kill=sc.nextInt();
            while(!(kill>0&&kill<N+1)) {
                System.out.println("Enter player you want to test");
                kill=sc.nextInt();
            }
            Character c=players.get(kill);
            while(c.getDead()==1||c.getRole().compareTo("detective")==0){
                System.out.println("You cannot test a dead player or a fellow detective");
                System.out.println("Enter player you want to test");
                kill=sc.nextInt();
                while(!(kill>0&&kill<N+1)) {
                    System.out.println("Enter player you want to test");
                    kill=sc.nextInt();
                }
                c=players.get(kill);
            }
            if(c.getRole().compareTo("mafia")==0) {
                System.out.println("Player"+kill+"is a mafia");return kill;}
            else{System.out.println("Player"+kill+"is not a mafia"); return 0;}
        }
        else
        {
            kill=1+rand.nextInt(N);
            Character c=players.get(kill);
            while(c.getDead()==1||c.getRole().compareTo("detective")==0){
                kill=1+rand.nextInt(N);
                c=players.get(kill);
            }
            if(c.getRole().compareTo("mafia")==0)return kill;
            else return 0;

        }
    }

    private int mafiakill(){
        int kill=0;
        if(u.getUser().getRole().compareTo("mafia")==0&&u.getUser().getDead()==0){
            System.out.println("Enter target");
            kill=sc.nextInt();
            while(!(kill>0&&kill<N+1)) {
                System.out.println("Enter target");
                kill=sc.nextInt();
            }
            Character c=players.get(kill);
            while(c.getDead()==1||c.getRole().compareTo("mafia")==0){
                System.out.println("You cannot kill a dead player or a mafia");
                System.out.println("Enter target");
                kill=sc.nextInt();
                while(!(kill>0&&kill<N+1)) {
                    System.out.println("Enter target");
                    kill=sc.nextInt();
                }
                c=players.get(kill);
            }
        }

        else{
            kill=1+rand.nextInt(N);
            Character c=players.get(kill);
            while(c.getDead()==1||c.getRole().compareTo("mafia")==0){
                kill=1+rand.nextInt(N);
                 c=players.get(kill);
            }

        }
        return kill;

    }


    private void assigncharacter() {

        for (int i = 1; i <=N; i++) {
            a[i-1]=i;
        }
        int i=N;
        for (int j = 0; j < mafiaN; j++) {
            int r=rand.nextInt(i);
            i--;
            swap(a,i,r);
            m.add(new Mafia(a[i]));
            players.set(a[i],m.get(j));
        }
        for (int j = 0; j < detectiveN; j++) {
            int r=rand.nextInt(i);
            i--;
            swap(a,i,r);
            d.add(new Detective(a[i]));
            players.set(a[i],d.get(j));
        }
        for (int j = 0; j < healerN; j++) {
            int r=rand.nextInt(i);
            i--;
            swap(a,i,r);
            h.add(new Healer(a[i]));
            players.set(a[i],h.get(j));
        }
        for (int j = 0; j < commonerN; j++) {
            int r=rand.nextInt(i);
            i--;
            swap(a,i,r);
            c.add(new Commoner(a[i]));
            players.set(a[i],c.get(j));
        }

        int n=0;
        while(n<1||n>5){
            System.out.println("Choose a Character\n" +
                    "1) Mafia\n" +
                    "2) Detective\n" +
                    "3) Healer\n" +
                    "4) Commoner\n" +
                    "5) Assign Randomly\n");
            n=sc.nextInt();
        }
        if(n==5) n=1+rand.nextInt(5);
        Assignuser(n);
        System.out.println("Characters have been assigned");

    }

    private void Assignuser(int n){
        if(n==1){
            u.setUser(m.get(0));
            System.out.println("You are player "+u.getUser().getPlayerno());
            System.out.println("You are a Mafia");
            System.out.println("Other Mafia's are --> ");
            for(int i=1;i<m.size();i++){
                System.out.print(","+m.get(i).getName());
            }
            System.out.println();
        }
        if(n==2){
            u.setUser((d.get(0)));
            System.out.println("You are player "+u.getUser().getPlayerno());
            System.out.println("You are a Detective");
            System.out.println("Other Detectives are --> ");
            for(int i=1;i<d.size();i++){
                System.out.print(","+d.get(i).getName());
            }
            System.out.println();
        }
        if(n==3){
            u.setUser(h.get(0));
            System.out.println("You are player "+u.getUser().getPlayerno());
            System.out.println("You are a Healer");
            System.out.println("Other healers are --> ");
            for (int i = 1; i <h.size(); i++) {
                System.out.print(","+h.get(i).getName());
            }
            System.out.println();
        }
        if(n==4){
            u.setUser(c.get(0));
            System.out.println("You are player "+u.getUser().getPlayerno());
            System.out.println("You are a Commoner");
        }

    }

    private void swap(int[]a,int i,int j){
        int temp=a[i];
        a[i]=a[j];
        a[j]=temp;
    }




    public void getnoofplayers(){
        int n=0;
        while(n<5){
            System.out.println("Enter number of players greater than 4");
            n=sc.nextInt();
        }
        this.N=this.alive=n;
        this.detectivealive=this.detectiveN=this.mafiaalive=this.mafiaN=n/5;
        this.healeralive=this.healerN=Math.max(1,n/10);
        this.commonerN=n-this.detectiveN-this.mafiaN-this.healerN;
        players=new ArrayList<>(this.N);
        for (int i = 0; i <= N; i++) {
            players.add(null);
        }
        a=new int[N];
    }




    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public int getMafiaN() {
        return mafiaN;
    }

    public void setMafiaN(int mafiaN) {
        this.mafiaN = mafiaN;
    }

    public int getDetectiveN() {
        return detectiveN;
    }

    public void setDetectiveN(int detectiveN) {
        this.detectiveN = detectiveN;
    }

    public int getHealerN() {
        return healerN;
    }

    public void setHealerN(int healerN) {
        this.healerN = healerN;
    }

    public int getCommonerN() {
        return commonerN;
    }

    public void setCommonerN(int commonerN) {
        this.commonerN = commonerN;
    }

    public int getNotmafiaN() {
        return NotmafiaN;
    }

    public void setNotmafiaN(int notmafiaN) {
        NotmafiaN = notmafiaN;
    }
}
