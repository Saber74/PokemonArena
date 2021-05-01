/*Pokemon Project
Main class
Nizar Alrifai
Everything led our pokemons for the fight they will go through in the Arena!
pokemons are loaded here, then the user selects his pokemon and goes through battles
to become trainer supreme
 */
import java.util.*;
import java.io.*;
public class Arena {
    private static ArrayList<Pokemon> allPokes = new ArrayList<Pokemon>();//inital ArrayList of all pokemons, it's remains after selection is used for enemies
    private static ArrayList<Pokemon> selectedPokes = new ArrayList<Pokemon>(); //ArrayList including all pokemons picked by the user
    private static void load() {
        try {//checking if there is a text file, then reading it and adding pokemons using the pokemon constructor to the ArrayList
            Scanner file = new Scanner(new BufferedReader(new FileReader("pokemons.txt")));
            int n = Integer.parseInt(file.nextLine());
            for (int i = 0; i < n; i++) {
                String info = file.nextLine();
                allPokes.add(new Pokemon(info));
            }
        }
        catch (IOException ex) { //if the txt file does not exist gives an error
            System.out.println("Installation error,check that no file is missing then try again");
        }
    }

    private static void selectpokes() {//allows the user to select his team of 4 pokemons, the rest are enemies
        Scanner choice = new Scanner(System.in); //to get input from user
        for (int k = 0; k < 4; k += 0) {/*only increments when a valid pokemon is picked*/
            for (int i = 0; i < allPokes.size(); i++) {
                System.out.printf("\t\t\t  ❀%d❀%s\n", i + 1, allPokes.get(i));
                //shows the screen every time becuase it's easier to not mess up the arraylists
            }
            System.out.println("Select your pokemons by entering the number next to its name!");
            try{//checking for integer input if integer then the input is valid the pokemon is selected
                int numSelected=Integer.parseInt(choice.next());
                numSelected=getNum(numSelected, 1, allPokes.size());
                selectedPokes.add(allPokes.get(numSelected - 1)); //user inputs from 1-28 index starts at 0 so input-1
                allPokes.remove(numSelected - 1);
                k++; //increment for succeful pick

            }
            catch(NumberFormatException e){
                System.out.println("This is not a valid integer try again!");
            }
        }
    }

    private static int getNum(int usernum, int min, int max) {
        /*function that requires an input, a min and max possible value, if not with in the range
        it forces the user to go till they have a input within range*/
        if (usernum >= min && usernum <= max) {//in range , input is fine
            return usernum;
        }
        else {//not in range keep going
            System.out.println("Invalid please try again");
            Scanner choice = new Scanner(System.in);
            int userselect=choice.nextInt();
            while (!(userselect >= min && userselect <= max)) {
                System.out.println("Please enter a valid number");
                userselect = choice.nextInt();
            }
            return userselect;
        }
    }

    private static final int PLAYER = 1; //turn magic numbers
    private static final int NPC = 2;
    private static Pokemon fighter; //pokemon fighting for user
    private static Pokemon enemy; //pokemon fighting for computer

    private static void battleselect() {//selects a random enemy, and allows user to select his first choice in upcoming battle
        Scanner choice = new Scanner(System.in);
        if (turn == NPC) { //random enemy selection from remanining pokes
            if (allPokes.isEmpty() == false) {
                int randomenemy = (int) (Math.random() * allPokes.size() - 1 + 1);
                enemy = allPokes.get(randomenemy);
                allPokes.remove(enemy);
            }
        }
        else if (turn == PLAYER) {
            if (selectedPokes.isEmpty()==false) {
                //user selects pokemon from within range that is ensured using getNum method
                //then the user shouts i choose you
                for(int i=0;i<selectedPokes.size();i++){
                    System.out.printf(ConsoleColors.CYAN+"\t\t\t  ❀%d❀%s\n",i+1,selectedPokes.get(i));
                }
                System.out.println(ConsoleColors.RED+"Enemy encountered!");
                System.out.println(ConsoleColors.RESET + "Choose your pokemon by entering its number");
                int userselect = choice.nextInt();
                userselect=getNum(userselect, 1, selectedPokes.size());
                fighter = selectedPokes.get(userselect - 1);
                System.out.println(ConsoleColors.YELLOW+fighter+"\nI CHOOOOSE YOU");

            }
            else {//if team ArrayList is empty user loses and is given a premptive nice message
                System.out.println("you lost");
            }
        }
    }

    private static int turn = 1;

    public static void randomturn() {//deciding who starts first randomly
        // PLAYER final value=1 NPC  final value=2
        turn = (int) (Math.random() * 2 + 1);
    }

