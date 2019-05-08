package application;
	
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.GroupLayout.Alignment;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class Main extends Application implements PropertyChangeListener, EventHandler<ActionEvent>
{
	
	private Button resetButton;
	
	private Button passButton;

	private ArrayList<Button> gameBoard;

	private FinalBaronGame game;

	private ComboBox<Integer> size;

	private Label info;

	private GridPane root;

	private GridPane root2;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			gameBoard = new ArrayList<Button>();
			game = new FinalBaronGame(4);
			game.addPropertyChangeListener(this);
			root = new GridPane();
			Scene scene = new Scene(root, 800, 600);
			// Set up GUI
			primaryStage.setTitle("Land Barron");
			info = new Label(game.getInfo());
			root.add(info, 0, 50);
			ArrayList<Integer> listOfSizes = new ArrayList<Integer>();
			listOfSizes.add(4);
			listOfSizes.add(5);
			listOfSizes.add(6);
			listOfSizes.add(7);
			size = new ComboBox<Integer>();
			size.getItems().addAll(listOfSizes);
			size.setOnAction(this);
			size.setMinHeight(50);
			Label sizeLabel =new Label("Size");
			sizeLabel.setFont(Font.font(14));
			root.add(sizeLabel, 0, 0);
			root.add(size, 0, 1);
			root2 = new GridPane();
			for (int i = 0; i < game.getSize()*game.getSize(); i++) {
				Button button = generateButton(game.getTileAt(i));
				int index1 = i;
				button.setOnMouseClicked(e -> {
						try {
							game.makeBid(index1);
						} catch (Exception e1) {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle("Error");
							alert.setContentText(e1.getMessage());
							alert.showAndWait();
						}
					});
					gameBoard.add(i, button);
					root2.add(gameBoard.get(i), i%game.getSize(), i/game.getSize());				
			}
			root.add(root2, 0, 3);
			resetButton = new Button("RESET");
			resetButton.setAlignment(Pos.CENTER);
			resetButton.setMinSize(100, 50);
			resetButton.setOnAction(this);
			root.add(resetButton, 1, 50);

			passButton = new Button("PASS");
			passButton.setAlignment(Pos.CENTER);
			passButton.setMinSize(100, 50);
			passButton.setOnAction(this);
			root.add(passButton, 1, 1);
			
			
			/////
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private Button generateButton(LandTile tile) {
		Button block= new Button();
		block.setAlignment(Pos.CENTER);
		block.setMaxSize(100, 100);
		block.setMinSize(100, 100);
		if (tile.getType()=='O') {
			block.setText("OFF LIMITS");
			block.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY,Insets.EMPTY)));
		}
		else if(tile.getType()=='E') {
			block.setText("END");
			block.setBackground(new Background(new BackgroundFill(Color.GREEN,CornerRadii.EMPTY,Insets.EMPTY)));
		}
		else if(tile.getType()=='C') {
			block.setText("COMPANY \n LAND");
			block.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW,CornerRadii.EMPTY,Insets.EMPTY)));
		}
		else if(tile.getType()=='U') {
			block.setText("0");
			block.setBackground(new Background(new BackgroundFill(Color.GREY,CornerRadii.EMPTY,Insets.EMPTY)));
		}
		else if(tile.getType()=='1') {
			block.setText(Integer.toString(tile.getCost()));
			block.setBackground(new Background(new BackgroundFill(Color.PINK,CornerRadii.EMPTY,Insets.EMPTY)));
		}
		else if(tile.getType()=='2') {
			block.setText(Integer.toString(tile.getCost()));
			block.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE,CornerRadii.EMPTY,Insets.EMPTY)));
		}
		return block;
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource()==resetButton)
			game.resetGame();
		else if(event.getSource()==passButton)
			game.passClicked();
		else if (event.getSource()==size)
			game.setSize(size.getValue());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName()=="board") {
			root.getChildren().remove(root2);
			root2 = new GridPane();
			gameBoard = new ArrayList<Button>();
			for (int i = 0; i < game.getSize()*game.getSize(); i++) {
				Button button = generateButton(game.getTileAt(i));
				int index1 = i;
				button.setOnMouseClicked(e -> {
						try {
							game.makeBid(index1);
						} catch (Exception e1) {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle("Error");
							alert.setContentText(e1.getMessage());
							alert.showAndWait();
						}
					});
					gameBoard.add(i, button);
					root2.add(gameBoard.get(i), i%game.getSize(), i/game.getSize());				
			}
			root.add(root2, 0, 3);
		}
		for(int i=0;i<game.getSize()*game.getSize();i++) {
			String name= "position "+i;
			if(evt.getPropertyName().equals(name)) {
				Button button = generateButton(game.getTileAt(i));
				int index1 = i;
				button.setOnMouseClicked(e -> {
					try {
						game.makeBid(index1);
					} catch (Exception e1) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Error");
						alert.setContentText(e1.getMessage());
						alert.showAndWait();
					}
				});
				gameBoard.add(i,button);
				root2.add(gameBoard.get(i), i%game.getSize(), i/game.getSize());
			}
		}
		info.setText(game.getInfo());
		
	}
}
