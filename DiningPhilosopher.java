import java.util.Scanner;

class Philosopher extends Thread{
    private String index;
    private Fork fork;
    private int thinkingTimes = 0;
    private int eatingTimes = 0;
    
    public Philosopher(String index,Fork fork){
        super(index);
        this.index = index;
        this.fork = fork;
    }
    
    public void run(){
        while(true){
            thinking();
            fork.takeFork();
            eating();
            fork.putFork();
        }    
    }
     
    public void eating(){
    	eatingTimes++;
    	int name = Integer.parseInt(index) + 1;
        System.out.println("Philosopher " + name + ": eating!    Times: " + eatingTimes);
        
        /*Holding a period of time, as eating*/
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
     
    public void thinking(){
    	thinkingTimes++;
    	int name = Integer.parseInt(index) + 1;
        System.out.println("Philosopher "+ name + ": thinking!  Times: " + thinkingTimes);
        
        /*Holding a period of time, as thinking*/
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Fork{
    /*The array represents the state of the N forks with initiates states of "false"*/
    private boolean[] useState = new boolean[DiningPhilosopher.philosophersAmount];
    
    /*When the left and right hand forks are both available, the philosopher could take the fork and eat*/
    public synchronized void takeFork(){
        String index = Thread.currentThread().getName();
        int indexInt = Integer.parseInt(index);
        
        /*If either of the surrounding forks is occupied, wait!*/
        while(useState[indexInt] || useState[(indexInt + 1) % DiningPhilosopher.philosophersAmount]){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /*Set the surrounding forks states as "true"*/
        useState[indexInt] = true;
        useState[(indexInt + 1) % DiningPhilosopher.philosophersAmount] = true;
    }
    
    /*The philosopher put down the 2 forks*/
    public synchronized void putFork(){
        String index = Thread.currentThread().getName();
        int indexInt = Integer.parseInt(index);
        
        useState[indexInt]= false;
        useState[(indexInt + 1) % DiningPhilosopher.philosophersAmount]=false;
        notifyAll();  //Inform/Notify other threads
    }
}


public class DiningPhilosopher {
	public static int philosophersAmount;
	
	public static void main(String[] args){
		/*Enter a integer, so N philosophers are available*/
		System.out.println("Please enter the amount of philosophers: ");
		Scanner input = new Scanner(System.in);
		philosophersAmount = input.nextInt();
	
		Fork fork = new Fork();
		Philosopher[] philosophers = new Philosopher[philosophersAmount];
		
		for(int i=0;i<philosophers.length;i++) {
			philosophers[i] = new Philosopher(String.valueOf(i), fork);
			philosophers[i].start();
		}		
	}
}
