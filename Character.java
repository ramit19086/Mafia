import java.lang.invoke.TypeDescriptor;
import java.util.Comparator;
import java.lang.Class;
public abstract class Character implements Comparable<Character> {
    protected double HP;

    protected int Playerno;

    protected int dead=0;

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    protected int vote=0;

    public String getRole() {
        return role;
    }

    protected final String role;

    public int getDead() {
        return dead;
    }

    public void setDead(int dead) {
        this.dead = dead;
    }

    public String getName() {
        return name;
    }

    protected final String name;

    public Character(int HP, int playerno,String role) {
        this.HP = HP;
        Playerno = playerno;
        name=("Player "+ playerno);
        this.role=role;
        this.dead=0;
    }

    @Override
    public int compareTo(Character o) {
        if(o==null) return -1;
        return (this.getVote()>o.getVote())?-1:1;
    }

    @Override
    public boolean equals(Object o) {
        if(o != null && getClass() == o.getClass()) {
            Character p = (Character) o;
            return (p.getPlayerno() == Playerno);
        } else {
            return false;
        }
    }

    public void display(){
        System.out.println("Player"+Playerno);
    }


    public int getPlayerno() {
        return Playerno;
    }

    public void setPlayerno(int playerno) {
        Playerno = playerno;
    }

    public double getHP() {
        return HP;
    }

    public void setHP(double HP) {
        this.HP = HP;
    }


}
