package com.example.zapiska;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Notebook extends Application {

    private List<User> userList = new ArrayList<>();
    private Map<String, ObservableList<String>> notes = new HashMap<>();
    private TextField phoneField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private TextArea noteTextArea = new TextArea();
    private ListView<String> noteListView = new ListView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Записная книжка");

        userList.add(new User("111", "Иван", "111"));
        userList.add(new User("222", "Мария", "222"));

        VBox loginLayout = new VBox(10);
        loginLayout.getChildren().addAll(new Label("Введите номер телефона:"), phoneField);

        Button loginButton = new Button("Войти");
        loginButton.setOnAction(e -> showPasswordDialog());
        loginLayout.getChildren().add(loginButton);

        Scene loginScene = new Scene(loginLayout, 300, 200);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void showPasswordDialog() {
        String phoneNumber = phoneField.getText();

        Optional<User> foundUser = userList.stream()
                .filter(user -> user.getPhoneNumber().equals(phoneNumber))
                .findFirst();

        if (foundUser.isPresent()) {
            User user = foundUser.get();
            Stage passwordStage = new Stage();
            passwordStage.setTitle("Введите пароль");
            passwordStage.initModality(Modality.APPLICATION_MODAL);

            VBox passwordLayout = new VBox(10);
            passwordLayout.getChildren().addAll(new Label("Введите пароль:"), passwordField);

            Button submitButton = new Button("Войти");
            submitButton.setOnAction(e -> checkPassword(user, passwordStage));
            passwordLayout.getChildren().add(submitButton);

            Scene passwordScene = new Scene(passwordLayout, 300, 200);
            showPassword(user.getPassword());
            passwordStage.setScene(passwordScene);
            passwordStage.show();
        } else {
            showInfo("Такого клиента не существует");
        }
    }

    private void checkPassword(User user, Stage passwordStage) {
        String enteredPassword = passwordField.getText();

        if (enteredPassword.equals(user.getPassword())) {
            passwordStage.close();
            createNotebookUI(user.getName());
        } else {
            showInfo("Неверный пароль");
        }
    }

    private void createNotebookUI(String userName) {
        Stage notebookStage = new Stage();
        notebookStage.setTitle("Записная книжка");

        VBox notebookLayout = new VBox(10);
        notebookLayout.getChildren().add(new Label("Добро пожаловать, " + userName + "!"));

        noteTextArea.setPromptText("Введите заметку...");
        Button addNoteButton = new Button("Добавить заметку");
        addNoteButton.setOnAction(e -> addNote());
        Button deleteNoteButton = new Button("Удалить заметку");
        deleteNoteButton.setOnAction(e -> deleteNote());

        notebookLayout.getChildren().addAll(noteTextArea, addNoteButton, deleteNoteButton, noteListView);

        Scene notebookScene = new Scene(notebookLayout, 400, 400);
        notebookStage.setScene(notebookScene);
        notebookStage.show();
    }

    private void addNote() {
        String note = noteTextArea.getText();
        if (!note.isEmpty()) {
            String phoneNumber = phoneField.getText();
            if (!notes.containsKey(phoneNumber)) {
                notes.put(phoneNumber, FXCollections.observableArrayList());
            }
            notes.get(phoneNumber).add(note);
            updateNoteListView(phoneNumber);
            noteTextArea.clear();
        }
    }

    private void deleteNote() {
        String selectedNote = noteListView.getSelectionModel().getSelectedItem();
        String phoneNumber = phoneField.getText();

        if (selectedNote != null && notes.containsKey(phoneNumber)) {
            notes.get(phoneNumber).removeIf(note -> note.equals(selectedNote.substring(selectedNote.indexOf(". ") + 2)));
            updateNoteListView(phoneNumber);
        }
    }

    private void updateNoteListView(String phoneNumber) {
        ObservableList<String> numberedNotes = FXCollections.observableArrayList();
        List<String> noteList = notes.get(phoneNumber);

        if (noteList != null) {
            for (int i = 0; i < noteList.size(); i++) {
                numberedNotes.add((i + 1) + ". " + noteList.get(i));
            }
        }

        noteListView.setItems(numberedNotes);
    }

    private void showPassword(String message) {
        Stage infoStage = new Stage();
        infoStage.setTitle("Пароль");

        VBox infoLayout = new VBox(10);
        infoLayout.getChildren().addAll(new Label(message));

        Scene infoScene = new Scene(infoLayout, 300, 100);
        infoStage.setScene(infoScene);
        infoStage.show();
    }

    private void showInfo(String message) {
        Stage infoStage = new Stage();
        infoStage.setTitle("Информация");

        VBox infoLayout = new VBox(10);
        infoLayout.getChildren().addAll(new Label(message));

        Scene infoScene = new Scene(infoLayout, 300, 100);
        infoStage.setScene(infoScene);
        infoStage.show();
    }
}

class User {
    private String phoneNumber;
    private String name;
    private String password;

    public User(String phoneNumber, String name, String password) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}