    private static void move() {//desicion making of the fight
        if (turn == PLAYER) {
            if (fighter.isNotStunned()) {//if pokemon is stunned, poke is unstunned and turn is passed
                if (fighter.isAwake()) {//if pokemon is not KO the user can go on, else he selects a new poke
                    while (true) { //loop to allow for going back in selection
                        System.out.println("Choose your next move!");
                        System.out.println(ConsoleColors.YELLOW + "1.Attack\n2.Retreat\n3.Pass");
                        Scanner choice = new Scanner(System.in);
                        int moveselect = choice.nextInt();
                        //user selects a number from 1 to 3 for attack retreat or passing
                        moveselect = getNum(moveselect, 1, 3);
                        if (moveselect == 1) {//attack, user is given only the attacks he can use
                            clearScreen();
                            ArrayList<Attack> validAttacks = fighter.enoughEnergy(); //returns attacks the user has enough energy to use
                            System.out.println("0.Go back to move selection"); //returns to initial descion of attack vs retreat vs pass
                            for (int i = 1; i < validAttacks.size() + 1; i++) {
                                System.out.println(ConsoleColors.YELLOW + "\n" + i + "." + validAttacks.get(i - 1));
                            }
                            int attackselect = choice.nextInt();
                            attackselect = getNum(attackselect, 0, validAttacks.size());
                            if (attackselect == 0) {
                                continue; //sends back to inital choice
                            }
                            enemy.attack(validAttacks.get(attackselect - 1), fighter);
                            //if the user picks a valid attack the loop is broken and the user attack the enemy
                            break;
                        }
                        else if (moveselect == 2) {//the user gets to replace the pokemon he is using
                            if(selectedPokes.size()==1){
                                System.out.println("You can't retreat with one pokemon left");
                                continue;
                            }
                            selectedPokes.remove(fighter); //removing current pokemon from ArrayList
                            Pokemon placeholder=fighter; //keeping that pokemon saved for a sec
                            battleselect(); //replacing the pokemon
                            selectedPokes.add(placeholder); //readding the pokemon to the ArrayList
                            break; //breaking to move to npc turn
                        }
                        else if(moveselect==3){//passing the turn
                            System.out.println("pass");
                            break; //breaking to move to npc turn
                        }

                    }
                }
                else {//if the pokemon is KO allow the user to pick one more and remove the
                    //current one from the team as it is KO and can't continue the adventure
                    System.out.println("your pokemon has fainted");
                    selectedPokes.remove(fighter);
                    battleselect();
                }
            }
            for (int i = 0; i < selectedPokes.size(); i++) {//recover the 10 energy per round at the end of the round for all pokes on the team
                selectedPokes.get(i).recoverenergy();
            }
            fighter.unstun(); //if the pokemon was stunned he is now unstunned
            turn = NPC; //the turn is now the computer's
        }
        else if (turn == NPC) {
            if (enemy.isNotStunned()) {//checking for stun to pass
                if (enemy.isAwake()) {//if not KO
                    ArrayList<Attack> validAttacks = enemy.enoughEnergy(); //checking for valid attacks
                    if (!validAttacks.isEmpty()) { //as long as an attack can be used a random attack is picked
                        int randattack = (int) (Math.random() * validAttacks.size());
                        System.out.println(ConsoleColors.RED + "enemy attack");
                        fighter.attack(validAttacks.get(randattack), enemy);
                    }
                    else {//the ArrayList is empty therefore no attacks can be made and NPC pass
                        System.out.println(ConsoleColors.BLACK + "Enemy passed their turn");
                    }
                }
            }
            else{
                System.out.println("Enemy is stunned");
            }
            turn = PLAYER; //pass turn to player
            enemy.unstun(); //unstun if stunned
            enemy.recoverenergy();//recover 10 energy per round at the end
        }
    }
    private static void battle() {
        turn=1;
        battleselect(); //allows for user select first time
        turn = 2;
        battleselect(); //allows for NPC inital select
        randomturn();//decides who goes first
        while (selectedPokes.isEmpty()==false&&enemy.isAwake()) {
            //as long as the user has a team and enemy is not Ko fight can go on
            clearScreen();
            fighter.battlescreen(enemy); //display the stats of the fight
            move();
        }
        if (selectedPokes.isEmpty()) { //user lost since all pokes KO
            System.out.println("You are so bad at this game");
        }
        else if(enemy.isAwake()==false){
            if(allPokes.isEmpty()){ //no more pokes for user to fight so he wins
                System.out.println("You are the ultimate trainer supreme");
            }
            else{//a pokemon used by NPC was KO by user so user wins
                System.out.println("super dead");
                System.out.println(allPokes.size()+" more to go");
            }
        }
    }

    private static void clearScreen(){
        //function to clear screen to make stuff look better
        //a delay is included to allow for user to see what is happening between phases(i.e health changed and attacks etc)
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            System.out.println("got interrupted!");
        }
        for (int i = 0; i < 25; i++) {
            System.out.print("\n");
        }
    }
    private static void startScreen(){
        //bad ascii art for start of the game
        System.out.println(ConsoleColors.YELLOW+"                                  ,'\\\n" +
                "    _.----.        ____         ,'  _\\   ___    ___     ____\n" +
                "_,-'       `.     |    |  /`.   \\,-'    |   \\  /   |   |    \\  |`.\n" +
                "\\      __    \\    '-.  | /   `.  ___    |    \\/    |   '-.   \\ |  |\n" +
                " \\.    \\ \\   |  __  |  |/    ,','_  `.  |          | __  |    \\|  |\n" +
                "   \\    \\/   /,' _`.|      ,' / / / /   |          ,' _`.|     |  |\n" +
                "    \\     ,-'/  /   \\    ,'   | \\/ / ,`.|         /  /   \\  |     |\n" +
                "     \\    \\ |   \\_/  |   `-.  \\    `'  /|  |    ||   \\_/  | |\\    |\n" +
                "      \\    \\ \\      /       `-.`.___,-' |  |\\  /| \\      /  | |   |\n" +
                "       \\    \\ `.__,'|  |`-._    `|      |__| \\/ |  `.__,'|  | |   |\n" +
                "        \\_.-'       |__|    `-._ |              '-.|     '-.| |   |\n" +
                "                                `'                            '-._|");
    }

    public static void main(String[] args) {
        startScreen();
        clearScreen();
        load();//load pokes
        selectpokes();//select team
        clearScreen();
        while (!allPokes.isEmpty()&&!selectedPokes.isEmpty()){//fight till you run out of enemies
            for (int i = 0; i < selectedPokes.size(); i++) {
                selectedPokes.get(i).battlerecover(); //after every winning battle recover health and energy
            }
            System.out.println("You moved to the next battle!");
            battle();
        }
    }
}

