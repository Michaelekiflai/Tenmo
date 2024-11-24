package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;
import com.techelevator.util.BasicLogger;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService;
    private final TransferService transferService;
    private final UserService userService;
    private AuthenticatedUser currentUser;

    public App() {
        this.accountService = new AccountService(new RestTemplate(), API_BASE_URL);
        this.userService = new UserService(new RestTemplate(), API_BASE_URL);
        this.transferService = new TransferService(new RestTemplate(), API_BASE_URL);
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            BasicLogger.log("User registered: " + credentials.getUsername());
            System.out.println("Registration successful. You can now login.");
        } else {
            BasicLogger.log("Error creating user");
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        if (currentUser != null) {
            try {
                BigDecimal balance = accountService.getBalance(currentUser);
                System.out.println("Your current account balance is: $" + balance);
            } catch (Exception e) {
                System.out.println("An error occurred while retrieving your balance.");
            }
        } else {
            System.out.println("You must be logged in to view your balance.");
        }
    }

    private void viewTransferHistory() {
        if (currentUser != null) {
            try {
                List<Transfer> transfers = transferService.getTransferHistory(currentUser);
                if (transfers.isEmpty()) {
                    System.out.println("No transfers found.");
                    return;
                }

                System.out.println("-------------------------------------------");
                System.out.println("Transfers");
                System.out.println("ID          From/To                 Amount");
                System.out.println("-------------------------------------------");

                for (Transfer transfer : transfers) {
                    String fromOrTo = transfer.getFromUserId() == currentUser.getUser().getId() ? "To:    " : "From:  ";
                    String otherUsername = transfer.getFromUserId() == currentUser.getUser().getId()
                            ? userService.getUserByAccountId(currentUser, transfer.getToUserId()).getUsername()
                            : userService.getUserByAccountId(currentUser, transfer.getFromUserId()).getUsername();

                    System.out.printf("%-12d %-20s $%10.2f%n", transfer.getTransferId(), fromOrTo + otherUsername, transfer.getAmount());
                }

                int transferId = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
                if (transferId != 0) {
                    viewTransferDetails(transferId);
                }
            } catch (Exception e) {
                System.out.println("An error occurred while retrieving transfer history.");
                e.printStackTrace();
            }
        } else {
            System.out.println("You must be logged in to view your transfer history.");
        }
    }

    private void viewTransferDetails(int transferId) {
        Transfer transfer = transferService.getTransferById(transferId);

        if (transfer == null) {
            System.out.println("Transfer not found.");
            return;
        }

        String fromUser = userService.getUserByAccountId(currentUser, transfer.getFromUserId()).getUsername();
        String toUser = userService.getUserByAccountId(currentUser, transfer.getToUserId()).getUsername();

        System.out.println("-------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("-------------------------------------------");
        System.out.println("ID: " + transfer.getTransferId());
        System.out.println("From: " + fromUser);
        System.out.println("To: " + toUser);
        System.out.println("Amount: $" + transfer.getAmount());
        System.out.println("Status: " + (transfer.getTransferStatusId() == 2 ? "Approved" : "Pending/Rejected"));
    }

    private void viewPendingRequests() {
        if (currentUser != null) {
            try {
                List<Transfer> pendingTransfers = transferService.getPendingRequests(currentUser);
                if (pendingTransfers.isEmpty()) {
                    System.out.println("No pending requests found.");
                } else {
                    System.out.println("Pending Requests:");
                    for (Transfer transfer : pendingTransfers) {
                        System.out.printf("ID: %d | From: %s | Amount: $%.2f%n",
                                transfer.getTransferId(),
                                transfer.getOtherUsername(),
                                transfer.getAmount());
                    }
                }
            } catch (Exception e) {
                System.out.println("An error occurred while retrieving pending requests.");
                e.printStackTrace();
            }
        } else {
            System.out.println("You must be logged in to view your pending requests.");
        }
    }

    private void sendBucks() {
        if (currentUser != null) {
            try {
                List<User> users = userService.getAllUsers(currentUser);
                if (users.isEmpty()) {
                    System.out.println("No users found.");
                    return;
                }

                System.out.println("-------------------------------------------");
                System.out.println("Users");
                System.out.println("ID          Name");
                System.out.println("-------------------------------------------");

                for (User user : users) {
                    if (user.getId() != currentUser.getUser().getId()) {
                        System.out.printf("%-12d %s%n", user.getId(), user.getUsername());
                    }
                }

                int recipientId = consoleService.promptForInt("Enter the ID of the recipient: ");
                BigDecimal amount = consoleService.promptForBigDecimal("Enter the amount to send: ");

                if (recipientId == currentUser.getUser().getId()) {
                    System.out.println("You cannot send money to yourself.");
                    return;
                }

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Amount must be greater than zero.");
                    return;
                }

                BigDecimal balance = accountService.getBalance(currentUser);
                if (amount.compareTo(balance) > 0) {
                    System.out.println("You do not have enough balance.");
                    return;
                }

                Transfer transfer = new Transfer();
                transfer.setFromUserId(currentUser.getUser().getId());
                transfer.setToUserId(recipientId);
                transfer.setAmount(amount);
                transfer.setTransferTypeId(2);  // "Send"
                transfer.setTransferStatusId(2);  // "Approved"

                boolean success = transferService.createTransfer(currentUser, transfer);
                if (success) {
                    System.out.println("Transfer successful!");
                } else {
                    System.out.println("Transfer failed.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred while sending TE bucks.");
                e.printStackTrace();
            }
        } else {
            System.out.println("You must be logged in to send TE bucks.");
        }
    }

    private void requestBucks() {
        if (currentUser != null) {
            try {
                // Get the list of all users
                List<User> users = userService.getAllUsers(currentUser);
                if (users.isEmpty()) {
                    System.out.println("No users found.");
                    return;
                }

                // Display the list of users (excluding the current user)
                System.out.println("-------------------------------------------");
                System.out.println("Users");
                System.out.println("ID          Name");
                System.out.println("-------------------------------------------");

                for (User user : users) {
                    if (user.getId() != currentUser.getUser().getId()) {
                        System.out.printf("%-12d %s%n", user.getId(), user.getUsername());
                    }
                }

                // Prompt the user to select a user to request TE Bucks from
                int recipientId = consoleService.promptForInt("Enter the ID of the user you want to request TE Bucks from: ");

                // Validate that the user is not requesting from themselves
                if (recipientId == currentUser.getUser().getId()) {
                    System.out.println("You cannot request money from yourself.");
                    return;
                }

                // Prompt the user to enter the amount they want to request
                BigDecimal amount = consoleService.promptForBigDecimal("Enter the amount to request: ");

                // Validate that the amount is greater than zero
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Amount must be greater than zero.");
                    return;
                }

                // Create a new transfer request with "Pending" status
                Transfer transfer = new Transfer();
                transfer.setFromUserId(recipientId); // The user you are requesting from
                transfer.setToUserId(currentUser.getUser().getId()); // The current user (requester)
                transfer.setAmount(amount);
                transfer.setTransferTypeId(1);  // "Request"
                transfer.setTransferStatusId(1);  // "Pending"

                // Send the transfer request to the server
                boolean success = transferService.createTransfer(currentUser, transfer);
                if (success) {
                    System.out.println("Request sent successfully!");
                } else {
                    System.out.println("Request failed.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred while requesting TE bucks.");
                e.printStackTrace();
            }
        } else {
            System.out.println("You must be logged in to request TE bucks.");
        }
    }
}