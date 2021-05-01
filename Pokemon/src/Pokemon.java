import java.util.*;
import java.util.stream.Collectors;

public class Pokemon {
    public ArrayList<Attack> attacklist= new ArrayList<>();
    private final String name;
    private final int hp;
    private float currhp;
    private int energy;
    private final String type;
    private final String resistance;
    private final String weakness;
    private String state;
    private boolean stun;
    public Pokemon(String info) {
        String [] stats = info.split(",");
        name = stats[0];
        hp = Integer.parseInt(stats[1]);
        currhp=hp;
        energy=50;
        type=stats[2];
        if(stats[3].equals(" ")){
            resistance="N/A";
        }
        else {
            resistance = stats[3];
        }
        if(stats[4].equals(" ")){
            weakness="N/A";
        }
        else {
            weakness = stats[4];
        }
        state="N/A";
        int p=6;
        for(int i=0; i<Integer.parseInt(stats[5]);i+=1){
            attacklist.add(new Attack(stats[p],stats[p+1],stats[p+2],stats[p+3]));
            p+=4;
        }
        stun=false;
    }
    public Boolean isAwake() {
        return currhp > 0;
    }
    public void recoverenergy(){
        energy+=10;
        if(energy>50){
            energy=50;
        }
    }
    public float getDamage(float damage,Pokemon attacker){
        if(attacker.state.equals("disabled")){
            damage-=10;
            if(damage<0){
                damage=0;
            }
        }
        if(weakness.equals(attacker.type)){
            damage*=2;
            System.out.println(ConsoleColors.BLUE+"Super effective!");
        }
        else if(resistance.equals(attacker.type)){
            damage*=0.5;
            System.out.println(ConsoleColors.BLUE+"not very effective....!");
        }
        return damage;
    }
    public boolean isNotStunned(){
        boolean bol;
        bol= !stun;
        return bol;
    }
    public void unstun(){
        stun=false;
    }
    public void attack(Attack selectedAttack,Pokemon attacker){
        attacker.energy-=selectedAttack.energycost;
        System.out.println(attacker.name+" used:"+selectedAttack.name);
        float damage=selectedAttack.damage;
        String special=selectedAttack.special;
        damage=getDamage(damage,attacker);
        Random random = new Random();
        int num = random.nextInt(2)+1;
        int success=1;
        int fail=2;
        if (special.equals("stun")) {
            if(num==success){
                stun=true;
                System.out.println(name+" is stunned");
            }
        }
        else if(special.equals("wild card")){
            if(num==fail){
                damage=0;
                System.out.println("Wild Card failed!");
            }
        }
        else if(special.equals("wild storm")){
            while(num==success){
                System.out.println("Wild storm succeeded");
                currhp-=damage;
                num = random.nextInt(2)+1;
            }
        }
        else if(special.equals("disable")){
            System.out.println("Enemy got disabled!");
            state="disabled";
        }
        else if(special.equals("recharge")){
            attacker.energy+=20;
            System.out.println("20 Energy was restored!");
        }
        currhp-=damage;
        if(currhp<0){
            currhp=0;
        }
        System.out.println(damage+" damage"+" was done");
    }
    public void battlerecover(){
        currhp+=20;
        if(currhp>hp){
            currhp=hp;
        }
        energy=50;
        state="N/A";

    }
    public String toString(){
        return String.format(ConsoleColors.BLUE+"\n╔═══════════════════════════════╗\n"+
                        ConsoleColors.BLACK+"║ |Name            %-13s║\n" +
                        ConsoleColors.GREEN+"║ |HP              [%-6.2f/%-3d] ║\n"+
                        ConsoleColors.CYAN+"║ |Type            %-13s║\n"+
                        ConsoleColors.PURPLE+"║ |Resistance      %-13s║\n"+
                        ConsoleColors.RED+"║ |Weakness        %-13s║\n"+
                        ConsoleColors.BLUE+"╚═══════════════════════════════╝"
                                            ,name,currhp,hp,type,resistance,weakness);
    }
    public void battlescreen(Pokemon npc){
        System.out.println(ConsoleColors.BLUE+"╔═══════════════════════════════╗    ╔═══════════════════════════════╗\n"+
                String.format(ConsoleColors.BLACK+"║ |Name            %-13s║    ║ |Name            %-13s║\n" +
                ConsoleColors.GREEN+"║ |HP              [%-6.2f/%-3d] ║" +ConsoleColors.RED+" VS "+ConsoleColors.GREEN+ "║ |HP              [%-6.2f/%-3d] ║\n"+
                ConsoleColors.YELLOW+"║ |Energy          [%-2d/50]      ║    ║ |Energy          [%-2d/50]      ║\n"+
                        ConsoleColors.BLUE+"╚═══════════════════════════════╝    ╚═══════════════════════════════╝\n",
                name,npc.name,currhp,hp,npc.currhp,npc.hp,energy,npc.energy));

    }
    public ArrayList<Attack> enoughEnergy(){
        return attacklist.stream().filter(currAttack -> energy - currAttack.energycost >= 0).collect(Collectors.toCollection(ArrayList::new));

    }

}
