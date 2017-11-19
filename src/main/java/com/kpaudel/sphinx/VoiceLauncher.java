package com.kpaudel.sphinx;

import java.io.IOException;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VoiceLauncher extends Application implements EventHandler<ActionEvent> {
	private static final String ACOUSTIC_MODEL_PATH = "resource:/edu/cmu/sphinx/models/en-us/en-us";
	private static final String DICTIONARY_PATH = "resource:/edu/cmu/sphinx/models/de-de/0036.dic";
	private static final String LANGUAGE_MODEL_PATH = "resource:/edu/cmu/sphinx/models/de-de/0036.lm";
	private static final String TITLE = "Sphinx Sample Test";
	private static final String RECORD_BUTTONID = "record-id";

	private Button button;
	private TextArea commandsTextArea;
	private boolean listening;
	private Label statusLabel;

	private LiveSpeechRecognizer recognizer;

	public static void main(String[] args) throws IOException {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.initRecognizer();
		primaryStage.setTitle(TITLE);
		VBox vBox = new VBox(10.0);

		this.listening = false;
		this.button = new Button("Start..");
		this.button.setId(RECORD_BUTTONID);
		this.button.setOnAction(this);
		vBox.getChildren().add(this.button);

		this.statusLabel = new Label("test");
		vBox.getChildren().add(this.statusLabel);

		this.commandsTextArea = new TextArea();
		this.commandsTextArea.setMinWidth(200.0);
		this.commandsTextArea.setMaxHeight(400.0);
		vBox.getChildren().add(this.commandsTextArea);

		StackPane root = new StackPane();
		root.getChildren().add(vBox);
		root.setPadding(new Insets(10.0));

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private void initRecognizer() throws IOException {
		Configuration configuration = new Configuration();

		// Set path to the acoustic model.
		configuration.setAcousticModelPath(ACOUSTIC_MODEL_PATH);

		// Set path to the dictionary.
		configuration.setDictionaryPath(DICTIONARY_PATH);
		// Set path to the language model.
		configuration.setLanguageModelPath(LANGUAGE_MODEL_PATH);
		//configuration.setGrammarPath(grammarPath);
		//configuration.setGrammarName(grammarName);
		configuration.setUseGrammar(true);

		this.recognizer = new LiveSpeechRecognizer(configuration);

	}

	@Override
	public void handle(ActionEvent event) {
		if (((Button) event.getSource()).getId() == RECORD_BUTTONID) {
			// ((Button) event.getSource()).setText("...");
			try {
				this.startListening();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void startListening() throws InterruptedException {
		System.out.println("Listening...");

		this.listening = true;
		this.recognizer.startRecognition(true);
		SpeechResult result;

		// while ((result = recognizer.getResult()) != null) {
		// String command = result.getHypothesis();
		// System.out.println(command);
		// }
		if ((result = recognizer.getResult()) != null) {
			String command = result.getHypothesis();
			System.out.println("COMMAND= " + command);
			this.commandsTextArea.setText(command + "\n" + this.commandsTextArea.getText());
		}

		this.recognizer.stopRecognition();
		System.out.println("....Stopped Listening");
	}
}
