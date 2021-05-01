
import java.util.*;
import java.io.*;
public class Arena {
    private static final ArrayList<Pokemon> allPokes = new ArrayList<Pokemon>();
    private static final ArrayList<Pokemon> selectedPokes = new ArrayList<Pokemon>();
    private static void load() {
        try {
            Scanner file = new Scanner(new BufferedReader(new FileReader("pokemons.txt")));
            int n = Integer.parseInt(file.nextLine());
            for (int i = 0; i < n; i++) {
                String info = file.nextLine();
                allPokes.add(new Pokemon(info));
            }
        }
        catch (IOException ex) {
            System.out.println("Installation error,check that no file is missing then try again");
        }
    }

    private static void selectpokes() {
        Scanner choice = new Scanner(System.in);
        for (int k = 0; k < 4; k += 0) {
            for (int i = 0; i < allPokes.size(); i++) {
                System.out.printf("\t\t\t  ❀%d❀%s\n", i + 1, allPokes.get(i));
            }
            System.out.println("Select your pokemons by entering the number next to its name!");
            try{
                int numSelected=Integer.parseInt(choice.next());
                numSelected=getNum(numSelected, 1, allPokes.size());
                selectedPokes.add(allPokes.get(numSelected - 1));
                allPokes.remove(numSelected - 1);
                k++;

            }
            catch(NumberFormatException e){
                System.out.println("This is not a valid integer try again!");
            }
        }
    }

    private static int getNum(int usernum, int min, int max) {
        if (usernum >= min && usernum <= max) {
            return usernum;
        }
        else {
            System.out.println("Invalid please try again");
            Scanner choice = new Scanner(System.in);
            int userselect=choice.nextInt();
            while (!(usernum >= min && usernum <= max)) {
                System.out.println("Please enter a valid number");
                userselect = choice.nextInt();
            }
            return userselect;
        }
    }

    private static final int PLAYER = 1;
    private static final int NPC = 2;
    private static Pokemon fighter;
    private static Pokemon enemy;

    private static void battleselect() {
        Scanner choice = new Scanner(System.in);
        if (turn == NPC) {
            if (allPokes.isEmpty() == false) {
                int randomenemy = (int) (Math.random() * allPokes.size() - 1 + 1);
                enemy = allPokes.get(randomenemy);
                allPokes.remove(enemy);
            }
        }
        else if (turn == PLAYER) {
            if (selectedPokes.isEmpty()==false) {
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
            else {
                System.out.println("you lost");
            }
        }
    }

    private static int turn = 1;

    public static void randomturn() {
        turn = (int) (Math.random() * 2 + 1);
    }

    private static void move() {
        if (turn == PLAYER) {
            if (fighter.isNotStunned()) {
                if (fighter.isAwake()) {
                    while (true) {
                        System.out.println("Choose your next move!");
                        System.out.println(ConsoleColors.YELLOW + "1.Attack\n2.Retreat\n3.Pass");
                        Scanner choice = new Scanner(System.in);
                        int moveselect = choice.nextInt();
                        moveselect = getNum(moveselect, 1, 3);
                        if (moveselect == 1) {
                            clearScreen();
                            ArrayList<Attack> validAttacks = fighter.enoughEnergy();
                            System.out.println("0.Go back to move selection");
                            for (int i = 1; i < validAttacks.size() + 1; i++) {
                                System.out.println(ConsoleColors.YELLOW + "\n" + i + "." + validAttacks.get(i - 1));
                            }
                            int attackselect = choice.nextInt();
                            attackselect = getNum(attackselect, 0, validAttacks.size());
                            if (attackselect == 0) {
                                continue;
                            }
                            enemy.attack(validAttacks.get(attackselect - 1), fighter);
                            break;
                        }
                        else if (moveselect == 2) {
                            selectedPokes.remove(fighter);
                            Pokemon placeholder=fighter;
                            battleselect();
                            selectedPokes.add(placeholder);
                            break;
                        }
                        else if(moveselect==3){
                            System.out.println("pass");
                            break;
                        }

                    }
                }
                else {
                    System.out.println("your pokemon has fainted");
                    selectedPokes.remove(fighter);
                    battleselect();
                }
            }
            for (int i = 0; i < selectedPokes.size(); i++) {
                selectedPokes.get(i).recoverenergy();
            }
            fighter.unstun();
            turn = NPC;
        }
        else if (turn == NPC) {
            if (enemy.isNotStunned()) {
                if (enemy.isAwake()) {
                    ArrayList<Attack> validAttacks = enemy.enoughEnergy();
                    if (!validAttacks.isEmpty()) {
                        int randattack = (int) (Math.random() * validAttacks.size());
                        System.out.println(ConsoleColors.RED + "enemy attack");
                        fighter.attack(validAttacks.get(randattack), enemy);
                    }
                    else {
                        System.out.println(ConsoleColors.BLACK + "Enemy passed their turn");
                        turn = 2;
                    }
                }
            }
            turn = PLAYER;
            enemy.unstun();
            enemy.recoverenergy();
        }
    }
    private static void battle() {
        turn=1;
        battleselect();
        turn = 2;
        battleselect();
        randomturn();
        while (selectedPokes.isEmpty()==false&&enemy.isAwake()) {
            clearScreen();
            fighter.battlescreen(enemy);
            move();
        }
        if (selectedPokes.isEmpty()) {
            System.out.println("You are so bad at this game");
        }
        else if(enemy.isAwake()==false){
            if(allPokes.isEmpty()){
                System.out.println("You are the ultimate trainer supreme");
            }
            else{
                System.out.println("super dead");
                System.out.println(allPokes.size()+" more to go");
            }
        }
    }

    private static void clearScreen(){
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
        System.out.println();
    }

    public static void main(String[] args) {
        startScreen();
        clearScreen();
        load();
        selectpokes();
        clearScreen();
        while (!allPokes.isEmpty()&&!selectedPokes.isEmpty()){
            for (int i = 0; i < selectedPokes.size(); i++) {
                selectedPokes.get(i).battlerecover();
            }
            System.out.println("You moved to the next battle!");
            battle();
        }
    }
}

