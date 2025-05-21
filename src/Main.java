import java.util.ArrayList;
import java.util.List;
import model.core.card.WestCard;
import controller.PhomLogicController;
import controller.PhomViewController;
import controller.TienLenLogicController;
import model.phom.PhomBotPlayer;
import model.phom.PhomPlayer;
import model.tienlen.TienLenBotPlayer;
import model.tienlen.TienLenPlayer;
import view.PhomGameViewController;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // This is a demonstration of the architecture without actual JavaFX integration
        System.out.println("Starting Card Game Demo");
        
        // Choose which game to play
        String gameType = "Phom"; // or "TienLen"
        
        if ("Phom".equals(gameType)) {
            playPhomGame();
        } else {
            playTienLenGame();
        }
    }
    
    /**
     * Set up and play a Phom game
     */
    private static void playPhomGame() {
        System.out.println("\n=== Starting Phom Game ===\n");
        
        // Create players
        List<PhomPlayer> players = new ArrayList<>();
        players.add(new PhomBotPlayer("Human Player")); // Using PhomBotPlayer as a stand-in for human
        players.add(new PhomBotPlayer("Bot 1"));
        players.add(new PhomBotPlayer("Bot 2"));
        players.add(new PhomBotPlayer("Bot 3"));
        
        // Set up game orchestrator
        MainGameController orchestrator = new MainGameController();
        PhomLogicController logicController = (PhomLogicController) orchestrator.selectGame("Phom", players, 10);
        
        // Create view controller
        PhomViewController viewController = new PhomGameViewController();
        
        // Connect logic and view controllers
        logicController.setViewController(viewController);
        viewController.setLogicController(logicController);
        
        // Start the game
        logicController.startGame();
        
        // Simulate some player actions
        System.out.println("\n--- Simulating Player Actions ---\n");
        
        // Human player draws a card
        logicController.playerRequestsDraw(players.get(0));
        
        // Human player discards a card (assuming they have cards)
        if (!players.get(0).getHand().isEmpty()) {
            logicController.playerRequestsDiscard(players.get(0), players.get(0).getHand().get(0));
        }
        
        // End the game
        System.out.println("\n--- Ending Game ---\n");
        logicController.endGame();
    }
    
    /**
     * Set up and play a TienLen game
     */
    private static void playTienLenGame() {
        System.out.println("\n=== Starting TienLen Game ===\n");
        
        // Create players
        List<TienLenPlayer> players = new ArrayList<>();
        players.add(new TienLenBotPlayer("Human Player")); // Using TienLenBotPlayer as a stand-in for human
        players.add(new TienLenBotPlayer("Bot 1"));
        players.add(new TienLenBotPlayer("Bot 2"));
        players.add(new TienLenBotPlayer("Bot 3"));
        
        // Set up game orchestrator
        MainGameController orchestrator = new MainGameController();
        TienLenLogicController logicController = (TienLenLogicController) orchestrator.selectGame("TienLen", players, 13);
        
        // The TienLenViewController would be implemented similar to PhomViewController
        // For this demo, we'll skip the view connection
        
        // Start the game
        logicController.startGame();
        
        // Simulate some player actions
        System.out.println("\n--- Simulating Player Actions ---\n");
        
        // Human player plays a card (assuming they have cards)
        if (!players.get(0).getHand().isEmpty()) {
            List<WestCard> cardsToPlay = new ArrayList<>();
            cardsToPlay.add(players.get(0).getHand().get(0));
            logicController.playerRequestsPlayCards(players.get(0), cardsToPlay);
        }
        
        // Human player passes
        logicController.playerRequestsPass(players.get(0));
        
        // End the game
        System.out.println("\n--- Ending Game ---\n");
        logicController.endGame();
    }
}