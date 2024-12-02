package com.spamdetector;

import com.github.javafaker.Faker;
import com.spamdetector.dto.*;
import com.spamdetector.service.*;
import org.slf4j.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Profile("dev") // Ensure this runs only in development
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final UserService userService;
    private final ContactService contactService;
    private final SpamReportService spamReportService;

    private Faker faker;
    private Set<String> usedPhoneNumbers;

    public DataLoader(UserService userService, ContactService contactService, SpamReportService spamReportService) {
        this.userService = userService;
        this.contactService = contactService;
        this.spamReportService = spamReportService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting data population...");

        // Clear existing data
        userService.deleteAllUsers();
        contactService.deleteAllContacts();
        spamReportService.deleteAllSpamReports();

        faker = new Faker();
        usedPhoneNumbers = new HashSet<>();

        int numberOfUsers = 200;
        int maxContactsPerUser = 5;
        int numberOfSpamReports = 50;     // Adjusted as needed

        createUsers(numberOfUsers);
        List<UserProfileDTO> users = userService.getAllUsers();
        createContactsForUsers(users, maxContactsPerUser);
        createSpamReports(users, numberOfSpamReports);

        logger.info("Data population completed successfully.");
    }

    private void createUsers(int numberOfUsers) {
        for (int i = 0; i < numberOfUsers; i++) {
            UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
            registrationDTO.setName(faker.name().fullName());
            registrationDTO.setPhoneNumber(generateRandomPhoneNumber());
            registrationDTO.setEmail(faker.internet().emailAddress());
            registrationDTO.setPassword("password");

            try {
                userService.registerUser(registrationDTO);
            } catch (Exception e) {
                logger.error("Failed to register user: {}", registrationDTO.getPhoneNumber(), e);
            }
        }
    }

    private void createContactsForUsers(List<UserProfileDTO> users, int maxContactsPerUser) {
        for (UserProfileDTO user : users) {
            int contactsToCreate = faker.number().numberBetween(1, maxContactsPerUser + 1);
            for (int j = 0; j < contactsToCreate; j++) {
                ContactDTO contactDTO = new ContactDTO();

                if (faker.bool().bool()) {
                    // 50% chance to associate with an existing user
                    UserProfileDTO existingUser = users.get(faker.random().nextInt(users.size()));
                    contactDTO.setName(existingUser.getName());
                    contactDTO.setPhoneNumber(existingUser.getPhoneNumber());
                } else {
                    // Create a new random contact
                    contactDTO.setName(faker.name().fullName());
                    contactDTO.setPhoneNumber(generateRandomPhoneNumber());
                }

                try {
                    contactService.addContact(user.getId(), contactDTO);
                } catch (Exception e) {
                    logger.error("Failed to add contact for user {}: {}", user.getPhoneNumber(), e.getMessage());
                }
            }
        }
    }

    private void createSpamReports(List<UserProfileDTO> users, int numberOfSpamReports) {
        // Collect all phone numbers from users and contacts
        Set<String> allPhoneNumbers = new HashSet<>();
        allPhoneNumbers.addAll(users.stream().map(UserProfileDTO::getPhoneNumber).collect(Collectors.toSet()));
        allPhoneNumbers.addAll(usedPhoneNumbers);

        for (int k = 0; k < numberOfSpamReports; k++) {
            Long randomUserId = users.get(faker.random().nextInt(users.size())).getId();
            SpamReportDTO spamReportDTO = new SpamReportDTO();

            if (faker.number().numberBetween(1, 100) <= 70) {
                String[] phoneNumbersArray = allPhoneNumbers.toArray(new String[0]);
                spamReportDTO.setReportedPhoneNumber(phoneNumbersArray[faker.random().nextInt(phoneNumbersArray.length)]);
            } else {
                spamReportDTO.setReportedPhoneNumber(generateRandomPhoneNumber());
            }

            try {
                spamReportService.reportSpam(randomUserId, spamReportDTO);
            } catch (Exception e) {
                logger.error("Failed to report spam: {}", e.getMessage());
            }
        }
    }

    private String generateRandomPhoneNumber() {
        String phoneNumber;
        do {
            phoneNumber = faker.phoneNumber().subscriberNumber(10);
        } while (usedPhoneNumbers.contains(phoneNumber));
        usedPhoneNumbers.add(phoneNumber);
        return phoneNumber;
    }
}
