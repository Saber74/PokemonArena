public class Attack {
    public String name;
    public int energycost;
    public float damage;
    public String special;
    public Attack(String one,String two,String three, String four){
        name=one;
        energycost=Integer.parseInt(two);
        damage=Integer.parseInt(three);
        special=four;
    }
    public String toString(){
        return String.format(ConsoleColors.BLUE+"\n╔═══════════════════════════════╗\n"+
                        ConsoleColors.BLACK+"║ |Name            %-13s║\n" +
                        ConsoleColors.GREEN+"║ |Cost            %-2d           ║\n"+
                        ConsoleColors.CYAN+"║ |Damage          %-2f    ║\n"+
                        ConsoleColors.PURPLE+"║ |Special         %-13s║\n"+
                        ConsoleColors.BLUE+"╚═══════════════════════════════╝"
                ,name,energycost,damage,special);
    }
}
