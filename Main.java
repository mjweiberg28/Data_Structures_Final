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

	private ArrayList<ArrayList<Button>> gameBoard;

	private FinalBaronGame game;

	private ComboBox<Integer> size;

	private Label info;

	private GridPane root;

	private GridPane root2;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			gameBoard = new ArrayList<ArrayList<Button>>();
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
			for (int i = 0; i < game.getSize(); i++) {
				gameBoard.add(i, new ArrayList<Button>());
				for (int j = 0; j < game.getSize(); j++) {
					Button button = generateButton(game.getTileAt(i, j));
					button.setOnAction(this);
					gameBoard.get(i).add(j, button);
					root2.add(gameBoard.get(i).get(j), j, i);
				}
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
		else {
			for(int i=0;i<game.getSize();i++) {
				for(int j=0;j<game.getSize();j++) {
					if(event.getSource()==gameBoard.get(i).get(j)) {
						System.out.println("Row: "+i+", Col: "+j);
						try {
							game.makeBid(i, j);
						} catch (Exception e) {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle("Error");
							alert.setContentText(e.getMessage());
							alert.showAndWait();
						}
					}
				}
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName()=="board") {
			root.getChildren().remove(root2);
			root2 = new GridPane();
			gameBoard = new ArrayList<ArrayList<Button>>();
			for (int i = 0; i < game.getSize(); i++) {
				gameBoard.add(i, new ArrayList<Button>());
				for (int j = 0; j < game.getSize(); j++) {
					gameBoard.get(i).add(j, generateButton(game.getTileAt(i, j)));
					gameBoard.get(i).get(j).setOnAction(this);
					root2.add(gameBoard.get(i).get(j), j, i);
				}
			}
			root.add(root2, 0, 3);
		}
		for(int i=0;i<game.getSize();i++) {
			for(int j=0;j<game.getSize();j++) {
				String name= "position"+i+" "+j;
				if(evt.getPropertyName().equals(name)) {
					gameBoard.get(i).add(j, generateButton(game.getTileAt(i, j)));
					gameBoard.get(i).get(j).setOnAction(this);
					root2.add(gameBoard.get(i).get(j), j, i);
				}
			}
		}
		info.setText(game.getInfo());
			
		
	}
}
