/*Pokemon Project
Pokemon class
Nizar Alrifai
One of the biggest aspects of the pokemon project, strings are passed from the text file with
each line constructed into a new pokemon with its own stats and attributes and attacks
everything to do with pokemons, ranging from healing/recovering to inflecting damage is done here
other functions within include checking the state of pokemon(disabled/stun/KO) in addition
to proper handling of all attacks including getting damage and energy losses*/
import java.util.*;
public class Pokemon {
    private final ArrayList<Attack> attacklist= new ArrayList<Attack>();
    private final String name; //name of pokemon
    private final int hp; // max hit points of a pokemon, these are never affected
    private float currhp; //hit points of a pokemon that will change
    private int energy; //current energy of a pokemon up to a max of 50
    private final String type; //type of pokemon
    private final String resistance; //types this pokemon is strong against
    private final String weakness; //types this pokemon is weak against
    private String state; // checks for disables
    private boolean stun; //checks for stun
    public Pokemon(String info) { //using a string construct a pokemon with all of it's required stats
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
        state="N/A"; //not yet disabled
        int p=6;
        //allows for continuation of adding of attacks to the attack list for any number of attacks
        //works by calling the Attack class to make an attack object and store it in an ArrayList
        for(int i=0; i<Integer.parseInt(stats[5]);i+=1){
            attacklist.add(new Attack(stats[p],stats[p+1],stats[p+2],stats[p+3]));
            p+=4;
        }
        stun=false; //not yet stunned
    }
    public Boolean isAwake() {//checks if the pokemon is knocked out
        if (currhp > 0) {
            return true;
        }
        else return false;
    }
    public void recoverenergy(){ //end of turn energy recovery
        energy+=10;
        if(energy>50){
            energy=50;
        }
    }
    private float getDamage(float damage, Pokemon attacker){/*gets the appropriate damage
    the pokemon should deal by checking if it is disbaled or is dealing against a weakness or resistance
    the attacker and the supposed damage are paramters and the function is called by using
    the pokemon reciving the attack (all attributes directly used belong to defender
    i.e weakness/resistance)*/
        if(attacker.state.equals("disabled")){ //subtracts 10 dmg if disabled
            damage-=10;
            if(damage<0){
                damage=0;
            }
        }
        if(weakness.equals(attacker.type)){ //doubled damage
            damage*=2;
            System.out.println(ConsoleColors.BLUE+"Super effective!");
        }
        else if(resistance.equals(attacker.type)){
            damage*=0.5;
            System.out.println(ConsoleColors.BLUE+"not very effective....!");
        }
        return damage;
    }
    public boolean isNotStunned(){ //checks if the pokemon is stunned and returns a boolean
        boolean bol;
        if(stun){
            bol= false;
        }
        else{
            bol= true;
        }
        return bol;
    }
    public void unstun(){ //unstuns the pokemon after a turn where he was stunned
        stun=false;
    }
    public void attack(Attack selectedAttack,Pokemon attacker){
        /*taking the defender attributes the function takes the selected attack as a parameter
        as well as the attack pokemon. This method calls the getDamage mehtod to get the appropriate
        damage as well as subtracts the energy and applies all possible special effects
         */
        attacker.energy-=selectedAttack.energycost; //subtracting energy
        System.out.println(attacker.name+" used:"+selectedAttack.name);
        float damage=selectedAttack.damage; //getting initial damage
        String special=selectedAttack.special; //getting the special effect of the attack
        damage=getDamage(damage,attacker); //getting the damage after modifiers
        Random random = new Random(); //for the 50% chances
        int num = random.nextInt(2)+1;
        int success=1; //avoiding magic numbers
        int fail=2;
        if (special.equals("stun")) { //checks for the possibility of stunning
            if(num==success){
                stun=true;
                System.out.println(name+" is stunned");
            }
        }
        else if(special.equals("wild card")){ //wild card attack makes damage 0 upon failure
            if(num==fail){
                damage=0;
                System.out.println("Wild Card failed!");
            }
        }
        else if(special.equals("wild storm")){//as long as the roll suceeded wild storm continues going on for free
            //the intial damage is taken off at the end of the attack method while all sequel attacks are handled with in the loop
            while(num==success){ //keeps going till roll fails
                System.out.println("Wild storm succeeded");
                currhp-=damage;
                num = random.nextInt(2)+1; //rerolls
            }
        }
        else if(special.equals("disable")){//disables the target pokemon
            System.out.println("Enemy got disabled!");
            state="disabled";
        }
        else if(special.equals("recharge")){ //recharges energy of user
            attacker.energy+=20;
            if(attacker.energy>50){
                attacker.energy=50;
            }
            System.out.println("20 Energy was restored!");
        }
        currhp-=damage; //dealing damage
        if(currhp<0){ //no such thing as an overkill a pokemon can only be KO
            //anything less than 0 implies a pokemon is dead which is barbaric
            currhp=0;
        }
        System.out.println(damage+" damage"+" was done");
    }
    public void battlerecover(){//after winning a fight, pokemons are healed, recover energy and
        //disables are removed
        currhp+=20;
        if(currhp>hp){
            currhp=hp;
        }
        energy=50;
        state="N/A";

    }
    public String toString(){
        //formating for pokemon selection
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
        //method to display pokemons in a fight
        System.out.println(ConsoleColors.BLUE+"╔═══════════════════════════════╗    ╔═══════════════════════════════╗\n"+
                String.format(ConsoleColors.BLACK+"║ |Name            %-13s║    ║ |Name            %-13s║\n" +
                ConsoleColors.GREEN+"║ |HP              [%-6.2f/%-3d] ║" +ConsoleColors.RED+" VS "+ConsoleColors.GREEN+ "║ |HP              [%-6.2f/%-3d] ║\n"+
                ConsoleColors.YELLOW+"║ |Energy          [%-2d/50]      ║    ║ |Energy          [%-2d/50]      ║\n"+
                        ConsoleColors.BLUE+"╚═══════════════════════════════╝    ╚═══════════════════════════════╝\n",
                name,npc.name,currhp,hp,npc.currhp,npc.hp,energy,npc.energy));

    }
    public ArrayList<Attack> enoughEnergy(){
        //outputs an available attack ArrayList by checking wether the pokemon has enough energy
        //for said attack
        ArrayList<Attack> validAttacks=new ArrayList<Attack>();
        for(int i=0;i<attacklist.size();i++){
            Attack currAttack=attacklist.get(i);
            if(energy-currAttack.energycost>=0){
                validAttacks.add(currAttack);
            }
        }
        return validAttacks;
    }

}
