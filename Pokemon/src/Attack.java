/*Pokemon Project
Attack class
Nizar Alrifai
The attack class consists of a constructor and a toString method, all to set up
all possible attacks a pokemon can create and add them to an ArrayList in the pokemon class
so every pokemon object has it's own attack list.
 */


public class Attack { //constructor receives 4 portions of the split string from the txt file
    //and uses them to set up an attack
    public String name; //name of attack
    public int energycost; //cost of attack
    public float damage; //damage value, float due to some damages becoming floats when halved
    public String special; //special attribute of an attack
    public Attack(String one,String two,String three, String four){
        name=one;
        energycost=Integer.parseInt(two);
        damage=Integer.parseInt(three);
        special=four;
    }
    public String toString(){ //creating a table to easily display an attack when it is printed
        return String.format(ConsoleColors.BLUE+"\n╔═══════════════════════════════╗\n"+
                        ConsoleColors.BLACK+"║ |Name            %-13s║\n" +
                        ConsoleColors.GREEN+"║ |Cost            %-2d           ║\n"+
                        ConsoleColors.CYAN+"║ |Damage          %-2f    ║\n"+
                        ConsoleColors.PURPLE+"║ |Special         %-13s║\n"+
                        ConsoleColors.BLUE+"╚═══════════════════════════════╝"
                ,name,energycost,damage,special);
    }
}
